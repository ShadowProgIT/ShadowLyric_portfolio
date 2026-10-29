package com.logistic.graphql.types;

import com.logistic.dto.SendingResponse;
import java.util.List;

public record SendingConnectionGql(
        List<SendingResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}