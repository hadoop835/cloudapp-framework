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
package io.cloudapp.messaging.redis.demo.intercepter;

import io.cloudapp.api.cache.interceptors.EncryptInterceptor;
import io.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.PrivateKey;

public class RsaEncryptInterceptor extends EncryptInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RsaEncryptInterceptor.class);

    private final PrivateKey privateKey;

    public RsaEncryptInterceptor(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public byte[] intercept(byte[] param) throws CloudAppException {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(param);
        } catch (Exception e) {
            logger.error("encrypt error, error:{}", e.getMessage());
            return param;
        }
    }

}
