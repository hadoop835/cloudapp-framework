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

import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CloudAppKafkaConsumerFactory<K, V> extends DefaultKafkaConsumerFactory<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(CloudAppKafkaConsumer.class);
    
    private static final List<CloudAppKafkaConsumer<?, ?>> CONSUMERS =
            Collections.synchronizedList(new ArrayList<>());

    public CloudAppKafkaConsumerFactory(Map<String, Object> configs) {
        super(configs);
    }

    protected Consumer<K, V> createRawConsumer(Map<String, Object> configProps) {
        CloudAppKafkaConsumer<K,V> consumer = new CloudAppKafkaConsumer<>(
                configProps, getKeyDeserializer(), getValueDeserializer());
        CONSUMERS.add(consumer);
        return consumer;
    }
    
    public void refresh(Map<String, Object> configs) {
        super.updateConfigs(configs);
        CONSUMERS.forEach(CloudAppKafkaConsumer::close);
        CONSUMERS.clear();
    }
}
