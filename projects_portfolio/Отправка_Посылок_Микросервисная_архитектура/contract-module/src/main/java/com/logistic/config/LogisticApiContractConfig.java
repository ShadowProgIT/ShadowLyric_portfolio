package com.logistic.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Проект логистики API",
                version = "недопиленный",
                description = """
                        Сервис для управления логистическими отправлениями:
                        регистрация участников, создание и отслеживание отправок,
                        ведение истории их перемещений.
                        """
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "")
        })
@SecurityScheme(
        name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "JWT Bearer token. Пример: `Authorization: Bearer eyJhbGci...`"
)
public final class LogisticApiContractConfig {

    public static final String SECURITY_SCHEME_BEARER = "bearerAuth";

    private LogisticApiContractConfig() {

    }
}
