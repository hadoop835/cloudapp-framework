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
import static org.junit.Assert.assertFalse;

public class JwtPropertiesTest {
    
    private JwtProperties jwtPropertiesUnderTest;
    
    @Before
    public void setUp() {
        jwtPropertiesUnderTest = new JwtProperties();
    }
    
    @Test
    public void testKeyIdGetterAndSetter() {
        final String keyId = "keyId";
        jwtPropertiesUnderTest.setKeyId(keyId);
        assertEquals(keyId, jwtPropertiesUnderTest.getKeyId());
    }
    
    @Test
    public void testSecretGetterAndSetter() {
        final String secret = "secret";
        jwtPropertiesUnderTest.setSecret(secret);
        assertEquals(secret, jwtPropertiesUnderTest.getSecret());
    }
    
    @Test
    public void testIssuerGetterAndSetter() {
        final String issuer = "issuer";
        jwtPropertiesUnderTest.setIssuer(issuer);
        assertEquals(issuer, jwtPropertiesUnderTest.getIssuer());
    }
    
    @Test
    public void testSubjectGetterAndSetter() {
        final String subject = "subject";
        jwtPropertiesUnderTest.setSubject(subject);
        assertEquals(subject, jwtPropertiesUnderTest.getSubject());
    }
    
    @Test
    public void testExpiredSecondGetterAndSetter() {
        final long expiredSecond = 0L;
        jwtPropertiesUnderTest.setExpiredSecond(expiredSecond);
        assertEquals(expiredSecond, jwtPropertiesUnderTest.getExpiredSecond());
    }
    
    @Test
    public void testAlgorithmGetterAndSetter() {
        final String algorithm = "algorithm";
        jwtPropertiesUnderTest.setAlgorithm(algorithm);
        assertEquals(algorithm, jwtPropertiesUnderTest.getAlgorithm());
    }
    
    @Test
    public void testAudienceGetterAndSetter() {
        final String audience = "audience";
        jwtPropertiesUnderTest.setAudience(audience);
        assertEquals(audience, jwtPropertiesUnderTest.getAudience());
    }
    
    @Test
    public void testBase64EncodeSecretGetterAndSetter() {
        final boolean base64EncodeSecret = false;
        jwtPropertiesUnderTest.setBase64EncodeSecret(base64EncodeSecret);
        assertFalse(jwtPropertiesUnderTest.isBase64EncodeSecret());
    }
    
}
