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

package com.alibaba.cloudapp.oauth2.verifier;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.cloudapp.api.oauth2.verifier.TokenVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Use OAuth server introspection endpoint to verify token
 */
public class IntrospectionTokenVerifier implements TokenVerifier {
    
    private static final Logger logger = LoggerFactory.getLogger(
            IntrospectionTokenVerifier.class);
    
    private final URI introspectionUri;
    private final RestTemplate restTemplate;
    
    public IntrospectionTokenVerifier(URI introspectionUri, RestTemplate restTemplate) {
        this.introspectionUri = introspectionUri;
        this.restTemplate = restTemplate;
    }
    
    public IntrospectionTokenVerifier(URI introspectionUri) {
        this(introspectionUri, new RestTemplate());
    }
    
    @Override
    public boolean verify(String token) {
        RequestEntity<String> request = RequestEntity.post(introspectionUri)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body("");
        
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        
        if (logger.isDebugEnabled()) {
            logger.debug("verify token response: {}", response);
        }
        
        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject object = JSONObject.parseObject(response.getBody());
            
            return object != null && object.getBoolean("active");
        }
        return false;
    }
    
}
