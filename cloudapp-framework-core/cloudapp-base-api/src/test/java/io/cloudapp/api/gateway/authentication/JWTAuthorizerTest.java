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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import io.cloudapp.api.gateway.AlgorithmUtil;
import io.cloudapp.api.gateway.model.JWTParams;
import io.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JWTAuthorizerTest {
    
    private static final String SECRET_KEY = "secretKey";
    private static final String ALGORITHM = "HS256";
    private static final String ISSUER = "issuer";
    private static final String SUBJECT = "subject";
    private static final String AUDIENCE = "audience";
    private static final long TTL_SECONDS = 3600;
    
    private JWTAuthorizer jwtAuthorizer;
    
    @Mock
    private HttpRequest request;
    @Mock
    private HttpHeaders headers;
    
    @Mock
    private JWTAuthorizer.ClaimsGenerator claimsGenerator;
    
    @Before
    public void setUp() throws Exception {
        JWTParams params = JWTParams.builder()
                                    .base64EncodeSecret(false)
                                    .audience(AUDIENCE)
                                    .secretKey(SECRET_KEY)
                                    .algorithm(ALGORITHM)
                                    .issuer(ISSUER)
                                    .ttlSeconds(TTL_SECONDS)
                                    .subject(SUBJECT)
                                    .build();
        
        jwtAuthorizer = new JWTAuthorizer(params);
        
        when(request.getHeaders()).thenReturn(headers);
    }
    
    @Test
    public void applyAuthorization_ShouldSetBearerAuthHeader() {
        String jwt = "dummy.jwt.token";
        jwtAuthorizer = spy(jwtAuthorizer);
        doReturn(jwt).when(jwtAuthorizer).createJWT(any(HttpRequest.class));
        
        jwtAuthorizer.applyAuthorization(request);
        
        verify(request.getHeaders(), times(1)).add(HttpHeaders.AUTHORIZATION,
                                                   jwt
        );
    }
    
    @Test
    public void createJWT_ShouldGenerateValidJWT() {
        long currentTime = System.currentTimeMillis();
        
        String jwt = jwtAuthorizer.createJWT(request);
        
        assertNotNull(jwt);
        
        Date expiresAt = new Date(currentTime + TTL_SECONDS * 1000);
        Claim claim = JWT.decode(jwt).getClaim("exp");
        assertNotNull(claim);
        assertEquals(expiresAt.toString(), claim.asDate().toString());
    }
    
    @Test(expected = CloudAppException.class)
    public void applyAuthorization_ShouldThrowException_OnCreateJWTException() {
        jwtAuthorizer = spy(jwtAuthorizer);
        
        doThrow(new CloudAppException("JWT creation failed"))
                .when(jwtAuthorizer)
                .createJWT(any(HttpRequest.class));
        
        jwtAuthorizer.applyAuthorization(request);
    }
    
    @Test
    public void getVerifyAlgorithm_ValidAlgorithm_ReturnsAlgorithm()
            throws Exception {
        Algorithm mockAlgorithm = mock(Algorithm.class);
        
        Algorithm algorithm = AlgorithmUtil.getVerifyAlgorithm(
                "HS256",
                "secret".getBytes(StandardCharsets.UTF_8)
        );
        assertNotNull(algorithm);
        assertEquals("HS256", algorithm.getName());
    }
    
    @Test
    public void getGenerateAlgorithm_ValidAlgorithm_ReturnsAlgorithm()
            throws Exception {
        Algorithm mockAlgorithm = mock(Algorithm.class);
        
        Algorithm algorithm = AlgorithmUtil.getGenerateAlgorithm(
                "HS256",
                "secret".getBytes(StandardCharsets.UTF_8)
        );
        assertNotNull(algorithm);
        assertEquals("HS256", algorithm.getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getVerifyAlgorithm_UnsupportedAlgorithm_ThrowsException()
            throws Exception {
        AlgorithmUtil.getVerifyAlgorithm("UNSUPPORTED", "secret".getBytes(
                StandardCharsets.UTF_8));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getGenerateAlgorithm_UnsupportedAlgorithm_ThrowsException()
            throws Exception {
        AlgorithmUtil.getGenerateAlgorithm("UNSUPPORTED", "secret".getBytes(
                StandardCharsets.UTF_8));
    }
    
}
