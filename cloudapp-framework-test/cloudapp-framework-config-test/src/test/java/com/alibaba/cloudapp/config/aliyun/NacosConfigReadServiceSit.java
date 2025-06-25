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
package com.alibaba.cloudapp.config.aliyun;

import com.alibaba.cloudapp.config.aliyun.NacosConfigReadService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Consumer;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NacosConfigReadServiceSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            NacosConfigReadServiceSit.class);
    
    @Autowired
    NacosConfigReadService nacos;
    
    @Test
    public void testGetDelegatingClient() {
        Assert.assertNotNull(nacos.getDelegatingClient());
    }
    
    @Test
    public void getConfig_configName() {
//        String configName = "config-aliyun-sit";
        String configName = "test";
        String config = nacos.getConfig(configName);
        logger.info("get config {} result is {}", configName, config);
    }
    
    @Test
    public void getAndListen_configName_Consumer() {
        String configName = "test";
        Consumer listener =
                (Consumer<String>) content -> logger.info(
                        "get config {} result is {}", configName, content
                );
        String listen = nacos.getAndListen(configName, listener);
        logger.info("get and listen config {} result is {}", configName,
                    listen
        );
    }
    
    @Test
    public void getConfig_configName_Class() {
        String configName = "test_config_name";
        Class cls = String.class;
        Object config = nacos.getConfig(configName, cls);
        logger.info("get config {} result is {}", configName, config);
    }
    
}