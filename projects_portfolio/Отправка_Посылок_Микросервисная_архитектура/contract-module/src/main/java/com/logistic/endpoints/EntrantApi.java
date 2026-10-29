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

@Tag(name = "Entrants", description = "Управление участниками отправки (отправители и получатели)")
@RequestMapping(value = "/api/entrants", produces = MediaType.APPLICATION_JSON_VALUE)
public interface EntrantApi {

    @Operation(
            summary = "Список участников",
            description = "Возвращает постраничный список участников с умными ссылками",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Список участников")
    @GetMapping
    PagedModel<EntityModel<EntrantResponse>> getAllEntrants(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20")
            @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "Найти участника по ID",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Участник найден")
    @ApiResponse(responseCode = "404", description = "Участник не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<EntrantResponse> getEntrantById(
            @Parameter(description = "ID участника", required = true, example = "12345678") @PathVariable Long id
    );

    @Operation(
            summary = "Зарегистрировать нового участника",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Участник зарегистрирован")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<EntrantResponse>> createEntrant(@Valid @RequestBody EntrantRequest request);

    @Operation(
            summary = "Полное обновление участника",
            description = "Заменяет все поля участника",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Участник обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Участник не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<EntrantResponse> updateEntrant(
            @Parameter(description = "ID участника", required = true, example = "12345678") @PathVariable Long id,
            @Valid @RequestBody EntrantRequest request
    );

    @Operation(
            summary = "Частичное обновление участника",
            description = "Обновляет только переданные поля",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Участник обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Участник не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<EntrantResponse> patchEntrant(
            @Parameter(description = "ID участника", required = true, example = "12345678") @PathVariable Long id,
            @Valid @RequestBody PatchEntrantRequest request
    );

    @Operation(
            summary = "Удалить участника",
            description = "Удаляет участника. Если участник связан с активными отправлениями — возврат 409 Conflict.",
            security = @SecurityRequirement(name = LogisticApiContractConfig.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Участник удалён")
    @ApiResponse(responseCode = "404", description = "Участник не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Невозможно удалить, участник связан с отправлениями",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteEntrant(
            @Parameter(description = "ID участника", required = true, example = "12345678") @PathVariable Long id
    );
}

