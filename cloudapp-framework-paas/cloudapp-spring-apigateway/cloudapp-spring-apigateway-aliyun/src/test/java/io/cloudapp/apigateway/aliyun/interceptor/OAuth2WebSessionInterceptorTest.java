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

package io.cloudapp.apigateway.aliyun.interceptor;

import io.cloudapp.api.gateway.authentication.OAuth2Authorizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OAuth2WebSessionInterceptorTest {
    
    @InjectMocks
    private OAuth2WebSessionInterceptor oAuth2WebSessionInterceptor;
    
    @Mock
    private HttpSession httpSession;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HandlerInterceptor handlerInterceptor;
    
    @Before
    public void setUp() {
        // Setup mock session
        when(request.getSession()).thenReturn(httpSession);
//        when(request.getSession(true)).thenReturn(httpSession);
    }
    
    @Test
    public void preHandle_SetsCurrentIdAndCallsSuperPreHandle()
            throws Exception {
        // Arrange
        String sessionId = "testSessionId";
        when(httpSession.getId()).thenReturn(sessionId);
        
        // Act
        boolean result = oAuth2WebSessionInterceptor.preHandle(request,
                                                               response,
                                                               handlerInterceptor
        );
        
        // Assert
        assertEquals(sessionId, OAuth2Authorizer.getCurrentId());
        assertTrue(result); // Assuming super.preHandle returns true
    }
    
    @Test
    public void afterCompletion_CleansCurrentIdAndCallsSuperAfterCompletion()
            throws Exception {
        // Arrange
        String sessionId = "testSessionId";
        when(httpSession.getId()).thenReturn(sessionId);
        
        oAuth2WebSessionInterceptor.preHandle(request, response,
                                              handlerInterceptor
        );
        
        // Act
        oAuth2WebSessionInterceptor.afterCompletion(request, response,
                                                    handlerInterceptor, null
        );
        
        // Assert
        assertNull(OAuth2Authorizer.getCurrentId());
    }
    
}
