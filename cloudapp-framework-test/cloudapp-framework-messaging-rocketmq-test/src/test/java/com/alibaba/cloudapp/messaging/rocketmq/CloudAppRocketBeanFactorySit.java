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

package com.alibaba.cloudapp.messaging.rocketmq;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CloudAppRocketBeanFactorySit {
    
    @Autowired
    @Qualifier("rocketProducer")
    CloudAppRocketProducer producer;
    
    @Autowired
    @Qualifier("rocketProducer2")
    CloudAppRocketProducer producer2;
    
    @Autowired
    @Qualifier("rocketConsumer")
    CloudAppRocketConsumer consumer;
    
    @Autowired
    RocketMQMessageConverter rocketMQMessageConverter;
    
    @Autowired
    RocketMQTemplate rocketMQTemplate;
    
    @Test
    public void initAutowired() {
        assertNotNull(producer);
        assertNotNull(producer2);
        assertNotNull(consumer);
        assertNotNull(rocketMQMessageConverter);
        assertNotNull(rocketMQTemplate);
    }
    
}
