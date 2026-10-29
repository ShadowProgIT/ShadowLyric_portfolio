package com.logistic.controllers;

import com.logistic.assemblers.SendingHistoryModelAssembler;
import com.logistic.dto.SendingHistoryRequest;
import com.logistic.dto.SendingHistoryResponse;
import com.logistic.endpoints.SendingHistoryApi;
import com.logistic.service.SendingHistoryService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class SendingHistoryController implements SendingHistoryApi {

    private final SendingHistoryService historyService;
    private final SendingHistoryModelAssembler historyModelAssembler;

    public SendingHistoryController(SendingHistoryService historyService,
                                    SendingHistoryModelAssembler historyModelAssembler) {
        this.historyService = historyService;
        this.historyModelAssembler = historyModelAssembler;
    }

    @Override
    public ResponseEntity<EntityModel<SendingHistoryResponse>> addHistoryEvent(
            Long sendingId, SendingHistoryRequest request) {

        SendingHistoryResponse created = historyService.addEvent(sendingId, request);

        EntityModel<SendingHistoryResponse> model = historyModelAssembler.toModel(created);
        return ResponseEntity
                .created(linkTo(methodOn(SendingHistoryApi.class)
                        .addHistoryEvent(sendingId, null)).toUri())
                .body(model);
    }
}

