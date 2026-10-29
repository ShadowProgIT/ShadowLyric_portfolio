package com.logistic.graphql.types;

import com.logistic.dto.EntrantResponse;
import java.util.List;

public record EntrantConnectionGql(
        List<EntrantResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {}