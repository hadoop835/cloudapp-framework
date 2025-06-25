package com.alibaba.cloudapp.messaging.kafka;

import com.alibaba.cloudapp.api.messaging.Notifier;
import com.alibaba.cloudapp.api.messaging.TraceStorage;
import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.messaging.kafka.model.KafkaDestination;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaConsumerProperties;
import net.mguenther.kafka.junit.EmbeddedKafkaCluster;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static net.mguenther.kafka.junit.EmbeddedKafkaCluster.provisionWith;
import static net.mguenther.kafka.junit.EmbeddedKafkaClusterConfig.defaultClusterConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(MockitoJUnitRunner.class)
public class CloudAppKafkaConsumerTest {
    
    private EmbeddedKafkaCluster kafka;
    private Destination destination;
    
    
    private CloudAppKafkaConsumer<String, String> kafkaConsumer;
    
    private AutoCloseable mockitoCloseable;
    private Collection<TopicPartition> topicPartitions;
    
    public static final String TOPIC = "topic";
    
    @Before
    public void setUp() {
        mockitoCloseable = openMocks(this);
        
        kafka = provisionWith(defaultClusterConfig());
        kafka.start();
        
        Deserializer<String> keyDeserializer = new StringDeserializer();
        Deserializer<String> valueDeserializer = new StringDeserializer();
        
        destination = new KafkaDestination(TOPIC);
        
        KafkaConsumerProperties properties = new KafkaConsumerProperties();
        properties.setName("test-consumer");
        properties.setTopic(TOPIC);
        properties.setBootstrapServers("localhost:9092");
        properties.setName("test-consumer");
        properties.setGroup("group");
        
        kafkaConsumer = new CloudAppKafkaConsumer<>(
                properties.buildConsumerProperties(),
                keyDeserializer,
                valueDeserializer
        );
        
    }
    
    @After
    public void tearDown() throws Exception {
        mockitoCloseable.close();
        if(kafka != null) {
            kafka.stop();
        }
    }
    
    @Test
    public void testGetDelegatingConsumer() {
        // Setup
        // Run the test
        final KafkaConsumer<String, String> result =
                kafkaConsumer.getDelegatingConsumer();
        
        // Verify the results
        assertNotNull(result);
    }
    
    @Test
    public void testPull1() {
        // Setup
        
        // Run the test
        assertThrows(UnsupportedOperationException.class, () ->
                kafkaConsumer.pull(destination)
        );
        
        // Verify the results
    }
    
