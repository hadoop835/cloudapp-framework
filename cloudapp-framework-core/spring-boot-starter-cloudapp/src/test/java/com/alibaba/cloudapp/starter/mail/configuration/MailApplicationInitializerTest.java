package com.alibaba.cloudapp.starter.mail.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MailApplicationInitializerTest {
    
    private MailApplicationInitializer initializer;
    
    @BeforeEach
    void setUp() {
        initializer = new MailApplicationInitializer();
    }
    
    @Test
    void testInitialize() {
        // Setup
        final ConfigurableApplicationContext applicationContext =
                mock(ConfigurableApplicationContext.class);
        
        final ConfigurableEnvironment environment =
                mock(ConfigurableEnvironment.class);
        
        final MutablePropertySources propertySources =
                mock(MutablePropertySources.class);
        
        when(applicationContext.getEnvironment()).thenReturn(environment);
        when(environment.getPropertySources()).thenReturn(propertySources);
        
        // Run the test
        initializer.initialize(applicationContext);
        
        // Verify the results
    }
    
}
