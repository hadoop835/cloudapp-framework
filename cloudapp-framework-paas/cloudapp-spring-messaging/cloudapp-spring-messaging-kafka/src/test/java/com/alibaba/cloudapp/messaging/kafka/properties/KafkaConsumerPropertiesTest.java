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

import org.apache.kafka.common.IsolationLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KafkaConsumerPropertiesTest {
    
    private KafkaConsumerProperties properties;
    
    @BeforeEach
    public void setUp() {
        properties = new KafkaConsumerProperties();
        properties.setAutoOffsetReset("autoOffsetReset");
        properties.setBootstrapServers("bootstrapServers");
        properties.setGroup("group");
        properties.setIsolationLevel(IsolationLevel.READ_COMMITTED);
        properties.setKeyDeserializer(String.class);
        properties.setListenerBean("listenerBean");
        properties.setMaxFetchBytes(0);
        properties.setName("name");
        properties.setProperties(new HashMap<>());
        properties.setSessionTimeout(0);
        properties.setTopic("topic");
        properties.setType("type");
        properties.setValueDeserializer(String.class);
        properties.setSessionTimeout(0);
    }
    
    @Test
    public void testGetListenerBean() {
        assertEquals("listenerBean", properties.getListenerBean()
        );
    }
    
    @Test
    public void testSetListenerBean() {
        // Setup
        // Run the test
        properties.setListenerBean("listenerBean");
        
        // Verify the results
    }
    
    @Test
    public void testGetGroup() {
        assertEquals("group", properties.getGroup());
    }
    
    @Test
    public void testSetGroup() {
        // Setup
        // Run the test
        properties.setGroup("group");
        
        // Verify the results
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
    public void testGetAutoOffsetReset() {
        assertEquals("autoOffsetReset",
                     properties.getAutoOffsetReset()
        );
    }
    
    @Test
    public void testSetAutoOffsetReset() {
        // Setup
        // Run the test
        properties.setAutoOffsetReset("autoOffsetReset");
        
        // Verify the results
    }
    
    @Test
    public void testKeyDeserializerGetterAndSetter() {
        final Class<?> keyDeserializer = String.class;
        properties.setKeyDeserializer(keyDeserializer);
        assertEquals(keyDeserializer,
                     properties.getKeyDeserializer()
        );
    }
    
    @Test
    public void testValueDeserializerGetterAndSetter() {
        final Class<?> valueDeserializer = String.class;
        properties.setValueDeserializer(
                valueDeserializer);
        assertEquals(valueDeserializer,
                     properties.getValueDeserializer()
        );
    }
    
    @Test
    public void testIsolationLevelGetterAndSetter() {
        final IsolationLevel isolationLevel = IsolationLevel.READ_UNCOMMITTED;
        properties.setIsolationLevel(isolationLevel);
        assertEquals(isolationLevel,
                     properties.getIsolationLevel()
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
    public void testGetType() {
        assertEquals("type", properties.getType());
    }
    
    @Test
    public void testSetType() {
        // Setup
        // Run the test
        properties.setType("type");
        
        // Verify the results
    }
    
    @Test
    public void testMaxFetchBytesGetterAndSetter() {
        final int maxFetchBytes = 0;
        properties.setMaxFetchBytes(maxFetchBytes);
        assertEquals(maxFetchBytes,
                     properties.getMaxFetchBytes()
        );
    }
    
    @Test
    public void testSessionTimeoutGetterAndSetter() {
        final int sessionTimeout = 0;
        properties.setSessionTimeout(sessionTimeout);
        assertEquals(sessionTimeout,
                     properties.getSessionTimeout()
        );
    }
    
    @Test
    public void testBuildConsumerProperties() {
        // Setup
        // Run the test
        final Map<String, Object> result = properties.buildConsumerProperties();
        
        // Verify the results
    }
    
}
