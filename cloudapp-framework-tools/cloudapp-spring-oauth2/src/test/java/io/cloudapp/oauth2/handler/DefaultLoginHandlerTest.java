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

import io.cloudapp.api.gateway.model.OAuthToken;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;

public class DefaultLoginHandlerTest {
    
    private DefaultLoginHandler defaultLoginHandlerUnderTest;
    
    @Before
    public void setUp() throws Exception {
        defaultLoginHandlerUnderTest = new DefaultLoginHandler("successUrl");
    }
    
    @Test
    public void testLoginSuccess() throws Exception {
        // Setup
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final OAuthToken token = new OAuthToken();
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setIdToken("idToken");
        token.setTokenType("tokenType");
        token.setScope("scope");
        
        // Run the test
        defaultLoginHandlerUnderTest.loginSuccess(request, response, token);
        
        // Verify the results
    }
    
    @Test
    public void testLoginSuccess_ThrowsIOException() throws Exception {
        // Setup
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final OAuthToken token = new OAuthToken();
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setIdToken("idToken");
        token.setTokenType("tokenType");
        token.setScope("scope");
        
        defaultLoginHandlerUnderTest.loginSuccess(
                request, response, token
        );
        // Run the test
    }
    
    @Test
    public void testLoginSuccess_ThrowsServletException() throws Exception {
        // Setup
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final OAuthToken token = new OAuthToken();
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setIdToken("idToken");
        token.setTokenType("tokenType");
        token.setScope("scope");
        
        // Run the test
         defaultLoginHandlerUnderTest.loginSuccess(
                 request, response, token
         );
         
         assertEquals(200, response.getStatus());
    }
    
    @Test
    public void testLoginFailure() throws Exception {
        // Setup
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        
        // Run the test
        defaultLoginHandlerUnderTest.loginFailure(request, response,
                                                  new Exception("message")
        );
        
        // Verify the results
    }
    
    @Test
    public void testLoginFailure_ThrowsIOException() throws Exception {
        // Setup
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        
        // Run the test
        defaultLoginHandlerUnderTest.loginFailure(
                request, response, new Exception("message")
        );
        
        assertEquals(401, response.getStatus());
    }
    
}
