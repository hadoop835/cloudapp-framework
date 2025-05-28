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
package io.cloudapp.starter.redis.configuration;

import io.cloudapp.api.cache.interceptors.CacheDiskInterceptor;
import io.cloudapp.api.cache.interceptors.DecryptInterceptor;
import io.cloudapp.api.cache.interceptors.EncryptInterceptor;
import io.cloudapp.api.cache.interceptors.MonitoringInterceptor;
import io.cloudapp.exeption.CloudAppInvalidAccessException;
import io.cloudapp.redis.RefreshableRedisTemplateImpl;
import io.cloudapp.redis.interceptor.NoOpCacheDiskInterceptor;
import io.cloudapp.redis.interceptor.NoOpDecryptInterceptor;
import io.cloudapp.redis.interceptor.NoOpEncryptInterceptor;
import io.cloudapp.redis.interceptor.NoOpMonitorInterceptor;
import io.cloudapp.starter.properties.EnableModuleProperties;
import io.cloudapp.starter.redis.properties.CloudAppRedisProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@AutoConfiguration
@ConditionalOnClass(RefreshableRedisTemplateImpl.class)
@EnableModuleProperties({CloudAppRedisProperties.class})
@ConditionalOnProperty(prefix = "io.cloudapp.redis",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true )
@Import(CloudAppRedisConnectionFactoryBuilder.class)
public class CloudAppRedisConfiguration {

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

    @Bean
    @ConditionalOnMissingBean
    public EncryptInterceptor encryptInterceptor() {
        return new NoOpEncryptInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public DecryptInterceptor decryptInterceptor() {
        return new NoOpDecryptInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public MonitoringInterceptor<?> monitoringInterceptor() {
        return new NoOpMonitorInterceptor<>();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheDiskInterceptor cacheDiskInterceptor() {
        return new NoOpCacheDiskInterceptor();
    }

    @Bean("refreshableRedisTemplate")
    @ConditionalOnMissingBean(name = "refreshableRedisTemplate")
    @DependsOn("redisConnectionFactory")
    public RefreshableRedisTemplateImpl<?,?> refreshableRedisTemplate(
            @Qualifier("redisConnectionFactory") RedisConnectionFactory factory,
            CloudAppRedisProperties properties,
            EncryptInterceptor encryptInterceptor,
            DecryptInterceptor decryptInterceptor,
            MonitoringInterceptor<?> monitoringInterceptor,
            CacheDiskInterceptor cacheDiskInterceptor,
            @Qualifier("keySerializer") RedisSerializer<?> keySerializer,
            @Qualifier("valueSerializer") RedisSerializer<?> valueSerializer,
            @Qualifier("hashKeySerializer") RedisSerializer<?> hashKeySerializer,
            @Qualifier("hashValueSerializer") RedisSerializer<?> hashValueSerializer
    ) throws CloudAppInvalidAccessException {

        RefreshableRedisTemplateImpl<?, ?> template =
                new RefreshableRedisTemplateImpl<>();

        template.setConnectionFactory(factory);

        template.setDecryptInterceptor(decryptInterceptor);
        template.setEncryptInterceptor(encryptInterceptor);
        template.setMonitoringInterceptor(monitoringInterceptor);
        template.setCacheDiskInterceptor(cacheDiskInterceptor);

        template.setKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(hashKeySerializer);
        template.setHashValueSerializer(hashValueSerializer);

        template.setHashMonitoring(properties.isHashMonitoring());
        template.setUsedHash(properties.isUsedHash());
        template.setValueMonitoring(properties.isValueMonitoring());
        template.setKeyEncrypt(properties.isKeyEncrypt());

        return template;
    }

}
