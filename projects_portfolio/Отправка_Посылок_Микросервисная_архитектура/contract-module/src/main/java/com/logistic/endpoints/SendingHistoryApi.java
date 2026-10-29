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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Sending History", description = "Добавление и просмотр событий в истории перемещений")
@RequestMapping(value = "/api/sendings/{sendingId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
public interface SendingHistoryApi {

    @Operation(
            summary = "Добавить событие в историю отправки",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Событие добавлено")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации или недопустимый переход статуса",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Отправка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<SendingHistoryResponse>> addHistoryEvent(
            @Parameter(description = "ID отправки", required = true) @PathVariable Long sendingId,
            @Valid @RequestBody SendingHistoryRequest request
    );
}

