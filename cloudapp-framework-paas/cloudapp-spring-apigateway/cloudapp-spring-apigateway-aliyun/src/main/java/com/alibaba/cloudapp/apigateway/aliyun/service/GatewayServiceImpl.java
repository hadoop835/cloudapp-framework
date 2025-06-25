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

package com.alibaba.cloudapp.apigateway.aliyun.service;

import com.alibaba.cloudapp.api.gateway.Authorized;
import com.alibaba.cloudapp.api.gateway.GatewayService;
import com.alibaba.cloudapp.api.gateway.authentication.Authorizer;
import com.alibaba.cloudapp.apigateway.aliyun.interceptor.AsyncAuthorizationInterceptor;
import com.alibaba.cloudapp.apigateway.aliyun.interceptor.AuthorizationInterceptor;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.AsyncClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class GatewayServiceImpl extends GatewayService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    private final RestTemplate template;
    private final AsyncRestTemplate asyncTemplate;
    private Authorized authorized;
    private Authorizer authorizer;

    public GatewayServiceImpl(Authorized authorized) {
        this(authorized, new RestTemplate(), new AsyncRestTemplate());
    }

    public GatewayServiceImpl(Authorizer authorizer) {
        this(authorizer, new RestTemplate(), new AsyncRestTemplate());
    }

    public GatewayServiceImpl(Authorizer authorizer, RestTemplate template,
                              AsyncRestTemplate asyncTemplate) {
        this.template = template;
        this.asyncTemplate = asyncTemplate;
        this.authorizer = authorizer;
    }

    public GatewayServiceImpl(Authorized authorized, RestTemplate template,
                              AsyncRestTemplate asyncTemplate) {
        this.template = template;
        this.asyncTemplate = asyncTemplate;
        this.authorized = authorized;
    }

    @Override
    public RestTemplate getRestTemplate() {
        return template;
    }

    @Override
    public AsyncRestTemplate getAsyncRestTemplate() {
        return asyncTemplate;
    }

    @Override
    public Authorizer getAuthorizer() throws CloudAppException {
        return authorized.getAuthorizer();
    }

    @Override
    public void afterPropertiesSet() {
        if(authorizer == null && authorized != null) {
            logger.warn("authorizer is null, use default authorizer");
            authorizer = this.authorized.getAuthorizer();
        }

        if (authorizer == null) {
            throw new CloudAppException("CloudApp.InvalidAuthorizer");
        }

        // add interceptor to rest template
        List<ClientHttpRequestInterceptor> interceptors = this.template.getInterceptors();
        interceptors = new ArrayList<>(interceptors);
        interceptors.add(new AuthorizationInterceptor(authorizer));
        this.template.setInterceptors(interceptors);

        // add interceptor to async rest template
        List<AsyncClientHttpRequestInterceptor> asyncInterceptors =
                this.asyncTemplate.getInterceptors();
        asyncInterceptors = new ArrayList<>(asyncInterceptors);
        asyncInterceptors.add(new AsyncAuthorizationInterceptor(authorizer));
        this.asyncTemplate.setInterceptors(asyncInterceptors);
    }
}
