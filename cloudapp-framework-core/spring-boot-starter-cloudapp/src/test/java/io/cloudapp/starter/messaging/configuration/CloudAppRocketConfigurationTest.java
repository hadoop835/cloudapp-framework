package io.cloudapp.starter.messaging.configuration;

import io.cloudapp.starter.messaging.factory.CloudAppRocketBeanFactory;
import io.cloudapp.starter.messaging.properties.CloudAppRocketProperties;
import io.cloudapp.starter.messaging.refresh.RocketRefreshComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CloudAppRocketConfigurationTest {
    
    private CloudAppRocketConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new CloudAppRocketConfiguration();
    }
    
    @Test
    void testRocketRefreshComponent() {
        // Setup
        final CloudAppRocketProperties properties = new CloudAppRocketProperties();
        properties.setEnabled(false);
        properties.setNameServer("nameServer");
        properties.setUsername("username");
        properties.setPassword("password");
        properties.setUseTLS(false);
        
        final CloudAppRocketBeanFactory beanFactory = new CloudAppRocketBeanFactory();
        
        // Run the test
        final RocketRefreshComponent result = configuration.rocketRefreshComponent(
                properties, beanFactory);
        
        // Verify the results
    }
    
    @Test
    void testCloudAppRocketBeanFactory() {
        // Setup
        // Run the test
        final CloudAppRocketBeanFactory result = configuration.cloudAppRocketBeanFactory();
        
        // Verify the results
    }
    
}
