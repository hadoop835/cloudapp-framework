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

package com.alibaba.cloudapp.starter.sequence.configuration;

import com.alibaba.cloudapp.sequence.Constants;
import com.alibaba.cloudapp.sequence.service.RedisSequenceGenerator;
import com.alibaba.cloudapp.starter.sequence.properties.CloudAppSequenceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@AutoConfiguration
@ConditionalOnClass({RedisOperations.class, RedisSequenceGenerator.class})
@EnableConfigurationProperties(CloudAppSequenceProperties.class)
@Import({SequenceConnectFactoryConfiguration.class})
@ConditionalOnProperty(
        prefix = Constants.SEQUENCE_CONFIG_REDIS_ROOT,
        name = "enabled",
        havingValue = "true"
)
public class RedisSequenceAutoConfiguration {

    @Bean("keySerializer")
    @ConditionalOnMissingBean(name = "keySerializer")
    public StringRedisSerializer keySerializer() {
        return new StringRedisSerializer();
    }

    @Bean("valueSerializer")
    @ConditionalOnMissingBean(name = "valueSerializer")
    public JdkSerializationRedisSerializer valueSerializer() {
        return new JdkSerializationRedisSerializer();
    }

    @Bean("hashKeySerializer")
    @ConditionalOnMissingBean(name = "hashKeySerializer")
    public StringRedisSerializer hashKeySerializer() {
        return new StringRedisSerializer();
    }

    @Bean("hashValueSerializer")
    @ConditionalOnMissingBean(name = "hashValueSerializer")
    public JdkSerializationRedisSerializer hashValueSerializer() {
        return new JdkSerializationRedisSerializer();
    }

    @Bean("sequenceRedisTemplate")
    @ConditionalOnMissingBean(RedisTemplate.class)
    @ConditionalOnBean(value = RedisConnectionFactory.class, name = "sequenceRedisConnectionFactory")
    public RedisTemplate<?, ?> getRedisTemplate(
            @Qualifier("sequenceRedisConnectionFactory")
            RedisConnectionFactory factory,
            @Qualifier("keySerializer") RedisSerializer<?> keySerializer,
            @Qualifier("valueSerializer") RedisSerializer<?> valueSerializer,
            @Qualifier("hashKeySerializer") RedisSerializer<?> hashKeySerializer,
            @Qualifier("hashValueSerializer") RedisSerializer<?> hashValueSerializer
    ) {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(hashKeySerializer);
        redisTemplate.setHashValueSerializer(hashValueSerializer);

        return redisTemplate;
    }

    @Bean("redisSequence")
    @ConditionalOnMissingBean(RedisSequenceGenerator.class)
    public RedisSequenceGenerator getRedisSequence(
            CloudAppSequenceProperties properties,
            @Qualifier("sequenceRedisTemplate") RedisTemplate<Object, Object> redisTemplate
    ) {

        return new RedisSequenceGenerator(redisTemplate,
                properties.getRedis().getSequenceName(),
                properties.getRedis().getStep());
    }
}
