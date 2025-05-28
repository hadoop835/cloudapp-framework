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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

import java.util.UUID;
import java.util.function.Function;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomAuthorizerTest {

    private static final String TOKEN = UUID.randomUUID().toString();
    private static final String AUTHORIZATION = "Authorization";

    @Mock
    private HttpRequest request;
    @Mock
    private HttpHeaders headers;

    @Mock
    private Function<HttpRequest, String> generate;

    private CustomAuthorizer customAuthorizer;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(request.getHeaders()).thenReturn(headers);
        when(generate.apply(any(HttpRequest.class))).thenReturn(TOKEN);
    }

    @Test
    public void applyAuthorization_ShouldAddBearerToken_WhenGenerateReturnsToken() {
        customAuthorizer = new CustomAuthorizer(generate);

        customAuthorizer.applyAuthorization(request);

        verify(request).getHeaders();
        verify(request.getHeaders()).add(AUTHORIZATION, TOKEN);
    }

    @Test
    public void applyAuthorization_ShouldAddCustomHeader_WhenGenerateReturnsToken() {
        String customHeader = "Custom-Header";
        customAuthorizer = new CustomAuthorizer(generate, customHeader);

        customAuthorizer.applyAuthorization(request);

        verify(request).getHeaders();
        verify(request.getHeaders()).add(customHeader, TOKEN);
    }

    @Test
    public void applyAuthorization_ShouldHandleEmptyToken() {
        customAuthorizer = new CustomAuthorizer(generate);

        when(generate.apply(any(HttpRequest.class))).thenReturn("");

        customAuthorizer.applyAuthorization(request);

        verify(request).getHeaders();
        verify(request.getHeaders()).add(AUTHORIZATION, "");
    }

    @Test
    public void applyAuthorization_ShouldHandleNullToken() {
        customAuthorizer = new CustomAuthorizer(generate);

        when(generate.apply(any(HttpRequest.class))).thenReturn(null);

        customAuthorizer.applyAuthorization(request);

        verify(request).getHeaders();
        verify(request.getHeaders()).add(AUTHORIZATION, null);
    }
}
