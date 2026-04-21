package com.ocit.simulator.protocol.c;

import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

@Component
public class OcitCTcpClient {

    private static final Logger logger = LoggerFactory.getLogger(OcitCTcpClient.class);

    @Value("${ocit.c.client.host:localhost}")
    private String host;

    @Value("${ocit.c.client.port:13000}")
    private int port;

    private OcitCProtocolHandler protocolHandler;

    public OcitCTcpClient(OcitCProtocolHandler protocolHandler) {
        this.protocolHandler = protocolHandler;
    }

    public OcitCMessage sendMessage(OcitCMessage message) throws IOException, JAXBException {
        String xml = protocolHandler.serializeMessage(message);
        
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            logger.debug("Sending OCIT-C message: {}", message.getMessageType());
            writer.println(xml);
            
            String response = reader.readLine();
            logger.debug("Received response: {}", response);
            
            return message;
        }
    }

    public OcitCMessage requestSupplyData(String remoteSystemId) throws IOException, JAXBException {
        OcitCMessage request = protocolHandler.createSupplyDataRequest("SIMULATOR", remoteSystemId);
        return sendMessage(request);
    }

    public OcitCMessage requestProcessData(String remoteSystemId) throws IOException, JAXBException {
        OcitCMessage request = protocolHandler.createProcessDataRequest("SIMULATOR", remoteSystemId);
        return sendMessage(request);
    }

    public OcitCMessage requestStatus(String remoteSystemId) throws IOException, JAXBException {
        OcitCMessage request = protocolHandler.createStatusRequest("SIMULATOR", remoteSystemId);
        return sendMessage(request);
    }

    public OcitCMessage sendHeartbeat(String remoteSystemId) throws IOException, JAXBException {
        OcitCMessage heartbeat = protocolHandler.createHeartbeat("SIMULATOR", remoteSystemId);
        return sendMessage(heartbeat);
    }

    public boolean testConnection() {
        try {
            sendHeartbeat("TEST");
            return true;
        } catch (IOException | JAXBException e) {
            logger.warn("OCIT-C connection test failed: {}", e.getMessage());
            return false;
        }
    }
}
