package com.logistic.controllers;

import com.logistic.assemblers.SendingHistoryModelAssembler;
import com.logistic.assemblers.SendingModelAssembler;
import com.logistic.dto.PatchSendingRequest;
import com.logistic.dto.SendingHistoryResponse;
import com.logistic.dto.SendingRequest;
import com.logistic.dto.SendingResponse;
import com.logistic.endpoints.SendingApi;
import com.logistic.service.SendingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendingController implements SendingApi {

    private final SendingService sendingService;
    private final SendingModelAssembler sendingModelAssembler;
    private final SendingHistoryModelAssembler historyModelAssembler;
    private final PagedResourcesAssembler<SendingResponse> pagedSendingsAssembler;
    private final PagedResourcesAssembler<SendingHistoryResponse> pagedHistoryAssembler;

    public SendingController(SendingService sendingService,
                             SendingModelAssembler sendingModelAssembler,
                             SendingHistoryModelAssembler historyModelAssembler,
                             PagedResourcesAssembler<SendingResponse> pagedSendingsAssembler,
                             PagedResourcesAssembler<SendingHistoryResponse> pagedHistoryAssembler) {
        this.sendingService = sendingService;
        this.sendingModelAssembler = sendingModelAssembler;
        this.historyModelAssembler = historyModelAssembler;
        this.pagedSendingsAssembler = pagedSendingsAssembler;
        this.pagedHistoryAssembler = pagedHistoryAssembler;
    }

    @Override
    public EntityModel<SendingResponse> getSendingById(Long id) {
        return sendingModelAssembler.toModel(sendingService.findById(id));
    }

    @Override
    public PagedModel<EntityModel<SendingResponse>> getAllSendings(
            String status, String trackingNumber, Long senderId, Long recipientId,
            int page, int size) {

        var paged = sendingService.findAll(status, trackingNumber, senderId, recipientId, page, size);
        Page<SendingResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedSendingsAssembler.toModel(springPage, sendingModelAssembler);
    }

    @Override
    public ResponseEntity<EntityModel<SendingResponse>> createSending(SendingRequest request) {
        var created = sendingService.create(request);
        var model = sendingModelAssembler.toModel(created);
        return ResponseEntity.created(model.getRequiredLink("self").toUri()).body(model);
    }

    @Override
    public EntityModel<SendingResponse> updateSending(Long id, SendingRequest request) {
        return sendingModelAssembler.toModel(sendingService.update(id, request));
    }

    @Override
    public EntityModel<SendingResponse> patchSending(Long id, PatchSendingRequest request) {
        return sendingModelAssembler.toModel(sendingService.patchSending(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteSending(Long id) {
        sendingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public PagedModel<EntityModel<SendingHistoryResponse>> getSendingHistory(Long id, int page, int size) {
        var paged = sendingService.getHistory(id, page, size);
        Page<SendingHistoryResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedHistoryAssembler.toModel(springPage, historyModelAssembler);
    }
}

