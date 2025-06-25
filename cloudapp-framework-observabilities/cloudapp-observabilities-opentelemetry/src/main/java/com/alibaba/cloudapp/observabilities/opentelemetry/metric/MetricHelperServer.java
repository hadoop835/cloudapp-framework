package com.alibaba.cloudapp.observabilities.opentelemetry.metric;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class MetricHelperServer {
    
    private final Boolean enabled;
    private final Integer port;
    private final String address;
    
    private HttpServer server;
    
    public MetricHelperServer(Boolean enabled,
                              String helperServerAddress,
                              Integer helperServerPort) {
        this.enabled = enabled;
        this.port = helperServerPort;
        this.address = helperServerAddress;
    }
    
    /**
     * Init and start  metrics templates server. Register interfaces to export
     * common metrics template for grafana dashboard.
     */
    public void start() {
        if (!enabled || server != null) {
            return;
        }
        log.info("Enabled CloudApp metric helper server.");
        try {
            HttpServer httpServer = HttpServer.create(
                    new InetSocketAddress(address, port), 0);
            // register metric templates handler
            httpServer.createContext("/grafana/templates.json",
                                 new MetricsTemplateHandler());
            httpServer.start();
            server = httpServer;
            log.info("CloudApp metric helper server started at [{}:{}]", address,
                     port);
        } catch (Exception e) {
            log.error("Failed to start metrics template server", e);
        }
    }
}
