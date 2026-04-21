package com.ocit.simulator.config;

import com.ocit.simulator.protocol.OcitProtocolHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProtocolConfig {

    @Bean
    public OcitProtocolHandler ocitProtocolHandler() {
        try {
            return new OcitProtocolHandler();
        } catch (jakarta.xml.bind.JAXBException e) {
            throw new RuntimeException("Failed to create OcitProtocolHandler", e);
        }
    }
}
