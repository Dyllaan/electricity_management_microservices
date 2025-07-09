package com.louisfiges.citizen.controllers;

import com.louisfiges.common.dtos.provider.ProvidersGeneratedWithWarningsDTO;
import com.louisfiges.common.dtos.simulation.CreateSimulationDTO;
import com.louisfiges.citizen.services.SimulationService;
import com.louisfiges.common.dtos.simulation.SimulationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    private static final Logger logger = LoggerFactory.getLogger(SimulationController.class);

    @Autowired
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }
    /**
     * Endpoint to submit a manual reading
     * Will try to submit the reading
     * and return what the provider responds
     * @return ResponseEntity<Response>
     */
    @PostMapping
    public ResponseEntity<SimulationStatus> simulate(@RequestBody CreateSimulationDTO createSimulationDTO) {
        try {
            simulationService.simulateReadings(
                    createSimulationDTO.startDate(),
                    createSimulationDTO.endDate()
            );


            return ResponseEntity.status(200).body(new SimulationStatus("Started"));
        } catch (Exception e) {
            logger.error("Simulation request", e.getMessage());
            logger.error(e.getStackTrace().toString());
            return ResponseEntity.internalServerError().body(new SimulationStatus("Error"));
        }
    }

}
