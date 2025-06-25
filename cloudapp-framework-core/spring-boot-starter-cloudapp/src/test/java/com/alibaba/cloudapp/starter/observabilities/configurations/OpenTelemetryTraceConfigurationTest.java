package com.alibaba.cloudapp.starter.observabilities.configurations;

import com.alibaba.cloudapp.api.observabilities.TraceService;
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
