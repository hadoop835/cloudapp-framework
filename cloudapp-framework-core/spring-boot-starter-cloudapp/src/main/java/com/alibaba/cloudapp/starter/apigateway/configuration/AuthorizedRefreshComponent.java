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

package com.alibaba.cloudapp.starter.apigateway.configuration;

import com.alibaba.cloudapp.api.gateway.authentication.OAuth2Authorizer;
import com.alibaba.cloudapp.apigateway.aliyun.AuthTypeEnum;
import com.alibaba.cloudapp.apigateway.aliyun.service.AuthorizedService;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.starter.apigateway.properties.ApiGatewayProperties;
import com.alibaba.cloudapp.starter.base.RefreshableComponent;

public class AuthorizedRefreshComponent extends
        RefreshableComponent<ApiGatewayProperties, AuthorizedService> {
    
    public static final String BINDING_KEY = "io.cloudapp.apigateway.aliyun";
    
    public AuthorizedRefreshComponent(ApiGatewayProperties properties) {
        super(properties);
    }
    
    @Override
    public void postStart() {
        bean.setProperties(properties);
        
        // init oauth2 service
        if (bean.getProperties().getAuthType() == AuthTypeEnum.OAUTH2
                && bean.getAuthorizer() != null
                && !(bean.getAuthorizer() instanceof OAuth2Authorizer)) {
            throw new CloudAppException("Authorized cannot be refreshed, " +
                                                "Unsupported change authType!");
        }
        
        bean.initAuthorizer();
    }
    
    @Override
    public void preStop() {
    
    }
    
    @Override
    public String bindKey() {
        return BINDING_KEY;
    }
    
    @Override
    public String getName() {
        return "apiGateway.authorized";
    }
    
    @Override
    protected AuthorizedService createBean(ApiGatewayProperties properties) {
        return new AuthorizedService(properties);
    }
    
}
