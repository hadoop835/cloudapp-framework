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

package com.alibaba.cloudapp.starter.redis.connection.client;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.time.Duration;
import java.util.Optional;

public class DistributedJedisClientConfiguration implements JedisClientConfiguration {

    private final boolean useSsl;

    private final boolean usePooling;

    private final Optional<GenericObjectPoolConfig> poolConfig;

    private final Optional<String> clientName;

    private final Duration readTimeout;

    private final Duration connectTimeout;

    private final Optional<SSLParameters> sslParameters;

    private final Optional<SSLSocketFactory> sslSocketFactory;

    private final Optional<HostnameVerifier> hostnameVerifier;

    DistributedJedisClientConfiguration(boolean useSsl,
                                        boolean usePooling,
                                        GenericObjectPoolConfig poolConfig,
                                        String clientName,
                                        Duration readTimeout,
                                        Duration connectTimeout,
                                        SSLParameters sslParameters,
                                        SSLSocketFactory sslSocketFactory,
                                        HostnameVerifier hostnameVerifier) {
        this.useSsl = useSsl;
        this.usePooling = usePooling;
        this.poolConfig = Optional.ofNullable(poolConfig);
        this.clientName = Optional.ofNullable(clientName);
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
        this.sslParameters = Optional.ofNullable(sslParameters);
        this.sslSocketFactory = Optional.ofNullable(sslSocketFactory);
        this.hostnameVerifier = Optional.ofNullable(hostnameVerifier);
    }

    @Override
    public boolean isUseSsl() {
        return useSsl;
    }

    @Override
    public Optional<SSLSocketFactory> getSslSocketFactory() {
        return sslSocketFactory;
    }

    @Override
    public Optional<SSLParameters> getSslParameters() {
        return sslParameters;
    }

    @Override
    public Optional<HostnameVerifier> getHostnameVerifier() {
        return hostnameVerifier;
    }

    @Override
    public boolean isUsePooling() {
        return usePooling;
    }

    @Override
    public Optional<GenericObjectPoolConfig> getPoolConfig() {
        return poolConfig;
    }

    @Override
    public Optional<String> getClientName() {
        return clientName;
    }

    @Override
    public Duration getReadTimeout() {
        return readTimeout;
    }

    @Override
    public Duration getConnectTimeout() {
        return connectTimeout;
    }


}