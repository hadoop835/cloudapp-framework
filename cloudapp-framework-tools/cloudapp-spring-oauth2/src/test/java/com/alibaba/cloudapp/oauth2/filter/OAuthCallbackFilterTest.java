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
import com.alibaba.cloudapp.api.oauth2.handler.LoginHandler;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OAuthCallbackFilterTest {
    
    @Mock
    private AuthorizationService mockOauthService;
    @Mock
    private TokenStorageService mockTokenStore;
    @Mock
    private LoginHandler mockLoginHandler;
    
    private OAuthCallbackFilter oAuthCallbackFilterUnderTest;
    
    @Before
    public void setUp() throws Exception {
        oAuthCallbackFilterUnderTest = new OAuthCallbackFilter(
                mockOauthService, mockTokenStore, mockLoginHandler
        );
    }
    
    @Test
    public void testDoFilter() throws Exception {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "requestURI"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain chain = new MockFilterChain();
        
        when(mockOauthService.isRedirectUrl("requestURI")).thenReturn(false);
        
        // Run the test
        oAuthCallbackFilterUnderTest.doFilter(servletRequest, servletResponse,
                                              chain
        );
        
        // Verify the results
    }
    
    @Test
    public void testDoFilter_AuthorizationServiceIsRedirectUrlReturnsTrue()
            throws Exception {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "requestURI"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain chain = new MockFilterChain();
        when(mockOauthService.isRedirectUrl("requestURI")).thenReturn(true);
        when(mockOauthService.getGrantType())
                .thenReturn(GrantType.AUTHORIZATION_CODE);
        
        // Configure AuthorizationService.getToken(...).
        final OAuthToken oAuthToken = new OAuthToken();
        oAuthToken.setAccessToken("accessToken");
        oAuthToken.setRefreshToken("refreshToken");
        oAuthToken.setIdToken("idToken");
        oAuthToken.setTokenType("tokenType");
        oAuthToken.setExpiresIn(0L);
        when(mockOauthService.getToken(any(GrantType.class), anyMap()))
                .thenReturn(oAuthToken);
        
        // Run the test
        oAuthCallbackFilterUnderTest.doFilter(servletRequest, servletResponse,
                                              chain
        );
        
        // Verify the results
        verify(mockTokenStore).saveToken(
                anyString(),
                any(OAuthToken.class)
        );
        verify(mockLoginHandler).loginSuccess(any(HttpServletRequest.class),
                                              any(HttpServletResponse.class),
                                              any(OAuthToken.class)
        );
    }
    
    @Test
    public void testDoFilter_AuthorizationServiceGetTokenReturnsNull()
            throws Exception {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "requestURI"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain chain = new MockFilterChain();
        when(mockOauthService.isRedirectUrl("requestURI")).thenReturn(true);
        when(mockOauthService.getGrantType()).thenReturn(
                GrantType.AUTHORIZATION_CODE);
        when(mockOauthService.getToken(any(GrantType.class), anyMap()))
                .thenReturn(null);
        
        // Run the test
        oAuthCallbackFilterUnderTest.doFilter(
                servletRequest, servletResponse, chain
        );
        
        // Verify the results
        verify(mockLoginHandler).loginFailure(any(HttpServletRequest.class),
                                              any(HttpServletResponse.class),
                                              any(Exception.class)
        );
    }
    
    @Test
    public void testDoFilter_AuthorizationServiceGetTokenThrowsCloudAppException()
            throws Exception {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "requestURI"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain chain = new MockFilterChain();
        when(mockOauthService.isRedirectUrl("requestURI")).thenReturn(true);
        when(mockOauthService.getGrantType())
                .thenReturn(GrantType.AUTHORIZATION_CODE);
        when(mockOauthService.getToken(any(GrantType.class), anyMap()))
                .thenThrow(CloudAppException.class);
        
        // Run the test
        oAuthCallbackFilterUnderTest.doFilter(servletRequest, servletResponse,
                                              chain
        );
        
        // Verify the results
        verify(mockLoginHandler).loginFailure(any(HttpServletRequest.class),
                                              any(HttpServletResponse.class),
                                              any(Exception.class)
        );
    }
    
    @Test
    public void testDoFilter_LoginHandlerLoginSuccessThrowsIOException()
            throws Exception {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "requestURI"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain chain = new MockFilterChain();
        when(mockOauthService.isRedirectUrl("requestURI")).thenReturn(true);
        when(mockOauthService.getGrantType()).thenReturn(
                GrantType.AUTHORIZATION_CODE);
        
        // Configure AuthorizationService.getToken(...).
        final OAuthToken oAuthToken = new OAuthToken();
        oAuthToken.setAccessToken("accessToken");
        oAuthToken.setRefreshToken("refreshToken");
        oAuthToken.setIdToken("idToken");
        oAuthToken.setTokenType("tokenType");
        oAuthToken.setExpiresIn(0L);
        when(mockOauthService.getToken(any(GrantType.class),
                                       anyMap()
        )).thenReturn(oAuthToken);
        
        doThrow(IOException.class).when(mockLoginHandler).loginSuccess(
                any(HttpServletRequest.class), any(HttpServletResponse.class),
                any(OAuthToken.class)
        );
        
        // Run the test
        oAuthCallbackFilterUnderTest.doFilter(servletRequest, servletResponse,
                                              chain
        );
        
        // Verify the results
        verify(mockTokenStore).saveToken(any(String.class),
                                         any(OAuthToken.class)
        );
        verify(mockLoginHandler).loginFailure(any(HttpServletRequest.class),
                                              any(HttpServletResponse.class),
                                              any(Exception.class)
        );
    }
    
    @Test
    public void testDoFilter_LoginHandlerLoginSuccessThrowsServletException()
            throws Exception {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "requestURI"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain chain = new MockFilterChain();
        when(mockOauthService.isRedirectUrl("requestURI")).thenReturn(true);
        when(mockOauthService.getGrantType())
                .thenReturn(GrantType.AUTHORIZATION_CODE);
        
        // Configure AuthorizationService.getToken(...).
        final OAuthToken oAuthToken = new OAuthToken();
        oAuthToken.setAccessToken("accessToken");
        oAuthToken.setRefreshToken("refreshToken");
        oAuthToken.setIdToken("idToken");
        oAuthToken.setTokenType("tokenType");
        oAuthToken.setExpiresIn(0L);
        when(mockOauthService.getToken(any(GrantType.class), anyMap()))
                .thenReturn(oAuthToken);
        
        doThrow(ServletException.class).when(mockLoginHandler).loginSuccess(
                any(HttpServletRequest.class), any(HttpServletResponse.class),
                any(OAuthToken.class)
        );
        
        // Run the test
        oAuthCallbackFilterUnderTest.doFilter(servletRequest, servletResponse,
                                              chain
        );
        
        // Verify the results
        verify(mockTokenStore).saveToken(
                anyString(),
                any(OAuthToken.class)
        );
        verify(mockLoginHandler).loginFailure(any(HttpServletRequest.class),
                                              any(HttpServletResponse.class),
                                              any(Exception.class)
        );
    }
    
    @Test
    public void testDoFilter_LoginHandlerLoginFailureThrowsIOException()
            throws Exception {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "requestURI"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain chain = new MockFilterChain();
        when(mockOauthService.isRedirectUrl("requestURI")).thenReturn(true);
        when(mockOauthService.getGrantType())
                .thenReturn(GrantType.AUTHORIZATION_CODE);
        
        // Configure AuthorizationService.getToken(...).
        final OAuthToken oAuthToken = new OAuthToken();
        oAuthToken.setAccessToken("accessToken");
        oAuthToken.setRefreshToken("refreshToken");
        oAuthToken.setIdToken("idToken");
        oAuthToken.setTokenType("tokenType");
        oAuthToken.setExpiresIn(0L);
        when(mockOauthService.getToken(any(GrantType.class), anyMap()))
                .thenReturn(oAuthToken);
        
        doThrow(IOException.class).when(mockLoginHandler).loginSuccess(
                any(HttpServletRequest.class), any(HttpServletResponse.class),
                any(OAuthToken.class)
        );
        doThrow(IOException.class).when(mockLoginHandler).loginFailure(
                any(HttpServletRequest.class), any(HttpServletResponse.class),
                any(Exception.class)
        );
        
        // Run the test
        assertThrows(IOException.class,
                     () -> oAuthCallbackFilterUnderTest.doFilter(servletRequest,
                                                                 servletResponse,
                                                                 chain
                     )
        );
        verify(mockTokenStore).saveToken(
                anyString(),
                any(OAuthToken.class)
        );
    }
    
    @Test
    public void testDoFilter_LoginHandlerLoginFailureThrowsServletException()
            throws Exception {
        // Setup
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                "POST", "requestURI"
        );
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final FilterChain chain = new MockFilterChain();
        when(mockOauthService.isRedirectUrl("requestURI")).thenReturn(true);
        when(mockOauthService.getGrantType())
                .thenReturn(GrantType.AUTHORIZATION_CODE);
        
        // Configure AuthorizationService.getToken(...).
        final OAuthToken oAuthToken = new OAuthToken();
        oAuthToken.setAccessToken("accessToken");
        oAuthToken.setRefreshToken("refreshToken");
        oAuthToken.setIdToken("idToken");
        oAuthToken.setTokenType("tokenType");
        oAuthToken.setExpiresIn(0L);
        when(mockOauthService.getToken(any(GrantType.class), anyMap()))
                .thenReturn(oAuthToken);
        
        doThrow(IOException.class).when(mockLoginHandler).loginSuccess(
                any(HttpServletRequest.class), any(HttpServletResponse.class),
                any(OAuthToken.class)
        );
        doThrow(ServletException.class).when(mockLoginHandler).loginFailure(
                any(HttpServletRequest.class), any(HttpServletResponse.class),
                any(Exception.class)
        );
        
        // Run the test
        assertThrows(ServletException.class,
                     () -> oAuthCallbackFilterUnderTest.doFilter(servletRequest,
                                                                 servletResponse,
                                                                 chain
                     )
        );
        verify(mockTokenStore).saveToken(
                anyString(),
                any(OAuthToken.class)
        );
    }
    
    @Test
    public void testLoginHandlerGetterAndSetter() {
        final LoginHandler loginHandler = null;
        oAuthCallbackFilterUnderTest.setLoginHandler(loginHandler);
        assertEquals(loginHandler,
                     oAuthCallbackFilterUnderTest.getLoginHandler()
        );
    }
    
    @Test
    public void testGetOrder() {
        assertEquals(-1, oAuthCallbackFilterUnderTest.getOrder());
    }
    
}
