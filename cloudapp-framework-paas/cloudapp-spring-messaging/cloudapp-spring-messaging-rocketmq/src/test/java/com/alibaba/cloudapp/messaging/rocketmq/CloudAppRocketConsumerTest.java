package com.alibaba.cloudapp.messaging.rocketmq;


import com.alibaba.cloudapp.api.messaging.Notifier;
import com.alibaba.cloudapp.api.messaging.TraceStorage;
import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.messaging.rocketmq.model.RocketDestination;
import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketConsumerProperties;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.LitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CloudAppRocketConsumerTest {

    @Mock
    private RocketConsumerProperties mockProperties;

    @Mock
    private DefaultLitePullConsumer mockConsumer;

    @Mock
    private TraceStorage mockTraceStorage;

    @Mock
    private Notifier<MessageExt> mockNotifier;

    @Mock
    private MessageExt mockMessageExt;

    private Destination destination;

    @InjectMocks
    private CloudAppRocketConsumer consumer;
    
    private static final String TOPIC = "test-topic";

    @Before
    public void setUp() throws Exception {
        when(mockProperties.getPullBatchSize()).thenReturn(10);
        when(mockProperties.getNameServer()).thenReturn("localhost:9876");
        when(mockProperties.getAccessChannel()).thenReturn(AccessChannel.LOCAL.name());
        when(mockProperties.getGroup()).thenReturn("group");
        when(mockProperties.getMessageModel()).thenReturn(MessageModel.CLUSTERING);
        when(mockProperties.getUsername()).thenReturn("username");
        when(mockProperties.getPassword()).thenReturn("password");
        when(mockProperties.isUseTLS()).thenReturn(false);
        when(mockProperties.getTraceTopic()).thenReturn("traceTopic");
        when(mockProperties.getNamespace()).thenReturn("namespace");
        
        destination = new RocketDestination(TOPIC);
        

        consumer = new CloudAppRocketConsumer(mockProperties){
            
            @Override
            public void start(DefaultLitePullConsumer consumer) {
                // do nothing
            }
            
        };
        consumer.setTraceStorage(mockTraceStorage);
        
        Field field = CloudAppRocketConsumer.class.getDeclaredField("defaultConsumer");
        field.setAccessible(true);
        field.set(consumer, mockConsumer);
        
        field = CloudAppRocketConsumer.class.getDeclaredField("CONSUMERS");
        field.setAccessible(true);
        Map<Destination, DefaultLitePullConsumer> consumers =
                (Map<Destination, DefaultLitePullConsumer>)field.get(consumer);
        
        consumers.put(destination, mockConsumer);
    }

    @Test
    public void testGetDelegatingConsumer() {
        LitePullConsumer result = consumer.getDelegatingConsumer();
        assertEquals(mockConsumer, result);
    }

    @Test
    public void testPull1() throws CloudAppException {
        when(mockConsumer.poll(anyLong()))
                .thenReturn(Collections.singletonList(mockMessageExt));

        MQMessage<? extends MessageExt> result = consumer.pull(
                destination);

        assertEquals(mockMessageExt, result.getMessageBody());
    }

    @Test
    public void testPull2() throws CloudAppException {
        when(mockConsumer.poll(anyLong())).thenReturn(Collections.singletonList(mockMessageExt));

        Collection<MQMessage<? extends MessageExt>> result = consumer.pull(
                destination, 1);

        assertEquals(1, result.size());
        assertEquals(mockMessageExt, result.iterator().next().getMessageBody());
    }

    @Test
    public void testPull3() {
        when(mockConsumer.poll(anyLong())).thenReturn(Collections.singletonList(mockMessageExt));

        MessageExt result = consumer.pull(TOPIC);

        assertEquals(mockMessageExt, result);
    }

    @Test
    public void testPull4() {
        when(mockConsumer.poll(anyLong())).thenReturn(Collections.singletonList(mockMessageExt));

        Collection<MessageExt> result = consumer.pull(TOPIC, 1);

        assertEquals(1, result.size());
        assertEquals(mockMessageExt, result.iterator().next());
    }

    @Test
    public void testPull5() throws CloudAppException {
        when(mockConsumer.poll(anyLong())).thenReturn(Collections.singletonList(mockMessageExt));

        MQMessage<? extends MessageExt> result = consumer.pull(
                destination, Duration.ofSeconds(5));

        assertEquals(mockMessageExt, result.getMessageBody());
    }

    @Test
    public void testPull6() {
        when(mockConsumer.poll(anyLong())).thenReturn(Collections.singletonList(mockMessageExt));

        MessageExt result = consumer.pull(TOPIC, Duration.ofSeconds(5));

        assertEquals(mockMessageExt, result);
    }

    @Test
    public void testPull7() throws CloudAppException {
        when(mockConsumer.poll(anyLong())).thenReturn(Collections.singletonList(mockMessageExt));

        Collection<MQMessage<? extends MessageExt>> result = consumer.pull(
                destination, 1, Duration.ofSeconds(5));

        assertEquals(1, result.size());
        assertEquals(mockMessageExt, result.iterator().next().getMessageBody());
    }

    @Test
    public void testPull8() {
        when(mockConsumer.poll(anyLong())).thenReturn(Collections.singletonList(mockMessageExt));

        Collection<MessageExt> result = consumer.pull(TOPIC, 1, Duration.ofSeconds(5));

        assertEquals(1, result.size());
        assertEquals(mockMessageExt, result.iterator().next());
    }

    @Test
     public void testSubscribe1() {
        consumer.subscribe(destination, mockNotifier);

        verify(mockNotifier).onMessageNotified(any(MQMessage.class));
    }
    
    @Test
     public void testSubscribe_resubscribe() throws Exception {
        Field field = CloudAppRocketConsumer.class.getDeclaredField("initialized");
        field.setAccessible(true);
        field.set(consumer, true);
        
        field = CloudAppRocketConsumer.class.getDeclaredField("defaultDestination");
        field.setAccessible(true);
        field.set(consumer, destination);
        
        
        Destination dt = new RocketDestination("topic");
        consumer.subscribe(dt, mockNotifier);

        verify(mockNotifier).onMessageNotified(any(MQMessage.class));
    }

    @Test
    public void testSubscribe2() throws Exception {
        String topic = "topic";
        consumer.subscribe(topic, mockNotifier);

        verify(mockConsumer, times(1))
                .subscribe(eq(topic), anyString());
        verify(mockNotifier, times(1))
                .onMessageNotified(any(MQMessage.class));
        
        Field field = CloudAppRocketConsumer.class.getDeclaredField("defaultDestination");
        field.setAccessible(true);
        field.set(consumer, destination);
        
        field = CloudAppRocketConsumer.class.getDeclaredField("initialized");
        field.setAccessible(true);
        field.set(consumer, true);
        
        consumer.unsubscribe(topic, mockNotifier);
    }

    @Test
    public void testUnsubscribe1() {
        consumer.unsubscribe(destination);

        verify(mockConsumer).unsubscribe(eq(TOPIC));
    }
    
    @Test
    public void testUnsubscribe2() {
        consumer.unsubscribe(TOPIC);

        verify(mockConsumer).unsubscribe(eq(TOPIC));
    }

    @Test
    public void testConvertMessage() {
        MQMessage<MessageExt> result = consumer.convertMessage(
                mockMessageExt, destination
        );

        assertEquals(mockMessageExt, result.getMessageBody());
        assertEquals(destination, result.getDestination());
    }
    
    @Test
    public void refresh() throws MQClientException {
        when(mockProperties.getTopic()).thenReturn(TOPIC);
        
        consumer.refresh(mockProperties);
    }
    
    @Test
    public void afterPropertiesSet() {
        when(mockProperties.getTopic()).thenReturn(TOPIC);
        
        consumer.afterPropertiesSet();
    }
    
    @Test
    public void convertMessage() {
        when(mockMessageExt.getBornHost()).thenReturn(
                new InetSocketAddress("localhost", 80));
        MQMessage<MessageExt> result = consumer.convertMessage(
                mockMessageExt, destination);
        
        assertEquals(mockMessageExt, result.getMessageBody());
        assertEquals(destination, result.getDestination());
    }
}
