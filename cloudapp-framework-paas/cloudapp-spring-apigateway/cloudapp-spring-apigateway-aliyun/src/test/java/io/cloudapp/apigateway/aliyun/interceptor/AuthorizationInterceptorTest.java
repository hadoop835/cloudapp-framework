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

import io.cloudapp.api.gateway.authentication.Authorizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationInterceptorTest {
    
    @Mock
    private Authorizer mockAuthorizer;
    
    private AuthorizationInterceptor interceptor;
    
    @Before
    public void setUp() {
        interceptor = new AuthorizationInterceptor(
                mockAuthorizer);
    }
    
    @Test
    public void testIntercept() throws Exception {
        // Setup
        final HttpRequest request = mock(HttpRequest.class);
        final ClientHttpRequestExecution execution = mock(
                ClientHttpRequestExecution.class);
        
        when(request.getHeaders()).thenReturn(new HttpHeaders());
        // Run the test
        try (final ClientHttpResponse result =
                     interceptor.intercept(
                             request, "content".getBytes(), execution)) {
            
            // Verify the results
            verify(mockAuthorizer).applyAuthorization(any(HttpRequest.class));
        }
    }
    
    @Test
    public void testIntercept_AuthorizerThrowsCloudAppException() {
        // Setup
        final HttpRequest request = mock(HttpRequest.class);
        final ClientHttpRequestExecution execution = mock(
                ClientHttpRequestExecution.class);
        
        // Run the test
        assertThrows(RuntimeException.class,
                     () -> interceptor.intercept(request, "content".getBytes(),
                                                 execution
                     )
        );
    }
    
}
