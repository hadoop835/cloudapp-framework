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
package io.cloudapp.oauth2.demo.configuration;

import io.cloudapp.api.oauth2.AuthorizationService;
import io.cloudapp.api.oauth2.TokenStorageService;
import io.cloudapp.api.oauth2.handler.LoginHandler;
import io.cloudapp.api.oauth2.verifier.TokenVerifier;
import io.cloudapp.oauth2.demo.OAuthLoginHandler;
import io.cloudapp.oauth2.filter.OAuthCallbackFilter;
import io.cloudapp.oauth2.filter.OAuthCheckLoginFilter;
import io.cloudapp.oauth2.service.DefaultTokenStorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoConfiguration {
    
    @Bean
    public TokenStorageService storageToken() {
        return new DefaultTokenStorageService();
    }
    
    /**
     * grant_type: authorization_code | implicit
     */
    @Bean
    public LoginHandler loginHandler() {
        return new OAuthLoginHandler();
    }
    
    /**
     * grant_type: authorization_code | implicit
     */
    @Bean
    public OAuthCallbackFilter oAuthCallbackInterceptor(
            AuthorizationService authorizationService,
            TokenStorageService storageToken,
            LoginHandler loginHandler
    ) {
        return new OAuthCallbackFilter(authorizationService,
                                       storageToken,
                                       loginHandler
        );
    }
    
    /**
     * grant_type: authorization_code | implicit
     */
    @Bean
    public OAuthCheckLoginFilter oAuthCheckLoginFilter(
            TokenStorageService storageToken,
            TokenVerifier tokenVerifier,
            AuthorizationService authorizationService) {
        return new OAuthCheckLoginFilter(storageToken, tokenVerifier,
                                         authorizationService
        );
    }
    
}
