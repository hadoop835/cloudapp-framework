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
import com.alibaba.cloudapp.api.oauth2.GrantType;
import com.alibaba.cloudapp.model.OAuth2Client;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationServiceImplTest {
    
    @Mock
    private OAuth2Client mockClient;
    @Mock
    private RestTemplate mockRestTemplate;
    
    private AuthorizationServiceImpl authorizationService;
    
    @Before
    public void setUp() throws Exception {
        authorizationService = new AuthorizationServiceImpl(
                mockClient, mockRestTemplate);
    }
    
    @Test
    public void testGetCodeUrl() throws Exception {
        // Setup
        when(mockClient.getGrantTypes()).thenReturn(
                Collections.singletonList("value"));
        when(mockClient.getAuthorizationUri())
                .thenReturn(new URI("https://example.com/"));
        when(mockClient.getClientId()).thenReturn("result");
        when(mockClient.getRedirectUri()).thenReturn("result");
        when(mockClient.getScopes()).thenReturn(
                Collections.singletonList("value"));
        when(mockClient.isEnablePkce()).thenReturn(false);
        when(mockClient.getGrantTypes()).thenReturn(Arrays.asList(
                GrantType.AUTHORIZATION_CODE.getGrantType(),
                GrantType.REFRESH_TOKEN.getGrantType()
        ));
        
        // Run the test
        final Map<String, String> result = authorizationService.getCodeUrl(
                "state");
        
        // Verify the results
        assertNotNull(result.get("authorizationURI"));
    }
    
    @Test
    public void testGetCodeUrl_OAuth2ClientGetGrantTypesReturnsNoItems() {
        // Setup
        when(mockClient.getGrantTypes()).thenReturn(Collections.emptyList());
        
        // Run the test
        final Map<String, String> result = authorizationService.getCodeUrl(
                "state");
        
        // Verify the results
        assertNull(result);
    }
    
    @Test
    public void testGetCodeUrl_OAuth2ClientGetScopesReturnsNoItems() {
        // Setup
        when(mockClient.getGrantTypes()).thenReturn(Collections.singletonList(
                "password"));
        
        // Run the test
        final Map<String, String> result = authorizationService.getCodeUrl(
                "state");
        
        // Verify the results
        assertNull(result);
    }
    
    @Test
    public void testGetToken() throws Exception {
        // Setup
        final Map<String, String> params = Collections.singletonMap(
                "code", "code"
        );
        when(mockClient.getClientId()).thenReturn("clientId");
        when(mockClient.getClientSecret()).thenReturn("clientSecret");
        when(mockClient.getTokenUri())
                .thenReturn(new URI("https://example.com/"));
        when(mockClient.getRedirectUri()).thenReturn("redirectUri");
        when(mockClient.isEnablePkce()).thenReturn(false);
        OAuthToken token = new OAuthToken();
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       eq(String.class)
        ))
                .thenReturn(new ResponseEntity<>(JSON.toJSONString(token),
                                                 HttpStatus.OK
                ));
        
        // Run the test
        final OAuthToken result = authorizationService.getToken(
                GrantType.AUTHORIZATION_CODE, params);
        
        // Verify the results
        assertEquals(token.getAccessToken(), result.getAccessToken());
    }
    
    @Test
    public void testGetToken_RestTemplateThrowsRestClientException()
            throws Exception {
        // Setup
        final Map<String, String> params = Collections.singletonMap(
                "code", "code");
        when(mockClient.getClientId()).thenReturn("clientId");
        when(mockClient.getClientSecret()).thenReturn("clientSecret");
        when(mockClient.getTokenUri())
                .thenReturn(new URI("https://example.com/"));
        when(mockClient.getRedirectUri()).thenReturn("redirectUri");
        when(mockClient.isEnablePkce()).thenReturn(false);
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       eq(String.class)
        ))
                .thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(RestClientException.class,
                     () -> authorizationService.getToken(
                             GrantType.AUTHORIZATION_CODE, params)
        );
    }
    
    @Test
    public void testClientGetterAndSetter() {
        final OAuth2Client client = new OAuth2Client();
        authorizationService.setOAuth2Client(client);
        assertEquals(client,
                     authorizationService.getOAuth2Client()
        );
    }
    
    @Test
    public void testGenerateCodeVerifier() {
        assertNotNull(
                AuthorizationServiceImpl.generateCodeVerifier()
        );
    }
    
    @Test
    public void testGenerateCodeChallenge() {
        assertEquals("N1E4yRMD7xixn_oFyO_W3htYN3rY7-HMDKJe6z6r928",
                     AuthorizationServiceImpl.generateCodeChallenge(
                             "codeVerifier")
        );
    }
    
    @Test
    public void testIsRedirectUrl() {
        // Setup
        when(mockClient.getRedirectUri()).thenReturn("result");
        
        // Run the test
        final boolean result = authorizationService.isRedirectUrl(
                "requestURI");
        
        // Verify the results
        assertFalse(result);
    }
    
    @Test
    public void testGetGrantType() {
        // Setup
        when(mockClient.getGrantTypes()).thenReturn(Collections.singletonList(
                "authorization_code"));
        
        // Run the test
        final GrantType result = authorizationService.getGrantType();
        
        // Verify the results
        assertEquals(GrantType.AUTHORIZATION_CODE, result);
    }
    
    @Test
    public void testGetGrantType_OAuth2ClientReturnsNull() {
        // Setup
        when(mockClient.getGrantTypes()).thenReturn(null);
        
        // Run the test
        final GrantType result = authorizationService.getGrantType();
        
        // Verify the results
        assertNull(result);
    }
    
    @Test
    public void testGetGrantType_OAuth2ClientReturnsNoItems() {
        // Setup
        when(mockClient.getGrantTypes()).thenReturn(Arrays.asList(
                GrantType.AUTHORIZATION_CODE.getGrantType(),
                GrantType.REFRESH_TOKEN.getGrantType()
        ));
        
        // Run the test
        final GrantType result = authorizationService.getGrantType();
        
        // Verify the results
        assertEquals(GrantType.AUTHORIZATION_CODE, result);
    }
    
}
