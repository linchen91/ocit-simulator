package com.ocit.simulator.protocol.datex2;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.util.UUID;

@Component
public class Datex2ProtocolHandler {

    private static final String PROTOCOL_VERSION = "3.1";
    private static final String SUPPLIER_ID = "OCIT-SIMULATOR";
    private static final String DATEX2_NS = "http://datex2.eu/schema/3/d2Payload";
    private final JAXBContext jaxbContext;

    public Datex2ProtocolHandler() throws JAXBException {
        this.jaxbContext = JAXBContext.newInstance(Datex2Message.class);
    }

    public String serializeMessage(Datex2Message message) throws JAXBException {
        setupExchange(message.getExchange());
        
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, DATEX2_NS);
        StringWriter writer = new StringWriter();
        marshaller.marshal(message, writer);
        return writer.toString();
    }

    public Datex2Message deserializeMessage(String xml) throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return (Datex2Message) unmarshaller.unmarshal(reader);
    }

    private void setupExchange(Datex2Message.Exchange exchange) {
        if (exchange == null) {
            exchange = new Datex2Message.Exchange();
        }
        if (exchange.getProtocolVersion() == null) {
            exchange.setProtocolVersion(PROTOCOL_VERSION);
        }
        if (exchange.getSupplierIdentifier() == null) {
            exchange.setSupplierIdentifier(SUPPLIER_ID);
        }
        if (exchange.getMessageId() == null) {
            exchange.setMessageId(UUID.randomUUID().toString());
        }
        if (exchange.getMessageGenerationTime() == null) {
            exchange.setMessageGenerationTime(Instant.now());
        }
    }

    public Datex2Message createSituationPublication(String severity, String location, String description) {
        Datex2Message message = new Datex2Message();
        Datex2Message.Exchange exchange = new Datex2Message.Exchange();
        message.setExchange(exchange);

        Datex2Message.Payload payload = new Datex2Message.Payload();
        Datex2Message.SituationPublication situPub = new Datex2Message.SituationPublication();
        
        Datex2Message.SituationRecord record = new Datex2Message.SituationRecord();
        record.setId(UUID.randomUUID().toString());
        record.setOverallSeverity(severity);
        record.setSituationRecordVersionTime(Instant.now());
        
        Datex2Message.Location loc = new Datex2Message.Location();
        if (location != null) {
            String[] parts = location.split(",");
            if (parts.length >= 2) {
                try {
                    loc.setLatitude(Double.parseDouble(parts[0].trim()));
                    loc.setLongitude(Double.parseDouble(parts[1].trim()));
                } catch (NumberFormatException ignored) {}
            }
        }
        record.getGroupOfLocations().add(loc);
        situPub.getSituationRecords().add(record);
        payload.setSituationPublication(situPub);
        message.setPayload(payload);
        
        return message;
    }

    public Datex2Message createMeasuredData(String siteId, float speed, int vehicles, float occupancy) {
        Datex2Message message = new Datex2Message();
        Datex2Message.Exchange exchange = new Datex2Message.Exchange();
        message.setExchange(exchange);

        Datex2Message.Payload payload = new Datex2Message.Payload();
        Datex2Message.MeasuredDataPublication measPub = new Datex2Message.MeasuredDataPublication();
        
        Datex2Message.MeasuredData data = new Datex2Message.MeasuredData();
        data.setMeasurementSiteReference(siteId);
        data.setMeasurementTime(Instant.now());
        
        Datex2Message.MeasurementValue value = new Datex2Message.MeasurementValue();
        value.setSpeed(speed);
        value.setNumberOfVehicles(vehicles);
        value.setOccupancy(occupancy);
        data.getMeasuredValues().add(value);
        
        measPub.getMeasuredDataList().add(data);
        payload.setMeasuredDataPublication(measPub);
        message.setPayload(payload);
        
        return message;
    }

    public Datex2Message createElaboratedData(String siteId, String value, String status) {
        Datex2Message message = new Datex2Message();
        Datex2Message.Exchange exchange = new Datex2Message.Exchange();
        message.setExchange(exchange);

        Datex2Message.Payload payload = new Datex2Message.Payload();
        Datex2Message.ElaboratedDataPublication elabPub = new Datex2Message.ElaboratedDataPublication();
        
        Datex2Message.ElaboratedData data = new Datex2Message.ElaboratedData();
        data.setMeasurementSiteReference(siteId);
        data.setDataValue(value);
        data.setElaboratedDataGenerationTime(Instant.now());
        data.setElaboratedDataStatus(status);
        
        elabPub.getElaboratedDataList().add(data);
        payload.setElaboratedDataPublication(elabPub);
        message.setPayload(payload);
        
        return message;
    }

    public Datex2Message createErrorResponse(Datex2Message original, String errorDetails) {
        return createElaboratedData("ERROR", errorDetails, "FAILURE");
    }
}
