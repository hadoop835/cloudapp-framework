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

package com.alibaba.cloudapp.oauth2.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.cloudapp.api.gateway.model.OAuthToken;
import com.alibaba.cloudapp.api.oauth2.AuthorizationService;
import com.alibaba.cloudapp.api.oauth2.GrantType;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.model.OAuth2Client;
import com.alibaba.cloudapp.oauth2.OAuthConstant;
import com.alibaba.cloudapp.oauth2.handler.ClientTokenRequestHandler;
import com.alibaba.cloudapp.oauth2.handler.OAuthCodeTokenRequestHandler;
import com.alibaba.cloudapp.oauth2.handler.PasswordTokenRequestHandler;
import com.alibaba.cloudapp.oauth2.handler.RefreshTokenRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorizationServiceImpl implements AuthorizationService {
    
    private static final Logger logger = LoggerFactory.getLogger(
            AuthorizationServiceImpl.class);
    
    private OAuth2Client client;
    
    private final RestTemplate restTemplate;
    
    public AuthorizationServiceImpl(OAuth2Client client,
                                    RestTemplate restTemplate) {
        this.client = client;
        this.restTemplate = restTemplate;
    }
    
    public AuthorizationServiceImpl(OAuth2Client client) {
        this(client, new RestTemplate());
    }
    
    /**
     * get authorization url
     *
     * @param state user parameter STATE value
     * @return a map with key "url" for value of authorization url and key
     * "code_verifier" for value of code_verifier when PKCE is enabled
     */
    @Override
    public Map<String, String> getCodeUrl(String state) {
        List<String> grantTypes = client.getGrantTypes();
        boolean isAuthCode = grantTypes.contains(
                GrantType.AUTHORIZATION_CODE.getGrantType());
        boolean isImplicit = grantTypes.contains(
                GrantType.IMPLICIT.getGrantType());
        
        if (!isAuthCode && !isImplicit) {
            return null;
        }
        
        Map<String, String> ret = new HashMap<>(4);
        
        state = StringUtils.hasText(state) ? state.trim() : "";
        
        StringBuilder uri =
                new StringBuilder(client.getAuthorizationUri().toString());
        
        String responseType = isAuthCode ? OAuthConstant.CODE
                : OAuthConstant.TOKEN;
        //clientId
        uri.append(OAuthConstant.QUERY_START).append(OAuthConstant.CLIENT_ID)
           .append(OAuthConstant.QUERY_EQUAL).append(client.getClientId())
           //redirectUri
           .append(OAuthConstant.QUERY_SPLIT).append(OAuthConstant.REDIRECT_URI)
           .append(OAuthConstant.QUERY_EQUAL).append(client.getRedirectUri())
           //scopes
           .append(OAuthConstant.QUERY_SPLIT).append(OAuthConstant.SCOPE)
           .append(OAuthConstant.QUERY_EQUAL).append(
                   String.join("+", client.getScopes()))
           //responseType
           .append(OAuthConstant.QUERY_SPLIT).append(
                   OAuthConstant.RESPONSE_TYPE)
           .append(OAuthConstant.QUERY_EQUAL).append(responseType)
           //state
           .append(OAuthConstant.QUERY_SPLIT).append(OAuthConstant.STATE)
           .append(OAuthConstant.QUERY_EQUAL).append(state);
        
        if (isImplicit) {
            uri.append(OAuthConstant.QUERY_SPLIT).append(OAuthConstant.CLIENT_SECRET).append(OAuthConstant.QUERY_EQUAL).append(client.getClientSecret());
        }
        
        if (client.isEnablePkce() && isAuthCode) {
            String codeVerifier = generateCodeVerifier();
            String codeChallenge = generateCodeChallenge(codeVerifier);
            
            ret.put(OAuthConstant.CODE_VERIFIER, codeVerifier);
            
            uri.append(OAuthConstant.QUERY_SPLIT).append(OAuthConstant.CODE_CHALLENGE).append(OAuthConstant.QUERY_EQUAL).append(codeChallenge).append("&code_challenge_method=S256");
        }
        
        ret.put(OAuthConstant.AUTHORIZATION_URI, uri.toString());
        
        return ret;
    }
    
    /**
     * get token by code
     */
    @Override
    public OAuthToken getToken(GrantType grantType, Map<String, String> params)
            throws CloudAppException {
        if (grantType == null) {
            return null;
        }
        switch (grantType) {
            case AUTHORIZATION_CODE:
                return requestToken(new OAuthCodeTokenRequestHandler(client.getClientId(), client.getClientSecret(), client.getTokenUri(), client.getRedirectUri(), client.isEnablePkce(), params.get(OAuthConstant.CODE), params.get(OAuthConstant.CODE_VERIFIER)).create());
            case CLIENT_CREDENTIALS:
                return requestToken(new ClientTokenRequestHandler(client.getClientId(), client.getClientSecret(), client.getTokenUri()).create());
            case PASSWORD:
                return requestToken(new PasswordTokenRequestHandler(client.getClientId(), client.getClientSecret(), client.getTokenUri(), params.get(OAuthConstant.USERNAME), params.get(OAuthConstant.PASSWORD)).create());
            case IMPLICIT:
                throw new CloudAppException("Implicit grant type is not supported");
            case REFRESH_TOKEN:
                return requestToken(new RefreshTokenRequestHandler(client.getClientId(), client.getClientSecret(), client.getTokenUri(), params.get(OAuthConstant.REFRESH_TOKEN)).create());
            default:
                return null;
        }
    }
    
    @Override
    public void setOAuth2Client(OAuth2Client client) {
        this.client = client;
    }
    
    /**
     * request authorization center and get tokens
     */
    private OAuthToken requestToken(RequestEntity<String> request)
            throws CloudAppException {
        ResponseEntity<String> response = restTemplate
                .exchange(request, String.class);
        
        int code = response.getStatusCodeValue();
        if (code > 300) {
            throw new CloudAppException(
                    "Response code returned from server is " + code,
                    "CloudApp.InvalidCode"
            );
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("get token response: {}", response.getBody());
        }
        
        return JSON.parseObject(response.getBody(), OAuthToken.class);
    }
    
    
    //generate code_verifier
    public static String generateCodeVerifier() {
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(
                randomBytes);
    }
    
    // calculate code_challenge
    public static String generateCodeChallenge(String codeVerifier) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No SHA-256 algorithm available", e);
        }
        
        byte[] hashed = digest.digest(
                codeVerifier.getBytes(StandardCharsets.US_ASCII));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hashed);
    }
    
    /**
     * get authorization header value, accessToken with type
     */
    private String getAuthorization(OAuthToken token) {
        String tokenType = token.getTokenType();
        String accessToken = token.getAccessToken();
        
        return StringUtils.hasText(tokenType) ?
                tokenType + " " + accessToken : accessToken;
    }
    
    public OAuth2Client getOAuth2Client() {
        return client;
    }
    
    @Override
    public boolean isRedirectUrl(String requestURI) {
        URI uri = URI.create(client.getRedirectUri());
        return uri.getPath().equals(requestURI);
    }
    
    @Override
    public GrantType getGrantType() {
        if (client.getGrantTypes() == null) {
            return null;
        }
        String grantType = client.getGrantTypes().stream().filter(
                e -> !GrantType.REFRESH_TOKEN.getGrantType().equals(e)
        ).findFirst().orElse(null);
        
        return GrantType.getInstance(grantType);
    }
    
}
