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

package com.alibaba.cloudapp.redis.handler;

import com.alibaba.cloudapp.api.cache.interceptors.CacheDiskInterceptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisCallBackProxyTest {

    @Mock
    private RedisTemplate<String, String> template;

    @Mock
    private CacheDiskInterceptor cacheDiskInterceptor;

    @InjectMocks
    private RedisCallBackProxy<String> proxy;

    private RedisCallback<String> callback;

    @Before
    public void setUp() {
        callback = connection -> "success";
    }

    @Test
    public void doInRedis_CallbackIsNull_ThrowsRuntimeException() {
        proxy.setCallback(null);
        assertThrows(RuntimeException.class, () -> proxy.doInRedis(mock(RedisConnection.class)));
    }

    @Test
    public void doInRedis_CacheDiskInterceptorIsNull_LogsWarningAndProceeds() {
        proxy.setCallback(callback);
        proxy.setCacheDiskInterceptor(null);

        RedisConnection connection = mock(RedisConnection.class);
        String result = proxy.doInRedis(connection);

        assertEquals("success", result);
        verify(connection, never()).close();
    }

    @Test
    public void doInRedis_CacheDiskInterceptorIsNotNull_WrapsConnection() {
        proxy.setCallback(callback);
        proxy.setCacheDiskInterceptor(cacheDiskInterceptor);

        RedisConnection connection = mock(RedisConnection.class);

        String result = proxy.doInRedis(connection);

        assertEquals("success", result);
        verify(connection, never()).close();
        verify(cacheDiskInterceptor, never()).delete(any());
    }

    @Test
    public void setCallback_ValidCallback_SetsCallback() {
        proxy.setCallback(callback);
        assertEquals(callback, proxy.getCallback());
    }

    @Test
    public void getCallback_CallbackSet_ReturnsCallback() {
        proxy.setCallback(callback);
        assertEquals(callback, proxy.getCallback());
    }

    @Test
    public void setCacheDiskInterceptor_ValidInterceptor_SetsInterceptor() {
        proxy.setCacheDiskInterceptor(cacheDiskInterceptor);
        assertEquals(cacheDiskInterceptor, proxy.getCacheDiskInterceptor());
    }

    @Test
    public void getCacheDiskInterceptor_InterceptorSet_ReturnsInterceptor() {
        proxy.setCacheDiskInterceptor(cacheDiskInterceptor);
        assertEquals(cacheDiskInterceptor, proxy.getCacheDiskInterceptor());
    }
}
