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
import io.cloudapp.starter.redis.connection.client.DistributedJedisClientConfiguration;
import io.cloudapp.starter.redis.connection.client.JedisClientConfigurationBuilder;
import io.cloudapp.starter.redis.connection.pool.GenericObjectPoolCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;

public class JedisConnectionFactoryBuilder implements RedisConnectionFactoryBuilder {

    public static final Log logger = LogFactory.getLog(JedisConnectionFactoryBuilder.class);

    private final RedisProperties properties;
    private JedisConnectionFactory connectionFactory;
    private boolean init = false;

    public JedisConnectionFactoryBuilder(RedisProperties redisProperties) {
        this.properties = redisProperties;
    }

    public void initConnectFactory() {
        this.init = true;

        GenericObjectPoolCreator creator = new GenericObjectPoolCreator(properties.getJedis().getPool());

        if (StringUtils.hasText(properties.getUrl())) {
            new RedisUrlPropertyResolver(properties.getUrl()).assignment(properties);
        }

        DistributedJedisClientConfiguration clientConfiguration = JedisClientConfigurationBuilder.builder()
                .poolCreator(creator)
                .clientName(properties.getClientName())
                .connectTimeout(properties.getConnectTimeout())
                .readTimeout(properties.getTimeout())
                .useSsl(properties.isSsl())
                .build();

        RedisConfigurationBuilder configurationBuilder = RedisConfigurationBuilder.builder()
                .cluster(properties.getCluster())
                .database(properties.getDatabase())
                .host(properties.getHost())
                .password(properties.getPassword())
                .port(properties.getPort())
                .sentinel(properties.getSentinel())
                .url(properties.getUrl())
                .username(properties.getUsername());

        if (configurationBuilder.isCluster()) {
            this.connectionFactory = new JedisConnectionFactory(configurationBuilder.buildCluster(),
                    clientConfiguration);
        }

        if (configurationBuilder.isSentinel()) {
            this.connectionFactory = new JedisConnectionFactory(configurationBuilder.buildSentinel(),
                    clientConfiguration);
        }

        this.connectionFactory = new JedisConnectionFactory(configurationBuilder.buildStandalone(),
                clientConfiguration);

        logger.info("Jedis connection factory init complete");
    }

    @Override
    public RedisConnectionFactory getConnectionFactory() {
        if(logger.isTraceEnabled()) {
            logger.trace("Redis connection factory get connection factory");
        }

        return connectionFactory;
    }

    public boolean isInit() {
        return this.init;
    }

    @Override
    public void destroy() {
        connectionFactory.destroy();
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("Redis connection factory init after properties set");

        if (!init) {
            initConnectFactory();
        }

        connectionFactory.afterPropertiesSet();
    }
}
