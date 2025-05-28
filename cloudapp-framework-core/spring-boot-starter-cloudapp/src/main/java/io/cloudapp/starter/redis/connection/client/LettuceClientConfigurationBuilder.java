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
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;

import java.time.Duration;

public class LettuceClientConfigurationBuilder {

    private boolean useSsl;

    private boolean verifyPeer;

    private boolean startTls;

    private String clientName;

    private Duration timeout = Duration.ofSeconds(RedisURI.DEFAULT_TIMEOUT);

    private Duration shutdownTimeout = Duration.ofMillis(100);

    private Duration shutdownQuietPeriod;

    private ClientResources clientResources;

    private ClientOptions clientOptions;

    private ReadFrom readFrom;

    LettuceClientConfigurationBuilder() {}

    public static LettuceClientConfigurationBuilder builder() {
        return new LettuceClientConfigurationBuilder();
    }

    public LettuceClientConfigurationBuilder useSsl(boolean useSsl) {
        this.useSsl = useSsl;
        return this;
    }

    public LettuceClientConfigurationBuilder verifyPeer(boolean verifyPeer) {
        this.verifyPeer = verifyPeer;
        return this;
    }

    public LettuceClientConfigurationBuilder startTls(boolean startTls) {
        this.startTls = startTls;
        return this;
    }

    public LettuceClientConfigurationBuilder clientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public LettuceClientConfigurationBuilder timeout(Duration timeout) {
        if(timeout != null) {
            this.timeout = timeout;
        }
        return this;
    }

    public LettuceClientConfigurationBuilder shutdownTimeout(Duration shutdownTimeout) {
        if(shutdownTimeout != null) {
            this.shutdownTimeout = shutdownTimeout;
        }
        return this;
    }

    public LettuceClientConfigurationBuilder shutdownQuietPeriod(Duration shutdownQuietPeriod) {
        this.shutdownQuietPeriod = shutdownQuietPeriod;
        return this;
    }

    public LettuceClientConfigurationBuilder clientResources(ClientResources clientResources) {
        this.clientResources = clientResources;
        return this;
    }

    public LettuceClientConfigurationBuilder clientOptions(ClientOptions clientOptions) {
        this.clientOptions = clientOptions;
        return this;
    }

    public LettuceClientConfigurationBuilder readFrom(ReadFrom readFrom) {
        this.readFrom = readFrom;
        return this;
    }



    public DistributedLettuceClientConfiguration build() {
        return new DistributedLettuceClientConfiguration(
                useSsl,
                verifyPeer,
                startTls,
                clientName,
                timeout,
                shutdownTimeout,
                shutdownQuietPeriod,
                clientOptions,
                clientResources,
                readFrom);
    }
}
