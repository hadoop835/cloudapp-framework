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

package com.alibaba.cloudapp.oauth2.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.cloudapp.api.oauth2.GrantType;
import com.alibaba.cloudapp.exeption.CloudAppInvalidRequestException;
import com.alibaba.cloudapp.oauth2.OAuthConstant;
import com.alibaba.cloudapp.oauth2.TokenRequestHandler;
import com.alibaba.cloudapp.util.CloudAppStringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class OAuthCodeTokenRequestHandler implements TokenRequestHandler {
    
    private final String code;
    private final String codeVerifier;
    private final String clientId;
    private final String clientSecret;
    private final URI tokenUri;
    private final String redirectUri;
    private final boolean enablePkce;
    
    public OAuthCodeTokenRequestHandler(String clientId,
                                        String clientSecret,
                                        URI tokenUri,
                                        String redirectUri,
                                        boolean enablePkce,
                                        String code,
                                        String codeVerifier) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUri = tokenUri;
        this.redirectUri = redirectUri;
        this.enablePkce = enablePkce;
        this.code = code;
        this.codeVerifier = codeVerifier;
    }
    
    @Override
    public RequestEntity<String> create() {
        if (enablePkce && !StringUtils.hasText(codeVerifier)) {
            throw new CloudAppInvalidRequestException("code_verifier is required");
        }
        
        if (!StringUtils.hasText(code)) {
            throw new CloudAppInvalidRequestException("invalid code");
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put(OAuthConstant.CLIENT_ID,
                   CloudAppStringUtils.trimSpaceChar(clientId));
        params.put(OAuthConstant.CLIENT_SECRET,
                   CloudAppStringUtils.trimSpaceChar(clientSecret));
        params.put(OAuthConstant.GRANT_TYPE, CloudAppStringUtils.trimSpaceChar(
                   GrantType.AUTHORIZATION_CODE.getGrantType()));
        params.put(OAuthConstant.REDIRECT_URI,
                   CloudAppStringUtils.trimSpaceChar(redirectUri));
        params.put(OAuthConstant.CODE, CloudAppStringUtils.trimSpaceChar(code));
        
        if (enablePkce) {
            params.put(OAuthConstant.CODE_VERIFIER, codeVerifier);
        }
        
        String body = JSON.toJSONString(params);
        
        return RequestEntity.post(tokenUri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(body);
    }
    
}
