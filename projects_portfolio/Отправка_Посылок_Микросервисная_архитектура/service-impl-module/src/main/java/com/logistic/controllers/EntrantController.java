package com.logistic.controllers;


import com.logistic.assemblers.EntrantModelAssembler;
import com.logistic.dto.EntrantRequest;
import com.logistic.dto.EntrantResponse;
import com.logistic.dto.PagedResponse;
import com.logistic.dto.PatchEntrantRequest;
import com.logistic.endpoints.EntrantApi;
import com.logistic.service.EntrantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntrantController implements EntrantApi {

    private final EntrantService entrantService;
    private final EntrantModelAssembler entrantModelAssembler;
    private final PagedResourcesAssembler<EntrantResponse> pagedEntrantsAssembler;

    public EntrantController(EntrantService entrantService,
                             EntrantModelAssembler entrantModelAssembler,
                             PagedResourcesAssembler<EntrantResponse> pagedEntrantsAssembler) {
        this.entrantService = entrantService;
        this.entrantModelAssembler = entrantModelAssembler;
        this.pagedEntrantsAssembler = pagedEntrantsAssembler;
    }

    @Override
    public PagedModel<EntityModel<EntrantResponse>> getAllEntrants(int page, int size) {
        PagedResponse<EntrantResponse> paged = entrantService.findAll(page, size);
        Page<EntrantResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedEntrantsAssembler.toModel(springPage, entrantModelAssembler);
    }

    @Override
    public EntityModel<EntrantResponse> getEntrantById(Long id) {
        return entrantModelAssembler.toModel(entrantService.findById(id));
    }

    @Override
    public ResponseEntity<EntityModel<EntrantResponse>> createEntrant(EntrantRequest request) {
        EntrantResponse created = entrantService.create(request);
        EntityModel<EntrantResponse> model = entrantModelAssembler.toModel(created);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<EntrantResponse> updateEntrant(Long id, EntrantRequest request) {
        return entrantModelAssembler.toModel(entrantService.update(id, request));
    }

    @Override
    public EntityModel<EntrantResponse> patchEntrant(Long id, PatchEntrantRequest request) {
        return entrantModelAssembler.toModel(entrantService.patchEntrant(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteEntrant(Long id) {
        entrantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

