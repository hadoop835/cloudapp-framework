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

package com.alibaba.cloudapp.oauth2.service;

import com.alibaba.cloudapp.api.gateway.model.OAuthToken;
import org.junit.Before;
import org.junit.Test;

public class SingleTokenStorageServiceTest {
    
    private SingleTokenStorageService singleTokenStorageServiceUnderTest;
    
    @Before
    public void setUp() throws Exception {
        singleTokenStorageServiceUnderTest = new SingleTokenStorageService();
    }
    
    @Test
    public void testSaveToken() {
        // Setup
        final OAuthToken token = new OAuthToken();
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setIdToken("idToken");
        token.setTokenType("tokenType");
        token.setScope("scope");
        
        // Run the test
        singleTokenStorageServiceUnderTest.saveToken("key", token);
        
        // Verify the results
    }
    
    @Test
    public void testGetToken() {
        // Setup
        // Run the test
        final OAuthToken result = singleTokenStorageServiceUnderTest.getToken(
                "key");
        
        // Verify the results
    }
    
}
