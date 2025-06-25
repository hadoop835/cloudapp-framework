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

import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerPostProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CloudAppKafkaProducerFactory<K, V>
        extends DefaultKafkaProducerFactory<K, V> {
    
    private static final List<CloudAppKafkaProducer<?, ?>> PRODUCERS =
            Collections.synchronizedList(new ArrayList<>());

    public CloudAppKafkaProducerFactory(Map<String, Object> configs) {
        super(configs);
    }

    @Override
    public CloudAppKafkaProducer<K, V> createRawProducer(Map<String, Object> rawConfigs) {
        CloudAppKafkaProducer<K, V> kafkaProducer = new CloudAppKafkaProducer<>(
                rawConfigs,
                getKeySerializer(),
                getValueSerializer());

        for (ProducerPostProcessor<K, V> pp : getPostProcessors()) {
            kafkaProducer = (CloudAppKafkaProducer<K, V>) pp
                    .apply(kafkaProducer);
        }
        
        PRODUCERS.add(kafkaProducer);
        return kafkaProducer;
    }
    
    public void refresh(Map<String, Object> configs) {
        super.updateConfigs(configs);
        
        PRODUCERS.forEach(CloudAppKafkaProducer::close);
        PRODUCERS.clear();
        
        super.reset();
    }
}
