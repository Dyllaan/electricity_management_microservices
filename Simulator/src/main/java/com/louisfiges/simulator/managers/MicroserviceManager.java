package com.louisfiges.simulator.managers;

import com.louisfiges.simulator.microservices.*;

import java.io.IOException;
import java.util.ArrayList;

public class MicroserviceManager {

    private final ArrayList<Microservice> microservices;

    public MicroserviceManager() {
        microservices = new ArrayList<>();
    }

    public void add(Microservice microservice) {
        microservices.add(microservice);
    }

    public void startAll() throws IOException {
        for (Microservice microservice : microservices) {
            microservice.start();
        }
    }

    public void shutdown() {
        for (Microservice microservice : microservices) {
            microservice.stop();
        }
    }
}
