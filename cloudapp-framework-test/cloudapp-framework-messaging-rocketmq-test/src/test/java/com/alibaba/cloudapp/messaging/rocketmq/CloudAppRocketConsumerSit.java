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

package com.alibaba.cloudapp.messaging.rocketmq;

import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.messaging.rocketmq.model.RocketDestination;
import org.apache.rocketmq.client.consumer.LitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CloudAppRocketConsumerSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppRocketConsumerSit.class);
    
    @Autowired
    CloudAppRocketConsumer consumer;
    
    @Test
    public void getDelegatingConsumer() {
        LitePullConsumer delegatingConsumer = consumer.getDelegatingConsumer();
        try {
            delegatingConsumer.subscribe("test-topic");
            
            if (!delegatingConsumer.isRunning()) {
                delegatingConsumer.start();
            }
            
            try {
                List<MessageExt> messageExts =
                        delegatingConsumer.poll(1000L * 120);
                messageExts.stream().forEach(
                        m -> logger.info("message: {}", m));
            } finally {
                delegatingConsumer.shutdown();
            }
            logger.info("consumer poll end.");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void pull_Destination() {
        List tags = new ArrayList<>();
        tags.add("MQMessage");
        Destination destination = new RocketDestination("test-topic", tags);
        
        MQMessage<? extends MessageExt> message = consumer.pull(destination);
        
        logger.info("pull message: ");
        if (message != null) {
            Arrays.stream(message.getClass().getDeclaredFields()).forEach(
                    field -> {
                        field.setAccessible(true);
                        String fieldName = field.getName();
                        try {
                            String fieldValue = String.valueOf(
                                    field.get(message));
                            logger.info("field name is: {}, value is {}",
                                        fieldName, fieldValue
                            );
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }
    
    @Test
    public void pull_Destination_count() {
        List tags = new ArrayList<>();
        tags.add("MQMessage");
        Destination destination = new RocketDestination("test-topic", tags);
        
        Collection<MQMessage<? extends MessageExt>> messages = consumer.pull(
                destination, 1);
        
        messages.stream().forEach(m -> logger.info("pull message: {}", m));
    }
    
    @Test
    public void pull_topic() {
        MessageExt message = consumer.pull("test-topic");
        logger.info("pull message: {}", message);
    }
    
    @Test
    public void pull_topic_count() {
        Collection<MessageExt> messages = consumer.pull("test-topic", 2);
        messages.stream().forEach(m -> logger.info("pull message: {}", m));
    }
    
    @Test
    public void pull_Destination_timeout() {
        List tags = new ArrayList<>();
        tags.add("MQMessage");
        Destination destination = new RocketDestination("test-topic", tags);
        
        MQMessage<? extends MessageExt> message = consumer.pull(destination,
                                                                Duration.ofSeconds(
                                                                        5)
        );
        logger.info("pull message: {}", message);
    }
    
    @Test
    public void pull_topic_timeout() {
        MessageExt message = consumer.pull("test-topic", Duration.ofSeconds(5));
        logger.info("pull message: {}", message);
    }
    
    @Test
    public void pull_Destination_count_timeout() {
        List tags = new ArrayList<>();
        tags.add("MQMessage");
        Destination destination = new RocketDestination("test-topic", tags);
        
        Collection<MQMessage<? extends MessageExt>> messages = consumer.pull(
                destination, 2, Duration.ofSeconds(5));
        messages.stream().forEach(m -> logger.info("pull message: {}", m));
    }
    
    
    @Test
    public void pull_topic_count_timeout() {
        Collection<MessageExt> messages = consumer.pull("test-topic", 2,
                                                        Duration.ofSeconds(5)
        );
        messages.stream().forEach(m -> logger.info("pull message: {}", m));
    }
    
    @Test
    public void subscribe_Destination_Notifier() {
        List tags = new ArrayList<>();
        tags.add("tag1");
        Destination destination = new RocketDestination("test-topic", tags);
        
        consumer.subscribe(destination, message -> {
            logger.info("message: {}", message);
        });
    }
    
    @Test
    public void unsubscribe_Destination_Notifier() {
        List tags = new ArrayList<>();
        tags.add("tag1");
        Destination destination = new RocketDestination("test-topic", tags);
        
        consumer.unsubscribe(destination, message -> {
            logger.info("message: {}", message);
        });
    }
    
    @Test
    public void subscribe_topic_Notifier() {
        consumer.subscribe("test-topic", message -> {
            logger.info("message: {}", message);
        });
    }
    
    @Test
    public void unsubscribe_topic_Notifier() {
        
        consumer.unsubscribe("test-topic", message -> {
            logger.info("message: {}", message);
        });
    }
    
    @Test
    public void unsubscribe_Destination() {
        List tags = new ArrayList<>();
        tags.add("tag2");
        Destination destination = new RocketDestination("test-topic", tags);
        
        consumer.unsubscribe(destination);
    }
    
    @Test
    public void unsubscribe_topic() {
        consumer.unsubscribe("test-topic");
    }
    
}
