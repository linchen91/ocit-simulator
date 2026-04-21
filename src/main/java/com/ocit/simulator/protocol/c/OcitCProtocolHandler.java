package com.ocit.simulator.protocol.c;

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
public class OcitCProtocolHandler {

    private static final int OCIT_C_VERSION = 2;
    private final JAXBContext jaxbContext;

    public OcitCProtocolHandler() throws JAXBException {
        this.jaxbContext = JAXBContext.newInstance(OcitCMessage.class);
    }

    public String serializeMessage(OcitCMessage message) throws JAXBException {
        if (message.getMessageId() == null) {
            message.setMessageId(UUID.randomUUID().toString());
        }
        if (message.getTimestamp() == null) {
            message.setTimestamp(Instant.now());
        }
        if (message.getProtocolVersion() == null) {
            message.setProtocolVersion(OCIT_C_VERSION);
        }

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(message, writer);
        return writer.toString();
    }

    public OcitCMessage deserializeMessage(String xml) throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return (OcitCMessage) unmarshaller.unmarshal(reader);
    }

    public OcitCMessage createMessage(OcitCMessage.CMessageType type, String senderId, String receiverId) {
        OcitCMessage message = new OcitCMessage();
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageType(type);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setTimestamp(Instant.now());
        message.setProtocolVersion(OCIT_C_VERSION);
        message.setStatus(OcitCMessage.MessageStatus.PENDING);
        return message;
    }

    public OcitCMessage createSupplyDataRequest(String senderId, String receiverId) {
        return createMessage(OcitCMessage.CMessageType.SUPPLY_DATA_REQUEST, senderId, receiverId);
    }

    public OcitCMessage createSupplyDataResponse(String senderId, String receiverId) {
        return createMessage(OcitCMessage.CMessageType.SUPPLY_DATA_RESPONSE, senderId, receiverId);
    }

    public OcitCMessage createProcessDataRequest(String senderId, String receiverId) {
        return createMessage(OcitCMessage.CMessageType.PROCESS_DATA_REQUEST, senderId, receiverId);
    }

    public OcitCMessage createProcessDataResponse(String senderId, String receiverId) {
        return createMessage(OcitCMessage.CMessageType.PROCESS_DATA_RESPONSE, senderId, receiverId);
    }

    public OcitCMessage createStatusRequest(String senderId, String receiverId) {
        return createMessage(OcitCMessage.CMessageType.STATUS_REQUEST, senderId, receiverId);
    }

    public OcitCMessage createStatusResponse(String senderId, String receiverId) {
        return createMessage(OcitCMessage.CMessageType.STATUS_RESPONSE, senderId, receiverId);
    }

    public OcitCMessage createHeartbeat(String senderId, String receiverId) {
        return createMessage(OcitCMessage.CMessageType.HEARTBEAT, senderId, receiverId);
    }

    public OcitCMessage createErrorResponse(OcitCMessage originalMessage, String errorDetails) {
        OcitCMessage errorMessage = new OcitCMessage();
        errorMessage.setMessageId(UUID.randomUUID().toString());
        errorMessage.setMessageType(OcitCMessage.CMessageType.ERROR);
        errorMessage.setSenderId(originalMessage.getReceiverId());
        errorMessage.setReceiverId(originalMessage.getSenderId());
        errorMessage.setTimestamp(Instant.now());
        errorMessage.setProtocolVersion(OCIT_C_VERSION);
        errorMessage.setErrorDetails(errorDetails);
        errorMessage.setStatus(OcitCMessage.MessageStatus.FAILURE);
        return errorMessage;
    }

    public OcitCMessage createAckResponse(OcitCMessage originalMessage) {
        OcitCMessage ackMessage = new OcitCMessage();
        ackMessage.setMessageId(UUID.randomUUID().toString());
        ackMessage.setMessageType(OcitCMessage.CMessageType.CONTROL_ACK);
        ackMessage.setSenderId(originalMessage.getReceiverId());
        ackMessage.setReceiverId(originalMessage.getSenderId());
        ackMessage.setTimestamp(Instant.now());
        ackMessage.setProtocolVersion(OCIT_C_VERSION);
        ackMessage.setStatus(OcitCMessage.MessageStatus.SUCCESS);
        return ackMessage;
    }
}
