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

package io.cloudapp.messaging.kafka;

import io.cloudapp.api.messaging.Producer;
import io.cloudapp.api.messaging.model.Destination;
import io.cloudapp.api.messaging.model.MQMessage;
import io.cloudapp.messaging.kafka.model.KafkaDestination;
import io.cloudapp.messaging.kafka.properties.KafkaProducerProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class CloudAppKafkaProducer<K, V> extends KafkaProducer<K, V>
        implements Producer<KafkaProducer<K, V>, ProducerRecord<K, V>> {

    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppKafkaProducer.class);

    public CloudAppKafkaProducer(Map<String, Object> rawConfigs,
                                 Serializer<K> keySerializerSupplier,
                                 Serializer<V> valueSerializer) {
        super(rawConfigs, keySerializerSupplier, valueSerializer);
    }

    public CloudAppKafkaProducer(KafkaProducerProperties properties) {
        super(properties.buildProducerProperties());
    }


    @Override
    public KafkaProducer<K, V> getDelegatingProducer() {
        return this;
    }

    @Override
    public void send(MQMessage<? extends ProducerRecord<K, V>> message) {
        send(message.getMessageBody());
    }

    @Override
    public void send(Destination destination,
                     ProducerRecord<K, V> typeProducerRecord) {
        ProducerRecord<K, V> record = new ProducerRecord<>(destination.getTopic(),
                typeProducerRecord.partition(),
                typeProducerRecord.key(),
                typeProducerRecord.value());

        send(record);
    }

    @Override
    public void send(String topic, String message) {
        ProducerRecord record = new ProducerRecord<>(topic, message);
        send(record);
    }

    @Override
    public void send(Destination destination, String message) {
        ProducerRecord record;
        if(destination instanceof KafkaDestination) {

            KafkaDestination kd = (KafkaDestination) destination;
            record = new ProducerRecord<>(
                    kd.getTopic(), kd.getPartition(), null, message);

        } else {
            record = new ProducerRecord(destination.getTopic(), message);
        }

        send(record);
    }

    @Override
    public CompletableFuture<?> sendAsync(
            MQMessage<? extends ProducerRecord<K, V>> message) {

        Future future = this.send(message.getMessageBody());

        return CompletableFuture.completedFuture(future);
    }

    @Override
    public CompletableFuture<?> sendAsync(Destination destination,
                                          ProducerRecord<K, V> typeProducerRecord) {
        ProducerRecord record;
        if(destination instanceof KafkaDestination) {

            KafkaDestination kd = (KafkaDestination) destination;
            record = new ProducerRecord<>(kd.getTopic(),
                    kd.getPartition(),
                    typeProducerRecord.timestamp(),
                    null,
                    typeProducerRecord.value());

        } else {
            record = new ProducerRecord<>(destination.getTopic(),
                    null,
                    typeProducerRecord.timestamp(),
                    typeProducerRecord.key(),
                    typeProducerRecord.value());
        }

        Future<Void> future = send(record);
        return CompletableFuture.completedFuture(future);
    }

    @Override
    public CompletableFuture<?> sendAsync(String topic, String message) {
        ProducerRecord record = new ProducerRecord<>(topic, message);

        Future<?> future = send(record);

        return CompletableFuture.completedFuture(future);
    }

    @Override
    public CompletableFuture<?> sendAsync(Destination destination,
                                          String message) {
        ProducerRecord record;
        if(destination instanceof KafkaDestination) {

            KafkaDestination kd = (KafkaDestination) destination;
            record = new ProducerRecord<>(kd.getTopic(),
                    kd.getPartition(),
                    null,
                    message);

        } else {
            record = new ProducerRecord<>(destination.getTopic(), message);
        }

        Future<?> future = send(record);
        return CompletableFuture.completedFuture(future);
    }

    @Override
    public void close() {
        super.close();
    }
}
