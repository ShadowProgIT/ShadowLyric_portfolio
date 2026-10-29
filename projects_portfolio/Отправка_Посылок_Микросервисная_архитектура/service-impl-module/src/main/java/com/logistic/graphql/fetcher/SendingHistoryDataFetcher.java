package com.logistic.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.logistic.dto.PagedResponse;
import com.logistic.dto.SendingHistoryRequest;
import com.logistic.dto.SendingHistoryResponse;
import com.logistic.graphql.types.AddHistoryEventInputGql;
import com.logistic.graphql.types.PageInfoGql;
import com.logistic.graphql.types.SendingHistoryConnectionGql;
import com.logistic.service.SendingHistoryService;

@DgsComponent
public class SendingHistoryDataFetcher {

    private final SendingHistoryService historyService;

    public SendingHistoryDataFetcher(SendingHistoryService historyService) {
        this.historyService = historyService;
    }

    @DgsQuery
    public SendingHistoryConnectionGql sendingHistory(
            @InputArgument String sendingId,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 50;

        PagedResponse<SendingHistoryResponse> paged = historyService.findBySendingId(
                Long.parseLong(sendingId), pageNum, pageSize
        );

        return new SendingHistoryConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements()
        );
    }

    @DgsMutation
    public SendingHistoryResponse addHistoryEvent(
            @InputArgument String sendingId,
            @InputArgument AddHistoryEventInputGql input) {

        SendingHistoryRequest request = new SendingHistoryRequest(
                input.status(),
                input.location(),
                input.comment()
        );

        return historyService.addEvent(Long.parseLong(sendingId), request);
    }
}