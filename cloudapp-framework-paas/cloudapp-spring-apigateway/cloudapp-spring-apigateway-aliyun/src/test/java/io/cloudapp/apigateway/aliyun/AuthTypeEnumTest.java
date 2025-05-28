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

package io.cloudapp.apigateway.aliyun;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AuthTypeEnumTest {
    
    @Test
    public void testGetType() {
        assertEquals(1, AuthTypeEnum.BASIC.getType());
        assertEquals(2, AuthTypeEnum.OAUTH2.getType());
        assertEquals(3, AuthTypeEnum.JWT.getType());
        assertEquals(5, AuthTypeEnum.APIKEY.getType());
        assertEquals(10, AuthTypeEnum.CUSTOM.getType());
        assertEquals(11, AuthTypeEnum.NO_AUTH.getType());
    }
    
    @Test
    public void testGetName() {
        assertEquals("BASIC", AuthTypeEnum.BASIC.getName());
        assertEquals("OAuth2.0", AuthTypeEnum.OAUTH2.getName());
        assertEquals("JWT", AuthTypeEnum.JWT.getName());
        assertEquals("API_KEY", AuthTypeEnum.APIKEY.getName());
        assertEquals("CUSTOM", AuthTypeEnum.CUSTOM.getName());
        assertEquals("NO_AUTH", AuthTypeEnum.NO_AUTH.getName());
    }
    
    @Test
    public void testGetAuthType1() {
        assertEquals(AuthTypeEnum.BASIC, AuthTypeEnum.getAuthType(1));
    }
    
    @Test
    public void testGetAuthType2() {
        assertEquals(AuthTypeEnum.BASIC, AuthTypeEnum.getAuthType("BASIC"));
    }
    
}
