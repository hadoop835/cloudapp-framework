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

package io.cloudapp.oauth2.handler;

import com.alibaba.fastjson2.JSON;
import io.cloudapp.api.oauth2.GrantType;
import io.cloudapp.oauth2.TokenRequestHandler;
import io.cloudapp.util.CloudAppStringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PasswordTokenRequestHandler implements TokenRequestHandler {
    
    private final String username;
    private final String password;
    private final String clientId;
    private final String clientSecret;
    private final URI tokenUri;
    
    public PasswordTokenRequestHandler(String clientId,
                                       String clientSecret,
                                       URI tokenUri,
                                       String username,
                                       String password) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUri = tokenUri;
        this.username = username;
        this.password = password;
    }
    
    @Override
    public RequestEntity<String> create() {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new IllegalArgumentException(
                    "username or password is required");
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("username", CloudAppStringUtils.trimSpaceChar(username));
        params.put("password", CloudAppStringUtils.trimSpaceChar(password));
        params.put("grant_type", CloudAppStringUtils.trimSpaceChar(
                GrantType.PASSWORD.getGrantType()));
        
        String token = CloudAppStringUtils.trimSpaceChar(clientId)
                + ":" + CloudAppStringUtils.trimSpaceChar(clientSecret);
        
        token = "Basic " + Base64.getEncoder().encodeToString(token.getBytes());
        
        String body = JSON.toJSONString(params);
        
        return RequestEntity.post(tokenUri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", token)
                            .body(body);
    }
    
}
