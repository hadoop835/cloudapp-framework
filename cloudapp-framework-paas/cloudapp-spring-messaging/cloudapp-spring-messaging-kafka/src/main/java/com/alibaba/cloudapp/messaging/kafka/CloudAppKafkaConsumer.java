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

import com.alibaba.cloudapp.api.messaging.Consumer;
import com.alibaba.cloudapp.api.messaging.Notifier;
import com.alibaba.cloudapp.api.messaging.ThreadLocalTraceStorage;
import com.alibaba.cloudapp.api.messaging.TraceStorage;
import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.Location;
import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.messaging.kafka.model.KafkaDestination;
import com.alibaba.cloudapp.messaging.kafka.model.KafkaMessage;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaConsumerProperties;
import com.alibaba.cloudapp.util.NetUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class CloudAppKafkaConsumer<K, V> extends KafkaConsumer<K, V>
        implements Consumer<KafkaConsumer<K, V>, ConsumerRecord<K, V>> {

    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppKafkaConsumer.class);

    private static final int DEFAULT_POLL_TIMEOUT_SECOND = 10;

    private TraceStorage traceStorage = new ThreadLocalTraceStorage();
    
    private Map<String, Object> properties;
    private KafkaConsumer<K, V> consumer;

    public CloudAppKafkaConsumer(Map<String, Object> properties,
                                 Deserializer<K> keyDeserializer,
                                 Deserializer<V> valueDeserializer) {
        super(properties, keyDeserializer, valueDeserializer);
    }

    public CloudAppKafkaConsumer(KafkaConsumerProperties properties) {
        super(properties.buildConsumerProperties());

        if(properties.getTopic() != null) {
            String topic = properties.getTopic();
            subscribe(Collections.singleton(topic));
        }
    }


    @Override
    public KafkaConsumer<K, V> getDelegatingConsumer() {
        return this;
    }

    /**
     * Pull a message from the destination
     *
     * @param destination The destination pulling from
     */
    @Override
    public MQMessage<? extends ConsumerRecord<K, V>> pull(Destination destination) {
        return this.pull(destination, Duration.ofSeconds(DEFAULT_POLL_TIMEOUT_SECOND));
    }

    /**
     * Pull specified numbers of message from the destination
     *
     * @param destination The destination pulling from
     * @param count       The desired message count
     * @return messages
     */
    @Override
    public Collection<MQMessage<? extends ConsumerRecord<K, V>>> pull(
            Destination destination,
            int count) {

        List<TopicPartition> topicPartitions = getTopicPartitions(destination);

        ConsumerRecords<K, V> records = this.poll(topicPartitions,
                count,
                DEFAULT_POLL_TIMEOUT_SECOND * 1000
        );

        return records.partitions()
                .stream()
                .flatMap(p -> records.records(p)
                        .stream()
                        .map(e -> createKafkaMessage(p, e, UUID.randomUUID().toString())))
                .collect(Collectors.toList());
    }


    /**
     * Pull a message from the topic
     *
     * @param topic the topic pulling from
     * @return message
     */
    @Override
    public ConsumerRecord<K, V> pull(String topic) {

        List<TopicPartition> topicPartitions = getTopicPartitions(topic);

        ConsumerRecords<K, V> records = this.poll(topicPartitions,
                1, DEFAULT_POLL_TIMEOUT_SECOND * 1000);

        if (records.count() == 1) {
            return records.iterator().next();
        }

        tryLogEmpty(topic);

        return null;
    }

    private static void tryLogEmpty(String topic) {
        if (logger.isDebugEnabled()) {
            logger.debug("Pull message from topic {} result empty", topic);
        }
    }

    /**
     * Pull specified numbers of message from the topic
     *
     * @param topic The topic pulling from
     * @param count The desired message count
     * @return messages
     */
    @Override
    public Collection<ConsumerRecord<K, V>> pull(String topic, int count) {
        List<TopicPartition> topicPartitions = getTopicPartitions(topic);

        ConsumerRecords<K, V> records = this.poll(topicPartitions,
                count, DEFAULT_POLL_TIMEOUT_SECOND * 1000);

        return records.partitions()
                .stream()
                .flatMap(p -> records.records(p).stream())
                .collect(Collectors.toList());
    }

    /**
     * Pull a message from the destination
     *
     * @param destination The destination pulling from
     * @param timeout     A timeout will waiting for.
     * @return message
     */
    @Override
    public MQMessage<? extends ConsumerRecord<K, V>> pull(
            Destination destination, Duration timeout) {
        List<TopicPartition> topicPartitions = getTopicPartitions(destination);

        ConsumerRecords<K, V> records = this.poll(topicPartitions,
                1, timeout.toMillis());

        if (records.count() == 1) {
            return createKafkaMessage(
                    records.partitions().iterator().next(),
                    records.iterator().next(),
                    UUID.randomUUID().toString()
            );
        }

        tryLogEmpty(destination.getTopic());

        return null;
    }

    /**
     * Pull a message from the topic and specified timeout
     *
     * @param topic   The topic pulling from
     * @param timeout A timeout will waiting for.
     * @return message
     */
    @Override
    public ConsumerRecord<K, V> pull(String topic, Duration timeout) {

        List<TopicPartition> topicPartitions = getTopicPartitions(topic);

        ConsumerRecords<K, V> records = this.poll(topicPartitions,
                1, timeout.toMillis());

        if (records.count() == 1) {
            return records.iterator().next();
        }

        tryLogEmpty(topic);

        return null;
    }

    /**
     * Pull specified numbers of message from the destination and specified timeout
     *
     * @param destination The destination pulling from
     * @param count       The desired message count
     * @param timeout     A timeout will waiting for.
     * @return messages
     */
    @Override
    public Collection<MQMessage<? extends ConsumerRecord<K, V>>> pull(
            Destination destination, int count, Duration timeout
    ) {
        List<TopicPartition> topicPartitions = getTopicPartitions(destination);

        ConsumerRecords<K, V> records = this.poll(
                topicPartitions, count, timeout.toMillis());

        return records.partitions()
                .stream()
                .flatMap(p ->
                        records.records(p)
                                .stream()
                                .map(e -> createKafkaMessage(p, e, UUID.randomUUID().toString())))
                .collect(Collectors.toList());
    }

    /**
     * Pull specified numbers of message from the topic and specified timeout
     *
     * @param topic   The topic pulling from
     * @param count   The desired message count
     * @param timeout A timeout will waiting for.
     * @return messages
     */
    @Override
    public Collection<ConsumerRecord<K, V>> pull(
            String topic, int count, Duration timeout) {
        List<TopicPartition> topicPartitions = getTopicPartitions(topic);

        ConsumerRecords<K, V> records = this.poll(topicPartitions,
                count, timeout.toMillis());

        return records.partitions()
                .stream()
                .flatMap(
                        p -> records.records(p).stream())
                .collect(Collectors.toList());
    }

    /**
     * Subscribe to the destination
     *
     * @param destination the destination to subscribe to
     * @param notifier    the message notifier
     */
    @Override
    public void subscribe(Destination destination,
                          Notifier<ConsumerRecord<K, V>> notifier) {
        this.subscribe(destination.getTopic(), notifier);
    }

    /**
     * Subscribe to the topic
     *
     * @param topic    the topic to subscribe to
     * @param notifier the message notifier
     */
    @Override
    public void subscribe(String topic,
                          Notifier<ConsumerRecord<K, V>> notifier) {
        subscribe(Collections.singletonList(topic));

        if (notifier != null) {
            notifier.onMessageNotified(null);
        }
    }

    /**
     * Unsubscribe from the destination
     *
     * @param destination the destination to unsubscribe from
     */
    @Override
    public void unsubscribe(Destination destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Unsubscribe from the topic
     *
     * @param topic the topic to unsubscribe from
     */
    @Override
    public void unsubscribe(String topic) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Unsubscribe from the destination
     *
     * @param destination the destination to unsubscribing from
     * @param notifier    message handler
     */
    @Override
    public void unsubscribe(Destination destination,
                            Notifier<ConsumerRecord<K, V>> notifier) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Unsubscribe from the topic
     *
     * @param topic    the topic unsubscribing from.
     * @param notifier the message notifier
     */
    @Override
    public void unsubscribe(String topic,
                            Notifier<ConsumerRecord<K, V>> notifier) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() {
        super.close();
    }

    public TraceStorage getTraceStorage() {
        return traceStorage;
    }

    public void setTraceStorage(TraceStorage traceStorage) {
        this.traceStorage = traceStorage;
    }

    private MQMessage<ConsumerRecord<K, V>> createKafkaMessage(
            TopicPartition partition,
            ConsumerRecord<K, V> record,
            String id
    ) {
        KafkaMessage<K, V> message = new KafkaMessage<>();
        message.setMessageID(id);
        if (record != null) {
            message.setSentTimestamp(record.timestamp());
        }
        message.setDeliveredTimestamp(System.currentTimeMillis());
        message.setReceivedTimestamp(System.currentTimeMillis());
        message.setMessageBody(record);
        message.setReceiver(createLocation(NetUtil.getLocalHostName()));
        message.setDestination(
                new KafkaDestination(partition.topic(), partition.partition()));
        return message;
    }

    private Location createLocation(String host) {
        Location loc = new Location();
        loc.setHost(host);
        loc.setPid(NetUtil.getProcessId());
        loc.setThreadId(Thread.currentThread().getId());
        loc.setThreadName(Thread.currentThread().getName());
        loc.setTraceId(traceStorage.getTraceId());
        loc.setSpanId(traceStorage.getSpanId());
        return loc;
    }

    private List<TopicPartition> getTopicPartitions(String topic) {
        Map<String, List<PartitionInfo>> topicMap = listTopics();

        if (!topicMap.containsKey(topic)) {
            logger.info("Topic {} not found", topic);
            return Collections.emptyList();
        } else {
            return topicMap.get(topic).stream().map(
                    e -> new TopicPartition(e.topic(), e.partition())
            ).collect(Collectors.toList());
        }
    }

    public List<TopicPartition> getTopicPartitions(Destination destination) {
        if (destination instanceof KafkaDestination) {

            KafkaDestination ks = (KafkaDestination) destination;
            return Collections.singletonList(
                    new TopicPartition(ks.getTopic(), ks.getPartition()));

        } else {
            return getTopicPartitions(destination.getTopic());
        }
    }

    public ConsumerRecords<K, V> poll(Collection<TopicPartition> partitions,
                                       int count, long timeout) {
        throw new UnsupportedOperationException("Not supported Specify result number.");
    }

}
