package com.ocit.simulator.protocol;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class OcitTcpServer {

    private static final Logger logger = LoggerFactory.getLogger(OcitTcpServer.class);

    @Value("${ocit.server.port:12000}")
    private int port;

    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private volatile boolean running = false;
    private OcitProtocolHandler protocolHandler;

    public OcitTcpServer(OcitProtocolHandler protocolHandler) {
        this.protocolHandler = protocolHandler;
    }

    @PostConstruct
    public void start() {
        executorService = Executors.newFixedThreadPool(10);
        running = true;
        executorService.submit(this::acceptConnections);
        logger.info("OCIT-O TCP Server started on port {}", port);
    }

    private void acceptConnections() {
        try {
            serverSocket = new ServerSocket(port);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            if (running) {
                logger.error("Error accepting connections", e);
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request;
            while ((request = reader.readLine()) != null) {
                logger.debug("Received: {}", request);
                processMessage(request, writer);
            }
        } catch (IOException e) {
            logger.error("Error handling client", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing client socket", e);
            }
        }
    }

    private void processMessage(String request, PrintWriter writer) {
        try {
            OcitMessage message = protocolHandler.deserializeMessage(request);
            logger.info("Processed message: {} from {}", message.getMessageType(), message.getSenderId());
            writer.println("OK");
        } catch (Exception e) {
            logger.error("Error processing message", e);
            writer.println("ERROR: " + e.getMessage());
        }
    }

    @PreDestroy
    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (executorService != null) {
                executorService.shutdown();
            }
            logger.info("OCIT-O TCP Server stopped");
        } catch (IOException e) {
            logger.error("Error stopping server", e);
        }
    }
}
