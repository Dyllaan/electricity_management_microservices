package com.louisfiges.simulator.services;

import com.louisfiges.common.SourceDefinitions;
import com.louisfiges.common.dtos.customer.CustomerDTO;
import com.louisfiges.common.dtos.simulation.SimulationStatus;
import com.louisfiges.common.dtos.subject.CreateSubjectDTO;
import com.louisfiges.common.dtos.subject.SubjectDTO;
import com.louisfiges.simulator.config.CityConfig;
import com.louisfiges.simulator.simulators.CustomerSimulator;
import com.louisfiges.simulator.simulators.ReadingSimulator;
import com.louisfiges.simulator.simulators.SubjectSimulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SimulationService {

    private final Logger logger = LoggerFactory.getLogger(SimulationService.class);

    private final int MANUAL_CUSTOMERS = 100;
    private final int SMART_METER_CUSTOMERS = 50;

    private final CityConfig cityConfig;

    private final List<String> providerUris;

    @Autowired
    public SimulationService(CityConfig cityConfig) {
        this.cityConfig = cityConfig;
        this.providerUris = new ArrayList<>();
        providerUris.add("prov-a");
        providerUris.add("prov-b");
        providerUris.add("prov-c");
    }

    /**
     * Simulates customers and assigns them to random providers and sources
     */
    @Async
    public void simulate() {
        simulateForSource(providerUris, SourceDefinitions.MANUAL_SOURCE_NAME, MANUAL_CUSTOMERS);
        simulateForSource(providerUris, SourceDefinitions.SMART_METER_SOURCE_NAME, SMART_METER_CUSTOMERS);
        ReadingSimulator readingSim = new ReadingSimulator(getGatewayUrl());
        SimulationStatus status = readingSim.sendSimulationRequest();
        logger.info("Simulation request status: {}", status);
    }

    /**
     * Simulates customers and assigns them to random providers and sources
     *
     * @param providerUris provider URIs
     * @param source       Source to assign
     * @param totalCustomers Total number of customers
     */
    private void simulateForSource(List<String> providerUris, String source, int totalCustomers) {
        List<String> shuffledProviders = new ArrayList<>(providerUris);
        Collections.shuffle(shuffledProviders);

        int customersPerProvider = totalCustomers / providerUris.size();
        int remainingCustomers = totalCustomers % providerUris.size();

        for (int i = 0; i < providerUris.size(); i++) {
            String providerUri = shuffledProviders.get(i);
            int customersToAssign = customersPerProvider + (i < remainingCustomers ? 1 : 0);

            simulateForProviderAndSource(providerUri, source, customersToAssign);
        }
    }

    /**
     * Send customer and subject data to the specified provider.
     */
    private void simulateForProviderAndSource(String providerUri, String source, int customerCount) {
        CustomerSimulator cusSim = new CustomerSimulator(providerUri, getGatewayUrl());

        List<CustomerDTO> customerDTOs = cusSim.sendCustomers(cusSim.createCustomers(customerCount));
        logger.info("Sent {} customers to provider: {}", customerCount, providerUri);

        SubjectSimulator subSim = new SubjectSimulator(providerUri, getGatewayUrl());
        List<CreateSubjectDTO> cSubjectDTOs = subSim.createSubjects(source, customerDTOs);
        List<SubjectDTO> subjectDTOs = subSim.sendSubjects(cSubjectDTOs);
        logger.info("Sent {} subjects to provider: {}", customerCount, providerUri);
    }

    private String getGatewayUrl() {
        return "http://" + cityConfig.getName() + "-api-gateway:8080/";
    }

}
