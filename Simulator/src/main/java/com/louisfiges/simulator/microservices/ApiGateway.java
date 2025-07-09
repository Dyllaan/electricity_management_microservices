package com.louisfiges.simulator.microservices;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ApiGateway extends Microservice {

    public ApiGateway(CountDownLatch readyLatch) {
        super(readyLatch);
    }

    @Override
    public void start() throws IOException {
        Process process = startMicroservice("ApiGatewayApplication", "APIGateway-1.0.0-RELEASE.jar", "");
        checkForServiceStart(process, "ApiGatewayApplication", "");
    }
}
