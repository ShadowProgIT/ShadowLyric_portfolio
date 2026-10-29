package com.logistic.endpoints;

import com.logistic.config.LogisticApiContractConfig;
import com.logistic.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Sendings", description = "Управление отправлениями")
@RequestMapping(value = "/api/sendings", produces = MediaType.APPLICATION_JSON_VALUE)
public interface SendingApi {

    @Operation(
            summary = "Найти отправку по ID",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Отправка найдена")
    @ApiResponse(responseCode = "404", description = "Отправка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<SendingResponse> getSendingById(
            @Parameter(description = "ID отправки", required = true, example = "82842423257") @PathVariable Long id
    );

    @Operation(
            summary = "Список отправок",
            description = "Возвращает постраничный список отправок с поддержкой фильтрации по статусу, трекингу, участнику",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Постраничный список отправок")
    @GetMapping
    PagedModel<EntityModel<SendingResponse>> getAllSendings(
            @Parameter(description = "Фильтр по статусу", example = "in_way") @RequestParam(required = false) String status,
            @Parameter(description = "Поиск по трекинг-номеру", example = "RU12-23-42") @RequestParam(required = false) String trackingNumber,
            @Parameter(description = "Фильтр по ID отправителя") @RequestParam(required = false) Long senderId,
            @Parameter(description = "Фильтр по ID получателя") @RequestParam(required = false) Long recipientId,
            @Parameter(description = "Номер страницы", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "Создать новую отправку",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Отправка создана")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Отправитель или получатель не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Отправка с таким трекинг-номером уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<SendingResponse>> createSending(@Valid @RequestBody SendingRequest request);

    @Operation(
            summary = "Полное обновление отправки",
            description = "Заменяет все поля отправки",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Отправка обновлена")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Отправка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Конфликт: трекинг-номер уже занят",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<SendingResponse> updateSending(
            @Parameter(description = "ID отправки", required = true, example = "82842423257") @PathVariable Long id,
            @Valid @RequestBody SendingRequest request
    );

    @Operation(
            summary = "Частичное обновление отправки",
            description = "Обновляет только переданные поля",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Отправка обновлена")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Отправка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<SendingResponse> patchSending(
            @Parameter(description = "ID отправки", required = true, example = "82842423257") @PathVariable Long id,
            @Valid @RequestBody PatchSendingRequest request
    );

    @Operation(
            summary = "Удалить отправку",
            description = "Удаляет отправку и всю её историю. Разрешено только для отправок со статусом 'created' или 'cancelled'.",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Отправка удалена")
    @ApiResponse(responseCode = "404", description = "Отправка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Невозможно удалить: отправка уже в процессе доставки",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteSending(
            @Parameter(description = "ID отправкии", required = true, example = "82842423257") @PathVariable Long id
    );

    @Operation(
            summary = "История перемещений отправки",
            description = "Возвращает историю для указанной отправки",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список событий истории")
    @ApiResponse(responseCode = "404", description = "Отправка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}/history")
    PagedModel<EntityModel<SendingHistoryResponse>> getSendingHistory(
            @Parameter(description = "ID отправки", required = true, example = "82842423257") @PathVariable Long id,
            @Parameter(description = "Номер страницы", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "50") @RequestParam(defaultValue = "50") int size
    );
}


