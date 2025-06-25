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

package com.alibaba.cloudapp.messaging.kafka.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KafkaDestinationTest {
    
    private KafkaDestination destination;
    
    @Before
    public void setUp() {
        destination = new KafkaDestination("topic", 0);
    }
    
    @Test
    public void testConstructor() {
        KafkaDestination d = new KafkaDestination();
        d.setTopic("topic");
        d.setPartition(0);
        
        KafkaDestination d2 = new KafkaDestination("topic");
        d2.setPartition(0);
        
        assertEquals(d.getTopic(), d2.getTopic());
        assertEquals(d.getPartition(), d2.getPartition());
    }
    
    @Test
    public void testTopicGetterAndSetter() {
        final String topic = "topic";
        destination.setTopic(topic);
        assertEquals(topic, destination.getTopic());
    }
    
    @Test
    public void testPartitionGetterAndSetter() {
        final int partition = 0;
        destination.setPartition(partition);
        assertEquals(partition, destination.getPartition());
    }
    
}
