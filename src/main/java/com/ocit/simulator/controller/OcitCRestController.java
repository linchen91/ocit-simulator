package com.ocit.simulator.controller;

import com.ocit.simulator.protocol.c.OcitCMessage;
import com.ocit.simulator.protocol.c.OcitCProtocolHandler;
import com.ocit.simulator.service.SimulatorService;
import jakarta.xml.bind.JAXBException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ocit-c")
public class OcitCRestController {

    private final OcitCProtocolHandler protocolHandler;
    private final SimulatorService simulatorService;

    public OcitCRestController(OcitCProtocolHandler protocolHandler, SimulatorService simulatorService) {
        this.protocolHandler = protocolHandler;
        this.simulatorService = simulatorService;
    }

    @GetMapping(value = "/supply", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getSupplyData() {
        try {
            var controllers = simulatorService.getAllControllers();
            var supply = simulatorService.getSupplyData(controllers.get(0).getControllerId());
            
            var message = protocolHandler.createSupplyDataResponse("SIMULATOR", "CENTRAL");
            
            if (supply != null) {
                var supplyData = new OcitCMessage.SupplyData();
                supplyData.setObjectId(controllers.get(0).getControllerId());
                supplyData.setObjectType("LSA");
                supplyData.setManufacturer(supply.getManufacturer() != null ? supply.getManufacturer() : "Unknown");
                supplyData.setModel(supply.getControllerType() != null ? supply.getControllerType() : "LSA");
                supplyData.setSoftwareVersion(supply.getFirmwareVersion() != null ? supply.getFirmwareVersion() : "1.0");
                supplyData.setHardwareVersion("1.0");
                supplyData.setDataSource("OCIT-O");
                supplyData.setStatus(controllers.get(0).getStatus());
                message.getSupplyDataList().add(supplyData);
            }
            
            String xml = protocolHandler.serializeMessage(message);
            return ResponseEntity.ok(xml);
        } catch (JAXBException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/process", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getProcessData() {
        try {
            var controllers = simulatorService.getAllControllers();
            var traffic = simulatorService.getLatestTrafficData(controllers.get(0).getControllerId());
            
            var message = protocolHandler.createProcessDataResponse("SIMULATOR", "CENTRAL");
            
            var processData = new OcitCMessage.ProcessData();
            processData.setObjectId(controllers.get(0).getControllerId());
            
            if (traffic != null) {
                processData.setTimestamp(traffic.getTimestamp());
                processData.setActive(true);
                processData.setCycleTime(traffic.getQueueLength() != null ? traffic.getQueueLength() * 2 : 60);
            } else {
                processData.setActive(false);
                processData.setCycleTime(60);
            }
            
            message.getProcessDataList().add(processData);
            
            String xml = protocolHandler.serializeMessage(message);
            return ResponseEntity.ok(xml);
        } catch (JAXBException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/status", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getStatus() {
        try {
            var status = simulatorService.getSystemStatus();
            
            var message = protocolHandler.createStatusResponse("SIMULATOR", "CENTRAL");
            
            String xml = protocolHandler.serializeMessage(message);
            return ResponseEntity.ok(xml);
        } catch (JAXBException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/heartbeat", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getHeartbeat() {
        try {
            var message = protocolHandler.createHeartbeat("SIMULATOR", "CENTRAL");
            String xml = protocolHandler.serializeMessage(message);
            return ResponseEntity.ok(xml);
        } catch (JAXBException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
