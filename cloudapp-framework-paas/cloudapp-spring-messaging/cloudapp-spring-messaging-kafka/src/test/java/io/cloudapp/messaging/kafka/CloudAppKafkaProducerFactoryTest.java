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

import org.apache.kafka.clients.producer.Producer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.ProducerPostProcessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class CloudAppKafkaProducerFactoryTest {

    @Mock
    private CloudAppKafkaProducerFactory<String, String> producerFactory;

    private Map<String, Object> producerProps;

    @Before
    public void setUp() {
        producerProps = new HashMap<>();
        producerProps.put("bootstrap.servers", "localhost:9092");
        producerProps.put("acks", "all");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerFactory =
                new CloudAppKafkaProducerFactory<String, String>(producerProps) {
            @Override
            public List<ProducerPostProcessor<String, String>> getPostProcessors() {
                return Collections.singletonList(producer -> producer);
            }
        };
    }

    @Test
    public void createRawProducer_WithoutPostProcessor_ShouldCreateProducer() {
        Producer<String, String> producer = producerFactory
                .createRawProducer(producerProps);
        
        assertNotNull(producer);
    }
    
    @Test
    public void refresh() {
        producerFactory.refresh(producerProps);

    }
}
