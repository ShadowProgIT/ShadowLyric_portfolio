package com.logistic.graphql.scalar;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsRuntimeWiring;
import graphql.scalars.ExtendedScalars;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.stereotype.Component;

@Component
@DgsComponent
public class DateTimeScalarRegistration {

    @DgsRuntimeWiring
    public RuntimeWiring.Builder addScalars(RuntimeWiring.Builder builder) {
        return builder
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.Date);
    }
}
