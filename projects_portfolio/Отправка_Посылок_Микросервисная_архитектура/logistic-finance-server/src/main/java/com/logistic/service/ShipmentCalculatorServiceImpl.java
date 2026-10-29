package com.logistic.service;

import com.logistic.grpc.contract.ETACalculationRequest;
import com.logistic.grpc.contract.ETACalculationResponse;
import com.logistic.grpc.contract.ShipmentCalculatorGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShipmentCalculatorServiceImpl extends ShipmentCalculatorGrpc.ShipmentCalculatorImplBase {

    private static final Logger log = LoggerFactory.getLogger(ShipmentCalculatorServiceImpl.class);

    @Override
    public void calculateETA(ETACalculationRequest request, StreamObserver<ETACalculationResponse> responseObserver) {
        log.info("gRPC запрос: расчёт ETA для отправки id={} ({} → {}), вес={} кг",
                request.getSendingId(), request.getOriginCity(), request.getDestCity(), request.getWeightKg());

        int estimatedMinutes = calculateMockETA(request);
        String routeStatus = "OPTIMAL";

        ETACalculationResponse response = ETACalculationResponse.newBuilder()
                .setSendingId(request.getSendingId())
                .setEstimatedMinutes(estimatedMinutes)
                .setRouteStatus(routeStatus)
                .build();

        log.info("gRPC ответ: отправка id={}, ETA={} мин, статус={}",
                request.getSendingId(), estimatedMinutes, routeStatus);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private int calculateMockETA(ETACalculationRequest req) {
        int baseTime = 1440;
        double weightFactor = Math.max(req.getWeightKg(), 1.0) * 0.5;
        return (int) (baseTime + weightFactor);
    }
}
