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

import com.alibaba.cloudapp.api.messaging.Notifier;
import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.messaging.kafka.model.KafkaDestination;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CloudAppKafkaConsumerSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppKafkaConsumerSit.class);
    
    @Autowired
    @Qualifier("kafkaConsumerConfig")
    KafkaConsumerProperties consumerProperties;
    
    
    private CloudAppKafkaConsumer consumer;
    
    @Before
    public void init() {
        consumer = new CloudAppKafkaConsumer(consumerProperties);
    }
    
    @Test
    public void getDelegatingConsumer() {
        KafkaConsumer delegatingConsumer = consumer.getDelegatingConsumer();
        
        List<String> subscribedTopics = new ArrayList();
        subscribedTopics.add("test-topic");
        
        delegatingConsumer.subscribe(subscribedTopics);
        int counts = 0;
        while (counts < 10) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                logger.info("consume partition: {}, offset: {}. message " +
                                    "counts: {}.", record.partition(),
                            record.offset(), counts + 1
                );
                counts++;
            }
        }
    }
    
    
    @Test(expected = UnsupportedOperationException.class)
    public void pull_Destination() {
        Destination destination = new KafkaDestination("test-topic", 0);
        consumer.pull(destination);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void pull_Destination_count() {
        Destination destination = new KafkaDestination("test-topic", 0);
        consumer.pull(destination, 10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void pull_topic() {
        consumer.pull("test-topic");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void pull_topic_count() {
        consumer.pull("test-topic", 10);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void pull_Destination_timeout() {
        Destination destination = new KafkaDestination("test-topic", 0);
        Duration timeout = Duration.ofSeconds(1);
        consumer.pull(destination, timeout);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void pull_topic_timeout() {
        Duration timeout = Duration.ofSeconds(1);
        consumer.pull("test-topic", timeout);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void pull_Destination_count_timeout() {
        Destination destination = new KafkaDestination("test-topic", 0);
        Duration timeout = Duration.ofSeconds(1);
        consumer.pull(destination, 10, timeout);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void pull_topic_count_timeout() {
        Duration timeout = Duration.ofSeconds(1);
        consumer.pull("test-topic", 10, timeout);
    }
    
    @Test
    public void subscribe_Destination_Notifier() {
        
        Destination destination = new KafkaDestination("test-topic", 0);
        consumer.subscribe(destination, new Notifier<ConsumerRecord>() {
            
            @Override
            public void onMessageNotified(MQMessage<ConsumerRecord> message) {
                logger.info("subscribe message: {}", message);
            }
        });
    }
    
    @Test
    public void subscribe_topic_Notifier() {
        consumer.subscribe("test-topic", new Notifier<ConsumerRecord>() {
            
            @Override
            public void onMessageNotified(MQMessage<ConsumerRecord> message) {
                logger.info("subscribe message: {}", message);
            }
        });
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void unsubscribe_Destination() {
        Destination destination = new KafkaDestination("test-topic", 0);
        consumer.unsubscribe(destination);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void unsubscribe_topic() {
        consumer.unsubscribe("test-topic");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void unsubscribe_Destination_Notifier() {
        Destination destination = new KafkaDestination("test-topic", 0);
        consumer.unsubscribe(destination, new Notifier<ConsumerRecord>() {
            
            @Override
            public void onMessageNotified(MQMessage<ConsumerRecord> message) {
                logger.info("unsubscribe message: {}", message);
            }
        });
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void unsubscribe_topic_Notifier() {
        consumer.unsubscribe("test-topic", new Notifier<ConsumerRecord>() {
            
            @Override
            public void onMessageNotified(MQMessage<ConsumerRecord> message) {
                logger.info("unsubscribe message: {}", message);
            }
        });
    }
    
}
