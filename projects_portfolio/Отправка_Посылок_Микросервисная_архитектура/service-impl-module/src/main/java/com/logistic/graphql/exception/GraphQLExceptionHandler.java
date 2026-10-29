package com.logistic.graphql.exception;

import com.netflix.graphql.types.errors.TypedGraphQLError;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.TrackingNumberAlreadyExistsException;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class GraphQLExceptionHandler implements DataFetcherExceptionHandler {

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(
            DataFetcherExceptionHandlerParameters handlerParameters) {

        Throwable exception = handlerParameters.getException();

        if (exception instanceof ResourceNotFoundException) {
            var error = TypedGraphQLError.newNotFoundBuilder()
                    .message(exception.getMessage())
                    .path(handlerParameters.getPath())
                    .build();
            return CompletableFuture.completedFuture(
                    DataFetcherExceptionHandlerResult.newResult()
                            .error(error)
                            .build());
        }

        if (exception instanceof TrackingNumberAlreadyExistsException) {
            var error = TypedGraphQLError.newConflictBuilder()
                    .message(exception.getMessage())
                    .path(handlerParameters.getPath())
                    .build();
            return CompletableFuture.completedFuture(
                    DataFetcherExceptionHandlerResult.newResult()
                            .error(error)
                            .build());
        }

        if (exception instanceof IllegalArgumentException) {
            var error = TypedGraphQLError.newBadRequestBuilder()
                    .message(exception.getMessage())
                    .path(handlerParameters.getPath())
                    .build();
            return CompletableFuture.completedFuture(
                    DataFetcherExceptionHandlerResult.newResult()
                            .error(error)
                            .build());
        }

        var error = TypedGraphQLError.newInternalErrorBuilder()
                .message("Внутренняя ошибка сервера")
                .path(handlerParameters.getPath())
                .build();

        return CompletableFuture.completedFuture(
                DataFetcherExceptionHandlerResult.newResult()
                        .error(error)
                        .build());
    }
}
