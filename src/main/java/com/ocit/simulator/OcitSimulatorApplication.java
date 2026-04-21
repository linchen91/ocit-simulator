package com.ocit.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OcitSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcitSimulatorApplication.class, args);
    }
}
