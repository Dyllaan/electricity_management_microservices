package com.louisfiges.simulator.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DatabasesManager {

    private final Logger logger = LoggerFactory.getLogger(DatabasesManager.class);
    public boolean start() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker-compose", "-f", "docker-compose.yml", "up", "-d");

            Process process = processBuilder.start();

            errorCheck(process);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }

            logger.info("Databases started successfully with Docker Compose");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean stop() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker-compose", "-f", "docker-compose.yml", "down");
            Process stopProcess = processBuilder.start();
            int exitCode = stopProcess.waitFor();

            logger.info("Databases stopped with code: " + exitCode);
            return exitCode == 0;
        } catch (Exception e) {
            logger.error("Error stopping databases");
            logger.error(e.getMessage());
            return false;
        }
    }

    private void errorCheck(Process process) {
        try {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = errorReader.readLine()) != null) {
                if(line.contains("error during connect: Get")) {
                    logger.error("Docker is not running, start docker and retry");
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            logger.error("Error checking for errors in process");
            logger.error(e.getMessage());
        }
    }
}
