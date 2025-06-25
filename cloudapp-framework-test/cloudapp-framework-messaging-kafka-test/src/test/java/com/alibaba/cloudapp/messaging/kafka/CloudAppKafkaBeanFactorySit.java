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

import com.alibaba.cloudapp.messaging.kafka.properties.KafkaConsumerProperties;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaProducerProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CloudAppKafkaBeanFactorySit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppKafkaBeanFactorySit.class);
    
    @Autowired
    @Qualifier("kafkaConsumerConfig")
    KafkaConsumerProperties consumerProperties;
    
    @Autowired
    @Qualifier("kafkaConsumerFactory")
    CloudAppKafkaConsumerFactory consumerFactory;
    
    
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
    
    @Autowired
    @Qualifier("kafkaProducer2Config")
    KafkaProducerProperties producerProperties2;
    
    @Autowired
    @Qualifier("kafkaProducer2Factory")
    CloudAppKafkaProducerFactory producerFactory2;
    
    @Autowired
    @Qualifier("kafkaProducer2Template")
    KafkaTemplate producerTemplate2;
    
    @Autowired
    @Qualifier("kafkaProducer2")
    CloudAppKafkaProducer producer2;
    
    @Test
    public void initAutowired() {
        assertNotNull(consumerProperties);
        assertNotNull(consumerFactory);
        assertNotNull(producerProperties);
        assertNotNull(producerFactory);
        assertNotNull(producer);
        assertNotNull(producerTemplate);
        assertNotNull(producerProperties2);
        assertNotNull(producerFactory2);
        assertNotNull(producer2);
        assertNotNull(producerTemplate2);
    }
    
}


