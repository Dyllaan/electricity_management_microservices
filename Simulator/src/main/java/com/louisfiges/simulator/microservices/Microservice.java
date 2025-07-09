package com.louisfiges.simulator.microservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public abstract class Microservice {

    private Process process;
    private final Logger logger = LoggerFactory.getLogger(Microservice.class);
    private final CountDownLatch readyLatch;

    public Microservice(CountDownLatch readyLatch) {
        this.readyLatch = readyLatch;
        this.process = null;
    }

    public abstract void start() throws IOException;

    protected Process startMicroservice(String serviceName, String jarPath, String profile) throws IOException {
        checkFile(jarPath);
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, profile);
        Process proc = processBuilder.start();
        this.setProcess(proc);

        logger.info("Starting {} microservice with profile: {}", serviceName, profile);
        return proc;
    }

    public void checkForServiceStart(Process process, String serviceName, String profile) {
        new Thread(() -> {
            try (Scanner scanner = new Scanner(process.getInputStream())) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if(line.contains("Error")) {
                        logger.info(line);
                    }
                    if (line.contains("Started " + serviceName + " in ")) {
                        logger.info(line);
                        readyLatch.countDown();
                    }
                }
            } catch (Exception e) {
                logger.error("Error for profile {}: {}", profile, e.getMessage());
            }
        }).start();
    }

    private void checkFile(String jarPath) {
        File file = new File(jarPath);
        if (!file.exists()) {
            logger.error("File not found: {}", jarPath);
            System.exit(1);
        }
    }

    public void stop() {
        process.destroy();
        logger.info("Microservices shut down.");
    }

    public Process getProcess() {
        return process;
    }

    protected void setProcess(Process process) {
        this.process = process;
    }

    public CountDownLatch getReadyLatch() {
        return readyLatch;
    }
}
