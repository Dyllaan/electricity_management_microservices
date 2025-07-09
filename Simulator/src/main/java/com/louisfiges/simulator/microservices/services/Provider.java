package com.louisfiges.simulator.microservices.services;

import com.louisfiges.simulator.microservices.CityMicroservice;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Provider extends CityMicroservice {

    private String profile;

    public Provider(CountDownLatch readyLatch, String profile, String city) {
        super(readyLatch, city);
        this.profile = profile;
    }

    @Override
    public void start() throws IOException {
        profile = "--spring.profiles.active=" + profile + " --city=" + this.getCity();
        Process process = startMicroservice("ProviderApplication", "provider-2.0.6-RELEASE.jar", profile);
        checkForServiceStart(process, "ProviderApplication", profile);
    }
}
