package com.ocit.simulator.protocol.c;

import jakarta.xml.bind.annotation.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "OCIT-C-Message")
@XmlAccessorType(XmlAccessType.FIELD)
public class OcitCMessage {

    @XmlElement(name = "MessageId")
    private String messageId;

    @XmlElement(name = "MessageType")
    private CMessageType messageType;

    @XmlElement(name = "SenderId")
    private String senderId;

    @XmlElement(name = "ReceiverId")
    private String receiverId;

    @XmlElement(name = "Timestamp")
    private Instant timestamp;

    @XmlElement(name = "ProtocolVersion")
    private Integer protocolVersion;

    @XmlElement(name = "Status")
    private MessageStatus status;

    @XmlElementWrapper(name = "SupplyDataList")
    @XmlElement(name = "SupplyData")
    private List<SupplyData> supplyDataList;

    @XmlElementWrapper(name = "ProcessDataList")
    @XmlElement(name = "ProcessData")
    private List<ProcessData> processDataList;

    @XmlElement(name = "ErrorDetails")
    private String errorDetails;

    public enum CMessageType {
        SUPPLY_DATA_REQUEST,
        SUPPLY_DATA_RESPONSE,
        PROCESS_DATA_REQUEST,
        PROCESS_DATA_RESPONSE,
        STATUS_REQUEST,
        STATUS_RESPONSE,
        CONTROL_COMMAND,
        CONTROL_ACK,
        HEARTBEAT,
        ERROR
    }

    public enum MessageStatus {
        SUCCESS,
        FAILURE,
        PENDING,
        TIMEOUT
    }

    public OcitCMessage() {
        this.supplyDataList = new ArrayList<>();
        this.processDataList = new ArrayList<>();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public CMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(CMessageType messageType) {
        this.messageType = messageType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(Integer protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public List<SupplyData> getSupplyDataList() {
        return supplyDataList;
    }

    public void setSupplyDataList(List<SupplyData> supplyDataList) {
        this.supplyDataList = supplyDataList;
    }

    public List<ProcessData> getProcessDataList() {
        return processDataList;
    }

    public void setProcessDataList(List<ProcessData> processDataList) {
        this.processDataList = processDataList;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    @XmlRootElement(name = "SupplyData")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SupplyData {

        @XmlElement(name = "ObjectId")
        private String objectId;

        @XmlElement(name = "ObjectType")
        private String objectType;

        @XmlElement(name = "ObjectName")
        private String objectName;

        @XmlElement(name = "DataSource")
        private String dataSource;

        @XmlElement(name = "InstallationDate")
        private String installationDate;

        @XmlElement(name = "Manufacturer")
        private String manufacturer;

        @XmlElement(name = "Model")
        private String model;

        @XmlElement(name = "SoftwareVersion")
        private String softwareVersion;

        @XmlElement(name = "HardwareVersion")
        private String hardwareVersion;

        @XmlElement(name = "Status")
        private String status;

        @XmlElementWrapper(name = "AttributeList")
        @XmlElement(name = "Attribute")
        private List<Attribute> attributes;

        public SupplyData() {
            this.attributes = new ArrayList<>();
        }

        public String getObjectId() { return objectId; }
        public void setObjectId(String objectId) { this.objectId = objectId; }

        public String getObjectType() { return objectType; }
        public void setObjectType(String objectType) { this.objectType = objectType; }

        public String getObjectName() { return objectName; }
        public void setObjectName(String objectName) { this.objectName = objectName; }

        public String getDataSource() { return dataSource; }
        public void setDataSource(String dataSource) { this.dataSource = dataSource; }

        public String getInstallationDate() { return installationDate; }
        public void setInstallationDate(String installationDate) { this.installationDate = installationDate; }

        public String getManufacturer() { return manufacturer; }
        public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }

        public String getSoftwareVersion() { return softwareVersion; }
        public void setSoftwareVersion(String softwareVersion) { this.softwareVersion = softwareVersion; }

        public String getHardwareVersion() { return hardwareVersion; }
        public void setHardwareVersion(String hardwareVersion) { this.hardwareVersion = hardwareVersion; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public List<Attribute> getAttributes() { return attributes; }
        public void setAttributes(List<Attribute> attributes) { this.attributes = attributes; }
    }

    @XmlRootElement(name = "ProcessData")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ProcessData {

        @XmlElement(name = "ObjectId")
        private String objectId;

        @XmlElement(name = "Timestamp")
        private Instant timestamp;

        @XmlElement(name = "Active")
        private Boolean active;

        @XmlElement(name = "CycleTime")
        private Integer cycleTime;

        @XmlElement(name = "CurrentPhase")
        private String currentPhase;

        @XmlElement(name = "SignalPlanId")
        private String signalPlanId;

        @XmlElementWrapper(name = "MeasurementList")
        @XmlElement(name = "Measurement")
        private List<Measurement> measurements;

        public ProcessData() {
            this.measurements = new ArrayList<>();
        }

        public String getObjectId() { return objectId; }
        public void setObjectId(String objectId) { this.objectId = objectId; }

        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }

        public Integer getCycleTime() { return cycleTime; }
        public void setCycleTime(Integer cycleTime) { this.cycleTime = cycleTime; }

        public String getCurrentPhase() { return currentPhase; }
        public void setCurrentPhase(String currentPhase) { this.currentPhase = currentPhase; }

        public String getSignalPlanId() { return signalPlanId; }
        public void setSignalPlanId(String signalPlanId) { this.signalPlanId = signalPlanId; }

        public List<Measurement> getMeasurements() { return measurements; }
        public void setMeasurements(List<Measurement> measurements) { this.measurements = measurements; }
    }

    @XmlRootElement(name = "Attribute")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Attribute {

        @XmlAttribute(name = "name")
        private String name;

        @XmlAttribute(name = "value")
        private String value;

        @XmlAttribute(name = "unit")
        private String unit;

        public Attribute() {}

        public Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public Attribute(String name, String value, String unit) {
            this.name = name;
            this.value = value;
            this.unit = unit;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
    }

    @XmlRootElement(name = "Measurement")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Measurement {

        @XmlElement(name = "MemberNumber")
        private Integer memberNumber;

        @XmlElement(name = "Value")
        private String value;

        @XmlElement(name = "Unit")
        private String unit;

        @XmlElement(name = "Quality")
        private String quality;

        public Measurement() {}

        public Measurement(Integer memberNumber, String value) {
            this.memberNumber = memberNumber;
            this.value = value;
        }

        public Measurement(Integer memberNumber, String value, String unit) {
            this.memberNumber = memberNumber;
            this.value = value;
            this.unit = unit;
        }

        public Integer getMemberNumber() { return memberNumber; }
        public void setMemberNumber(Integer memberNumber) { this.memberNumber = memberNumber; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }

        public String getQuality() { return quality; }
        public void setQuality(String quality) { this.quality = quality; }
    }
}
