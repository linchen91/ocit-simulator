package com.ocit.simulator.protocol;

import java.time.Instant;

public class OcitMessage {
    private String messageId;
    private OcitMessageType messageType;
    private String senderId;
    private String receiverId;
    private Instant timestamp;
    private Integer protocolVersion;
    private String payload;
    private MessageStatus status;

    public enum OcitMessageType {
        SUPPLY_DATA_REQUEST,
        SUPPLY_DATA_RESPONSE,
        PROCESS_DATA_REQUEST,
        PROCESS_DATA_RESPONSE,
        CONTROL_COMMAND,
        CONTROL_ACK,
        STATUS_REQUEST,
        STATUS_RESPONSE,
        HEARTBEAT,
        ERROR
    }

    public enum MessageStatus {
        SUCCESS,
        FAILURE,
        PENDING,
        TIMEOUT
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public OcitMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(OcitMessageType messageType) {
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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
