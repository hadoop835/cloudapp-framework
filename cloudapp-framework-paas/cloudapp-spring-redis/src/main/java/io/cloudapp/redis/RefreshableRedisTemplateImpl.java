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
package io.cloudapp.redis;

import io.cloudapp.api.cache.RefreshableRedisTemplate;
import io.cloudapp.api.cache.interceptors.CacheDiskInterceptor;
import io.cloudapp.api.cache.interceptors.DecryptInterceptor;
import io.cloudapp.api.cache.interceptors.EncryptInterceptor;
import io.cloudapp.api.cache.interceptors.MonitoringInterceptor;
import io.cloudapp.exeption.CloudAppInvalidAccessException;
import io.cloudapp.redis.handler.RedisCallBackProxy;
import io.cloudapp.redis.handler.InterceptorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

import java.io.Closeable;
import java.util.List;

public class RefreshableRedisTemplateImpl<K, V> extends RefreshableRedisTemplate<K, V>
        implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RefreshableRedisTemplateImpl.class);

    /**
     * Cache disk interceptor
     */
    private CacheDiskInterceptor cacheDiskInterceptor;
    /**
     * Decrypt interceptor
     */
    private DecryptInterceptor decryptInterceptor;
    /**
     * Encrypt interceptor
     */
    private EncryptInterceptor encryptInterceptor;
    /**
     * Monitoring interceptor
     */
    private MonitoringInterceptor monitoringInterceptor;

    /**
     * Interceptor handler for key,value
     */
    private InterceptorHandler<K> keyInterceptorHandler;

    private InterceptorHandler<V> valueInterceptorHandler;

    private InterceptorHandler<V> hashValueInterceptorHandler;

    private InterceptorHandler<K> hashKeyInterceptorHandler;

    private boolean initialized;

    private boolean refreshed = true;

    // mark as encrypting and decrypting hash values
    private boolean usedHash = false;

    // mark as encrypt key
    private boolean keyEncrypt = true;

    // mark as monitoring value
    private boolean valueMonitoring = false;

    //mark as monitoring hash key
    private boolean hashMonitoring = false;

    public RefreshableRedisTemplateImpl() {
        super();
    }

    public RefreshableRedisTemplateImpl(RedisTemplate<K, V> template) {
        super();

        this.valueInterceptorHandler = new InterceptorHandler(
                template.getValueSerializer());
        this.keyInterceptorHandler = new InterceptorHandler(
                template.getKeySerializer());
        this.hashValueInterceptorHandler = new InterceptorHandler(
                template.getHashValueSerializer());
        this.hashKeyInterceptorHandler = new InterceptorHandler(
                template.getHashValueSerializer());

        setDefaultSerializer(template.getValueSerializer());

        super.setKeySerializer(keyInterceptorHandler);
        super.setValueSerializer(valueInterceptorHandler);
        super.setHashKeySerializer(hashKeyInterceptorHandler);
        super.setValueSerializer(hashValueInterceptorHandler);

        if (template.getConnectionFactory() != null) {
            this.setConnectionFactory(template.getConnectionFactory());
        }

    }

    @Override
    public void postStart() {
        logger.info("Starting Redis producer");
        if(!refreshed) {
            throw new CloudAppInvalidAccessException("redis connection is not" +
                                                             " ready.");
        }

        freshInterceptor();

        initialized = true;
    }

    @Override
    public void preStop() {
        logger.info("Stopping Redis producer");
        // mark as not initialized
        initialized = false;
        // release current connection factory
        RedisConnectionUtils.unbindConnection(getRequiredConnectionFactory());
        //reset mark
        refreshed = false;
    }

    @Override
    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        super.setConnectionFactory(connectionFactory);
        refreshed = true;
    }

    @Nullable
    public <T> T execute(RedisCallback<T> action,
                         boolean exposeConnection,
                         boolean pipeline) {

        if(!initialized) {
            throw new CloudAppInvalidAccessException(
                    "template not initialized; call afterPropertiesSet() before using it");
        }

        RedisCallBackProxy<T> redisCallBackProxy = new RedisCallBackProxy<>(
                action, cacheDiskInterceptor);

        return super.execute(redisCallBackProxy, exposeConnection, pipeline);
    }

    @Override
    public <T> T execute(SessionCallback<T> session) {

        if(!initialized) {
            throw new CloudAppInvalidAccessException(
                    "template not initialized; call afterPropertiesSet() before using it");
        }
        
        return super.execute(session);
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> session,
                                         @Nullable
                                         RedisSerializer<?> resultSerializer) {

        if(!initialized) {
            throw new CloudAppInvalidAccessException(
                    "template not initialized; call afterPropertiesSet() before using it");
        }
        
        return super.executePipelined(session, resultSerializer);
    }


    @Override
    public List<Object> executePipelined(RedisCallback<?> action, @Nullable RedisSerializer<?> resultSerializer) {
        if(!initialized) {
            throw new CloudAppInvalidAccessException(
                    "template not initialized; call afterPropertiesSet() before using it");
        }
        return super.executePipelined(action, resultSerializer);
    }

    @Override
    public <T extends Closeable> T executeWithStickyConnection(RedisCallback<T> callback) {

        if(!initialized) {
            throw new CloudAppInvalidAccessException(
                    "template not initialized; call afterPropertiesSet() before using it");
        }
        
        RedisCallBackProxy<T> redisCallBackProxy = new RedisCallBackProxy<>(
                callback, cacheDiskInterceptor);

        return super.executeWithStickyConnection(redisCallBackProxy);
    }

    @Override
    public void setKeySerializer(RedisSerializer serializer) {
        if(this.keyInterceptorHandler == null) {
            this.keyInterceptorHandler = new InterceptorHandler(serializer);
            super.setKeySerializer(keyInterceptorHandler);
        } else {
            this.keyInterceptorHandler.setDelegate(serializer);
        }
    }

    @Override
    public void setValueSerializer(RedisSerializer serializer) {
        if(this.valueInterceptorHandler == null) {
            this.valueInterceptorHandler = new InterceptorHandler(serializer);
            super.setValueSerializer(valueInterceptorHandler);
        } else {
            this.valueInterceptorHandler.setDelegate(serializer);
        }
    }

    @Override
    public void setHashKeySerializer(RedisSerializer hashKeySerializer) {
        if(this.hashKeyInterceptorHandler == null) {
            this.hashKeyInterceptorHandler = new InterceptorHandler(hashKeySerializer);
            super.setHashKeySerializer(hashKeyInterceptorHandler);
        } else {
            this.hashKeyInterceptorHandler.setDelegate(hashKeySerializer);
        }
    }

    @Override
    public void setHashValueSerializer(RedisSerializer hashValueSerializer) {
        if(this.hashValueInterceptorHandler == null) {
            this.hashValueInterceptorHandler = new InterceptorHandler(hashValueSerializer);
            super.setHashValueSerializer(hashValueInterceptorHandler);
        } else {
            this.hashValueInterceptorHandler.setDelegate(hashValueSerializer);
        }
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        if (this.valueInterceptorHandler == null) {
            this.valueInterceptorHandler = new InterceptorHandler(getValueSerializer());
            super.setValueSerializer(valueInterceptorHandler);
        }

        if (this.keyInterceptorHandler == null) {
            this.keyInterceptorHandler = new InterceptorHandler(getKeySerializer());
            super.setKeySerializer(keyInterceptorHandler);
        }

        if (this.hashValueInterceptorHandler == null) {
            this.hashValueInterceptorHandler = new InterceptorHandler(getHashValueSerializer());
            super.setHashValueSerializer(hashValueInterceptorHandler);
        }

        if (this.hashKeyInterceptorHandler == null) {
            this.hashKeyInterceptorHandler = new InterceptorHandler(getHashValueSerializer());
            super.setHashKeySerializer(hashKeyInterceptorHandler);
        }

        freshInterceptor();

        initialized = true;
    }

    public void setEncryptInterceptor(EncryptInterceptor encryptInterceptor) {
        this.encryptInterceptor = encryptInterceptor;
    }

    public void setDecryptInterceptor(DecryptInterceptor decryptInterceptor) {
        this.decryptInterceptor = decryptInterceptor;
    }

    public void setMonitoringInterceptor(MonitoringInterceptor monitoringInterceptor) {
        this.monitoringInterceptor = monitoringInterceptor;
    }

    public void setCacheDiskInterceptor(CacheDiskInterceptor cacheDiskInterceptor) {
        this.cacheDiskInterceptor = cacheDiskInterceptor;
        if(cacheDiskInterceptor.getDelegate() == null) {
            cacheDiskInterceptor.setDelegate(this);
        }
    }

    public CacheDiskInterceptor getCacheDiskInterceptor() {
        return cacheDiskInterceptor;
    }

    public boolean isUsedHash() {
        return usedHash;
    }

    public void setUsedHash(boolean usedHash) {
        this.usedHash = usedHash;
    }

    public boolean isKeyEncrypt() {
        return keyEncrypt;
    }

    public void setKeyEncrypt(boolean keyEncrypt) {
        this.keyEncrypt = keyEncrypt;
    }

    public boolean isValueMonitoring() {
        return valueMonitoring;
    }

    public void setValueMonitoring(boolean valueMonitoring) {
        this.valueMonitoring = valueMonitoring;
    }

    public boolean isHashMonitoring() {
        return hashMonitoring;
    }

    public void setHashMonitoring(boolean hashMonitoring) {
        this.hashMonitoring = hashMonitoring;
    }

    /**
     * fresh interceptor
     */
    void freshInterceptor() {
        this.valueInterceptorHandler.setEncryptInterceptor(encryptInterceptor);
        this.valueInterceptorHandler.setDecryptInterceptor(decryptInterceptor);
        this.keyInterceptorHandler.setMonitoringInterceptor(monitoringInterceptor);

        if (valueMonitoring) {
            this.valueInterceptorHandler.setMonitoringInterceptor(monitoringInterceptor);
        }

        if(keyEncrypt) {
            this.keyInterceptorHandler.setEncryptInterceptor(encryptInterceptor);
            this.keyInterceptorHandler.setDecryptInterceptor(decryptInterceptor);
        }

        if(usedHash) {
            this.hashValueInterceptorHandler.setEncryptInterceptor(encryptInterceptor);
            this.hashValueInterceptorHandler.setDecryptInterceptor(decryptInterceptor);
        }

        if(usedHash && keyEncrypt) {
            this.hashKeyInterceptorHandler.setEncryptInterceptor(encryptInterceptor);
            this.hashKeyInterceptorHandler.setDecryptInterceptor(decryptInterceptor);
        }

        if (hashMonitoring) {
            this.hashKeyInterceptorHandler.setMonitoringInterceptor(monitoringInterceptor);
        }

        if (hashMonitoring && valueMonitoring) {
            this.hashValueInterceptorHandler.setMonitoringInterceptor(monitoringInterceptor);
        }
    }

    public boolean isInitialized() {
        return initialized;
    }
}
