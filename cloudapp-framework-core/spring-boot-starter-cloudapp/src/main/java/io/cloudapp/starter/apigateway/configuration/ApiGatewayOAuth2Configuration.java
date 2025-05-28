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

import io.cloudapp.api.oauth2.AuthorizationService;
import io.cloudapp.api.oauth2.TokenStorageService;
import io.cloudapp.oauth2.service.AuthorizationServiceImpl;
import io.cloudapp.oauth2.service.DefaultTokenStorageService;
import io.cloudapp.starter.apigateway.properties.ApiGatewayProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "io.cloudapp.apigateway.aliyun.oAuth2",
        name = {"clientId", "clientSecret"}
)
@ConditionalOnClass(AuthorizationServiceImpl.class)
public class ApiGatewayOAuth2Configuration {
    @Bean("oauthTemplate")
    @ConditionalOnMissingBean(name = "oauthTemplate")
    public RestTemplate oauth2Template() {
        return new RestTemplate();
    }
    
    @Bean("authorizationService")
    @ConditionalOnMissingBean(name = "authorizationService")
    public AuthorizationService authorizationService(
            ApiGatewayProperties properties,
            @Qualifier("oauthTemplate") RestTemplate restTemplate
    ) {
        return new AuthorizationServiceImpl(properties.getOAuth2(), restTemplate);
    }
    
    @Bean("tokenStorageService")
    @ConditionalOnMissingBean(name = "tokenStorageService")
    public TokenStorageService tokenStorageService() {
        return new DefaultTokenStorageService();
    }
    
}
