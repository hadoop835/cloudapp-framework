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
package com.alibaba.cloudapp.messaging.redis.demo.configuration;

import com.alibaba.cloudapp.api.cache.interceptors.CacheDiskInterceptor;
import com.alibaba.cloudapp.api.cache.interceptors.DecryptInterceptor;
import com.alibaba.cloudapp.api.cache.interceptors.EncryptInterceptor;
import com.alibaba.cloudapp.messaging.redis.demo.KeyUtil;
import com.alibaba.cloudapp.messaging.redis.demo.intercepter.BigKeyMonitorInterceptor;
import com.alibaba.cloudapp.messaging.redis.demo.intercepter.LocalCacheDiskInterceptor;
import com.alibaba.cloudapp.messaging.redis.demo.intercepter.RsaDecryptInterceptor;
import com.alibaba.cloudapp.messaging.redis.demo.intercepter.RsaEncryptInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisDemoConfiguration {

    @Bean
    public DecryptInterceptor customDecryptInterceptor() {
        return new RsaDecryptInterceptor(KeyUtil.getPublicKey());
    }

    @Bean
    public EncryptInterceptor customEncryptInterceptor() {
        return new RsaEncryptInterceptor(KeyUtil.getPrivateKey());
    }

    @Bean
    public CacheDiskInterceptor customDecryptInterceptor2() {
        return new LocalCacheDiskInterceptor();
    }

    @Bean
    public BigKeyMonitorInterceptor bigKeyMonitorInterceptor() {
        return new BigKeyMonitorInterceptor();
    }

}
