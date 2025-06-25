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

import com.alibaba.cloudapp.apigateway.aliyun.ApiGatewayConstant;

public class JwtProperties {

    private String keyId;

    private String secret;

    private String issuer;

    private String subject;

    private String audience;

    private long expiredSecond;

    private boolean base64EncodeSecret = false;

    private String algorithm = ApiGatewayConstant.JWT_ALGORITHM_HS256;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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

    public long getExpiredSecond() {
        return expiredSecond;
    }

    public void setExpiredSecond(long expiredSecond) {
        this.expiredSecond = expiredSecond;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public boolean isBase64EncodeSecret() {
        return base64EncodeSecret;
    }

    public void setBase64EncodeSecret(boolean base64EncodeSecret) {
        this.base64EncodeSecret = base64EncodeSecret;
    }
}
