package com.ocit.simulator.controller;

import com.ocit.simulator.model.TrafficSignalController;
import com.ocit.simulator.model.SupplyData;
import com.ocit.simulator.model.SignalGroup;
import com.ocit.simulator.model.TrafficData;
import com.ocit.simulator.service.SimulatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/controllers")
public class TrafficControllerRestController {

    private final SimulatorService simulatorService;

    public TrafficControllerRestController(SimulatorService simulatorService) {
        this.simulatorService = simulatorService;
    }

    @GetMapping
    public ResponseEntity<List<TrafficSignalController>> getAllControllers() {
        return ResponseEntity.ok(simulatorService.getAllControllers());
    }

    @GetMapping("/{controllerId}")
    public ResponseEntity<TrafficSignalController> getController(
            @PathVariable String controllerId) {
        return simulatorService.getController(controllerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{controllerId}/supply")
    public ResponseEntity<SupplyData> getSupplyData(@PathVariable String controllerId) {
        SupplyData supplyData = simulatorService.getSupplyData(controllerId);
        if (supplyData == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(supplyData);
    }

    @GetMapping("/{controllerId}/traffic")
    public ResponseEntity<TrafficData> getTrafficData(@PathVariable String controllerId) {
        TrafficData data = simulatorService.getLatestTrafficData(controllerId);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{controllerId}/signals")
    public ResponseEntity<List<SignalGroup>> getSignalGroups(@PathVariable String controllerId) {
        List<SignalGroup> groups = simulatorService.getSignalGroups(controllerId);
        return ResponseEntity.ok(groups);
    }

    @PutMapping("/{controllerId}/status")
    public ResponseEntity<Void> setControllerStatus(
            @PathVariable String controllerId,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        if (status == null) {
            return ResponseEntity.badRequest().build();
        }
        simulatorService.setControllerStatus(controllerId, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{controllerId}/signals/{groupId}")
    public ResponseEntity<Void> setSignalState(
            @PathVariable String controllerId,
            @PathVariable String groupId,
            @RequestBody Map<String, String> request) {
        String color = request.get("color");
        if (color == null) {
            return ResponseEntity.badRequest().build();
        }
        simulatorService.updateSignalGroupState(controllerId, groupId, color);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        return ResponseEntity.ok(simulatorService.getSystemStatus());
    }
}
