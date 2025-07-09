package com.louisfiges.simulator.microservices;

import java.util.concurrent.CountDownLatch;

public abstract class CityMicroservice extends Microservice {

    private final String city;

    public CityMicroservice(CountDownLatch readyLatch, String city) {
        super(readyLatch);
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
