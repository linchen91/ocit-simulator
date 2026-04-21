package com.ocit.simulator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.Instant;

@Entity
public class SupplyData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String controllerId;
    private String configVersion;
    private String controllerType;
    private String manufacturer;
    private String firmwareVersion;
    private Integer maxSignalGroups;
    private Integer maxDetectorInputs;
    private Instant lastConfigurationTime;
    private Boolean isConfigured;

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

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }

    public String getControllerType() {
        return controllerType;
    }

    public void setControllerType(String controllerType) {
        this.controllerType = controllerType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public Integer getMaxSignalGroups() {
        return maxSignalGroups;
    }

    public void setMaxSignalGroups(Integer maxSignalGroups) {
        this.maxSignalGroups = maxSignalGroups;
    }

    public Integer getMaxDetectorInputs() {
        return maxDetectorInputs;
    }

    public void setMaxDetectorInputs(Integer maxDetectorInputs) {
        this.maxDetectorInputs = maxDetectorInputs;
    }

    public Instant getLastConfigurationTime() {
        return lastConfigurationTime;
    }

    public void setLastConfigurationTime(Instant lastConfigurationTime) {
        this.lastConfigurationTime = lastConfigurationTime;
    }

    public Boolean getIsConfigured() {
        return isConfigured;
    }

    public void setIsConfigured(Boolean isConfigured) {
        this.isConfigured = isConfigured;
    }

    public String getModel() {
        return controllerType != null ? controllerType : "TSC-3000";
    }

    public String getHardwareVersion() {
        return firmwareVersion != null ? firmwareVersion : "1.0";
    }

    public String getSoftwareVersion() {
        return firmwareVersion != null ? firmwareVersion : "3.1.0";
    }

    public String getDataSource() {
        return "OCIT-O";
    }
}
