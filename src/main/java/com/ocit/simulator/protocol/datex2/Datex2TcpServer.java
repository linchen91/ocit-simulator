package com.ocit.simulator.protocol.datex2;

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
public class Datex2TcpServer {

    private static final Logger logger = LoggerFactory.getLogger(Datex2TcpServer.class);

    @Value("${datex2.server.port:14000}")
    private int port;

    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private volatile boolean running = false;
    private Datex2ProtocolHandler protocolHandler;

    public Datex2TcpServer(Datex2ProtocolHandler protocolHandler) {
        this.protocolHandler = protocolHandler;
    }

    @PostConstruct
    public void start() {
        executorService = Executors.newFixedThreadPool(10);
        running = true;
        executorService.submit(this::acceptConnections);
        logger.info("DATEX II TCP Server started on port {}", port);
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
                logger.error("Error accepting Datex2 connections", e);
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request;
            while ((request = reader.readLine()) != null) {
                logger.debug("Received Datex2: {}", request.substring(0, Math.min(100, request.length())));
                processMessage(request, writer);
            }
        } catch (IOException e) {
            logger.error("Error handling Datex2 client", e);
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
            Datex2Message message = protocolHandler.deserializeMessage(request);
            logger.info("Processed Datex2 message from {}", message.getExchange().getSupplierIdentifier());
            writer.println("OK");
        } catch (Exception e) {
            logger.error("Error processing Datex2 message", e);
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
            logger.info("DATEX II TCP Server stopped");
        } catch (IOException e) {
            logger.error("Error stopping server", e);
        }
    }
}
