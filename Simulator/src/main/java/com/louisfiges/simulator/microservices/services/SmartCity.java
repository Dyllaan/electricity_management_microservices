package com.louisfiges.simulator.microservices.services;

import com.louisfiges.simulator.microservices.CityMicroservice;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class SmartCity extends CityMicroservice {

    public SmartCity(CountDownLatch readyLatch, String city) {
        super(readyLatch, city);
    }

    @Override
    public void start() throws IOException {
        Process process = startMicroservice("SmartCityApplication", "smartcity-1.0.1-RELEASE.jar", "--spring.profiles.active=" + this.getCity());
        checkForServiceStart(process, "SmartCityApplication", "");
    }
}
