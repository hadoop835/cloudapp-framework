package io.cloudapp.starter.observabilities.configurations;

import io.cloudapp.observabilities.opentelemetry.logging.OpenTelemetryLog4J2Impl;
import io.cloudapp.starter.observabilities.properties.OpenTelemetryLoggingProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpenTelemetryLog4J2AutoConfigurationTest {
    
    private OpenTelemetryLog4J2AutoConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new OpenTelemetryLog4J2AutoConfiguration();
    }
    
    @Test
    void testOpenTelemetryLoggingConfiguration() {
        // Setup
        final OpenTelemetryLoggingProperties properties = new OpenTelemetryLoggingProperties();
        properties.setEnableTraceId(false);
        
        // Run the test
        final OpenTelemetryLog4J2Impl result = configuration.openTelemetryLoggingConfiguration(
                properties);
        
        // Verify the results
    }
    
}
