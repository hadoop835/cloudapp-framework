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

import com.alibaba.cloudapp.apigateway.aliyun.properties.ApiKeyProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.BasicProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.JwtProperties;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.exeption.CloudAppInvalidAccessException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiGatewayManagerTest {
    
    @Mock
    private RestTemplate mockRestTemplate;
    
    private ApiGatewayManager gatewayManager;
    
    @Before
    public void setUp() throws Exception {
        gatewayManager = new ApiGatewayManager(
                "accessKey",
                "secretKey",
                "gatewayUri",
                mockRestTemplate
        );
    }
    
    @Test
    public void testCreateApiKeyConsumer() {
        // Setup
        final ApiKeyProperties apiKey = new ApiKeyProperties();
        apiKey.setApiKey("apiKey");
        apiKey.setHeaderName("headerName");
        
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       any(Class.class)
        ))
                .thenReturn(new ResponseEntity<>("body", HttpStatus.OK));
        
        // Run the test
        gatewayManager.createApiKeyConsumer(
                "name", "gwInstanceId",
                Collections.singletonList("value"),
                apiKey
        );
        
        // Verify the results
    }
    
    @Test
    public void testCreateApiKeyConsumer_RestTemplateThrowsRestClientException() {
        // Setup
        final ApiKeyProperties apiKey = new ApiKeyProperties();
        apiKey.setApiKey("apiKey");
        apiKey.setHeaderName("headerName");
        
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       any(Class.class)
        ))
                .thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> gatewayManager.createApiKeyConsumer(
                             "name", "gwInstanceId",
                             Collections.singletonList("value"),
                             apiKey
                     )
        );
    }
    
    @Test
    public void testCreateJwtConsumer() {
        // Setup
        final JwtProperties jwt = new JwtProperties();
        jwt.setKeyId("keyId");
        jwt.setSecret("secret");
        jwt.setIssuer("issuer");
        jwt.setSubject("subject");
        jwt.setExpiredSecond(0L);
        
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       any(Class.class)
        ))
                .thenReturn(new ResponseEntity<>("body", HttpStatus.OK));
        
        // Run the test
        gatewayManager.createJwtConsumer(
                "name", "gwInstanceId",
                Collections.singletonList("value"), jwt
        );
        
        // Verify the results
    }
    
    @Test
    public void testCreateJwtConsumer_RestTemplateThrowsRestClientException() {
        // Setup
        final JwtProperties jwt = new JwtProperties();
        jwt.setKeyId("keyId");
        jwt.setSecret("secret");
        jwt.setIssuer("issuer");
        jwt.setSubject("subject");
        jwt.setExpiredSecond(0L);
        
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       any(Class.class)
        ))
                .thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> gatewayManager.createJwtConsumer(
                             "name", "gwInstanceId",
                             Collections.singletonList("value"), jwt
                     )
        );
    }
    
    @Test
    public void testCreateBasicConsumer() {
        // Setup
        final BasicProperties basic = new BasicProperties();
        basic.setUsername("username");
        basic.setPassword("password");
        
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       any(Class.class)
        ))
                .thenReturn(new ResponseEntity<>("body", HttpStatus.OK));
        
        // Run the test
        gatewayManager.createBasicConsumer(
                "name", "gwInstanceId",
                Collections.singletonList("value"), basic
        );
        
        // Verify the results
    }
    
    @Test
    public void testCreateBasicConsumer_RestTemplateThrowsRestClientException() {
        // Setup
        final BasicProperties basic = new BasicProperties();
        basic.setUsername("username");
        basic.setPassword("password");
        
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       any(Class.class)
        ))
                .thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> gatewayManager.createBasicConsumer(
                             "name", "gwInstanceId",
                             Collections.singletonList("value"), basic
                     )
        );
    }
    
    @Test
    public void testCheckConsumerExists() {
        // Setup
        
        // Run the test
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       any(Class.class)
        )).thenReturn(new ResponseEntity<>(
                "{\"data\": {\"records\": [{\"appName\":\"name\"}]}}",
                HttpStatus.OK
        ));
        
        
        final boolean result = gatewayManager.checkConsumerExists(
                "name", "gwInstanceId");
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    public void testCheckConsumerExists_RestTemplateThrowsRestClientException() {
        // Setup
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       any(Class.class)
        ))
                .thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> gatewayManager.checkConsumerExists(
                             "name", "gwInstanceId")
        );
    }
    
    @Test
    public void testInitRequestEntity() throws Exception {
        // Setup
        final Map<String, String> query = new HashMap<>();
        final Map<String, Object> body = new HashMap<>();
        final MediaType type = new MediaType("type", "subtype",
                                             StandardCharsets.UTF_8
        );
        
        // Run the test
        final RequestEntity<String> result = gatewayManager.initRequestEntity(
                HttpMethod.GET, "path", query, body, type);
        
        // Verify the results
        assertNotNull(result.getHeaders());
        assertTrue(result.getHeaders().containsKey("X-HMAC-ACCESS-KEY"));
        assertTrue(result.getHeaders().containsKey("X-CSB-OPENAPI"));
        assertTrue(result.getHeaders().containsKey("X-HMAC-REQUEST-TIME"));
        assertTrue(result.getHeaders().containsKey("X-HMAC-SIGN"));
    }
    
    @Test
    public void testInitRequestEntity_ThrowsCloudAppInvalidAccessException() {
        // Setup
        final Map<String, String> query = new HashMap<>();
        final Map<String, Object> body = new HashMap<>();
        final MediaType type = new MediaType(
                "type", "subtype", StandardCharsets.UTF_8
        );
        
        gatewayManager = new ApiGatewayManager(null,
                                               null,
                                               null,
                                               mockRestTemplate
        );
        
        // Run the test
        assertThrows(CloudAppInvalidAccessException.class,
                     () -> gatewayManager.initRequestEntity(
                             HttpMethod.GET, "path", query, body, type)
        );
    }
    
}
