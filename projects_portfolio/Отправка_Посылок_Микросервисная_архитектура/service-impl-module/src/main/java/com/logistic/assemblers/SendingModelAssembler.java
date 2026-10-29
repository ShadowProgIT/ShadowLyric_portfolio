package com.logistic.assemblers;

import com.logistic.dto.SendingResponse;
import com.logistic.endpoints.SendingApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SendingModelAssembler implements RepresentationModelAssembler<SendingResponse, EntityModel<SendingResponse>> {

    @Override
    public EntityModel<SendingResponse> toModel(SendingResponse dto) {
        EntityModel<SendingResponse> model = EntityModel.of(dto);

        model.add(linkTo(methodOn(SendingApi.class).getSendingById(dto.getId())).withSelfRel());
        model.add(linkTo(methodOn(SendingApi.class).getAllSendings(null, null, null, null, 0, 20)).withRel("sendings"));
        model.add(linkTo(methodOn(SendingApi.class).getSendingHistory(dto.getId(), 0, 50)).withRel("history"));
        model.add(linkTo(methodOn(SendingApi.class).updateSending(dto.getId(), null)).withRel("update"));
        model.add(linkTo(methodOn(SendingApi.class).deleteSending(dto.getId())).withRel("delete"));

        return model;
    }
}

