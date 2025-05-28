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

package io.cloudapp.apigateway.aliyun.interceptor;

import io.cloudapp.api.gateway.authentication.APIKeyAuthorizer;
import io.cloudapp.api.gateway.authentication.Authorizer;
import io.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class AuthorizationInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(
            AuthorizationInterceptor.class);

    private final Authorizer authorizer;

    public AuthorizationInterceptor(Authorizer authorizer) {
        this.authorizer = authorizer;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        // apply authorization
        try {
            String headerName = HttpHeaders.AUTHORIZATION;
            if(authorizer instanceof APIKeyAuthorizer) {
                headerName = ((APIKeyAuthorizer) authorizer).getHeaderName();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("The authorizer is not APIKeyAuthorizer");
                }
            }

            if (!request.getHeaders().containsKey(headerName)) {
                authorizer.applyAuthorization(request);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("The request already contains the header: {}", headerName);
                }
            }
        } catch (CloudAppException e) {
            throw new RuntimeException("Failed to apply authorization", e);
        }

        return execution.execute(request, body);
    }
}
