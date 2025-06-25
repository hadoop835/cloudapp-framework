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

package com.alibaba.cloudapp.messaging.kafka.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KafkaProducerPropertiesTest {
    
    private KafkaProducerProperties properties;
    
    @BeforeEach
    public void setUp() {
        properties = new KafkaProducerProperties();
        properties.setBootstrapServers("bootstrapServers");
        properties.setName("name");
        properties.setTopic("topic");
        properties.setPartition(0);
        properties.setKeySerializer(String.class);
        properties.setValueSerializer(String.class);
        properties.setReconnectBackoff(0);
        properties.setGroup("group");
        properties.setProperties(new HashMap<>());
        properties.setReconnectBackoff(3000);
        properties.setGroup("group");
    }
    
    @Test
    public void testGetName() {
        assertEquals("name", properties.getName());
    }
    
    @Test
    public void testSetName() {
        // Setup
        // Run the test
        properties.setName("name");
        
        // Verify the results
    }
    
    @Test
    public void testGetTopic() {
        assertEquals("topic", properties.getTopic());
    }
    
    @Test
    public void testSetTopic() {
        // Setup
        // Run the test
        properties.setTopic("topic");
        
        // Verify the results
    }
    
    @Test
    public void testPartitionGetterAndSetter() {
        final Integer partition = 0;
        properties.setPartition(partition);
        assertEquals(partition,
                     properties.getPartition()
        );
    }
    
    @Test
    public void testKeySerializerGetterAndSetter() {
        final Class<?> keySerializer = String.class;
        properties.setKeySerializer(keySerializer);
        assertEquals(keySerializer,
                     properties.getKeySerializer()
        );
    }
    
    @Test
    public void testValueSerializerGetterAndSetter() {
        final Class<?> valueSerializer = String.class;
        properties.setValueSerializer(valueSerializer);
        assertEquals(valueSerializer,
                     properties.getValueSerializer()
        );
    }
    
    @Test
    public void testGetProperties() {
        final Map<String, Object> result = properties.getProperties();
    }
    
    @Test
    public void testSetProperties() {
        // Setup
        final Map<String, Object> properties = new HashMap<>();
        
        // Run the test
        this.properties.setProperties(properties);
        
        // Verify the results
    }
    
    @Test
    public void testGetBootstrapServers() {
        assertEquals("bootstrapServers",
                     properties.getBootstrapServers()
        );
    }
    
    @Test
    public void testSetBootstrapServers() {
        // Setup
        // Run the test
        properties.setBootstrapServers(
                "bootstrapServers");
        
        // Verify the results
    }
    
    @Test
    public void testReconnectBackoffGetterAndSetter() {
        final int reconnectBackoff = 0;
        properties.setReconnectBackoff(reconnectBackoff);
        assertEquals(reconnectBackoff,
                     properties.getReconnectBackoff()
        );
    }
    
    @Test
    public void testGroupGetterAndSetter() {
        final String group = "group";
        properties.setGroup(group);
        assertEquals(group, properties.getGroup());
    }
    
    @Test
    public void testBuildProducerProperties() {
        // Setup
        // Run the test
        final Map<String, Object> result = properties.buildProducerProperties();
        
        // Verify the results
    }
    
}
