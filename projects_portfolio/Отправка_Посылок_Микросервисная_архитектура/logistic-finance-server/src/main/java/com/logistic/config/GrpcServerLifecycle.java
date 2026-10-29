package com.logistic.config;

import com.logistic.service.ShipmentCalculatorServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class GrpcServerLifecycle implements SmartLifecycle {

    private final ShipmentCalculatorServiceImpl calculatorService;
    private final int grpcPort;
    private Server server;
    private boolean running = false;

    public GrpcServerLifecycle(ShipmentCalculatorServiceImpl calculatorService,
                               @Value("${grpc.server.port:9090}") int grpcPort) {
        this.calculatorService = calculatorService;
        this.grpcPort = grpcPort;
    }

    @Override
    public void start() {
        server = ServerBuilder.forPort(grpcPort)
                .addService(calculatorService)
                .build();
        try {
            server.start();
            running = true;
            System.out.println("gRPC-сервер запущен на порту " + grpcPort);
            System.out.println("Сервис: ShipmentCalculator.EstimateETA()");
        } catch (Exception e) {
            throw new RuntimeException("Не удалось запустить gRPC-сервер", e);
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            server.shutdown();
            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
