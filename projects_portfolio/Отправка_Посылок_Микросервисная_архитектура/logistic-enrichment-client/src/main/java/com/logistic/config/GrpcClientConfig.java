package com.logistic.config;

import com.logistic.grpc.contract.ShipmentCalculatorGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PreDestroy;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.client.calc-server.host:localhost}")
    private String grpcHost;
    @Value("${grpc.client.calc-server.port:9090}")
    private int grpcPort;

    private ManagedChannel channel;

    @Bean
    public ManagedChannel managedChannel() {
        channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        return channel;
    }

    @Bean
    public ShipmentCalculatorGrpc.ShipmentCalculatorBlockingStub shipmentCalculatorStub(ManagedChannel channel) {
        return ShipmentCalculatorGrpc.newBlockingStub(channel);
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }
}
