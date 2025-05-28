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

package io.cloudapp.starter.apigateway.configuration;

import io.cloudapp.api.gateway.Authorized;
import io.cloudapp.api.gateway.GatewayService;
import io.cloudapp.apigateway.aliyun.service.ApiGatewayManager;
import io.cloudapp.apigateway.aliyun.service.AuthorizedService;
import io.cloudapp.apigateway.aliyun.service.GatewayServiceImpl;
import io.cloudapp.starter.apigateway.properties.ApiGatewayManagerProperties;
import io.cloudapp.starter.apigateway.properties.ApiGatewayProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties({ApiGatewayProperties.class,
        ApiGatewayManagerProperties.class})
@ConditionalOnProperty(prefix = "io.cloudapp.apigateway.aliyun",
        value = "enabled",
        havingValue = "true")
@Import(ApiGatewayOAuth2Configuration.class)
public class ApiGatewayConfiguration {
    
    // manager config
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "io.cloudapp.apigateway.aliyun.server",
            name = "accessKey"
    )
    public ApiGatewayManagerRefreshComponent apiGatewayManagerRefreshComponent(
            ApiGatewayManagerProperties properties
    ) {
        return new ApiGatewayManagerRefreshComponent(properties);
    }
    
    @Bean("apiGatewayService")
    @ConditionalOnMissingBean
    @ConditionalOnBean(ApiGatewayManagerRefreshComponent.class)
    public ApiGatewayManager apiGatewayService(
            ApiGatewayManagerRefreshComponent managerRefreshComponent
    ) {
        return managerRefreshComponent.getBean();
    }
    
    // config authorizer
    @Bean
    @ConditionalOnMissingBean
    public AuthorizedRefreshComponent authorizedRefreshComponent(
            ApiGatewayProperties properties
    ) {
        return new AuthorizedRefreshComponent(properties);
    }
    
    @Bean("authorized")
    @ConditionalOnMissingBean
    public AuthorizedService authorized(
            AuthorizedRefreshComponent authorizedRefreshComponent
    ) {
        return authorizedRefreshComponent.getBean();
    }

    @Bean("gatewayService")
    @ConditionalOnMissingBean
    public GatewayService gatewayService(Authorized authorized) {
        return new GatewayServiceImpl(authorized);
    }
    
}
