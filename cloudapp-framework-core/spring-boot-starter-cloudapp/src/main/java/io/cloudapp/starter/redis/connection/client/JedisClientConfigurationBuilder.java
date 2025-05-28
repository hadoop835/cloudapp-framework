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

package io.cloudapp.starter.redis.connection.client;

import io.cloudapp.starter.redis.connection.pool.GenericObjectPoolCreator;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import redis.clients.jedis.Protocol;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.time.Duration;

public class JedisClientConfigurationBuilder {

    private boolean useSsl;

    private String clientName;

    private Duration readTimeout = Duration.ofMillis(Protocol.DEFAULT_TIMEOUT);

    private Duration connectTimeout = Duration.ofMillis(Protocol.DEFAULT_TIMEOUT);

    private HostnameVerifier hostnameVerifier;

    private SSLSocketFactory sslSocketFactory;

    private SSLParameters sslParameters;

    private GenericObjectPoolCreator poolCreator;

    JedisClientConfigurationBuilder() {}

    public static JedisClientConfigurationBuilder builder() {
        return new JedisClientConfigurationBuilder();
    }

    public JedisClientConfigurationBuilder poolCreator(GenericObjectPoolCreator creator) {
        this.poolCreator = creator;
        return this;
    }
    public JedisClientConfigurationBuilder useSsl(boolean useSsl) {
        this.useSsl = useSsl;
        return this;
    }

    public JedisClientConfigurationBuilder clientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public JedisClientConfigurationBuilder readTimeout(Duration readTimeout) {
        if(readTimeout != null) {
            this.readTimeout = readTimeout;
        }
        return this;
    }

    public JedisClientConfigurationBuilder connectTimeout(Duration connectTimeout) {
        if(connectTimeout != null) {
            this.connectTimeout = connectTimeout;
        }
        return this;
    }

    public JedisClientConfigurationBuilder hostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    public JedisClientConfigurationBuilder sslParameters(SSLParameters sslParameters) {
        this.sslParameters = sslParameters;
        return this;
    }

    public JedisClientConfigurationBuilder SSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    public DistributedJedisClientConfiguration build() {
        if(poolCreator == null) {
            poolCreator = new GenericObjectPoolCreator(new RedisProperties.Pool());
        }

        return new DistributedJedisClientConfiguration(useSsl,
                poolCreator.usePooling(),
                poolCreator.create(),
                clientName,
                readTimeout,
                connectTimeout,
                sslParameters,
                sslSocketFactory,
                hostnameVerifier);
    }
}
