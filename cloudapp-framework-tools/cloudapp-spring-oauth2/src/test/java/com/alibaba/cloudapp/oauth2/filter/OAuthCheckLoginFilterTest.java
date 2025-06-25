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

package com.alibaba.cloudapp.oauth2.filter;

import com.alibaba.cloudapp.api.gateway.model.OAuthToken;
import com.alibaba.cloudapp.api.oauth2.AuthorizationService;
import com.alibaba.cloudapp.api.oauth2.GrantType;
import com.alibaba.cloudapp.api.oauth2.TokenStorageService;
import com.alibaba.cloudapp.api.oauth2.verifier.TokenVerifier;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OAuthCheckLoginFilterTest {
    
    @Mock
    private TokenStorageService mockTokenStore;
    @Mock
    private TokenVerifier mockTokenVerifier;
    @Mock
    private AuthorizationService mockAuthorizationService;
    
    private OAuthCheckLoginFilter oAuthCheckLoginFilterUnderTest;
    
    @Before
    public void setUp() throws Exception {
        oAuthCheckLoginFilterUnderTest = new OAuthCheckLoginFilter(
                mockTokenStore, mockTokenVerifier, mockAuthorizationService);
    }
    
    @Test
    public void testDoFilter() throws Exception {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "/path"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain filterChain = new MockFilterChain();
        
        // Configure AuthorizationService.getToken(...).
        final OAuthToken oAuthToken1 = new OAuthToken();
        oAuthToken1.setAccessToken("accessToken");
        oAuthToken1.setRefreshToken("refreshToken");
        oAuthToken1.setIdToken("idToken");
        oAuthToken1.setTokenType("tokenType");
        oAuthToken1.setScope("scope");
        when(mockAuthorizationService.getToken(any(GrantType.class),
                                               any(Map.class)
        )).thenReturn(oAuthToken1);
        
        when(mockTokenStore.getToken(any(String.class))).thenReturn(oAuthToken1);
        when(mockTokenVerifier.verify(any(String.class))).thenReturn(false);
        
        // Run the test
        oAuthCheckLoginFilterUnderTest.doFilter(
                servletRequest, servletResponse, filterChain
        );
        
        
        // Verify the results
        verify(mockTokenStore).saveToken(any(String.class),
                                         any(OAuthToken.class)
        );
    }
    
    @Test
    public void testDoFilter_TokenStorageServiceGetTokenReturnsNull()
            throws Exception {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "/requestURI"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain filterChain = new MockFilterChain();
        
        // Run the test
        oAuthCheckLoginFilterUnderTest.doFilter(servletRequest, servletResponse,
                                                filterChain
        );
        
        // Verify the results
        assertEquals(302, servletResponse.getStatus());
        assertNull(servletResponse.getHeader("Location"));
    }
    
    @Test
    public void testDoFilter_TokenVerifierReturnsTrue() throws Exception {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "/path"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain filterChain = new MockFilterChain();
        
        // Configure TokenStorageService.getToken(...).
        final OAuthToken oAuthToken = new OAuthToken();
        oAuthToken.setAccessToken("accessToken");
        oAuthToken.setRefreshToken("refreshToken");
        oAuthToken.setIdToken("idToken");
        oAuthToken.setTokenType("tokenType");
        oAuthToken.setScope("scope");
        when(mockTokenStore.getToken(any(String.class))).thenReturn(oAuthToken);
        
        when(mockTokenVerifier.verify(any(String.class))).thenReturn(true);
        
        // Run the test
        oAuthCheckLoginFilterUnderTest.doFilter(servletRequest, servletResponse,
                                                filterChain
        );
        
        // Verify the results
    }
    
    @Test
    public void testDoFilter_AuthorizationServiceGetTokenThrowsCloudAppException() {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "/"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain filterChain = null;
        
        // Configure TokenStorageService.getToken(...).
        when(mockTokenStore.getToken(any(String.class))).thenThrow(
                CloudAppException.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> oAuthCheckLoginFilterUnderTest.doFilter(
                             servletRequest, servletResponse, filterChain)
        );
    }
    
    @Test
    public void testGetSkipPaths() {
        oAuthCheckLoginFilterUnderTest.addSkipUrls("value");
        assertEquals(Collections.singletonList("value"),
                     oAuthCheckLoginFilterUnderTest.getSkipPaths()
        );
    }
    
    @Test
    public void testSetSkipPaths() {
        // Setup
        // Run the test
        oAuthCheckLoginFilterUnderTest.setSkipPaths(
                Collections.singletonList("value"));
        
        // Verify the results
    }
    
    @Test
    public void testAddSkipUrls() {
        // Setup
        // Run the test
        oAuthCheckLoginFilterUnderTest.addSkipUrls("url");
        
        // Verify the results
    }
    
    @Test
    public void testIsSkipUrl() {
        // Setup
        // Run the test
        final boolean result = oAuthCheckLoginFilterUnderTest.isSkipUrl("url");
        
        // Verify the results
        assertFalse(result);
    }
    
    @Test
    public void testGetOrder() {
        assertEquals(0, oAuthCheckLoginFilterUnderTest.getOrder());
    }
    
}
