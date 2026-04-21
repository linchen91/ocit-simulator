package com.ocit.simulator.protocol;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.util.UUID;

public class OcitProtocolHandler {

    private static final int OCIT_VERSION = 3;
    private final JAXBContext jaxbContext;

    public OcitProtocolHandler() throws JAXBException {
        this.jaxbContext = JAXBContext.newInstance(OcitMessage.class);
    }

    public String serializeMessage(OcitMessage message) throws JAXBException {
        if (message.getMessageId() == null) {
            message.setMessageId(UUID.randomUUID().toString());
        }
        if (message.getTimestamp() == null) {
            message.setTimestamp(Instant.now());
        }
        if (message.getProtocolVersion() == null) {
            message.setProtocolVersion(OCIT_VERSION);
        }

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(message, writer);
        return writer.toString();
    }

    public OcitMessage deserializeMessage(String xml) throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return (OcitMessage) unmarshaller.unmarshal(reader);
    }

    public OcitMessage createMessage(OcitMessage.OcitMessageType type, String senderId, 
                                     String receiverId, String payload) {
        OcitMessage message = new OcitMessage();
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageType(type);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setTimestamp(Instant.now());
        message.setProtocolVersion(OCIT_VERSION);
        message.setPayload(payload);
        message.setStatus(OcitMessage.MessageStatus.PENDING);
        return message;
    }

    public OcitMessage createHeartbeat(String senderId, String receiverId) {
        return createMessage(OcitMessage.OcitMessageType.HEARTBEAT, senderId, receiverId, null);
    }

    public OcitMessage createErrorResponse(OcitMessage originalMessage, String errorDetails) {
        OcitMessage errorMessage = new OcitMessage();
        errorMessage.setMessageId(UUID.randomUUID().toString());
        errorMessage.setMessageType(OcitMessage.OcitMessageType.ERROR);
        errorMessage.setSenderId(originalMessage.getReceiverId());
        errorMessage.setReceiverId(originalMessage.getSenderId());
        errorMessage.setTimestamp(Instant.now());
        errorMessage.setProtocolVersion(OCIT_VERSION);
        errorMessage.setPayload(errorDetails);
        errorMessage.setStatus(OcitMessage.MessageStatus.FAILURE);
        return errorMessage;
    }

    public OcitMessage createAckResponse(OcitMessage originalMessage) {
        OcitMessage ackMessage = new OcitMessage();
        ackMessage.setMessageId(UUID.randomUUID().toString());
        ackMessage.setMessageType(OcitMessage.OcitMessageType.CONTROL_ACK);
        ackMessage.setSenderId(originalMessage.getReceiverId());
        ackMessage.setReceiverId(originalMessage.getSenderId());
        ackMessage.setTimestamp(Instant.now());
        ackMessage.setProtocolVersion(OCIT_VERSION);
        ackMessage.setPayload("Command acknowledged: " + originalMessage.getMessageId());
        ackMessage.setStatus(OcitMessage.MessageStatus.SUCCESS);
        return ackMessage;
    }
}
