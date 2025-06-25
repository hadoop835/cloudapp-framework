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

package com.alibaba.cloudapp.apigateway.aliyun.util;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CSBSignatureUtilTest {
    
    private CSBSignatureUtil csbSignatureUtilUnderTest;
    
    @Before
    public void setUp() throws Exception {
        csbSignatureUtilUnderTest = new CSBSignatureUtil(
                "accessKey", "secretKey");
    }
    
    @Test
    public void testGetQuerySign() throws Exception {
        // Setup
        final Map<String, String> query = new HashMap<>();
        final Map<String, Object> body = new HashMap<>();
        final MediaType type = new MediaType(
                "type", "subtype", StandardCharsets.UTF_8
        );
        
        // Run the test
        final String result = csbSignatureUtilUnderTest.getQuerySign(
                "method",
                "path",
                query,
                body, 0L,
                type
        );
        
        // Verify the results
        assertEquals("VODVrMVFuUhq0NdO7HUuTZdKcLVpMKhKTQIYjcKKIBI=", result);
    }
    
    @Test
    public void testGetQuerySign_ThrowsNoSuchAlgorithmException()
            throws NoSuchAlgorithmException, InvalidKeyException {
        final String methodName = "method";
        final String path = "path";
        // Setup
        final Map<String, String> query = new HashMap<>();
        final Map<String, Object> body = new HashMap<>();
        final MediaType type = new MediaType(
                "type", "subtype", StandardCharsets.UTF_8
        );
        
        csbSignatureUtilUnderTest = mock(CSBSignatureUtil.class);
        
        when(csbSignatureUtilUnderTest.getQuerySign(
                methodName, path,
                query, body, 0L, type
        )).thenThrow(NoSuchAlgorithmException.class);
        
        // Run the test
        assertThrows(NoSuchAlgorithmException.class,
                     () -> csbSignatureUtilUnderTest.getQuerySign(
                             methodName, path,  query,
                             body, 0L, type
                     )
        );
    }
    
    @Test
    public void testGetQuerySign_ThrowsInvalidKeyException()
            throws NoSuchAlgorithmException, InvalidKeyException {
        // Setup
        final String methodName = "method";
        final String path = "path";
        // Setup
        final Map<String, String> query = new HashMap<>();
        final Map<String, Object> body = new HashMap<>();
        final MediaType type = new MediaType(
                "type", "subtype", StandardCharsets.UTF_8
        );
        
        csbSignatureUtilUnderTest = mock(CSBSignatureUtil.class);
        
        when(csbSignatureUtilUnderTest.getQuerySign(
                methodName, path,
                query, body, 0L, type
        )).thenThrow(InvalidKeyException.class);
        
        // Run the test
        assertThrows(InvalidKeyException.class,
                     () -> csbSignatureUtilUnderTest.getQuerySign(
                             "method", "path", query,
                             body, 0L, type
                     )
        );
    }
    
    @Test
    public void testAccessKeyGetterAndSetter() {
        final String accessKey = "accessKey";
        csbSignatureUtilUnderTest.setAccessKey(accessKey);
        assertEquals(accessKey, csbSignatureUtilUnderTest.getAccessKey());
    }
    
    @Test
    public void testSecretKeyGetterAndSetter() {
        final String secretKey = "secretKey";
        csbSignatureUtilUnderTest.setSecretKey(secretKey);
        assertEquals(secretKey, csbSignatureUtilUnderTest.getSecretKey());
    }
    
}
