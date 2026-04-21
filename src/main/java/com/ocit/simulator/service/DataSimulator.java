package com.ocit.simulator.service;

import com.ocit.simulator.model.TrafficData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class DataSimulator {

    private static final Logger logger = LoggerFactory.getLogger(DataSimulator.class);
    private final SimulatorService simulatorService;
    private final Random random = new Random();

    private static final String[] SIGNAL_STATES = {"RED", "YELLOW", "GREEN", "RED_YELLOW"};
    private static final String[] COLORS = {"RED", "YELLOW", "GREEN"};

    public DataSimulator(SimulatorService simulatorService) {
        this.simulatorService = simulatorService;
    }

    @Scheduled(fixedRate = 5000)
    public void generateTrafficData() {
        List<String> controllerIds = simulatorService.getAllControllers().stream()
                .map(c -> c.getControllerId())
                .toList();

        for (String controllerId : controllerIds) {
            try {
                TrafficData data = generateRandomTrafficData();
                simulatorService.updateTrafficData(controllerId, data);

                List<com.ocit.simulator.model.SignalGroup> groups = 
                        simulatorService.getSignalGroups(controllerId);
                for (com.ocit.simulator.model.SignalGroup group : groups) {
                    if (random.nextInt(10) < 2) {
                        String newColor = COLORS[random.nextInt(COLORS.length)];
                        simulatorService.updateSignalGroupState(controllerId, 
                                group.getGroupId(), newColor);
                    }
                }
            } catch (Exception e) {
                logger.error("Error generating data for controller {}", controllerId, e);
            }
        }
        logger.debug("Generated traffic data for {} controllers", controllerIds.size());
    }

    private TrafficData generateRandomTrafficData() {
        TrafficData data = new TrafficData();
        data.setVehicleCount(random.nextInt(100));
        data.setPedestrianCount(random.nextInt(20));
        data.setAverageSpeed(Math.round(random.nextDouble() * 50 * 100.0) / 100.0);
        data.setQueueLength(random.nextInt(50));
        data.setSignalState(SIGNAL_STATES[random.nextInt(SIGNAL_STATES.length)]);
        return data;
    }

    @Scheduled(fixedRate = 30000)
    public void checkControllerHealth() {
        List<String> controllerIds = simulatorService.getAllControllers().stream()
                .map(c -> c.getControllerId())
                .toList();

        for (String controllerId : controllerIds) {
            if (random.nextInt(100) < 5) {
                String newStatus = random.nextBoolean() ? "MAINTENANCE" : "FAULT";
                simulatorService.setControllerStatus(controllerId, newStatus);
                logger.info("Controller {} status changed to {}", controllerId, newStatus);
            } else if (random.nextInt(100) < 10) {
                simulatorService.setControllerStatus(controllerId, "OPERATIONAL");
            }
        }
    }
}
