package io.cloudapp.messaging.kafka;

import io.cloudapp.messaging.kafka.properties.KafkaConsumerProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class CloudAppKafkaConsumerFactoryTest {
    
    private CloudAppKafkaConsumerFactory<String, String> factory;
    
    @Before
    public void setUp() {
        KafkaConsumerProperties properties = new KafkaConsumerProperties();
        properties.setBootstrapServers("localhost");
        properties.setGroup("group");
        properties.setName("name");
        properties.setTopic("topic");
        
        factory = new CloudAppKafkaConsumerFactory<>(
                properties.buildConsumerProperties());
    }
    
    @Test
    public void testCreateRawConsumer() {
        // Setup
        KafkaConsumerProperties properties = new KafkaConsumerProperties();
        properties.setBootstrapServers("localhost:8080");
        properties.setGroup("group");
        properties.setName("name");
        properties.setTopic("topic");
        final Map<String, Object> configProps = properties.buildConsumerProperties();
        
        // Run the test
        try (Consumer<String, String> result = factory.createRawConsumer(
                configProps)) {
            assert result != null;
        }
    }
    
    @Test
    public void testRefresh() {
        // Setup
        KafkaConsumerProperties properties = new KafkaConsumerProperties();
        properties.setBootstrapServers("localhost");
        properties.setGroup("group");
        properties.setName("newName");
        properties.setTopic("topic");
        final Map<String, Object> configs =
                properties.buildConsumerProperties();
        
        // Run the test
        factory.refresh(configs);
        
        // Verify the results
    }
    
}
