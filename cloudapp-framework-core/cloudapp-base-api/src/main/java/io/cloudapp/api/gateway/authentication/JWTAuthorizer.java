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
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.net.HttpHeaders;
import io.cloudapp.api.gateway.AlgorithmUtil;
import io.cloudapp.api.gateway.model.JWTParams;
import io.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class JWTAuthorizer implements Authorizer {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthorizer.class);

    private static final int REQUEST_DEADLINE_SECONDS = 10;

    private String jwtToken;

    private final Algorithm algorithm;
    private final String subject;
    private final String issuer;
    private final String audience;
    private final long ttlSeconds;
    private static ClaimsGenerator claimsGenerator = new DefaulClaimsGenerator();

    public JWTAuthorizer(JWTParams params) throws CloudAppException {
        try {
            byte[] secretKeyBytes = params.getSecretKey().getBytes();
            if(params.isBase64EncodeSecret()) {
                secretKeyBytes = Base64.getEncoder().encode(secretKeyBytes);
            }
            this.algorithm =
                    AlgorithmUtil.getGenerateAlgorithm(params.getAlgorithm(),
                                                       secretKeyBytes);
            this.subject = params.getSubject();
            this.audience = params.getAudience();
            this.issuer = params.getIssuer();
            this.ttlSeconds = params.getTtlSeconds();
        } catch (Exception e) {
            throw new CloudAppException("Failed to create algorithm",
                    "CloudApp.BadAlgorithm", e);
        }
    }


    @Override
    public void applyAuthorization(HttpRequest request) throws CloudAppException {
        if (isValid(jwtToken)) {
            logger.warn("JWT token is valid");
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, jwtToken);
            return;
        }
        jwtToken = createJWT(request);

        request.getHeaders().add(HttpHeaders.AUTHORIZATION, jwtToken);
    }

    private boolean isValid(String jwtToken) {
        if (!StringUtils.hasText(jwtToken)) {
            return false;
        }
        //decode token
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withSubject(StringUtils.hasText(subject) ? subject : "")
                .withIssuer(StringUtils.hasText(issuer) ? issuer : "")
                .build().verify(jwtToken);

        return decodedJWT.getExpiresAtAsInstant().plusSeconds(REQUEST_DEADLINE_SECONDS).isAfter(Instant.now());
    }

    /**
     * Specify a user-defined claim generator to implement a custom claim
     *
     * @param claimsGenerator claim generator
     */
    public void setClaimsGenerator(ClaimsGenerator claimsGenerator) {
        JWTAuthorizer.claimsGenerator = claimsGenerator;
    }

    public String createJWT(HttpRequest request) {
        long time = System.currentTimeMillis();

        JWTCreator.Builder builder = JWT.create().withIssuedAt(new Date(time));

        Map<String, String> claims = claimsGenerator.generateClaims(request);
        if (claims != null) {
            claims.forEach(builder::withClaim);
        }

        builder.withClaim("sub", StringUtils.hasText(subject) ? subject : "");
        builder.withClaim("iss", StringUtils.hasText(issuer) ? issuer : "");

        if (StringUtils.hasText(audience)) {
            builder.withAudience(audience);
        }

        if (ttlSeconds > 0) {
            builder.withIssuedAt(new Date(time));
            builder.withExpiresAt(new Date(time + ttlSeconds * 1000));
        }

        return builder.sign(algorithm);
    }

    public static ClaimsGenerator getClaimsGenerator() {
        return claimsGenerator;
    }

    public interface ClaimsGenerator {
        Map<String, String> generateClaims(HttpRequest request);
    }

    public static class DefaulClaimsGenerator implements ClaimsGenerator {
        @Override
        public Map<String, String> generateClaims(HttpRequest request) {
            return Collections.emptyMap();
        }
    }
    
}
