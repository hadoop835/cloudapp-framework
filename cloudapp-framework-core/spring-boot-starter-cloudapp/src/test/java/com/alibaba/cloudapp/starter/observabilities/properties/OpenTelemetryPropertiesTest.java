package com.alibaba.cloudapp.starter.observabilities.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class OpenTelemetryPropertiesTest {
    
    private OpenTelemetryProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new OpenTelemetryProperties();
    }
    
    @Test
    void testEnabledGetterAndSetter() {
        final boolean enabled = false;
        properties.setEnabled(enabled);
        assertFalse(properties.isEnabled());
    }
    
    @Test
    void testMetricEndpointGetterAndSetter() {
        final String metricEndpoint = "metricEndpoint";
        properties.setMetricEndpoint(metricEndpoint);
        assertEquals(metricEndpoint,
                     properties.getMetricEndpoint()
        );
    }
    
    @Test
    void testDefaultServiceNameGetterAndSetter() {
        final String defaultServiceName = "defaultServiceName";
        properties.setDefaultServiceName(
                defaultServiceName);
        assertEquals(defaultServiceName,
                     properties.getDefaultServiceName()
        );
    }
    
    @Test
    void testTimeoutGetterAndSetter() {
        final Integer timeout = 0;
        properties.setTimeout(timeout);
        assertEquals(timeout, properties.getTimeout());
    }
    
    @Test
    void testIntervalGetterAndSetter() {
        final Integer interval = 0;
        properties.setInterval(interval);
        assertEquals(interval, properties.getInterval());
    }
    
    @Test
    void testEnabledHelperServerGetterAndSetter() {
        final Boolean enabledHelperServer = false;
        properties.setEnabledHelperServer(
                enabledHelperServer);
        assertFalse(properties.isEnabledHelperServer());
    }
    
    @Test
    void testHelperServerPortGetterAndSetter() {
        final Integer helperServerPort = 0;
        properties.setHelperServerPort(helperServerPort);
        assertEquals(helperServerPort,
                     properties.getHelperServerPort()
        );
    }
    
    @Test
    void testHelperServerBindAddressGetterAndSetter() {
        final String helperServerBindAddress = "helperServerBindAddress";
        properties.setHelperServerBindAddress(
                helperServerBindAddress);
        assertEquals(helperServerBindAddress,
                     properties.getHelperServerBindAddress()
        );
    }
    
    @Test
    void testPrometheusPortGetterAndSetter() {
        final Integer prometheusPort = 0;
        properties.setPrometheusPort(prometheusPort);
        assertEquals(prometheusPort,
                     properties.getPrometheusPort()
        );
    }
    
    @Test
    void testPrometheusExporterGetterAndSetter() {
        final Boolean prometheusExporter = false;
        properties.setPrometheusExporter(
                prometheusExporter);
        assertFalse(properties.isPrometheusExporter());
    }
    
}
