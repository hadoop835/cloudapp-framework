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

import io.cloudapp.api.cache.interceptors.CacheDiskInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Redis callback proxy class, to handle cache.
 *
 * @param <T> return type
 */
public class RedisCallBackProxy<T> implements RedisCallback<T> {
    
    private static final Logger logger = LoggerFactory.getLogger(
            RedisCallBackProxy.class);
    
    private RedisCallback<T> callback;
    
    private CacheDiskInterceptor cacheDiskInterceptor;
    
    public RedisCallBackProxy(RedisCallback<T> callback) {
        this.callback = callback;
    }
    
    public RedisCallBackProxy(CacheDiskInterceptor cacheDiskInterceptor) {
        this.cacheDiskInterceptor = cacheDiskInterceptor;
    }
    
    public RedisCallBackProxy(RedisCallback<T> callback,
                              CacheDiskInterceptor cacheDiskInterceptor) {
        this.callback = callback;
        this.cacheDiskInterceptor = cacheDiskInterceptor;
    }
    
    // doInRedis
    @Override
    public T doInRedis(RedisConnection connection) throws DataAccessException {
        if (callback == null) {
            throw new RuntimeException("Redis callback is null");
        }
        
        if (cacheDiskInterceptor == null) {
            logger.warn("CacheDiskInterceptor is null");
            return callback.doInRedis(connection);
        }
        Class<? extends RedisConnection> interfaceClass;
        if(connection instanceof StringRedisConnection) {
            interfaceClass = StringRedisConnection.class;
        } else if(connection instanceof RedisClusterConnection) {
            interfaceClass = RedisClusterConnection.class;
        } else {
            interfaceClass = RedisConnection.class;
        }
        InvocationHandler handler = new RedisConnectionInvocationHandler(
                connection, cacheDiskInterceptor
        );
        RedisConnection proxyConnection = (RedisConnection) Proxy.newProxyInstance(
                connection.getClass().getClassLoader(),
                new Class<?>[] {interfaceClass},
                handler
        );
        
        return callback.doInRedis(proxyConnection);
    }
    
    public void setCallback(RedisCallback<T> callback) {
        this.callback = callback;
    }
    
    public RedisCallback<T> getCallback() {
        return callback;
    }
    
    public void setCacheDiskInterceptor(CacheDiskInterceptor cacheDiskInterceptor) {
        this.cacheDiskInterceptor = cacheDiskInterceptor;
    }
    
    public CacheDiskInterceptor getCacheDiskInterceptor() {
        return cacheDiskInterceptor;
    }
    
}
