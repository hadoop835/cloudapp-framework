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

package com.alibaba.cloudapp.starter.oauth2.configuration;

import com.alibaba.cloudapp.api.oauth2.AuthorizationService;
import com.alibaba.cloudapp.api.oauth2.TokenStorageService;
import com.alibaba.cloudapp.oauth2.service.AuthorizationServiceImpl;
import com.alibaba.cloudapp.oauth2.service.DefaultTokenStorageService;
import com.alibaba.cloudapp.oauth2.verifier.IntrospectionTokenVerifier;
import com.alibaba.cloudapp.oauth2.verifier.JwtTokenVerifier;
import com.alibaba.cloudapp.starter.oauth2.properties.OAuth2ClientProperties;
import com.alibaba.cloudapp.starter.properties.EnableModuleProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(AuthorizationServiceImpl.class)
@EnableModuleProperties(OAuth2ClientProperties.class)
@ConditionalOnProperty(
        prefix = OAuth2Component.BINDING_PROP_KEY,
        value = "enabled",
        havingValue = "true")
public class OAuth2AutoConfiguration {
    
    @Bean("oauth2Component")
    @ConditionalOnMissingBean
    public OAuth2Component oauth2Component(OAuth2ClientProperties properties) {
        return new OAuth2Component(properties);
    }
    
    @Bean("authorizationService")
    @ConditionalOnMissingBean
    public AuthorizationService authorizationService(OAuth2Component component) {
        return component.getBean();
    }
    
    @Bean("storageToken")
    @ConditionalOnMissingBean
    public TokenStorageService storageToken() {
        return new DefaultTokenStorageService();
    }
    
    @Bean("jwkTokenVerifier")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = OAuth2Component.BINDING_PROP_KEY, name = "jwks-url")
    public JwtTokenVerifier jwksTokenVerifier(OAuth2ClientProperties properties) {
        return new JwtTokenVerifier(properties.getJwksUrl());
    }
    
    @Bean("jwkTokenVerifier")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = OAuth2Component.BINDING_PROP_KEY, name =
            "introspection-url")
    public IntrospectionTokenVerifier introspectionTokenVerifier(OAuth2ClientProperties properties) {
        return new IntrospectionTokenVerifier(properties.getIntrospectionUri());
    }
}
