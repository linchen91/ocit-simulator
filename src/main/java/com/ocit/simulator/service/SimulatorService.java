package com.ocit.simulator.service;

import com.ocit.simulator.model.TrafficSignalController;
import com.ocit.simulator.model.TrafficData;
import com.ocit.simulator.model.SignalGroup;
import com.ocit.simulator.model.SupplyData;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimulatorService {

    private final Map<String, TrafficSignalController> controllers = new ConcurrentHashMap<>();
    private final Map<String, TrafficData> latestTrafficData = new ConcurrentHashMap<>();
    private final Map<String, List<SignalGroup>> signalGroups = new ConcurrentHashMap<>();

    public SimulatorService() {
        initializeSampleControllers();
    }

    private void initializeSampleControllers() {
        // Munich traffic signal controllers (Marienplatz area)
        String[][] munichControllers = {
            {"LSA-001", "Marienplatz", "48.1372", "11.5755"},
            {"LSA-002", "Odeonsplatz", "48.1428", "11.5804"},
            {"LSA-003", "Karlsplatz", "48.1387", "11.5695"},
            {"LSA-004", "Sendlinger Tor", "48.1339", "11.5667"},
            {"LSA-005", "Frauenkirche", "48.1360", "11.5784"}
        };

        for (String[] data : munichControllers) {
            String controllerId = data[0];
            TrafficSignalController controller = new TrafficSignalController();
            controller.setControllerId(controllerId);
            controller.setName(data[1]);
            controller.setStatus("OPERATIONAL");
            controller.setVersion(3);
            controller.setLastUpdate(Instant.now());
            controller.setLatitude(Double.parseDouble(data[2]));
            controller.setLongitude(Double.parseDouble(data[3]));
            controller.setLocation(data[1] + " (" + data[2] + ", " + data[3] + ")");
            controllers.put(controllerId, controller);

            initializeSignalGroups(controllerId);
        }
    }

    private void initializeSignalGroups(String controllerId) {
        List<SignalGroup> groups = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            SignalGroup group = new SignalGroup();
            group.setGroupId(controllerId + "-SG-" + i);
            group.setControllerId(controllerId);
            group.setSignalColor("RED");
            group.setPhaseDuration(30);
            group.setCurrentPhase(i);
            group.setIsActive(true);
            groups.add(group);
        }
        signalGroups.put(controllerId, groups);
    }

    public List<TrafficSignalController> getAllControllers() {
        return new ArrayList<>(controllers.values());
    }

    public Optional<TrafficSignalController> getController(String controllerId) {
        return Optional.ofNullable(controllers.get(controllerId));
    }

    public TrafficData getLatestTrafficData(String controllerId) {
        return latestTrafficData.get(controllerId);
    }

    public Map<String, TrafficData> getAllLatestTrafficData() {
        return new HashMap<>(latestTrafficData);
    }

    public List<SignalGroup> getSignalGroups(String controllerId) {
        return signalGroups.getOrDefault(controllerId, Collections.emptyList());
    }

    public SupplyData getSupplyData(String controllerId) {
        TrafficSignalController controller = controllers.get(controllerId);
        if (controller == null) {
            return null;
        }

        SupplyData supplyData = new SupplyData();
        supplyData.setControllerId(controllerId);
        supplyData.setConfigVersion("1.0." + controller.getVersion());
        supplyData.setControllerType("TSC-3000");
        supplyData.setManufacturer("OCIT Simulator");
        supplyData.setFirmwareVersion("3.1.0");
        supplyData.setMaxSignalGroups(8);
        supplyData.setMaxDetectorInputs(16);
        supplyData.setLastConfigurationTime(Instant.now());
        supplyData.setIsConfigured(true);
        return supplyData;
    }

    public void updateTrafficData(String controllerId, TrafficData data) {
        data.setControllerId(controllerId);
        data.setTimestamp(Instant.now());
        latestTrafficData.put(controllerId, data);

        TrafficSignalController controller = controllers.get(controllerId);
        if (controller != null) {
            controller.setLastUpdate(Instant.now());
        }
    }

    public void updateSignalGroupState(String controllerId, String groupId, String newColor) {
        List<SignalGroup> groups = signalGroups.get(controllerId);
        if (groups != null) {
            for (SignalGroup group : groups) {
                if (group.getGroupId().equals(groupId)) {
                    group.setSignalColor(newColor);
                    break;
                }
            }
        }
    }

    public void setControllerStatus(String controllerId, String status) {
        TrafficSignalController controller = controllers.get(controllerId);
        if (controller != null) {
            controller.setStatus(status);
            controller.setLastUpdate(Instant.now());
        }
    }

    public Map<String, Object> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("totalControllers", controllers.size());
        status.put("operationalControllers", controllers.values().stream()
                .filter(c -> "OPERATIONAL".equals(c.getStatus()))
                .count());
        status.put("timestamp", Instant.now());
        return status;
    }
}
