package com.ocit.simulator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class SignalGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String groupId;
    private String controllerId;
    private String signalColor;
    private Integer phaseDuration;
    private Integer currentPhase;
    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public String getSignalColor() {
        return signalColor;
    }

    public void setSignalColor(String signalColor) {
        this.signalColor = signalColor;
    }

    public Integer getPhaseDuration() {
        return phaseDuration;
    }

    public void setPhaseDuration(Integer phaseDuration) {
        this.phaseDuration = phaseDuration;
    }

    public Integer getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Integer currentPhase) {
        this.currentPhase = currentPhase;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getPhase() {
        return currentPhase != null ? currentPhase : 1;
    }
}
