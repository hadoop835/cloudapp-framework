package io.cloudapp.messaging.kafka;

import io.cloudapp.api.messaging.model.Destination;
import io.cloudapp.api.messaging.model.Location;
import io.cloudapp.api.messaging.model.MQMessage;
import io.cloudapp.messaging.kafka.model.KafkaDestination;
import io.cloudapp.messaging.kafka.properties.KafkaProducerProperties;
import net.mguenther.kafka.junit.EmbeddedKafkaCluster;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;

import static net.mguenther.kafka.junit.EmbeddedKafkaCluster.provisionWith;
import static net.mguenther.kafka.junit.EmbeddedKafkaClusterConfig.defaultClusterConfig;
import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(MockitoJUnitRunner.class)
public class CloudAppKafkaProducerTest {
    
    private CloudAppKafkaProducer<String, String> producer;
    
    private AutoCloseable mockitoCloseable;
    private EmbeddedKafkaCluster kafka;
    
    private Destination destination;
    private MQMessage<ProducerRecord<String, String>> message;
    
    
    @Before
    public void setupKafka() {
        destination = new KafkaDestination("topic");
        Serializer<String> keySerializer = new StringSerializer();
        Serializer<String> valueSerializer = new StringSerializer();
        
        mockitoCloseable = openMocks(this);
        kafka = provisionWith(defaultClusterConfig());
        kafka.start();
        
        KafkaProducerProperties properties = new KafkaProducerProperties();
        properties.setBootstrapServers("localhost:9092");
        properties.setTopic("topic");
        properties.setGroup("group");
        properties.setReconnectBackoff(1000);
        properties.getProperties().put("client.id", "test");
        
        producer = new CloudAppKafkaProducer<>(
                properties.buildProducerProperties(),
                keySerializer,
                valueSerializer
        );
        final Location sender = new Location();
        sender.setHost("localhost");
        sender.setPid(9092);
        sender.setThreadId(1234);
        sender.setThreadName("thread_sender");
        sender.setTraceId("traceId");
        sender.setSpanId("spanId");
        final Location receiver =  new Location();
        sender.setHost("localhost");
        sender.setPid(123);
        sender.setThreadId(123);
        sender.setThreadName("thread_receiver");
        sender.setTraceId("traceId");
        sender.setSpanId("spanId");
        
        message = new MQMessage<ProducerRecord<String, String>>(){};
        message.setReplyTo(destination);
        message.setDestination(destination);
        message.setMessageBody(new ProducerRecord<>(
                "topic", "key", "message"));
        message.setMessageID("messageID");
        message.setSentTimestamp(System.currentTimeMillis());
        message.setReceivedTimestamp(System.currentTimeMillis());
        message.setDeliveredTimestamp(System.currentTimeMillis());
        message.setSender(sender);
        message.setReceiver(receiver);
    }
    
    @After
    public void tearDownKafka() throws Exception {
        mockitoCloseable.close();
        if(kafka != null) {
            kafka.stop();
        }
    }
    
    @Test
    public void testGetDelegatingProducer() {
        // Setup
        // Run the test
        final KafkaProducer<String, String> result = producer.getDelegatingProducer();
        
        // Verify the results
    }
    
    @Test
    public void testSend1() {
        // Setup
        
        // Run the test
        producer.send(message);
        
        // Verify the results
    }
    
    @Test
    public void testSend2() {
        // Setup
        final ProducerRecord<String, String> typeProducerRecord = new ProducerRecord<>(
                "topic", "key", "message");
        
        // Run the test
        producer.send(destination, typeProducerRecord);
        
        // Verify the results
    }
    
    @Test
    public void testSend3() {
        // Setup
        
        // Run the test
        producer.send("topic", "message");
        
        // Verify the results
    }
    
    @Test
    public void testSend4() {
        // Setup
        
        // Run the test
        producer.send(destination, "message");
        
        // Verify the results
    }
    
    @Test
    public void testSendAsync1() {
        // Setup
        
        // Run the test
        final CompletableFuture<?> result = producer.sendAsync(message);
        
        // Verify the results
    }
    
    @Test
    public void testSendAsync2() {
        // Setup
        final ProducerRecord<String, String> typeProducerRecord = new ProducerRecord<>(
                "topic", "key", "message");
        
        // Run the test
        final CompletableFuture<?> result = producer.sendAsync(
                destination, typeProducerRecord);
        
        // Verify the results
    }
    
    @Test
    public void testSendAsync3() {
        // Setup
        
        // Run the test
        final CompletableFuture<?> result = producer.sendAsync(
                "topic", "message");
        
        // Verify the results
    }
    
    @Test
    public void testSendAsync4() {
        // Setup
        
        // Run the test
        final CompletableFuture<?> result = producer.sendAsync(
                destination, "message"
        );
        
        // Verify the results
    }
    
    @Test
    public void testClose() {
        // Setup
        // Run the test
        producer.close();
        
        // Verify the results
    }
    
}
