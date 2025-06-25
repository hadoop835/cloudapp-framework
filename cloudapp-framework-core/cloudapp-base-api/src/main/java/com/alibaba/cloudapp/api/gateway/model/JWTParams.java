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

package com.alibaba.cloudapp.api.gateway.model;

import com.alibaba.cloudapp.model.BaseModel;

public class JWTParams extends BaseModel {

    private String secretKey;

    private String algorithm;

    private String issuer;

    private String subject;

    private String audience;

    private long ttlSeconds;

    private boolean base64EncodeSecret;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public long getTtlSeconds() {
        return ttlSeconds;
    }

    public void setTtlSeconds(long ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }

    public boolean isBase64EncodeSecret() {
        return base64EncodeSecret;
    }

    public void setBase64EncodeSecret(boolean base64EncodeSecret) {
        this.base64EncodeSecret = base64EncodeSecret;
    }

    public static class Builder extends
            BaseModel.Builder<JWTParams.Builder, JWTParams> {


        public JWTParams.Builder secretKey(String value) {
            operations.add(param -> param.setSecretKey(value));
            return this;
        }

        public JWTParams.Builder algorithm(String value) {
            operations.add(param -> param.setAlgorithm(value));
            return this;
        }

        public JWTParams.Builder audience(String value) {
            operations.add(param -> param.setAudience(value));
            return this;
        }

        public JWTParams.Builder issuer(String value) {
            operations.add(param -> param.setIssuer(value));
            return this;
        }

        public JWTParams.Builder ttlSeconds(long value) {
            operations.add(param -> param.setTtlSeconds(value));
            return this;
        }

        public JWTParams.Builder base64EncodeSecret(boolean value) {
            operations.add(param -> param.setBase64EncodeSecret(value));
            return this;
        }

        public JWTParams.Builder subject(String value) {
            operations.add(param -> param.setSubject(value));
            return this;
        }



        @Override
        protected void validate(JWTParams args) {

        }
    }

    public static JWTParams.Builder builder() {
        return new JWTParams.Builder();
    }
}
