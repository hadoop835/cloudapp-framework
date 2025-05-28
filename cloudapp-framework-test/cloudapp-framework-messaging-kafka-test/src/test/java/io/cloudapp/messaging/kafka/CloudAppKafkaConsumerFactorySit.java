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

import io.cloudapp.messaging.kafka.properties.KafkaConsumerProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CloudAppKafkaConsumerFactorySit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppKafkaConsumerFactorySit.class);
    
    @Autowired
    @Qualifier("kafkaConsumerConfig")
    KafkaConsumerProperties consumerProperties;
    
    @Autowired
    @Qualifier("kafkaConsumerFactory")
    CloudAppKafkaConsumerFactory consumerFactory;
    
    @Test
    public void createRawConsumer() {
        Map<String, Object> configProps = consumerProperties.getProperties();
        configProps.put("key.deserializer",
                        org.apache.kafka.common.serialization.StringDeserializer.class
        );
        configProps.put("value.deserializer",
                        org.apache.kafka.common.serialization.StringDeserializer.class
        );
        Consumer rawConsumer = consumerFactory.createRawConsumer(configProps);
        logger.info("rawConsumer: {}", rawConsumer);
    }
    
}
