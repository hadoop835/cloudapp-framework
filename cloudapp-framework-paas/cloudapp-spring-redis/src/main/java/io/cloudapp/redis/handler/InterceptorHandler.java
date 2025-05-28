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
package io.cloudapp.redis.handler;

import io.cloudapp.api.cache.interceptors.DecryptInterceptor;
import io.cloudapp.api.cache.interceptors.EncryptInterceptor;
import io.cloudapp.api.cache.interceptors.MonitoringInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class InterceptorHandler<T> implements RedisSerializer<T> {
    
    private static final Logger log =
            LoggerFactory.getLogger(InterceptorHandler.class);
    
    private EncryptInterceptor encryptInterceptor;
    
    private DecryptInterceptor decryptInterceptor;
    
    private MonitoringInterceptor<T> monitoringInterceptor;
    
    private RedisSerializer<T> delegate;
    
    public InterceptorHandler(RedisSerializer<T> delegate) {
        this.delegate = delegate;
    }
    
    public InterceptorHandler(
            RedisSerializer<T> delegate,
            EncryptInterceptor encryptInterceptor,
            DecryptInterceptor decryptInterceptor,
            MonitoringInterceptor<T> monitoringInterceptor
    ) {
        this.delegate = delegate;
        this.encryptInterceptor = encryptInterceptor;
        this.decryptInterceptor = decryptInterceptor;
        this.monitoringInterceptor = monitoringInterceptor;
    }
    
    @Override
    public byte[] serialize(T t) throws SerializationException {
        byte[] result = delegate.serialize(t);
        //Handle monitoring
        boolean needMonitoring = monitoringInterceptor != null &&
                monitoringInterceptor.needMonitoring(result);
        
        if (needMonitoring) {
            monitoringInterceptor.intercept(t);
        }
        
        //Handle encryption
        if (encryptInterceptor != null) {
            result = encryptInterceptor.intercept(result);
        }
        
        return result;
    }
    
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        //Handle decryption
        try {
            if (decryptInterceptor != null) {
                bytes = decryptInterceptor.intercept(bytes);
            }
        } catch (Exception e) {
            log.error("decrypt error, error:{}", e.getMessage());
        }
        
        return delegate.deserialize(bytes);
    }
    
    public EncryptInterceptor getEncryptInterceptor() {
        return encryptInterceptor;
    }
    
    public void setEncryptInterceptor(EncryptInterceptor encryptInterceptor) {
        this.encryptInterceptor = encryptInterceptor;
    }
    
    public DecryptInterceptor getDecryptInterceptor() {
        return decryptInterceptor;
    }
    
    public void setDecryptInterceptor(DecryptInterceptor decryptInterceptor) {
        this.decryptInterceptor = decryptInterceptor;
    }
    
    public MonitoringInterceptor<T> getMonitoringInterceptor() {
        return monitoringInterceptor;
    }
    
    public void setMonitoringInterceptor(
            MonitoringInterceptor<T> monitoringInterceptor) {
        this.monitoringInterceptor = monitoringInterceptor;
    }
    
    public RedisSerializer<T> getDelegate() {
        return delegate;
    }
    
    public void setDelegate(RedisSerializer<T> delegate) {
        this.delegate = delegate;
    }
    
}
