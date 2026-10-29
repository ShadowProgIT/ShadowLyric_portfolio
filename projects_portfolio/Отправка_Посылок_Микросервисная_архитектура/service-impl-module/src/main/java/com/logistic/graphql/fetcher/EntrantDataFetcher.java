package com.logistic.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.logistic.dto.EntrantRequest;
import com.logistic.dto.EntrantResponse;
import com.logistic.dto.PagedResponse;
import com.logistic.graphql.types.CreateEntrantInputGql;
import com.logistic.graphql.types.EntrantConnectionGql;
import com.logistic.graphql.types.PageInfoGql;
import com.logistic.graphql.types.UpdateEntrantInputGql;
import com.logistic.service.EntrantService;
import org.springframework.stereotype.Component;

@DgsComponent
public class EntrantDataFetcher {

    private final EntrantService entrantService;

    public EntrantDataFetcher(EntrantService entrantService) {
        this.entrantService = entrantService;
    }

    @DgsQuery
    public EntrantConnectionGql entrants(
            @InputArgument Integer page,
            @InputArgument Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PagedResponse<EntrantResponse> paged = entrantService.findAll(pageNum, pageSize);

        return new EntrantConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements()
        );
    }

    @DgsQuery
    public EntrantResponse entrant(@InputArgument String id) {
        return entrantService.findById(Long.parseLong(id));
    }

    @DgsMutation
    public EntrantResponse createEntrant(@InputArgument CreateEntrantInputGql input) {
        EntrantRequest request = new EntrantRequest(
                input.firstname(),
                input.lastname(),
                input.email(),
                input.phone(),
                input.address()
        );
        return entrantService.create(request);
    }

    @DgsMutation
    public EntrantResponse updateEntrant(
            @InputArgument String id,
            @InputArgument UpdateEntrantInputGql input) {

        EntrantRequest request = new EntrantRequest(
                input.firstname(),
                input.lastname(),
                input.email(),
                input.phone(),
                input.address()
        );
        return entrantService.update(Long.parseLong(id), request);
    }

    @DgsMutation
    public boolean deleteEntrant(@InputArgument String id) {
        entrantService.delete(Long.parseLong(id));
        return true;
    }
}
