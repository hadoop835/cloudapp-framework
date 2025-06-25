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

package com.alibaba.cloudapp.starter.config.configuration;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.acm.model.v20200206.DeployConfigurationRequest;
import com.aliyuncs.profile.DefaultProfile;
import com.alibaba.cloudapp.config.aliyun.AliyunConfigManager;
import com.alibaba.cloudapp.starter.config.properties.AliyunConfigManagerProperties;
import com.alibaba.cloudapp.starter.properties.EnableModuleProperties;
import com.alibaba.cloudapp.starter.refresh.RefreshableProxyFactory;
import com.alibaba.cloudapp.starter.refresh.aspect.RefreshableBinding;
import com.alibaba.cloudapp.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(value = {DefaultAcsClient.class,
        DeployConfigurationRequest.class})
@EnableModuleProperties(AliyunConfigManagerProperties.class)
@ConditionalOnProperty(prefix =
        "io.cloudapp.config.aliyun.write",
        value = "enabled",
        havingValue = "true")
public class AliyunConfigManagerAutoConfiguration {
    public static final Logger logger = LoggerFactory.getLogger(AliyunConfigManagerAutoConfiguration.class);

    @Bean("aliyunConfigManager")
    @ConditionalOnMissingBean
    @RefreshableBinding(AliyunConfigManagerProperties.PREFIX)
    public AliyunConfigManager aliyunConfigManager(AliyunConfigManagerProperties properties) {
        
        return RefreshableProxyFactory.create(
                this::createConfigManager, properties
        );
        
    }
    
    private AliyunConfigManager createConfigManager(
            AliyunConfigManagerProperties properties
    ) {
        properties.validate();
        
        DefaultProfile.addEndpoint(properties.getRegionId(), "acm",
                                   properties.getDomain());
        
        DefaultProfile defaultProfile = DefaultProfile.getProfile(
                properties.getRegionId(),
                properties.getAccessKey(),
                properties.getSecretKey());
        
        DefaultAcsClient defaultAcsClient = new DefaultAcsClient(defaultProfile);
        AliyunConfigManager aliyunConfigManager = new AliyunConfigManager(
                defaultAcsClient,
                properties.getNamespaceId(),
                properties.getGroup(),
                properties.getTimeout(),
                properties.getProtocol());
        
        logger.info("Successfully create AliyunConfigManager {} using properties {}",
                    aliyunConfigManager,
                    SecurityUtil.safeMask(properties.toProperties()));
        
        return aliyunConfigManager;
    }
    
}
