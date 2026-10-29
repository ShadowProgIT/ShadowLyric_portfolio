package com.logistic.assemblers;

import com.logistic.dto.SendingHistoryResponse;
import com.logistic.endpoints.SendingApi;
import com.logistic.endpoints.SendingHistoryApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SendingHistoryModelAssembler implements RepresentationModelAssembler<SendingHistoryResponse, EntityModel<SendingHistoryResponse>> {

    @Override
    public EntityModel<SendingHistoryResponse> toModel(SendingHistoryResponse dto) {
        EntityModel<SendingHistoryResponse> model = EntityModel.of(dto);

        model.add(linkTo(methodOn(SendingHistoryApi.class)
                .addHistoryEvent(dto.getShipmentId(), null))
                .withRel("create-history"));

        model.add(linkTo(methodOn(SendingApi.class)
                .getSendingById(dto.getShipmentId()))
                .withRel("sending"));

        model.add(linkTo(methodOn(SendingApi.class)
                .getAllSendings(null, null, null, null, 0, 20))
                .withRel("sendings"));

        return model;
    }
}
