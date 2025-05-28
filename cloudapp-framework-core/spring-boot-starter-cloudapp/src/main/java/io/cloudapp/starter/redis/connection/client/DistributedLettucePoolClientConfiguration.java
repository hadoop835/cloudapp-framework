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

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.resource.ClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.time.Duration;
import java.util.Optional;

public class DistributedLettucePoolClientConfiguration
        implements LettucePoolingClientConfiguration {

    private final GenericObjectPoolConfig poolConfig;

    private final LettuceClientConfiguration clientConfiguration;

    DistributedLettucePoolClientConfiguration(LettuceClientConfiguration config,
                                              GenericObjectPoolConfig poolConfig) {

        this.clientConfiguration = config;
        this.poolConfig = poolConfig;
    }

    public LettuceClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    @Override
    public GenericObjectPoolConfig getPoolConfig() {
        return poolConfig;
    }

    @Override
    public boolean isUseSsl() {
        return clientConfiguration.isUseSsl();
    }

    @Override
    public boolean isVerifyPeer() {
        return clientConfiguration.isVerifyPeer();
    }

    @Override
    public boolean isStartTls() {
        return clientConfiguration.isStartTls();
    }

    @Override
    public Optional<ClientResources> getClientResources() {
        return clientConfiguration.getClientResources();
    }

    @Override
    public Optional<ClientOptions> getClientOptions() {
        return clientConfiguration.getClientOptions();
    }

    @Override
    public Optional<String> getClientName() {
        return clientConfiguration.getClientName();
    }

    @Override
    public Optional<ReadFrom> getReadFrom() {
        return clientConfiguration.getReadFrom();
    }

    @Override
    public Duration getCommandTimeout() {
        return clientConfiguration.getCommandTimeout();
    }

    @Override
    public Duration getShutdownTimeout() {
        return clientConfiguration.getShutdownTimeout();
    }

    @Override
    public Duration getShutdownQuietPeriod() {
        return clientConfiguration.getShutdownQuietPeriod();
    }
}