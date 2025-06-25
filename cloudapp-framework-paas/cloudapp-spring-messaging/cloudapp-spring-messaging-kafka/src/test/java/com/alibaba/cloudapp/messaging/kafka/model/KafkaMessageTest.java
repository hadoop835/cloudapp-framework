package com.alibaba.cloudapp.messaging.kafka.model;

import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.Location;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KafkaMessageTest {
    
    private KafkaMessage<String, String> kafkaMessage;
    
    @BeforeEach
    public void setUp() {
        kafkaMessage = new KafkaMessage<>();
    }
    
    @Test
    public void testMessageIDGetterAndSetter() {
        final String messageID = "messageID";
        kafkaMessage.setMessageID(messageID);
        assertEquals(messageID, kafkaMessage.getMessageID());
    }
    
    @Test
    public void testMessageBodyGetterAndSetter() {
        final ConsumerRecord<String, String> messageBody = new ConsumerRecord<>(
                "topic", 0, 0L, "key", "value");
        kafkaMessage.setMessageBody(messageBody);
        assertEquals(messageBody, kafkaMessage.getUnderlyingMessage());
    }
    
    @Test
    public void testDestinationGetterAndSetter() {
        final Destination destination = null;
        kafkaMessage.setDestination(destination);
        assertEquals(destination, kafkaMessage.getDestination());
    }
    
    @Test
    public void testReplyToGetterAndSetter() {
        final Destination replyTo = null;
        kafkaMessage.setReplyTo(replyTo);
        assertEquals(replyTo, kafkaMessage.getReplyTo());
    }
    
    @Test
    public void testSentTimestampGetterAndSetter() {
        final long sentTimestamp = 0L;
        kafkaMessage.setSentTimestamp(sentTimestamp);
        assertEquals(sentTimestamp, kafkaMessage.getSentTimestamp());
    }
    
    @Test
    public void testDeliveredTimestampGetterAndSetter() {
        final long deliveredTimestamp = 0L;
        kafkaMessage.setDeliveredTimestamp(deliveredTimestamp);
        assertEquals(deliveredTimestamp,
                     kafkaMessage.getDeliveredTimestamp()
        );
    }
    
    @Test
    public void testReceivedTimestampGetterAndSetter() {
        final long receivedTimestamp = 0L;
        kafkaMessage.setReceivedTimestamp(receivedTimestamp);
        assertEquals(receivedTimestamp,
                     kafkaMessage.getReceivedTimestamp()
        );
    }
    
    @Test
    public void testMessageBody1GetterAndSetter() {
        final ConsumerRecord<String, String> messageBody = new ConsumerRecord<>(
                "topic", 0, 0L, "key", "value");
        kafkaMessage.setMessageBody(messageBody);
        assertEquals(messageBody, kafkaMessage.getMessageBody());
    }
    
    @Test
    public void testSenderGetterAndSetter() {
        final Location sender = new Location();
        kafkaMessage.setSender(sender);
        assertEquals(sender, kafkaMessage.getSender());
    }
    
    @Test
    public void testReceiverGetterAndSetter() {
        final Location receiver = new Location();
        kafkaMessage.setReceiver(receiver);
        assertEquals(receiver, kafkaMessage.getReceiver());
    }
    
}
