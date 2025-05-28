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

package io.cloudapp.messaging.rocketmq;

import io.cloudapp.api.messaging.Consumer;
import io.cloudapp.api.messaging.Notifier;
import io.cloudapp.api.messaging.ThreadLocalTraceStorage;
import io.cloudapp.api.messaging.TraceStorage;
import io.cloudapp.api.messaging.model.Destination;
import io.cloudapp.api.messaging.model.Location;
import io.cloudapp.api.messaging.model.MQMessage;
import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.messaging.rocketmq.model.RocketDestination;
import io.cloudapp.messaging.rocketmq.model.RocketMQMessage;
import io.cloudapp.messaging.rocketmq.properties.RocketConsumerProperties;
import io.cloudapp.util.NetUtil;
import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.LitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.net.SocketAddress;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class CloudAppRocketConsumer implements InitializingBean,
        Consumer<LitePullConsumer, MessageExt> {
    
    private static final int SINGLE = 1;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppRocketConsumer.class);
    
    private RocketConsumerProperties properties;
    
    private TraceStorage traceStorage = new ThreadLocalTraceStorage();
    
    private static final Map<RocketDestination, DefaultLitePullConsumer> CONSUMERS =
            Collections.synchronizedMap(new HashMap<>());
    
    /**
     * default consumer and subscribed all topic
     */
    private DefaultLitePullConsumer defaultConsumer;
    /**
     * Configured destination
     */
    private RocketDestination defaultDestination;
    
    private boolean initialized = false;
    
    
    public CloudAppRocketConsumer(RocketConsumerProperties properties) {
        this.properties = properties;
    }
    
    @Override
    public LitePullConsumer getDelegatingConsumer() {
        return defaultConsumer;
    }
    
    @Override
    public MQMessage<? extends MessageExt> pull(Destination destination)
            throws CloudAppException {
        return this.pull(destination, DEFAULT_TIMEOUT);
    }
    
    @Override
    public Collection<MQMessage<? extends MessageExt>> pull(
            Destination destination, int count
    ) throws CloudAppException {
        
        return this.pull(destination, count, DEFAULT_TIMEOUT);
    }
    
    @Override
    public MessageExt pull(String topic) {
        
        List<MessageExt> list = pollTopicMessages(
                SINGLE, DEFAULT_TIMEOUT, createDestination(topic)
        );
        
        if (list == null || list.isEmpty()) {
            logger.info("No message from topic: {}", topic);
            return null;
        }
        
        return list.get(0);
    }
    
    @Override
    public Collection<MessageExt> pull(String topic, int count) {
        
        return pollTopicMessages(count, DEFAULT_TIMEOUT,
                                 createDestination(topic)
        );
    }
    
    @Override
    public MQMessage<? extends MessageExt> pull(
            Destination destination, Duration timeout
    ) {
        List<MessageExt> list = pollTopicMessages(
                SINGLE, DEFAULT_TIMEOUT, destination
        );
        
        if (list == null || list.isEmpty()) {
            logger.info("No message from topic: {}", destination.getTopic());
            return null;
        }
        
        return convertMessage(list.get(0), destination);
    }
    
    @Override
    public MessageExt pull(String topic, Duration timeout) {
        
        RocketDestination rd = createDestination(topic);
        
        List<MessageExt> list = pollTopicMessages(SINGLE, DEFAULT_TIMEOUT, rd);
        
        if (list == null || list.isEmpty()) {
            logger.info("No message from topic: {}", topic);
            return null;
        }
        
        return list.get(0);
    }
    
    
    @Override
    public Collection<MQMessage<? extends MessageExt>> pull(
            Destination destination, int count, Duration timeout
    ) {
        List<MessageExt> list = pollTopicMessages(count, timeout, destination);
        
        return list != null ? list.stream()
                                  .map(e -> convertMessage(e, destination))
                                  .collect(Collectors.toList()) : null;
    }
    
    
    @Override
    public Collection<MessageExt> pull(
            String topic, int count, Duration timeout
    ) {
        return pollTopicMessages(count, timeout, createDestination(topic));
    }
    
    MQMessage<MessageExt> convertMessage(
            MessageExt m, Destination destination
    ) {
        if (m == null) {
            return null;
        }
        
        MQMessage<MessageExt> message = new RocketMQMessage();
        message.setMessageBody(m);
        message.setDestination(destination);
        message.setMessageID(m.getMsgId());
        message.setSentTimestamp(m.getBornTimestamp());
        message.setReceivedTimestamp(m.getStoreTimestamp());
        message.setDeliveredTimestamp(System.currentTimeMillis());
        message.setReceiver(createLocation(m.getBornHost()));
        message.setSender(createLocation(m.getStoreHost()));
        
        return message;
    }
    
    @Override
    public void subscribe(
            Destination destination, Notifier<MessageExt> notifier
    ) {
        if (destination == null || destination.getTopic() == null
                || destination.getTopic().isEmpty()) {
            throw new CloudAppException("Topic can not be empty");
        }
        
        RocketDestination rd = destination instanceof RocketDestination ?
                (RocketDestination) destination :
                new RocketDestination(destination.getTopic());
        
        boolean contains = CONSUMERS.keySet().stream().anyMatch(d -> d.isContains(rd));
        
        if (!CONSUMERS.containsKey(rd)) {
            String name = "Pull" + System.currentTimeMillis()
                    + new Random().nextInt(10000);
            DefaultLitePullConsumer consumer = createConsumer(
                    rd, properties.getPullBatchSize(), name
            );
            this.start(consumer);
            
            CONSUMERS.put(rd, consumer);
            
            if (initialized) {
                reSubscribe();
            } else {
                try {
                    defaultConsumer.subscribe(rd.getTopic(), rd.getTagsString());
                } catch (MQClientException e) {
                    throw new CloudAppException("subscribe topic error", e);
                }
            }
        }
        
        if (notifier != null) {
            MessageExt message = new MessageExt();
            message.setBody(Base64.getEncoder().encode("subscribe".getBytes()));
            MQMessage<MessageExt> mq = convertMessage(message, rd);
            notifier.onMessageNotified(mq);
        }
    }
    
    private void reSubscribe() {
        synchronized (this) {
            defaultConsumer.shutdown();
            defaultConsumer = createConsumer(
                    defaultDestination,
                    properties.getPullBatchSize(),
                    properties.getName()
            );
            
            CONSUMERS.keySet().stream().filter(
                    d -> !d.equals(defaultDestination)
            ).forEach(d -> {
                try {
                    defaultConsumer.subscribe(d.getTopic(), d.getTagsString());
                } catch (MQClientException e) {
                    throw new CloudAppException(
                            "subscribe topic error", e);
                }
            });
            this.start(defaultConsumer);
        }
    }
    
    @Override
    public void subscribe(
            String topic, Notifier<MessageExt> notifier
    ) {
        this.subscribe(new RocketDestination(topic), notifier);
    }
    
    @Override
    public void unsubscribe(Destination destination) {
        this.unsubscribe(destination, null);
    }
    
    @Override
    public void unsubscribe(String topic) {
        this.unsubscribe(topic, null);
    }
    
    @Override
    public void unsubscribe(
            Destination destination, Notifier<MessageExt> notifier
    ) {
        if (destination == null || destination.getTopic() == null
                || destination.getTopic().isEmpty()) {
            throw new CloudAppException("Topic can not be empty");
        }
        
        RocketDestination rd = destination instanceof RocketDestination ?
                (RocketDestination) destination :
                new RocketDestination(destination.getTopic());
        
        if(rd.equals(defaultDestination)) {
            throw new CloudAppException("Default destination can not be unsubscribed");
        }
        
        DefaultLitePullConsumer consumer = this.getPullConsumer(rd);
        if(initialized) {
            synchronized (this) {
                consumer.shutdown();
                CONSUMERS.remove(rd);
                
                reSubscribe();
            }
        } else {
            defaultConsumer.unsubscribe(rd.getTopic());
            CONSUMERS.keySet().forEach(d -> {
                try {
                    defaultConsumer.subscribe(d.getTopic(), d.getTagsString());
                } catch (MQClientException e) {
                    throw new CloudAppException("subscribe topic error", e);
                }
            });
        }
        
        if (notifier != null) {
            MessageExt message = new MessageExt();
            message.setBody(Base64.getEncoder().encode("unsubscribe".getBytes()));
            MQMessage<MessageExt> mq = convertMessage(message, rd);
            notifier.onMessageNotified(mq);
        }
        
    }
    
    @Override
    public void unsubscribe(
            String topic, Notifier<MessageExt> notifier
    ) {
        RocketDestination rd = createDestination(topic);
        
        this.unsubscribe(rd, notifier);
        
    }
    
    @Override
    public void close() {
        defaultConsumer.shutdown();
    }
    
    public void start(DefaultLitePullConsumer consumer) {
        try {
            consumer.start();
        } catch (MQClientException e) {
            throw new CloudAppException("Error to start consumer", e);
        }
    }
    
    private DefaultLitePullConsumer createConsumer(
            RocketDestination destination, int size, String name
    ) {
        try {
            DefaultLitePullConsumer litePullConsumer = RocketMQUtil
                    .createDefaultLitePullConsumer(
                            properties.getNameServer(),
                            properties.getAccessChannel(),
                            properties.getGroup(),
                            destination.getTopic(),
                            properties.getMessageModel(),
                            SelectorType.TAG,
                            destination.getTagsString(),
                            properties.getUsername(),
                            properties.getPassword(),
                            size,
                            properties.isUseTLS()
                    );
            litePullConsumer.setEnableMsgTrace(properties.isEnableMsgTrace());
            
            litePullConsumer.setCustomizedTraceTopic(
                    properties.getTraceTopic());
            
            if (StringUtils.hasText(properties.getNamespace())) {
                litePullConsumer.setNamespace(properties.getNamespace());
            }
            
            litePullConsumer.setInstanceName(name);
            
            return litePullConsumer;
        } catch (MQClientException e) {
            throw new CloudAppException("try create consumer failed.", e);
        }
    }
    
    private List<MessageExt> pollTopicMessages(
            int count, Duration timeout, Destination destination
    ) {
        List<MessageExt> list;
        
        RocketDestination rd = null;
        
        if (destination instanceof RocketDestination) {
            rd = (RocketDestination) destination;
        } else if (destination != null) {
            new RocketDestination(destination.getTopic());
        }
        
        DefaultLitePullConsumer lpc = this.getPullConsumer(rd);
        int defaultCount = properties.getPullBatchSize();
        
        synchronized (this) {
            if(defaultCount != count) {
                lpc.setPullBatchSize(count);
            }
            long timeoutMillis = timeout == null ?
                    lpc.getPollTimeoutMillis() : timeout.toMillis();
            
            list = lpc.poll(timeoutMillis);
            
            if(defaultCount != count) {
                lpc.setPullBatchSize(defaultCount);
            }
        }
        
        return list;
    }
    
    /**
     * setting trace storage
     * @param traceStorage trace storage
     */
    public void setTraceStorage(TraceStorage traceStorage) {
        this.traceStorage = traceStorage;
    }
    
    /**
     * get current trace storage
     * @return trace storage
     */
    public TraceStorage getTraceStorage() {
        return traceStorage;
    }
    
    @Override
    public void afterPropertiesSet() {
        defaultDestination = new RocketDestination(
                properties.getTopic(),
                properties.getTags()
        );
        
        this.defaultConsumer = createConsumer(
                defaultDestination, properties.getPullBatchSize(), properties.getName()
        );
        
        CONSUMERS.put(defaultDestination, defaultConsumer);
        
        this.start(defaultConsumer);
        initialized = true;
    }
    
    @Nullable
    private static RocketDestination createDestination(String topic) {
        RocketDestination rd = null;
        if (StringUtils.hasText(topic)) {
            rd = new RocketDestination(topic);
        }
        return rd;
    }
    
    private Location createLocation(SocketAddress bornHost) {
        if (bornHost == null) {
            return null;
        }
        
        Location location = new Location();
        location.setPid(NetUtil.getProcessId());
        location.setHost(bornHost.toString());
        location.setTraceId(traceStorage.getTraceId());
        location.setSpanId(traceStorage.getSpanId());
        location.setThreadId(Thread.currentThread().getId());
        location.setThreadName(Thread.currentThread().getName());
        
        return location;
    }
    
    private DefaultLitePullConsumer getPullConsumer(
            RocketDestination destination
    ) {
        ClientConfig clientConfig = CONSUMERS.get(destination);
        
        if(clientConfig == null) {
            String message = "Topic " + destination.getTopic()
                    + ":" + destination.getTagsString()
                    + " not be subscribed";
            throw new CloudAppException(message);
        }
        return defaultConsumer;
    }
    
    public void refresh(RocketConsumerProperties input) {
        this.properties = input;
        initialized = true;
        
        CONSUMERS.forEach((k,v) -> v.shutdown());
        CONSUMERS.clear();
        
        defaultDestination = new RocketDestination(
                properties.getTopic(),
                properties.getTags()
        );
        
        this.defaultConsumer = createConsumer(
                defaultDestination, properties.getPullBatchSize(), properties.getName()
        );
        
        
        CONSUMERS.put(defaultDestination, defaultConsumer);
        
        this.start(defaultConsumer);
        
        try {
            this.defaultConsumer.subscribe(defaultDestination.getTopic(),
                                           defaultDestination.getTagsString());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        
        initialized = true;
    }
    
}
