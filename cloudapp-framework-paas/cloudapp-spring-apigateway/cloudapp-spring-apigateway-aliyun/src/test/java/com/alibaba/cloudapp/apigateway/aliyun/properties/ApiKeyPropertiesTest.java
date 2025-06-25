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

package com.alibaba.cloudapp.apigateway.aliyun.properties;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApiKeyPropertiesTest {
    
    private ApiKeyProperties apiKeyPropertiesUnderTest;
    
    @Before
    public void setUp() {
        apiKeyPropertiesUnderTest = new ApiKeyProperties();
    }
    
    @Test
    public void testApiKeyGetterAndSetter() {
        final String apiKey = "apiKey";
        apiKeyPropertiesUnderTest.setApiKey(apiKey);
        assertEquals(apiKey, apiKeyPropertiesUnderTest.getApiKey());
    }
    
    @Test
    public void testHeaderNameGetterAndSetter() {
        final String headerName = "headerName";
        apiKeyPropertiesUnderTest.setHeaderName(headerName);
        assertEquals(headerName, apiKeyPropertiesUnderTest.getHeaderName());
    }
    
}
