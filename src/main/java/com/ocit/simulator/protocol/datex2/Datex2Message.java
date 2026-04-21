package com.ocit.simulator.protocol.datex2;

import jakarta.xml.bind.annotation.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "d2Message", namespace = "http://datex2.eu/schema/3/d2Payload")
@XmlAccessorType(XmlAccessType.FIELD)
public class Datex2Message {

    @XmlElement(name = "exchange", namespace = "http://datex2.eu/schema/3/d2Payload")
    private Exchange exchange;

    @XmlElement(name = "payload", namespace = "http://datex2.eu/schema/3/d2Payload")
    private Payload payload;

    public Exchange getExchange() { return exchange; }
    public void setExchange(Exchange exchange) { this.exchange = exchange; }

    public Payload getPayload() { return payload; }
    public void setPayload(Payload payload) { this.payload = payload; }

    @XmlRootElement(name = "exchange")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Exchange {

        @XmlElement(name = "protocolVersion")
        private String protocolVersion;

        @XmlElement(name = "supplierIdentifier")
        private String supplierIdentifier;

        @XmlElement(name = "messageId")
        private String messageId;

        @XmlElement(name = "messageGenerationTime")
        private Instant messageGenerationTime;

        @XmlElement(name = "messageSequenceNumber")
        private Integer messageSequenceNumber;

        public Exchange() {
            this.protocolVersion = "3.1";
        }

        public String getProtocolVersion() { return protocolVersion; }
        public void setProtocolVersion(String protocolVersion) { this.protocolVersion = protocolVersion; }

        public String getSupplierIdentifier() { return supplierIdentifier; }
        public void setSupplierIdentifier(String supplierIdentifier) { this.supplierIdentifier = supplierIdentifier; }

        public String getMessageId() { return messageId; }
        public void setMessageId(String messageId) { this.messageId = messageId; }

        public Instant getMessageGenerationTime() { return messageGenerationTime; }
        public void setMessageGenerationTime(Instant messageGenerationTime) { this.messageGenerationTime = messageGenerationTime; }

        public Integer getMessageSequenceNumber() { return messageSequenceNumber; }
        public void setMessageSequenceNumber(Integer messageSequenceNumber) { this.messageSequenceNumber = messageSequenceNumber; }
    }

    @XmlRootElement(name = "payload")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Payload {

        @XmlElement(name = "situationPublication")
        private SituationPublication situationPublication;

        @XmlElement(name = "measuredDataPublication")
        private MeasuredDataPublication measuredDataPublication;

        @XmlElement(name = "elaboratedDataPublication")
        private ElaboratedDataPublication elaboratedDataPublication;

        public SituationPublication getSituationPublication() { return situationPublication; }
        public void setSituationPublication(SituationPublication situationPublication) { this.situationPublication = situationPublication; }

        public MeasuredDataPublication getMeasuredDataPublication() { return measuredDataPublication; }
        public void setMeasuredDataPublication(MeasuredDataPublication measuredDataPublication) { this.measuredDataPublication = measuredDataPublication; }

        public ElaboratedDataPublication getElaboratedDataPublication() { return elaboratedDataPublication; }
        public void setElaboratedDataPublication(ElaboratedDataPublication elaboratedDataPublication) { this.elaboratedDataPublication = elaboratedDataPublication; }
    }

    @XmlRootElement(name = "situationPublication")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SituationPublication {

        @XmlElement(name = "situationRecord")
        private List<SituationRecord> situationRecords;

        public SituationPublication() {
            this.situationRecords = new ArrayList<>();
        }

        public List<SituationRecord> getSituationRecords() { return situationRecords; }
        public void setSituationRecords(List<SituationRecord> situationRecords) { this.situationRecords = situationRecords; }
    }

