package io.cloudapp.starter.observabilities.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class OpenTelemetryLoggingPropertiesTest {
    
    private OpenTelemetryLoggingProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new OpenTelemetryLoggingProperties();
    }
    
    @Test
    void testEnableTraceIdGetterAndSetter() {
        final boolean enableTraceId = false;
        properties.setEnableTraceId(enableTraceId);
        assertFalse(properties.isEnableTraceId());
    }
    
}
