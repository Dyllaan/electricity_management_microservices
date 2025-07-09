package com.louisfiges.simulator.microservices.services;

import com.louisfiges.simulator.microservices.Microservice;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Citizen extends Microservice {

    public Citizen(CountDownLatch readyLatch) {
        super(readyLatch);
    }

    @Override
    public void start() throws IOException {
        Process process = startMicroservice("CitizenApplication", "citizen-1.0.0-SNAPSHOT.jar", "");
        checkForServiceStart(process, "CitizenApplication", "");
    }
}
