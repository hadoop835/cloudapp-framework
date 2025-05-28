package io.cloudapp.starter.observabilities.configurations;

import io.cloudapp.api.observabilities.TraceService;
import org.junit.jupiter.api.Test;

class OpenTelemetryTraceConfigurationTest {
    
    @Test
    void testOtTraceService() {
        // Setup
        // Run the test
        final TraceService result = OpenTelemetryTraceConfiguration.otTraceService();
        
        // Verify the results
    }
    
}
