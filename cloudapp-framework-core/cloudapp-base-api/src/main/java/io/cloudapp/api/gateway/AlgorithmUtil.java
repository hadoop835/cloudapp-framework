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

package io.cloudapp.api.gateway;

import com.auth0.jwt.algorithms.Algorithm;
import io.cloudapp.util.KeyUtil;

public class AlgorithmUtil {
    
    public static Algorithm getVerifyAlgorithm(String algorithm, byte[] secret) throws Exception {
        switch (algorithm) {
            case "HS256":
                return Algorithm.HMAC256(secret);
            case "HS384":
                return Algorithm.HMAC384(secret);
            case "HS512":
                return Algorithm.HMAC512(secret);
            case "RS256":
                return Algorithm.RSA256(KeyUtil.createPublicKey(secret), null);
            case "RS384":
                return Algorithm.RSA384(KeyUtil.createPublicKey(secret), null);
            case "RS512":
                return Algorithm.RSA512(KeyUtil.createPublicKey(secret), null);
            case "ECDSA256":
                return Algorithm.ECDSA256(KeyUtil.createECPublicKey(secret), null);
            case "ECDSA384":
                return Algorithm.ECDSA384(KeyUtil.createECPublicKey(secret), null);
            case "ECDSA512":
                return Algorithm.ECDSA512(KeyUtil.createECPublicKey(secret), null);
            default:
                throw new IllegalArgumentException("Unsupported algorithm");
        }
    }
    
    public static Algorithm getGenerateAlgorithm(String algorithm, byte[] secret) throws Exception {
        switch (algorithm) {
            case "HS256":
                return Algorithm.HMAC256(secret);
            case "HS384":
                return Algorithm.HMAC384(secret);
            case "HS512":
                return Algorithm.HMAC512(secret);
            case "RS256":
                return Algorithm.RSA256(null, KeyUtil.createPrivateKey(secret));
            case "RS384":
                return Algorithm.RSA384(null, KeyUtil.createPrivateKey(secret));
            case "RS512":
                return Algorithm.RSA512(null, KeyUtil.createPrivateKey(secret));
            case "ECDSA256":
                return Algorithm.ECDSA256(null, KeyUtil.createECPrivateKey(secret));
            case "ECDSA384":
                return Algorithm.ECDSA384(null, KeyUtil.createECPrivateKey(secret));
            case "ECDSA512":
                return Algorithm.ECDSA512(null, KeyUtil.createECPrivateKey(secret));
            default:
                throw new IllegalArgumentException("Unsupported algorithm");
        }
    }
    
}
