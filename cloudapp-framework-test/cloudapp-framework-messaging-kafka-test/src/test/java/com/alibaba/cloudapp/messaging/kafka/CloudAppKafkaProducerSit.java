/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.alibaba.cloudapp.messaging.kafka;

import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.messaging.kafka.model.KafkaDestination;
import com.alibaba.cloudapp.messaging.kafka.model.KafkaMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CloudAppKafkaProducerSit {
    
    private static final Logger logger =
            LoggerFactory.getLogger(CloudAppKafkaProducerSit.class);
    
    @Autowired
    @Qualifier("kafkaProducer")
    CloudAppKafkaProducer producer;
    
    @Test
    public void getDelegatingProducer() {
        KafkaProducer delegatingProducer = producer.getDelegatingProducer();
        ProducerRecord record = new ProducerRecord("test-topic", "hello, " +
                "world!");
        Future future = delegatingProducer.send(record);
        try {
            Object o = future.get();
            logger.info("send result: " + o.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(delegatingProducer);
    }
    
    @Test
    public void send_MQMessage() {
        MQMessage<ProducerRecord> message = new KafkaMessage();
        
        ProducerRecord record = new ProducerRecord("test-topic", "hello, " +
                "world!");
        message.setMessageBody(record);
        
        producer.send(message);
    }
    
    @Test
    public void send_Destination_ProducerRecord() {
        Destination destination = new KafkaDestination("test-topic", 0);
        ProducerRecord record = new ProducerRecord(destination.getTopic(), 0,
                                                   "testKey",
                                                   "hello, world!"
        );
        producer.send(destination, record);
    }
    
    @Test
    public void send_topic_message() {
        ProducerRecord record = new ProducerRecord<>("test-topic", "hello, " +
                "world!");
        producer.send(record);
    }
    
    @Test
    public void send_Destination_message() {
        Destination destination = new KafkaDestination("test-topic", 0);
        producer.send(destination, "hello, world!");
    }
    
    @Test
    public void sendAsync_MQMessage() {
        MQMessage<ProducerRecord> message = new KafkaMessage();
        
        ProducerRecord record = new ProducerRecord("test-topic", "hello, " +
                "world!");
        message.setMessageBody(record);
        
        producer.sendAsync(message);
    }
    
    @Test
    public void sendAsync_Destination_ProducerRecord() {
        Destination destination = new KafkaDestination("test-topic", 0);
        ProducerRecord record = new ProducerRecord(destination.getTopic(), 1,
                                                   "testKey",
                                                   "hello, world!"
        );
        producer.sendAsync(destination, record);
    }
    
    @Test
    public void sendAsync_topic_message() {
        producer.sendAsync("test-topic", "hello, world!");
    }
    
    @Test
    public void sendAsync_Destination_message() {
        Destination destination = new KafkaDestination("test-topic", 0);
        producer.sendAsync(destination, "hello, world!");
    }
    
}
