Логистика: набор микросервисов для учёта отправок (посылок)

Учебный проект на Java 21 и Spring Boot, собранный как Maven multi-module. Смысл его не в том, чтобы вести посылки в базу, а в том, чтобы показать, как несколько независимых сервисов общаются между собой: часть через HTTP, часть через брокер сообщений, часть через gRPC. Основной сервис хранит данные и принимает запросы, а остальные реагируют на события, которые он рассылает.

 Есть участник (Entrant) — отправитель или получатель: имя, фамилия, email, телефон, адрес. Есть отправка (Sending): трек-номер, статус, вес, габариты, ссылки на отправителя и получателя. Есть история отправки (SendingHistory): чередa событий со статусом, локацией, временем и комментарием. Данные лежат в памяти, в ConcurrentHashMap, при старте загружаются два участника и две отправки. Никакой настоящей БД в проекте нет, зависимость от JPA и H2 подключена, но в расчёт не участвует.

Состав по модулям

contract-module (артефакт logistic-api-contract) — контракт REST API. Тут DTO (EntrantRequest/Response, SendingRequest/Response, PagedResponse, ErrorResponse), интерфейсы контроллеров EntrantApi, SendingApi, SendingHistoryApi с Swagger-аннотациями и кастомная валидация телефона (ValidNumber, NumberValidator). Контроллеры основного сервиса реализуют эти интерфейсы, поэтому описание API и его реализация разведены.

events-contract-module — контракт событий для брокера. Классы EntrantEvent, SendingEvent (вложенные записи Created, Update, Deleted, Enriched), обёртка EventEnvelope с метаданными (eventId, тип, сервис, время) и RoutingKeys с именами exchange и routing key: logistic.events, sending.created, sending.status.updated, sending.deleted, sending.enriched, entrant.created, entrant.deleted.

service-impl-module (порт 8080) — основной сервис, логистическое ядро. Реализует REST поверх Spring MVC с HATEOAS (ссылки в ответах) и одновременно GraphQL на DGS: схема в resources/schema/schema.graphqls, дата-фетчеры по типам, input-типы для мутаций, постраничные Connection/PageInfo, свой DateTime-скаляр, обработка ошибок. В GraphQL отдельно ограничены глубина запроса (20) и сложность (200) — защита от тяжёлых запросов. Каждое изменение (создание, обновление статуса, удаление) публикуется в RabbitMQ через EventPublisher. Есть GlobalExceptionHandler и специфичный обработчик для GraphQL.

logistic-grpc-contract — protobuf-контракт ShipmentCalculator с методом CalculateETA: на вход id отправки, города отправления и назначения и вес, на выход id, примерное время в минутах и статус маршрута.

logistic-finance-server (порт 8083, gRPC на 9090) — расчётный сервис. Принимает gRPC-запрос и считает мок-ETA: базовые 1440 минут плюс половина веса в килограммах, статус маршрута всегда OPTIMAL. Сервер запускается отдельным lifecycle-бином GrpcServerLifecycle.

logistic-enrichment-client (порт 8082) — обогатитель. Подписан на очередь q.logistics.enrichment.sending-created (routing key sending.created), ловит создание отправки, дёргает finance-сервер по gRPC, чтобы получить ETA, и публикует в тот же exchange событие sending.enriched. Города в запросе захардкожены как Москва и Санкт-Петербург.

events-impl-module (порт 8084) — аудиториевка. Подписан на все события (routing key #), кладёт каждое в очередь q.logistic.audit.events с dead-letter очередью q.logistic.audit.events.dlq и разбирает payload по типу, чтобы записать человекочитаемую строку в файл sending_audit.log. То есть это попытка сделать event sourcing / журнал событий: вся история изменений отправок восстанавливается из лога.

notification-service (порт 8085) — уведомления. Тоже слушает все события, отбивает дубликаты по eventId, превращает событие в заголовок, описание, иконку и уровень (success, warning, info) и рассылает JSON всем подключённым клиентам по WebSocket на путь /ws/notifications.

Как это выглядит в сборе

Клиент создаёт отправку через REST или GraphQL основного сервиса. Сервис сохраняет её в памяти и кидает в RabbitMQ событие sending.created. На него независимо, ничего друг о друге не зная, реагируют трое: enrichment-client уходит по gRPC за расчётом ETA и возвращает в брокер sending.enriched, audit-сервис дописывает строку в лог, notification-service пушит уведомление в браузер по WebSocket. Это и есть суть проекта: одна публикация — несколько независимых подписчиков, плюс синхронный вызов между двумя из них.

Как запустить

Нужен локальный RabbitMQ на 5672 (guest/guest) — все сервисы, кроме finance-сервера, к нему подключаются. Дальше из корня:

mvnw.cmd install -DskipTests
start.bat

start.bat поднимает пять окон в порядке: finance-сервер (8083, gRPC 9090), основной сервис (8080), enrichment-client (8082), audit-сервис (8084), notification-service (8085). Если что-то из них хочется поднять отдельно:

mvnw.cmd -pl service-impl-module spring-boot:run

Точки входа после старта:

http://localhost:8080/swagger-ui.html — REST API
http://localhost:8080/graphiql — GraphQL песочница
ws://localhost:8085/ws/notifications — WebSocket с уведомлениями
