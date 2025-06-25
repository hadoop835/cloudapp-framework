package com.alibaba.cloudapp.starter.observabilities.configurations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.env.MockEnvironment;

class OpenTelemetryLoggingPostProcessorTest {
    
    private OpenTelemetryLoggingPostProcessor processor;
    
    @BeforeEach
    void setUp() {
        processor = new OpenTelemetryLoggingPostProcessor();
    }
    
    @Test
    void testPostProcessEnvironment() {
        // Setup
        final ConfigurableEnvironment environment = new MockEnvironment();
        final SpringApplication application = new SpringApplication(
                String.class);
        
        // Run the test
        processor.postProcessEnvironment(environment, application);
        
        // Verify the results
    }
    
}
