package com.logistic.graphql.types;

import com.logistic.dto.SendingHistoryResponse;
import java.util.List;

public record SendingHistoryConnectionGql(
        List<SendingHistoryResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}