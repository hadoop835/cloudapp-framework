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

import io.cloudapp.messaging.kafka.properties.KafkaProducerProperties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CloudAppKafkaProducerFactorySit {
    
    @Autowired
    @Qualifier("kafkaProducerConfig")
    KafkaProducerProperties producerProperties;
    
    @Autowired
    @Qualifier("kafkaProducerFactory")
    CloudAppKafkaProducerFactory producerFactory;
    
    @Autowired
    @Qualifier("kafkaProducerTemplate")
    KafkaTemplate producerTemplate;
    
    @Autowired
    @Qualifier("kafkaProducer")
    CloudAppKafkaProducer producer;
    
    
    @Test
    public void createRawProducer_ByFactory() {
        Map<String, Object> rawConfigs = producerProperties.getProperties();
        rawConfigs.put("key.serializer",
                       org.apache.kafka.common.serialization.StringSerializer.class
        );
        rawConfigs.put("value.serializer",
                       org.apache.kafka.common.serialization.StringSerializer.class
        );
        if (rawConfigs.size() != 0) {
            Producer rawProducer = producerFactory.createRawProducer(
                    rawConfigs);
            
            ProducerRecord<String, String> record = new ProducerRecord<>(
                    "test-topic", "test-message");
            rawProducer.send(record);
        }
    }
    
    @Test
    public void createProducer_ByProperties() {
        CloudAppKafkaProducer cloudAppKafkaProducer = new CloudAppKafkaProducer(
                producerProperties);
        cloudAppKafkaProducer.send("test-topic", "test-message");
    }
    
    @Test
    public void testKafkaTemplate() {
        producerTemplate.send("test-topic", "test-message");
    }
    
    @Test
    public void testProducer() {
        producer.send("test-topic", "test-message");
    }
    
}