    @XmlRootElement(name = "situationRecord")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SituationRecord {

        @XmlAttribute(name = "id")
        private String id;

        @XmlElement(name = "overallSeverity")
        private String overallSeverity;

        @XmlElement(name = "situationRecordVersionTime")
        private Instant situationRecordVersionTime;

        @XmlElementWrapper(name = "groupOfLocations")
        @XmlElement(name = "location")
        private List<Location> groupOfLocations;

        public SituationRecord() {
            this.groupOfLocations = new ArrayList<>();
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getOverallSeverity() { return overallSeverity; }
        public void setOverallSeverity(String overallSeverity) { this.overallSeverity = overallSeverity; }

        public Instant getSituationRecordVersionTime() { return situationRecordVersionTime; }
        public void setSituationRecordVersionTime(Instant situationRecordVersionTime) { this.situationRecordVersionTime = situationRecordVersionTime; }

        public List<Location> getGroupOfLocations() { return groupOfLocations; }
        public void setGroupOfLocations(List<Location> groupOfLocations) { this.groupOfLocations = groupOfLocations; }
    }

    @XmlRootElement(name = "measuredDataPublication")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MeasuredDataPublication {

        @XmlElement(name = "measurementSiteTableReference")
        private MeasurementSiteTableReference measurementSiteTableReference;

        @XmlElementWrapper(name = "measuredData")
        @XmlElement(name = "measuredData")
        private List<MeasuredData> measuredDataList;

        public MeasuredDataPublication() {
            this.measuredDataList = new ArrayList<>();
        }

        public MeasurementSiteTableReference getMeasurementSiteTableReference() { return measurementSiteTableReference; }
        public void setMeasurementSiteTableReference(MeasurementSiteTableReference measurementSiteTableReference) { this.measurementSiteTableReference = measurementSiteTableReference; }

        public List<MeasuredData> getMeasuredDataList() { return measuredDataList; }
        public void setMeasuredDataList(List<MeasuredData> measuredDataList) { this.measuredDataList = measuredDataList; }
    }

    @XmlRootElement(name = "measurementSiteTableReference")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MeasurementSiteTableReference {

        @XmlElement(name = "measurementSiteTableId")
        private String measurementSiteTableId;

        @XmlElement(name = "measurementSiteTableVersion")
        private Integer measurementSiteTableVersion;

        public String getMeasurementSiteTableId() { return measurementSiteTableId; }
        public void setMeasurementSiteTableId(String measurementSiteTableId) { this.measurementSiteTableId = measurementSiteTableId; }

        public Integer getMeasurementSiteTableVersion() { return measurementSiteTableVersion; }
        public void setMeasurementSiteTableVersion(Integer measurementSiteTableVersion) { this.measurementSiteTableVersion = measurementSiteTableVersion; }
    }

    @XmlRootElement(name = "measuredData")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MeasuredData {

        @XmlElement(name = "measurementSiteReference")
        private String measurementSiteReference;

        @XmlElement(name = "measurementTime")
        private Instant measurementTime;

        @XmlElementWrapper(name = "measuredValue")
        @XmlElement(name = "measuredValue")
        private List<MeasurementValue> measuredValues;

        public MeasuredData() {
            this.measuredValues = new ArrayList<>();
        }

        public String getMeasurementSiteReference() { return measurementSiteReference; }
        public void setMeasurementSiteReference(String measurementSiteReference) { this.measurementSiteReference = measurementSiteReference; }

        public Instant getMeasurementTime() { return measurementTime; }
        public void setMeasurementTime(Instant measurementTime) { this.measurementTime = measurementTime; }

        public List<MeasurementValue> getMeasuredValues() { return measuredValues; }
        public void setMeasuredValues(List<MeasurementValue> measuredValues) { this.measuredValues = measuredValues; }
    }

    @XmlRootElement(name = "measuredValue")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MeasurementValue {

        @XmlElement(name = "member")
        private Integer member;

        @XmlElement(name = "index")
        private Integer index;

        @XmlElement(name = "speed")
        private Float speed;

        @XmlElement(name = "numberOfVehicles")
        private Integer numberOfVehicles;

        @XmlElement(name = "occupancy")
        private Float occupancy;

        public Integer getMember() { return member; }
        public void setMember(Integer member) { this.member = member; }

        public Integer getIndex() { return index; }
        public void setIndex(Integer index) { this.index = index; }

        public Float getSpeed() { return speed; }
        public void setSpeed(Float speed) { this.speed = speed; }

        public Integer getNumberOfVehicles() { return numberOfVehicles; }
        public void setNumberOfVehicles(Integer numberOfVehicles) { this.numberOfVehicles = numberOfVehicles; }

        public Float getOccupancy() { return occupancy; }
        public void setOccupancy(Float occupancy) { this.occupancy = occupancy; }
    }

    @XmlRootElement(name = "elaboratedDataPublication")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ElaboratedDataPublication {

        @XmlElementWrapper(name = "elaboratedData")
        @XmlElement(name = "elaboratedData")
        private List<ElaboratedData> elaboratedDataList;

        public ElaboratedDataPublication() {
            this.elaboratedDataList = new ArrayList<>();
        }

        public List<ElaboratedData> getElaboratedDataList() { return elaboratedDataList; }
        public void setElaboratedDataList(List<ElaboratedData> elaboratedDataList) { this.elaboratedDataList = elaboratedDataList; }
    }

    @XmlRootElement(name = "elaboratedData")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ElaboratedData {

        @XmlElement(name = "measurementSiteReference")
        private String measurementSiteReference;

        @XmlElement(name = "dataValue")
        private String dataValue;

        @XmlElement(name = "elaboratedDataGenerationTime")
        private Instant elaboratedDataGenerationTime;

        @XmlElement(name = "elaboratedDataStatus")
        private String elaboratedDataStatus;

        public String getMeasurementSiteReference() { return measurementSiteReference; }
        public void setMeasurementSiteReference(String measurementSiteReference) { this.measurementSiteReference = measurementSiteReference; }

        public String getDataValue() { return dataValue; }
        public void setDataValue(String dataValue) { this.dataValue = dataValue; }

        public Instant getElaboratedDataGenerationTime() { return elaboratedDataGenerationTime; }
        public void setElaboratedDataGenerationTime(Instant elaboratedDataGenerationTime) { this.elaboratedDataGenerationTime = elaboratedDataGenerationTime; }

        public String getElaboratedDataStatus() { return elaboratedDataStatus; }
        public void setElaboratedDataStatus(String elaboratedDataStatus) { this.elaboratedDataStatus = elaboratedDataStatus; }
    }

    @XmlRootElement(name = "location")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Location {

        @XmlElement(name = "latitude")
        private Double latitude;

        @XmlElement(name = "longitude")
        private Double longitude;

        @XmlElement(name = "offset")
        private Integer offset;

        @XmlElement(name = "linearExtension")
        private LinearExtension linearExtension;

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }

        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }

        public Integer getOffset() { return offset; }
        public void setOffset(Integer offset) { this.offset = offset; }

        public LinearExtension getLinearExtension() { return linearExtension; }
        public void setLinearExtension(LinearExtension linearExtension) { this.linearExtension = linearExtension; }
    }

    @XmlRootElement(name = "linearExtension")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class LinearExtension {

        @XmlElement(name = "roadSegmentId")
        private String roadSegmentId;

        @XmlElement(name = "roadSegmentVersion")
        private Integer roadSegmentVersion;

        public String getRoadSegmentId() { return roadSegmentId; }
        public void setRoadSegmentId(String roadSegmentId) { this.roadSegmentId = roadSegmentId; }

        public Integer getRoadSegmentVersion() { return roadSegmentVersion; }
        public void setRoadSegmentVersion(Integer roadSegmentVersion) { this.roadSegmentVersion = roadSegmentVersion; }
    }
}
