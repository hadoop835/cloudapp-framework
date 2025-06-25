package com.alibaba.cloudapp.api.messaging.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MQMessageTest {
    
    private MQMessage<String> mqMessageUnderTest;
    
    @Before
    public void setUp() throws Exception {
        mqMessageUnderTest = new MQMessage<String>() {};
    }
    
    @Test
    public void testMessageIDGetterAndSetter() {
        final String messageID = "messageID";
        mqMessageUnderTest.setMessageID(messageID);
        assertEquals(messageID, mqMessageUnderTest.getMessageID());
    }
    
    @Test
    public void testMessageBodyGetterAndSetter() {
        final String messageBody = "messageBody";
        mqMessageUnderTest.setMessageBody(messageBody);
        assertEquals(messageBody, mqMessageUnderTest.getUnderlyingMessage());
    }
    
    @Test
    public void testDestinationGetterAndSetter() {
        final Destination destination = null;
        mqMessageUnderTest.setDestination(destination);
        assertEquals(destination, mqMessageUnderTest.getDestination());
    }
    
    @Test
    public void testReplyToGetterAndSetter() {
        final Destination replyTo = null;
        mqMessageUnderTest.setReplyTo(replyTo);
        assertEquals(replyTo, mqMessageUnderTest.getReplyTo());
    }
    
    @Test
    public void testSentTimestampGetterAndSetter() {
        final long sentTimestamp = 0L;
        mqMessageUnderTest.setSentTimestamp(sentTimestamp);
        assertEquals(sentTimestamp, mqMessageUnderTest.getSentTimestamp());
    }
    
    @Test
    public void testDeliveredTimestampGetterAndSetter() {
        final long deliveredTimestamp = 0L;
        mqMessageUnderTest.setDeliveredTimestamp(deliveredTimestamp);
        assertEquals(deliveredTimestamp,
                     mqMessageUnderTest.getDeliveredTimestamp()
        );
    }
    
    @Test
    public void testReceivedTimestampGetterAndSetter() {
        final long receivedTimestamp = 0L;
        mqMessageUnderTest.setReceivedTimestamp(receivedTimestamp);
        assertEquals(receivedTimestamp,
                     mqMessageUnderTest.getReceivedTimestamp()
        );
    }
    
    @Test
    public void testMessageBody1GetterAndSetter() {
        final String messageBody = "messageBody";
        mqMessageUnderTest.setMessageBody(messageBody);
        assertEquals(messageBody, mqMessageUnderTest.getMessageBody());
    }
    
    @Test
    public void testSenderGetterAndSetter() {
        final Location sender = new Location();
        mqMessageUnderTest.setSender(sender);
        assertEquals(sender, mqMessageUnderTest.getSender());
    }
    
    @Test
    public void testReceiverGetterAndSetter() {
        final Location receiver = new Location();
        mqMessageUnderTest.setReceiver(receiver);
        assertEquals(receiver, mqMessageUnderTest.getReceiver());
    }
    
}
