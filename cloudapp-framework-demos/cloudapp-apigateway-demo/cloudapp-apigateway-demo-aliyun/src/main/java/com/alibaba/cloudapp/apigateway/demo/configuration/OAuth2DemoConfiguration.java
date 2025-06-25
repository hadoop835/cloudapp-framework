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

package com.alibaba.cloudapp.apigateway.demo.configuration;

import com.alibaba.cloudapp.api.gateway.authentication.OAuth2Authorizer;
import com.alibaba.cloudapp.apigateway.aliyun.OAuth2WebMvcConfig;
import com.alibaba.cloudapp.apigateway.aliyun.service.AuthorizedService;
import com.alibaba.cloudapp.oauth2.filter.OAuthCallbackFilter;
import com.alibaba.cloudapp.oauth2.filter.OAuthCheckLoginFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(OAuth2WebMvcConfig.class)
public class OAuth2DemoConfiguration {
    
    
    
    /**
     * grant_type: authorization_code | implicit
     */
    @Bean
    public OAuthCallbackFilter oAuthCallbackInterceptor(@Qualifier("authorized") AuthorizedService authorizedService) {
        OAuth2Authorizer authorizer = (OAuth2Authorizer) authorizedService.getAuthorizer();
        
        return new OAuthCallbackFilter(authorizer.getAuthorizationService(),
                                       authorizer.getTokenStore()
        );
    }
    
    /**
     * grant_type: authorization_code | implicit
     */
    @Bean
    public OAuthCheckLoginFilter oAuthCheckLoginFilter(AuthorizedService authorizedService) {
        OAuth2Authorizer authorizer = (OAuth2Authorizer) authorizedService.getAuthorizer();
        
        OAuthCheckLoginFilter filter = new OAuthCheckLoginFilter(
                authorizer.getTokenStore(),
                authorizer.getTokenVerifier(),
                authorizer.getAuthorizationService()
        );
        filter.addSkipUrls("/manager/**");
        filter.addSkipUrls("/proxy/**");
        return filter;
    }
    
}
