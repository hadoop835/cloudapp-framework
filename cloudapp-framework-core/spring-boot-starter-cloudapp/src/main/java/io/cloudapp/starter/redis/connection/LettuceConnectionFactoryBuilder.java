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

package io.cloudapp.starter.redis.connection;

import io.cloudapp.starter.redis.RedisConnectionFactoryBuilder;
import io.cloudapp.starter.redis.connection.client.LettuceClientConfigurationBuilder;
import io.cloudapp.starter.redis.connection.client.LettuceClientPoolingConfigurationBuilder;
import io.cloudapp.starter.redis.connection.pool.GenericObjectPoolCreator;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.TimeoutOptions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.StringUtils;

public class LettuceConnectionFactoryBuilder implements RedisConnectionFactoryBuilder {

    public static final Log logger = LogFactory.getLog(LettuceConnectionFactoryBuilder.class);

    private final RedisProperties properties;
    private LettuceConnectionFactory connectionFactory;
    private boolean init = false;

    public LettuceConnectionFactoryBuilder(RedisProperties redisProperties) {
        this.properties = redisProperties;
    }

    @Override
    public void initConnectFactory() {
        this.init = true;
        GenericObjectPoolCreator creator = new GenericObjectPoolCreator(properties.getLettuce().getPool());

        if(StringUtils.hasText(properties.getUrl())) {
            new RedisUrlPropertyResolver(properties.getUrl()).assignment(properties);
        }

        LettuceClientConfiguration clientConfiguration = LettuceClientConfigurationBuilder.builder()
                .clientName(properties.getClientName())
                .useSsl(properties.isSsl())
                .verifyPeer(false)
                .startTls(false)
                .clientOptions(ClientOptions.builder().timeoutOptions(TimeoutOptions.enabled()).build())
                .shutdownQuietPeriod(properties.getLettuce().getShutdownTimeout())
                .shutdownTimeout(properties.getLettuce().getShutdownTimeout())
                .timeout(properties.getTimeout())
                .build();

        if(creator.usePooling()) {
            clientConfiguration = LettuceClientPoolingConfigurationBuilder.builder()
                    .clientConfiguration(clientConfiguration)
                    .poolCreator(creator)
                    .build();
        }

        RedisConfigurationBuilder configurationBuilder = RedisConfigurationBuilder.builder()
                .cluster(properties.getCluster())
                .database(properties.getDatabase())
                .host(properties.getHost())
                .password(properties.getPassword())
                .port(properties.getPort())
                .sentinel(properties.getSentinel())
                .url(properties.getUrl())
                .username(properties.getUsername());

        if(configurationBuilder.isCluster()) {
            this.connectionFactory = new LettuceConnectionFactory(configurationBuilder.buildCluster(),
                    clientConfiguration);
        }

        if(configurationBuilder.isSentinel()) {
            this.connectionFactory = new LettuceConnectionFactory(configurationBuilder.buildSentinel(),
                    clientConfiguration);
        }

        this.connectionFactory = new LettuceConnectionFactory(configurationBuilder.buildStandalone(),
                clientConfiguration);

        logger.info("Lettuce connection factory init complete");

    }

    @Override
    public RedisConnectionFactory getConnectionFactory() {
        return this.connectionFactory;
    }

    @Override
    public void destroy() {
        connectionFactory.destroy();
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("Lettuce connection factory init after properties");

        if( !init ) {
            initConnectFactory();
        }
        connectionFactory.afterPropertiesSet();
    }
}
