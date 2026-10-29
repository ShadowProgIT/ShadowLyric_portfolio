package com.logistic.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.logistic.dto.EntrantResponse;
import com.logistic.dto.SendingResponse;
import com.logistic.service.EntrantService;
import org.springframework.stereotype.Component;

@DgsComponent
public class SendingEntrantDataFetcher {

    private final EntrantService entrantService;

    public SendingEntrantDataFetcher(EntrantService entrantService) {
        this.entrantService = entrantService;
    }

    @DgsData(parentType = "Sending", field = "sender")
    public EntrantResponse sender(DgsDataFetchingEnvironment dfe) {
        SendingResponse sending = dfe.getSource();
        if (sending.getSenderId() != null) {
            return entrantService.findById(sending.getSenderId());
        }
        return null;
    }

    @DgsData(parentType = "Sending", field = "recipient")
    public EntrantResponse recipient(DgsDataFetchingEnvironment dfe) {
        SendingResponse sending = dfe.getSource();
        if (sending.getRecipientId() != null) {
            return entrantService.findById(sending.getRecipientId());
        }
        return null;
    }
}
