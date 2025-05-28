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

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class ClientTokenRequestHandlerTest {
    
    private ClientTokenRequestHandler clientTokenRequestHandlerUnderTest;
    
    @Before
    public void setUp() throws Exception {
        clientTokenRequestHandlerUnderTest = new ClientTokenRequestHandler(
                "clientId", "clientSecret", new URI("https://example.com/"));
    }
    
    @Test
    public void testCreate() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic Y2xpZW50SWQ6Y2xpZW50U2VjcmV0");
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // Setup
        final RequestEntity<String> expectedResult = new RequestEntity<>(
                "{\"grant_type\":\"client_credentials\"}",
                headers,
                HttpMethod.POST,
                new URI("https://example.com/")
        );
        
        
        // Run the test
        final RequestEntity<String> result = clientTokenRequestHandlerUnderTest.create();
        
        // Verify the results
        assertEquals(expectedResult, result);
    }
    
}
