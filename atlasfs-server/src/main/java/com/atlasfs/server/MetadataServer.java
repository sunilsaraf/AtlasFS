package com.atlasfs.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Main server class for AtlasFS metadata service.
 */
public class MetadataServer {
    private static final Logger logger = LoggerFactory.getLogger(MetadataServer.class);
    
    private final int port;
    private final Server server;

    public MetadataServer(int port) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(new MetadataServiceImpl())
                .addService(ProtoReflectionService.newInstance())
                .build();
    }

    /**
     * Start the metadata server.
     */
    public void start() throws IOException {
        server.start();
        logger.info("AtlasFS Metadata Server started on port {}", port);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down AtlasFS Metadata Server...");
            try {
                MetadataServer.this.stop();
            } catch (InterruptedException e) {
                logger.error("Error during shutdown", e);
            }
        }));
    }

    /**
     * Stop the metadata server.
     */
    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logger.error("Invalid port number: {}", args[0]);
                System.exit(1);
            }
        }

        MetadataServer server = new MetadataServer(port);
        server.start();
        server.blockUntilShutdown();
    }
}
