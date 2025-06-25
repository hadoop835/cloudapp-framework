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

package com.alibaba.cloudapp.api.gateway.authentication;

import com.alibaba.cloudapp.api.gateway.model.OAuthToken;
import com.alibaba.cloudapp.api.oauth2.AuthorizationService;
import com.alibaba.cloudapp.api.oauth2.GrantType;
import com.alibaba.cloudapp.api.oauth2.TokenStorageService;
import com.alibaba.cloudapp.api.oauth2.verifier.TokenVerifier;
import com.alibaba.cloudapp.exeption.CloudAppInvalidAccessException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.web.client.RestClientException;

import java.time.Instant;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OAuth2AuthorizerTest {
    
    @Mock
    private TokenStorageService tokenStore;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private TokenVerifier tokenVerifier;
    
    private OAuth2Authorizer auth2Authorizer;
    private OAuthToken token;
    private OAuthToken returnToken;
    
    @Before
    public void setUp() throws Exception {
        
        token = new OAuthToken();
        token.setExpiresIn(Instant.now().plusSeconds(100L)
                                  .getEpochSecond());
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setIdToken("idToken");
        token.setTokenType("tokenType");
        token.setScope("scope");
        
        returnToken = new OAuthToken();
        returnToken.setAccessToken("accessToken");
        returnToken.setRefreshToken("refreshToken");
        
        when(tokenStore.getToken(any(String.class))).thenReturn(
                mock(OAuthToken.class));
        
        when(authorizationService.getGrantType())
                .thenReturn(GrantType.AUTHORIZATION_CODE);
        
        auth2Authorizer = new OAuth2Authorizer(
                authorizationService, tokenStore, tokenVerifier
        );
        
    }
    
    @Test
    public void testApplyAuthorization() {
        // Setup
        final HttpRequest request = mock(HttpRequest.class);
        when(authorizationService.getToken(
                any(GrantType.class), anyMap()
        )).thenReturn(returnToken);
        
        OAuth2Authorizer.setCurrentId("id");
        
        token.setRefreshToken("refreshToken");
        when(tokenStore.getToken(any(String.class))).thenReturn(token);
        
        when(request.getHeaders()).thenReturn(new HttpHeaders());
        // Run the test
        auth2Authorizer.applyAuthorization(request);
        
        token.setAccessToken("accessToken");
        
    }
    
    @Test
    public void testApplyAuthorization_RestTemplateThrowsRestClientException() {
        // Setup
        final HttpRequest request = mock(HttpRequest.class);
        
        // Run the test
        assertThrows(CloudAppInvalidAccessException.class,
                     () -> auth2Authorizer.applyAuthorization(request)
        );
    }
    
    @Test
    public void testGetToken1() {
        OAuthToken token = mock(OAuthToken.class);
        // Setup
        when(authorizationService.getGrantType()).thenReturn(GrantType.CLIENT_CREDENTIALS);
        when(authorizationService.getToken(any(GrantType.class),
                                           anyMap()
        )).thenReturn(token);
        
        // Run the test
        final OAuthToken result = auth2Authorizer.getToken("sessionId");
        
        // Verify the results
        assertSame(token, result);
    }
    
    @Test
    public void testGetToken1_RestTemplateThrowsRestClientException() {
        // Setup
        when(authorizationService.getGrantType()).thenReturn(GrantType.CLIENT_CREDENTIALS);
        when(authorizationService.getToken(
                any(GrantType.class), anyMap()
        )).thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(RestClientException.class,
                     () -> auth2Authorizer.getToken("sessionId")
        );
    }
    
    @Test
    public void testRefreshToken() {
        // Setup
        token.setExpiresIn(Instant.now().plusSeconds(-100L)
                                   .getEpochSecond());
        
        when(tokenStore.getToken(any(String.class))).thenReturn(token);
        
        when(authorizationService.getToken(any(GrantType.class),
                                           anyMap()
        )).thenReturn(new OAuthToken());
        
        // Run the test
        final OAuthToken result = auth2Authorizer.getToken("sessionId");
        
        // Verify the results
        assertNotNull(result);
    }
    
    @Test
    public void testRefreshToken_RestTemplateThrowsRestClientException() {
        // Setup
        final OAuthToken token = new OAuthToken();
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setIdToken("idToken");
        token.setTokenType("tokenType");
        token.setScope("scope");
        token.setExpiresIn(Instant.now().plusSeconds(-100L)
                                   .getEpochSecond());
        
        when(tokenStore.getToken(any(String.class))).thenReturn(token);
        
        when(authorizationService.getToken(any(GrantType.class),
                                           anyMap()
        )).thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(RestClientException.class,
                     () -> auth2Authorizer.getToken("sessionId")
        );
    }
    
    @Test
    public void testIsValidToken() {
        final OAuthToken token = new OAuthToken();
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setIdToken("idToken");
        token.setTokenType("tokenType");
        token.setScope("scope");
        
        assert !auth2Authorizer.isValidToken(null);
        
        auth2Authorizer.setTokenVerifier(null);
        
        assertTrue(auth2Authorizer.isValidToken(token));
    }
    
    @Test
    public void testIsRedirectUrl() {
        assertFalse(auth2Authorizer.isRedirectUrl("requestURI"));
    }
    
    @Test
    public void testSaveToken() {
        // Setup
        final OAuthToken oauthToken = new OAuthToken();
        oauthToken.setAccessToken("accessToken");
        oauthToken.setRefreshToken("refreshToken");
        oauthToken.setIdToken("idToken");
        oauthToken.setTokenType("tokenType");
        oauthToken.setScope("scope");
        
        // Run the test
        auth2Authorizer.saveToken(oauthToken);
        
        // Verify the results
    }
    
    @Test
    public void getTokenVerifier_ReturnsCorrectTokenVerifier() {
        TokenVerifier returnedVerifier = auth2Authorizer.getTokenVerifier();
        assertEquals(tokenVerifier, returnedVerifier);
    }
    
    @Test
    public void setTokenVerifier_UpdatesTokenVerifier() {
        TokenVerifier newTokenVerifier = mock(TokenVerifier.class);
        auth2Authorizer.setTokenVerifier(newTokenVerifier);
        assertEquals(newTokenVerifier, auth2Authorizer.getTokenVerifier());
    }
    
    @Test
    public void getAuthorizationService_ReturnsCorrectAuthorizationService() {
        AuthorizationService returnedService = auth2Authorizer.getAuthorizationService();
        assertEquals(authorizationService, returnedService);
    }
    
    @Test
    public void getTokenStore_ReturnsCorrectTokenStore() {
        TokenStorageService returnedStore = auth2Authorizer.getTokenStore();
        assertEquals(tokenStore, returnedStore);
    }
    
    @Test
    public void setTokenStore_UpdatesTokenStore() {
        TokenStorageService newTokenStore = mock(TokenStorageService.class);
        auth2Authorizer.setTokenStore(newTokenStore);
        assertEquals(newTokenStore, auth2Authorizer.getTokenStore());
    }
    
}
