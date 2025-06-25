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

import com.aliyuncs.acm.model.v20200206.DeployConfigurationRequest;
import com.alibaba.cloudapp.api.config.ConfigObject;
import com.alibaba.cloudapp.config.aliyun.AliyunConfigManager;
import com.alibaba.cloudapp.util.RandomStringGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AliyunConfigManagerSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            AliyunConfigManagerSit.class);
    
    @Autowired
    AliyunConfigManager aliyunConfigManager;
    
    @Test
    public void testGetDelegatingClient() {
        Assert.assertNotNull(aliyunConfigManager.getDelegatingClient());
    }
    
    @Test
    public void publish_configName_content() {
        String configName =
                "test_config_name" + RandomStringGenerator.generate(3);
        String content = "test_content";
        boolean publishConfig = aliyunConfigManager.publish(configName, content);
        logger.info("publish config {} result is {}", configName,
                    publishConfig
        );
    }
    
    @Test
    public void publishConfigs_Configs() {
        Collection configs = new ArrayList<>();
        configs.add(ConfigObject.builder()
                                .configName("test_config_name" + RandomStringGenerator.generate(3))
                                .content("test_content")
                                .build());
        configs.add(ConfigObject.builder()
                                .configName("test_config_name" + RandomStringGenerator.generate(3))
                                .content("test: value")
                                .build());
        
        List<Boolean> publishConfigs = aliyunConfigManager.publishConfigs(configs);
        publishConfigs.stream().forEach(
                publishConfig -> logger.info("publish config result is {}",
                                             publishConfig
                ));
    }
    
    @Test
    public void publishConfig_ConfigObject() {
        ConfigObject object = ConfigObject.builder().configName(
                "test_config_name" + RandomStringGenerator.generate(3)).content(
                        "test_content").build();
        boolean publishConfig = aliyunConfigManager.publishConfig(object);
        logger.info("publish config result is {}", publishConfig);
    }
    
    @Test
    public void publishConfig_ConfigObject_configType() {
        DeployConfigurationRequest request = new DeployConfigurationRequest();
        request.setType("properties");
        request.setContent("test=key");
        
        ConfigObject object = ConfigObject.builder().configName(
                "test_config_name_specific_type" + RandomStringGenerator.generate(3)).content(request).build();
        boolean publishConfig = aliyunConfigManager.publishConfig(object);
        logger.info("publish config result is {}", publishConfig);
    }
    
    @Test
    public void deleteConfigs_ConfigObjects() {
        Collection configs = new ArrayList<>();
        configs.add(ConfigObject.builder()
                                .configName("test_config_name")
                                .content("test_content")
                                .build());
        configs.add(ConfigObject.builder()
                                .configName("test_config_name2")
                                .content("test_content2")
                                .build());
        List<Boolean> booleans = aliyunConfigManager.deleteConfigs(configs);
        booleans.stream().forEach(
                deleteConfig -> logger.info("delete config result is {}",
                                            deleteConfig
                ));
    }
    
    @Test
    public void deleteConfig_ConfigObject() {
        ConfigObject config = ConfigObject.builder().configName(
                "test_config_name").content("test_content").build();
        boolean deleteConfig = aliyunConfigManager.deleteConfig(config);
        logger.info("delete config result is {}", deleteConfig);
    }
    
    @Test
    public void deleteConfig_configName() {
        String configName = "test_config_name";
        boolean deleteConfig = aliyunConfigManager.deleteConfig(configName);
        logger.info("delete config {} result is {}", configName, deleteConfig);
    }
    
}
