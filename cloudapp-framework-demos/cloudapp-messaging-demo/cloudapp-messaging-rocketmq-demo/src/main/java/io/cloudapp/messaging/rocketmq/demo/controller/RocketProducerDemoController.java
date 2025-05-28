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

package io.cloudapp.messaging.rocketmq.demo.controller;

import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.messaging.rocketmq.CloudAppRocketProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class RocketProducerDemoController {

//    static ThreadLocal<Producer> threadLocal = new ThreadLocal<>();

    private static final Logger logger = LoggerFactory.getLogger(RocketProducerDemoController.class);

    @Autowired
    @Qualifier("rocketProducer")
    CloudAppRocketProducer rocketProducer;

    RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/testRocketProducer")
    public void testRocketProducer() {
        try {
            rocketProducer.send("test-topic", "hello world!");
            logger.info("send message success!");
        } catch (CloudAppException e) {
            logger.error("send message failed!", e);
        }
    }
    
    @RequestMapping("/testAsync")
    public void testAsync() {
        try {
            CompletableFuture<SendResult> future = rocketProducer.sendAsync(
                    "test-topic", "hello world!");
            
            future.whenComplete((result, throwable) -> {
                if(throwable != null) {
                    logger.error("send message failed!", throwable);
                    return;
                }
                logger.info("send message success!");
            });
        } catch (CloudAppException e) {
            logger.error("send message failed!", e);
        }
    }


}
