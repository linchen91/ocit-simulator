package com.ocit.simulator.controller;

import com.ocit.simulator.protocol.datex2.Datex2Message;
import com.ocit.simulator.protocol.datex2.Datex2ProtocolHandler;
import com.ocit.simulator.service.SimulatorService;
import jakarta.xml.bind.JAXBException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/datex2")
public class Datex2RestController {

    private final Datex2ProtocolHandler protocolHandler;
    private final SimulatorService simulatorService;

    public Datex2RestController(Datex2ProtocolHandler protocolHandler, SimulatorService simulatorService) {
        this.protocolHandler = protocolHandler;
        this.simulatorService = simulatorService;
    }

    @GetMapping(value = "/situation", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getSituation() {
        try {
            var controllers = simulatorService.getAllControllers();
            var status = simulatorService.getSystemStatus();
            
            List<String> situations = controllers.stream()
                .map(c -> c.getStatus())
                .toList();
            
            StringBuilder locations = new StringBuilder();
            for (var c : controllers) {
                if (locations.length() > 0) locations.append(";");
                locations.append(c.getLatitude()).append(",").append(c.getLongitude());
            }
            
            Datex2Message message = protocolHandler.createSituationPublication(
                "normal",
                locations.toString(),
                "Active controllers: " + situations.size()
            );
            
            String xml = protocolHandler.serializeMessage(message);
            return ResponseEntity.ok(xml);
        } catch (JAXBException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/measured", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getMeasuredData() {
        try {
            var controllers = simulatorService.getAllControllers();
            var trafficData = simulatorService.getLatestTrafficData(controllers.get(0).getControllerId());
            
            float speed = 0;
            int vehicles = 0;
            float occupancy = 0;
            
            if (trafficData != null) {
                speed = trafficData.getAverageSpeed() != null ? trafficData.getAverageSpeed().floatValue() : 50f;
                vehicles = trafficData.getVehicleCount() != null ? trafficData.getVehicleCount() : 10;
            }
            
            Datex2Message message = protocolHandler.createMeasuredData(
                controllers.get(0).getControllerId(),
                speed,
                vehicles,
                occupancy
            );
            
            String xml = protocolHandler.serializeMessage(message);
            return ResponseEntity.ok(xml);
        } catch (JAXBException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/elaborated", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getElaboratedData() {
        try {
            var controllers = simulatorService.getAllControllers();
            var status = simulatorService.getSystemStatus();
            
            Datex2Message message = protocolHandler.createElaboratedData(
                "SYSTEM",
                String.valueOf(status.get("totalControllers")),
                "operational"
            );
            
            String xml = protocolHandler.serializeMessage(message);
            return ResponseEntity.ok(xml);
        } catch (JAXBException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