    @Test
    public void testPull1_ThrowsCloudAppException() {
        // Setup
        final Destination destination = mock(Destination.class);
        
        // Run the test
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.pull(destination)
        );
    }
    
    @Test
    public void testPull2() {
        // Setup
        final Destination destination = mock(Destination.class);
        
        // Run the test
        assertThrows(UnsupportedOperationException.class, () ->
                kafkaConsumer.pull(destination, 1)
        );
        
        // Verify the results
    }
    
    @Test
    public void testPull2_ThrowsCloudAppException() {
        // Setup
        final Destination destination = mock(Destination.class);
        
        // Run the test
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.pull(destination, 0)
        );
    }
    
    @Test
    public void testPull3() {
        // Setup
        // Run the test
        assertThrows(UnsupportedOperationException.class, () ->
                kafkaConsumer.pull(TOPIC)
        );
        
        // Verify the results
    }
    
    @Test
    public void testPull3_ThrowsCloudAppException() {
        // Setup
        
        // Run the test
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.pull(TOPIC)
        );
    }
    
    @Test
    public void testPull4() {
        // Setup
        // Run the test
        assertThrows(UnsupportedOperationException.class, () ->
                kafkaConsumer.pull(TOPIC, 0)
        );
        
        // Verify the results
    }
    
    @Test
    public void testPull4_ThrowsCloudAppException() {
        // Setup
        
        // Run the test
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.pull(TOPIC, 0)
        );
    }
    
    @Test
    public void testPull5() {
        // Setup
        final Destination destination = mock(Destination.class);
        final Duration timeout = Duration.ofDays(0L);
        
        // Run the test
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.pull(destination, timeout)
        );
        
        // Verify the results
    }
    
    @Test
    public void testPull5_ThrowsCloudAppException() {
        // Setup
        final Duration timeout = Duration.ofDays(0L);
        
        // Run the test
        assertThrows(UnsupportedOperationException.class, () ->
                kafkaConsumer.pull(destination, timeout)
        );
    }
    
    @Test
    public void testPull6() {
        // Setup
        final Duration timeout = Duration.ofDays(0L);
        
        // Run the test
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.pull(TOPIC, timeout)
        );
        
        // Verify the results
    }
    
    @Test
    public void testPull6_ThrowsCloudAppException() {
        // Setup
        final Duration timeout = Duration.ofDays(0L);
        
        // Run the test
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.pull(TOPIC, timeout)
        );
    }
    
    @Test
    public void testPull7() {
        // Setup
        final Destination destination = mock(Destination.class);
        final Duration timeout = Duration.ofDays(0L);
        
        // Run the test
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.pull(destination, 0, timeout)
        );
        
        // Verify the results
    }
    
    @Test
    public void testPull7_ThrowsCloudAppException() {
        // Setup
        final Destination destination = mock(Destination.class);
        final Duration timeout = Duration.ofDays(0L);
        
        // Run the test
        assertThrows(UnsupportedOperationException.class, () ->
                kafkaConsumer.pull(destination, 0, timeout)
        );
    }
    
    @Test
    public void testPull8() {
        // Setup
        final Duration timeout = Duration.ofDays(0L);
        
        // Run the test
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.pull(TOPIC, 0, timeout)
        );
        
        // Verify the results
    }
    
    @Test
    public void testPull8_ThrowsCloudAppException() {
        // Setup
        final Duration timeout = Duration.ofDays(0L);
        
        // Run the test
        assertThrows(UnsupportedOperationException.class, () ->
                kafkaConsumer.pull(TOPIC, 0, timeout)
        );
    }
    
    @Test
    public void testSubscribe1() {
        // Setup
        final Notifier<ConsumerRecord<String, String>> notifier =
                message -> {};
        
        // Run the test
        kafkaConsumer.subscribe(destination, notifier);
        
        // Verify the results
    }
    
    @Test
    public void testSubscribe2() {
        // Setup
        final Notifier<ConsumerRecord<String, String>> notifier =
                msg -> {};
        
        // Run the test
        kafkaConsumer.subscribe(TOPIC, notifier);
        
        // Verify the results
    }
    
    @Test
    public void testUnsubscribe1() {
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.unsubscribe(TOPIC)
        );
    }
    
    @Test
    public void testUnsubscribe2() {
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.unsubscribe(TOPIC)
        );
    }
    
    @Test
    public void testUnsubscribe3() {
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.unsubscribe(TOPIC,null
                     )
        );
    }
    
    @Test
    public void testUnsubscribe4() {
        assertThrows(UnsupportedOperationException.class,
                     () -> kafkaConsumer.unsubscribe(TOPIC, null)
        );
    }
    
    @Test
    public void testClose() {
        // Setup
        // Run the test
        kafkaConsumer.close();
        
        // Verify the results
    }
    
    @Test
    public void testTraceStorageGetterAndSetter() {
        final TraceStorage traceStorage = mock(TraceStorage.class);
        kafkaConsumer.setTraceStorage(traceStorage);
        assertEquals(
                traceStorage, kafkaConsumer.getTraceStorage()
        );
    }
    
    @Test
    public void testGetTopicPartitions2() {
        // Setup
        final List<TopicPartition> expectedResult = Collections.singletonList(
                new TopicPartition(TOPIC, 0));
        
        // Run the test
        final List<TopicPartition> result = kafkaConsumer
                .getTopicPartitions(destination);
        
        // Verify the results
        assertEquals(expectedResult, result);
    }
    
}
