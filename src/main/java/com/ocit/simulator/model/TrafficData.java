package com.ocit.simulator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.Instant;

@Entity
public class TrafficData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String controllerId;
    private Instant timestamp;
    private Integer vehicleCount;
    private Integer pedestrianCount;
    private Double averageSpeed;
    private Integer queueLength;
    private String signalState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(Integer vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public Integer getPedestrianCount() {
        return pedestrianCount;
    }

    public void setPedestrianCount(Integer pedestrianCount) {
        this.pedestrianCount = pedestrianCount;
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(Double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public Integer getQueueLength() {
        return queueLength;
    }

    public void setQueueLength(Integer queueLength) {
        this.queueLength = queueLength;
    }

    public String getSignalState() {
        return signalState;
    }

    public void setSignalState(String signalState) {
        this.signalState = signalState;
    }

    public String getCurrentPhase() {
        return signalState != null ? signalState : "RED";
    }

    public Integer getCycleTime() {
        return queueLength != null ? queueLength * 2 : 60;
    }
}
