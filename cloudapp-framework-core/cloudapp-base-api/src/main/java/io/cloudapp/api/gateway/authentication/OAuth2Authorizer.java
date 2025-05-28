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

import io.cloudapp.api.gateway.model.OAuthToken;
import io.cloudapp.api.oauth2.AuthorizationService;
import io.cloudapp.api.oauth2.GrantType;
import io.cloudapp.api.oauth2.TokenStorageService;
import io.cloudapp.api.oauth2.verifier.TokenVerifier;
import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.exeption.CloudAppInvalidAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public class OAuth2Authorizer implements Authorizer {
    
    private static final Logger logger = LoggerFactory.getLogger(
            OAuth2Authorizer.class);
    /**
     * After the validity period reaches the set value, the token cannot be used to initiate a request
     */
    private static final int REQUEST_DEADLINE_SECONDS = 10 * 60;
    private static final ThreadLocal<String> UNIQUE_TOKEN_ID =
            new ThreadLocal<>();
    
    private final AuthorizationService authorizationService;
    private TokenStorageService tokenStore;
    private TokenVerifier tokenVerifier;
    
    public OAuth2Authorizer(AuthorizationService authorizationService,
                            TokenStorageService tokenStore,
                            TokenVerifier tokenVerifier) {
        
        this(authorizationService, tokenStore);
        this.tokenVerifier = tokenVerifier;
    }
    
    public OAuth2Authorizer(AuthorizationService authorizationService,
                            TokenStorageService tokenStore) {
        
        this.authorizationService = authorizationService;
        this.tokenStore = tokenStore;
    }
    
    @Override
    public void applyAuthorization(HttpRequest request) throws CloudAppException {
        
        OAuthToken authToken = getToken(getCurrentId());
        
        String token = authToken == null ? null :
                StringUtils.hasText(authToken.getIdToken()) ?
                        authToken.getIdToken() : authToken.getAccessToken();
        
        if (!StringUtils.hasText(token)) {
            if (logger.isDebugEnabled()) {
                logger.debug("token is invalid or expired!");
            }
            throw new CloudAppInvalidAccessException("token is invalid or expired!");
        }
        
        request.getHeaders().add(HttpHeaders.AUTHORIZATION, getAuthorization(authToken));
    }
    
    public OAuthToken getToken(String key) {
        GrantType grantType = authorizationService.getGrantType();
        OAuthToken oauthToken = tokenStore.getToken(key);
        
        boolean isValid = isValidToken(oauthToken);
        
        if (isValid) {
            return oauthToken;
        }
        
        if (oauthToken != null && StringUtils.hasText(oauthToken.getRefreshToken())) {
            
            if (logger.isDebugEnabled()) {
                logger.debug("start refresh token");
            }
            
            Map<String, String> params = Collections.singletonMap(
                    "refresh_token", oauthToken.getRefreshToken()
            );
            return authorizationService.getToken(GrantType.REFRESH_TOKEN, params);
        }
        
        if (GrantType.CLIENT_CREDENTIALS.equals(grantType)) {
            
            if (logger.isDebugEnabled()) {
                logger.debug("start get token");
            }
            
            Map<String, String> params = Collections.emptyMap();
            
            return authorizationService.getToken(grantType, params);
        }
        
        return null;
    }
    
    /**
     * check token is valid
     */
    public boolean isValidToken(OAuthToken token) {
        if (token == null) {
            return false;
        }
        
        if (tokenVerifier == null) {
            Instant expiresAt = token.getExpiresIn() > 0L ?
                    Instant.ofEpochSecond(token.getExpiresIn()) :
                    Instant.now().plusSeconds(REQUEST_DEADLINE_SECONDS);
            
            return StringUtils.hasText(token.getAccessToken())
                    && !token.getAccessToken().equals("null")
                    && !token.getAccessToken().equals("None")
                    && !token.getAccessToken().equals("nil")
                    && !token.getAccessToken().equals("undefined")
                    && expiresAt.isAfter(Instant.now());
        } else {
            String verifierToken = token.getIdToken() != null
                    ? token.getIdToken() : token.getAccessToken();
            return tokenVerifier.verify(verifierToken);
        }
    }
    
    
    /**
     * get authorization header value, accessToken with type
     */
    private String getAuthorization(OAuthToken token) {
        String tokenType = token.getTokenType();
        String accessToken = token.getIdToken() != null
                ? token.getIdToken() : token.getAccessToken();
        
        return StringUtils.hasText(tokenType) ?
                tokenType + " " + accessToken : accessToken;
    }
    
    public boolean isRedirectUrl(String requestURI) {
        return authorizationService.isRedirectUrl(requestURI);
    }
    
    public static void setCurrentId(String id) {
        UNIQUE_TOKEN_ID.set(id);
    }
    
    public static String getCurrentId() {
        return UNIQUE_TOKEN_ID.get();
    }
    
    public static void cleanCurrentId() {
        UNIQUE_TOKEN_ID.remove();
    }
    
    /**
     * save token
     */
    public void saveToken(OAuthToken oauthToken) {
        if (StringUtils.hasText(getCurrentId())) {
            tokenStore.saveToken(getCurrentId(), oauthToken);
        }
    }
    
    public TokenVerifier getTokenVerifier() {
        return tokenVerifier;
    }
    
    public void setTokenVerifier(TokenVerifier tokenVerifier) {
        this.tokenVerifier = tokenVerifier;
    }
    
    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }
    
    public TokenStorageService getTokenStore() {
        return tokenStore;
    }
    
    public void setTokenStore(TokenStorageService tokenStore) {
        this.tokenStore = tokenStore;
    }
    
}
