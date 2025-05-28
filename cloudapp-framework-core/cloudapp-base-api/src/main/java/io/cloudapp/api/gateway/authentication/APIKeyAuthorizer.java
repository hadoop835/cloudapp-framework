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
package io.cloudapp.api.gateway.authentication;

import io.cloudapp.exeption.CloudAppException;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;

public class APIKeyAuthorizer implements Authorizer {

    private static final String API_KEY_HEADER = "X-API-KEY";

    private final String apiKey;
    private final String apiKeyHeader;

    public APIKeyAuthorizer(String apiKey) {
        this(apiKey, API_KEY_HEADER);
    }

    public APIKeyAuthorizer(String apiKey, String apiKeyHeader) {
        this.apiKey = apiKey;
        if(StringUtils.hasText(apiKeyHeader)) {
            this.apiKeyHeader = apiKeyHeader;
        } else {
            this.apiKeyHeader = API_KEY_HEADER;
        }
    }

    @Override
    public void applyAuthorization(HttpRequest request) throws CloudAppException {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new CloudAppException("API key is not set", "CloudApp.ApiKeyNotSet");
        }

        request.getHeaders().add(apiKeyHeader, apiKey);
    }

    public String getHeaderName() {
        return apiKeyHeader;
    }
}

