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

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.Location;
import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.messaging.rocketmq.model.RocketDestination;
import com.alibaba.cloudapp.messaging.rocketmq.model.RocketMQMessage;
import com.alibaba.cloudapp.util.RandomStringGenerator;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CloudAppRocketProducerSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppRocketProducer.class);
    
    @Autowired
    @Qualifier("rocketProducer")
    CloudAppRocketProducer producer;
    
    @Test
    public void getDelegatingProducer_NormalMQ() {
        MQProducer delegatingProducer = producer.getDelegatingProducer();
        Message message =
                new Message("test-topic",
                            ("hello, world! " + RandomStringGenerator.generate(
                                    3)).getBytes(StandardCharsets.UTF_8)
                );
        try {
            SendResult result = delegatingProducer.send(message);
            logger.info("Send default message result is: {}",
                        result.getSendStatus().name()
            );
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void getDelegatingProducer_DelayMQ() {
        MQProducer delegatingProducer =
                producer.getDelegatingProducer();
        Message message = new Message("test-delay-topic", "delay", "delay",
                                      ("hello, world! " + RandomStringGenerator.generate(
                                              3)).getBytes(
                                              StandardCharsets.UTF_8)
        );
        message.setDelayTimeSec(1L * 3);
        try {
            SendResult result = delegatingProducer.send(message);
            logger.info("Send delay message result is: {}",
                        result.getSendStatus().name()
            );
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void getDelegatingProducer_OrderMQ() {
        MQProducer delegatingProducer =
                producer.getDelegatingProducer();
        Message message =
                new Message("test-order-topic", "ordered", "ordered",
                            ("hello, world! " + RandomStringGenerator.generate(
                                    3)).getBytes(StandardCharsets.UTF_8)
                );
        try {
            SendResult result = delegatingProducer.send(message);
            logger.info("Send order message result is: {}",
                        result.getSendStatus().name()
            );
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void getDelegatingProducer_TransactionMQ() {
        TransactionMQProducer delegatingProducer = (TransactionMQProducer) producer.getDelegatingProducer();
        
        TransactionListener transactionListener = new TransactionListener() {
            
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg,
                                                                 Object arg) {
                logger.info("Execute local transaction for message: {}",
                            new String(msg.getBody())
                );
                return LocalTransactionState.COMMIT_MESSAGE;
            }
            
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                logger.info(
                        "Check local transaction for message: {}" + new String(
                                msg.getBody()));
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        };
        
        delegatingProducer.setTransactionListener(transactionListener);
        
        
        Message message =
                new Message("test-tx-topic", "tx", "tx",
                            ("hello, world! " + RandomStringGenerator.generate(
                                    3)).getBytes(StandardCharsets.UTF_8)
                );
        
        try {
            TransactionSendResult result = delegatingProducer.sendMessageInTransaction(
                    message, null);
            logger.info("Send result: {}", result.getSendStatus().name());
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        
    }
    
    @Test
    public void send_MQMessage() {
        MQMessage message = new RocketMQMessage();
        message.setMessageID(RandomStringGenerator.generate(10));
        
        List tags = new ArrayList<>();
        tags.add("MQMessage");
        Destination destination = new RocketDestination("test-topic", tags);
        message.setDestination(destination);
        
        MessageExt messageExt = new MessageExt();
        messageExt.setTopic("test-topic");
        messageExt.setBody("hello, world!".getBytes());
        message.setMessageBody(messageExt);
        
        Location location = new Location();
        location.setHost("localhost-test");
        location.setPid(
                Integer.parseInt(
                        ManagementFactory.getRuntimeMXBean().getName().split(
                                "@")[0]));
        location.setThreadId(Thread.currentThread().getId());
        location.setThreadName(Thread.currentThread().getName());
        message.setSender(location);
        
        message.setSentTimestamp(System.currentTimeMillis());
        
        producer.send(message);
        
    }
    
    @Test
    public void send_Destination_MessageExt() throws CloudAppException {
        List tags = new ArrayList<>();
        tags.add("MQMessage");
        Destination destination = new RocketDestination("test-topic", tags);
        
        MessageExt messageExt = new MessageExt();
        messageExt.setTopic("test-topic");
        messageExt.setKeys("test-key");
        messageExt.setBody("hello, world!".getBytes());
        
        producer.send(destination, messageExt);
        
    }
    
    @Test
    public void send_topic_message() {
        producer.send("test-topic", "hello, world!");
    }
    
    @Test
    public void send_Destination_message() {
        List tags = new ArrayList<>();
        tags.add("MQMessage");
        Destination destination = new RocketDestination("test-topic", tags);
        
        producer.send(destination, "hello, world!");
    }
    
    @Test
    public void sendAsync_MQMessage() {
        MQMessage message = new RocketMQMessage();
        
        message.setMessageID(RandomStringGenerator.generate(10));
        
        List tags = new ArrayList<>();
        tags.add("tag" + RandomStringGenerator.generate(2));
        Destination destination = new RocketDestination("test-topic", tags);
        message.setDestination(destination);
        
        MessageExt messageExt = new MessageExt();
        messageExt.setTopic("test-topic");
        messageExt.setBody("hello, world!".getBytes());
        message.setMessageBody(messageExt);
        
        Location location = new Location();
        location.setHost("localhost-test");
        location.setPid(
                Integer.parseInt(
                        ManagementFactory.getRuntimeMXBean().getName().split(
                                "@")[0]));
        location.setThreadId(Thread.currentThread().getId());
        location.setThreadName(Thread.currentThread().getName());
        message.setSender(location);
        
        message.setSentTimestamp(System.currentTimeMillis());
        
        producer.sendAsync(message).whenComplete((result, throwable) -> {
            if (result != null) {
                logger.info("Send result is success. " +
                                    "message is: {}",
                            JSONObject.toJSONString(result)
                );
            } else {
                logger.info("Send result is failed.");
            }
        }).join();
        
    }
    
    @Test
    public void sendAsync_Destination_MessageExt() {
        List tags = new ArrayList<>();
        tags.add("tag" + RandomStringGenerator.generate(2));
        Destination destination = new RocketDestination("test-topic", tags);
        
        MessageExt messageExt = new MessageExt();
        messageExt.setTopic("test-topic");
        messageExt.setKeys("test-key-async");
        messageExt.setBody(("hello, world! " + RandomStringGenerator.generate(
                3)).getBytes());
        
        producer.sendAsync(destination, messageExt)
                .whenComplete((result, throwable) -> {
                    if (result.getSendStatus().equals(SendStatus.SEND_OK)) {
                        logger.info("Send result is success. " +
                                            "message is: {}",
                                    JSONObject.toJSONString(result)
                        );
                    } else {
                        logger.info("Send result is failed.");
                    }
                }).join();
        
    }
    
    @Test
    public void sendAsync_topic_message() throws Exception {
        producer.sendAsync("test-topic", "hello, world!")
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        logger.error("Send result is failed", throwable);
                    } else {
                        logger.info("Send result is success");
                    }
                    logger.info("Send result is: {}", result);
                }).join();
    }
    
    @Test
    public void sendAsync_Destination_message() {
        List tags = new ArrayList<>();
        tags.add("tag" + RandomStringGenerator.generate(2));
        Destination destination = new RocketDestination("test-topic", tags);
        
        producer.sendAsync(destination, "hello, world!")
                .whenComplete((result, throwable) -> {
                    if (result.getSendStatus().equals(SendStatus.SEND_OK)) {
                        logger.info("Send result is success. " +
                                            "message is: {}",
                                    JSONObject.toJSONString(result)
                        );
                    } else {
                        logger.info("Send result is failed.");
                    }
                }).join();
    }
    
}
