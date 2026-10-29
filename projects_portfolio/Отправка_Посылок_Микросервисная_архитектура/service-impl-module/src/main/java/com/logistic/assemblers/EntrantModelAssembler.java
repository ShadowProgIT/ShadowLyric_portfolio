package com.logistic.assemblers;


import com.logistic.dto.EntrantResponse;
import com.logistic.endpoints.EntrantApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EntrantModelAssembler implements RepresentationModelAssembler<EntrantResponse, EntityModel<EntrantResponse>> {

    @Override
    public EntityModel<EntrantResponse> toModel(EntrantResponse dto) {
        EntityModel<EntrantResponse> model = EntityModel.of(dto);

        model.add(linkTo(methodOn(EntrantApi.class).getEntrantById(dto.getId())).withSelfRel());

        model.add(linkTo(methodOn(EntrantApi.class).getAllEntrants(0, 20)).withRel("entrants"));

        model.add(linkTo(methodOn(EntrantApi.class).updateEntrant(dto.getId(), null)).withRel("update"));

        model.add(linkTo(methodOn(EntrantApi.class).deleteEntrant(dto.getId())).withRel("delete"));

        return model;
    }
}

