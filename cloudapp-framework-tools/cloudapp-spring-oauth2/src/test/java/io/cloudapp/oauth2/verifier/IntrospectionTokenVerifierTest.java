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

package io.cloudapp.oauth2.verifier;

import com.alibaba.fastjson2.JSONObject;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IntrospectionTokenVerifierTest {
    
    @Mock
    private RestTemplate mockRestTemplate;
    
    private IntrospectionTokenVerifier introspectionTokenVerifierUnderTest;
    
    @Before
    public void setUp() throws Exception {
        introspectionTokenVerifierUnderTest = new IntrospectionTokenVerifier(
                new URI("https://example.com/"), mockRestTemplate);
    }
    
    @Test
    public void testVerify() {
        JSONObject object = new JSONObject();
        object.put("active", false);
        // Setup
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       eq(String.class)))
                .thenReturn(new ResponseEntity<>(object.toJSONString(), HttpStatus.OK));
        
        // Run the test
        final boolean result = introspectionTokenVerifierUnderTest.verify(
                "token");
        
        // Verify the results
        assertFalse(result);
    }
    
    @Test
    public void testVerify_RestTemplateThrowsRestClientException() {
        // Setup
        when(mockRestTemplate.exchange(any(RequestEntity.class),
                                       eq(String.class)))
                .thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(RestClientException.class,
                     () -> introspectionTokenVerifierUnderTest.verify("token")
        );
    }
    
}
