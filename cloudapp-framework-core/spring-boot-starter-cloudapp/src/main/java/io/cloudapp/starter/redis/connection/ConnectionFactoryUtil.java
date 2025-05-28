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

import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.starter.redis.RedisConnectionFactoryBuilder;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

public class ConnectionFactoryUtil {

    public static RedisConnectionFactoryBuilder getRedisConnectionFactoryBuilder(
            RedisProperties properties) {
        boolean isJedisClient = hasClass(
                "org.springframework.data.redis.connection.jedis.JedisConnection")
                && hasClass("redis.clients.jedis.Jedis")
                && hasClass("org.apache.commons.pool2.impl.GenericObjectPool");

        boolean isLettuceClient = hasClass("io.lettuce.core.RedisClient");

        if(!isJedisClient && !isLettuceClient) {
            throw new CloudAppException("Redis client class is not exist");
        }

        if(properties.getClientType() == null) {
            return isJedisClient ? new JedisConnectionFactoryBuilder(properties)
                    : new LettuceConnectionFactoryBuilder(properties);
        }

        switch (properties.getClientType()) {
            case JEDIS:
                if(!isJedisClient) {
                    throw new CloudAppException("Jedis client class is not exist");
                }

                return new JedisConnectionFactoryBuilder(properties);
            case LETTUCE:
                if(!isLettuceClient) {
                    throw new CloudAppException("Lettuce client class is not exist");
                }
                return new LettuceConnectionFactoryBuilder(properties);
            default:
                throw new CloudAppException("Invalid redis client type");
        }
    }

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
