package io.cloudapp.starter.messaging.configuration;

import io.cloudapp.starter.messaging.factory.CloudAppKafkaBeanFactory;
import io.cloudapp.starter.messaging.properties.CloudAppKafkaProperties;
import io.cloudapp.starter.messaging.refresh.KafkaRefreshComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

class CloudAppKafkaConfigurationTest {
    
    private CloudAppKafkaConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new CloudAppKafkaConfiguration();
    }
    
    @Test
    void testKafkaRefreshComponent() {
        // Setup
        final CloudAppKafkaProperties properties = new CloudAppKafkaProperties();
        properties.setEnabled(false);
        properties.setMechanism("mechanism");
        properties.setServers("servers");
        final KafkaProperties.Ssl ssl = new KafkaProperties.Ssl();
        ssl.setKeyPassword("keyPassword");
        properties.setSsl(ssl);
        
        final CloudAppKafkaBeanFactory beanFactory = new CloudAppKafkaBeanFactory();
        
        // Run the test
        final KafkaRefreshComponent result = configuration.kafkaRefreshComponent(
                properties, beanFactory);
        
        // Verify the results
    }
    
    @Test
    void testCloudAppKafkaBeanFactory() {
        // Setup
        // Run the test
        final CloudAppKafkaBeanFactory result = CloudAppKafkaConfiguration.cloudAppKafkaBeanFactory();
        
        // Verify the results
    }
    
}
