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

package io.cloudapp.util;

import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtil {

    public static final String ALGORITHM_RSA = "RSA";
    public static final String ALGORITHM_ECDSA = "EC";
    public static final String ALGORITHM_HMAC = "HS";

    public static RSAPublicKey createPublicKey(byte[] publicKey) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);

        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public static RSAPrivateKey createPrivateKey(byte[] privateKe) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKe);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);

        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static ECPublicKey createECPublicKey(byte[] publicKey) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_ECDSA);

        return (ECPublicKey) keyFactory.generatePublic(keySpec);
    }

    public static ECPrivateKey createECPrivateKey(byte[] privateKey) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_ECDSA);

        return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
    }

   
}
