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
package io.cloudapp.starter.config.configuration;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import io.cloudapp.config.aliyun.NacosConfigReadService;
import io.cloudapp.starter.config.properties.NacosConfigReadServiceProperties;
import io.cloudapp.starter.properties.EnableModuleProperties;
import io.cloudapp.starter.refresh.RefreshableProxyFactory;
import io.cloudapp.starter.refresh.aspect.RefreshableBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(value = {NacosConfigManager.class})
@EnableModuleProperties(NacosConfigReadServiceProperties.class)
@ConditionalOnProperty(prefix = NacosConfigReadServiceProperties.PREFIX,
        value = "enabled",
        havingValue = "true")
public class NacosConfigReadServiceAutoConfiguration {
    public static final Logger logger = LoggerFactory.getLogger(
            NacosConfigReadServiceAutoConfiguration.class);
    
    @Bean("nacosConfigReadService")
    @ConditionalOnMissingBean
    @ConditionalOnBean(NacosConfigManager.class)
    @RefreshableBinding(NacosConfigReadServiceProperties.PREFIX)
    public NacosConfigReadService nacosConfigReadService(
            NacosConfigReadServiceProperties properties,
            NacosConfigManager nacosConfigManager) {
        
        ConfigService configService = nacosConfigManager.getConfigService();
        
        return RefreshableProxyFactory.create(
                prop -> this.createNacosConfigReadService(prop, configService),
                properties
        );
    }
    
    private NacosConfigReadService createNacosConfigReadService(
            NacosConfigReadServiceProperties properties,
            ConfigService configService
    ) {
        
        NacosConfigReadService nacosConfigReadService = new NacosConfigReadService(
                properties.getTimeout(),
                properties.getGroup(),
                configService
        );
        
        logger.info("Successfully create NacosConfigReadService {} " +
                            "using properties {}",
                    nacosConfigReadService,
                    properties
        );
        
        return nacosConfigReadService;
    }
    
}
