package com.alibaba.cloudapp.starter.observabilities.configurations;

import com.alibaba.cloudapp.observabilities.opentelemetry.metric.MetricCollectionAspect;
import com.alibaba.cloudapp.observabilities.opentelemetry.metric.MetricHelperServer;
import com.alibaba.cloudapp.starter.observabilities.properties.OpenTelemetryProperties;
import com.alibaba.cloudapp.starter.observabilities.refresh.OpenTelemetryRefreshComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpenTelemetryMetricConfigurationTest {
    
    private OpenTelemetryMetricConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new OpenTelemetryMetricConfiguration();
    }
    
    @Test
    void testOtRefreshComponent() {
        // Setup
        final OpenTelemetryProperties properties = new OpenTelemetryProperties();
        properties.setEnabled(false);
        properties.setMetricEndpoint("metricEndpoint");
        properties.setDefaultServiceName("defaultServiceName");
        properties.setTimeout(0);
        properties.setInterval(0);
        
        // Run the test
        final OpenTelemetryRefreshComponent result = configuration.otRefreshComponent(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testOtMetricCollectionAspect() {
        // Setup
        final OpenTelemetryProperties openTelemetryProperties = new OpenTelemetryProperties();
        openTelemetryProperties.setEnabled(false);
        openTelemetryProperties.setMetricEndpoint("metricEndpoint");
        openTelemetryProperties.setDefaultServiceName("defaultServiceName");
        openTelemetryProperties.setTimeout(0);
        openTelemetryProperties.setInterval(0);
        final OpenTelemetryRefreshComponent component = new OpenTelemetryRefreshComponent(
                openTelemetryProperties);
        
        // Run the test
        final MetricCollectionAspect result = OpenTelemetryMetricConfiguration.otMetricCollectionAspect(
                component);
        
        // Verify the results
    }
    
    @Test
    void testMetricHelperServer() {
        // Setup
        final OpenTelemetryProperties openTelemetryProperties = new OpenTelemetryProperties();
        openTelemetryProperties.setEnabled(false);
        openTelemetryProperties.setMetricEndpoint("metricEndpoint");
        openTelemetryProperties.setDefaultServiceName("defaultServiceName");
        openTelemetryProperties.setTimeout(0);
        openTelemetryProperties.setInterval(0);
        final OpenTelemetryRefreshComponent component = new OpenTelemetryRefreshComponent(
                openTelemetryProperties);
        
        // Run the test
        final MetricHelperServer result = OpenTelemetryMetricConfiguration.metricHelperServer(
                component);
        
        // Verify the results
    }
    
}
