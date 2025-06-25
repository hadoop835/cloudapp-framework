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

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.resource.ClientResources;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;

import java.time.Duration;
import java.util.Optional;

public class DistributedLettuceClientConfiguration implements LettuceClientConfiguration {

    private final boolean useSsl;

    private final boolean verifyPeer;

    private final boolean startTls;

    private final Optional<String> clientName;

    private final Duration timeout;

    private final Duration shutdownTimeout;

    private final Duration shutdownQuietPeriod;

    private final Optional<ClientOptions> clientOptions;

    private final Optional<ClientResources> clientResources;

    private final Optional<ReadFrom> readFrom;

    DistributedLettuceClientConfiguration(boolean useSsl,
                                          boolean verifyPeer,
                                          boolean startTls,
                                          String clientName,
                                          Duration timeout,
                                          Duration shutdownTimeout,
                                          Duration shutdownQuietPeriod,
                                          ClientOptions clientOptions,
                                          ClientResources clientResources,
                                          ReadFrom readFrom) {
        this.useSsl = useSsl;
        this.verifyPeer = verifyPeer;
        this.startTls = startTls;
        this.clientName = Optional.ofNullable(clientName);
        this.timeout = timeout;
        this.shutdownTimeout = shutdownTimeout;
        this.shutdownQuietPeriod = shutdownQuietPeriod;
        this.clientResources = Optional.ofNullable(clientResources);
        this.clientOptions = Optional.ofNullable(clientOptions);
        this.readFrom = Optional.ofNullable(readFrom);
    }

    @Override
    public boolean isUseSsl() {
        return useSsl;
    }

    @Override
    public boolean isVerifyPeer() {
        return verifyPeer;
    }

    @Override
    public boolean isStartTls() {
        return startTls;
    }

    @Override
    public Optional<ClientResources> getClientResources() {
        return clientResources;
    }

    @Override
    public Optional<ClientOptions> getClientOptions() {
        return clientOptions;
    }

    @Override
    public Optional<String> getClientName() {
        return clientName;
    }

    @Override
    public Optional<ReadFrom> getReadFrom() {
        return readFrom;
    }

    @Override
    public Duration getCommandTimeout() {
        return this.timeout;
    }

    public Duration getTimeout() {
        return timeout;
    }

    @Override
    public Duration getShutdownTimeout() {
        return shutdownTimeout;
    }

    @Override
    public Duration getShutdownQuietPeriod() {
        return shutdownQuietPeriod;
    }

}