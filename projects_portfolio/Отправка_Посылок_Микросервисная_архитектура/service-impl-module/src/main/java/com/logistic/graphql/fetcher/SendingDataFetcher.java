package com.logistic.graphql.fetcher;

import com.logistic.graphql.types.*;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.logistic.dto.PagedResponse;
import com.logistic.dto.SendingRequest;
import com.logistic.dto.SendingResponse;
import com.logistic.service.SendingService;
import org.springframework.stereotype.Component;

@DgsComponent
public class SendingDataFetcher {

    private final SendingService sendingService;

    public SendingDataFetcher(SendingService sendingService) {
        this.sendingService = sendingService;
    }

    @DgsQuery
    public SendingConnectionGql sendings(
            @InputArgument SendingFilterGql filter,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        String status = filter != null ? filter.status() : null;
        String trackingNumber = filter != null ? filter.trackingNumber() : null;
        Long senderId = filter != null && filter.senderId() != null ? Long.parseLong(filter.senderId()) : null;
        Long recipientId = filter != null && filter.recipientId() != null ? Long.parseLong(filter.recipientId()) : null;

        PagedResponse<SendingResponse> paged = sendingService.findAll(
                status, trackingNumber, senderId, recipientId, pageNum, pageSize
        );

        return new SendingConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements()
        );
    }

    @DgsQuery
    public SendingResponse sending(@InputArgument String id) {
        return sendingService.findById(Long.parseLong(id));
    }

    @DgsMutation
    public SendingResponse createSending(@InputArgument CreateSendingInputGql input) {
        SendingRequest request = new SendingRequest(
                input.trackingNumber(),
                input.weight(),
                input.size(),
                Long.parseLong(input.recipientId()),
                Long.parseLong(input.senderId())
        );
        return sendingService.create(request);
    }

    @DgsMutation
    public SendingResponse updateSending(
            @InputArgument String id,
            @InputArgument UpdateSendingInputGql input) {

        SendingRequest request = new SendingRequest(
                input.trackingNumber(),
                input.weight(),
                input.size(),
                input.recipientId() != null ? Long.parseLong(input.recipientId()) : null,
                input.senderId() != null ? Long.parseLong(input.senderId()) : null
        );
        return sendingService.update(Long.parseLong(id), request);
    }

    @DgsMutation
    public boolean deleteSending(@InputArgument String id) {
        sendingService.delete(Long.parseLong(id));
        return true;
    }
}
