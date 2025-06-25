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

package com.alibaba.cloudapp.apigateway.aliyun.service;

import com.alibaba.cloudapp.api.gateway.Authorized;
import com.alibaba.cloudapp.api.gateway.authentication.Authorizer;
import com.alibaba.cloudapp.apigateway.aliyun.properties.Config;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GatewayServiceImplTest {


    @Mock
    private AsyncRestTemplate asyncRestTemplate;

    @Mock
    private Authorizer authorizer;

    @Mock
    private Authorized authorized;

    @Mock
    private Config config;

    @Mock
    private RestTemplate restTemplate;

    @Spy
    @InjectMocks
    private GatewayServiceImpl gatewayService;
    
    @Before
    public void setUp() {
        gatewayService = new GatewayServiceImpl(authorized, restTemplate, asyncRestTemplate);
    }

    @Test
    public void testGetRestTemplate() {
        assertSame(restTemplate, gatewayService.getRestTemplate());
    }

    @Test
    public void testGetAsyncRestTemplate() {
        assertEquals(asyncRestTemplate, gatewayService.getAsyncRestTemplate());
    }

    @Test
    public void testAfterPropertiesSet_AuthorizerNotNull() throws CloudAppException {
        gatewayService = new GatewayServiceImpl(authorizer, restTemplate, asyncRestTemplate);

        gatewayService.afterPropertiesSet();

        assertNotNull(gatewayService.getRestTemplate());
        assertNotNull(gatewayService.getAsyncRestTemplate());
    }

    @Test
    public void testAfterPropertiesSet_AuthorizerNull_ThrowsException() {
        gatewayService = new GatewayServiceImpl(authorized, restTemplate,
                                                asyncRestTemplate);
        try {
            gatewayService.afterPropertiesSet();
            fail("Expected CloudAppException to be thrown");
        } catch (CloudAppException e) {
            assertEquals("CloudApp.InvalidAuthorizer", e.getMessage());
        }
    }

    @Test
    public void testAfterPropertiesSet_AuthorizerNull_UsesDefaultAuthorizer() throws CloudAppException {
        Authorized authorized = mock(Authorized.class);
        when(authorized.getAuthorizer()).thenReturn(authorizer);

        gatewayService = new GatewayServiceImpl(authorized, restTemplate, asyncRestTemplate);

        gatewayService.afterPropertiesSet();

        assertNotNull(gatewayService.getRestTemplate());
        assertNotNull(gatewayService.getAsyncRestTemplate());
    }

    @Test
    public void testGetAuthorizer() throws CloudAppException {
        when(authorized.getAuthorizer()).thenReturn(authorizer);

        assertEquals(authorizer, gatewayService.getAuthorizer());
    }

}
