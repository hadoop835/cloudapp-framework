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

package com.alibaba.cloudapp.redis;

import com.alibaba.cloudapp.api.cache.interceptors.CacheDiskInterceptor;
import com.alibaba.cloudapp.api.cache.interceptors.DecryptInterceptor;
import com.alibaba.cloudapp.api.cache.interceptors.EncryptInterceptor;
import com.alibaba.cloudapp.api.cache.interceptors.MonitoringInterceptor;
import com.alibaba.cloudapp.exeption.CloudAppInvalidAccessException;
import com.alibaba.cloudapp.redis.handler.InterceptorHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RefreshableRedisTemplateImplTest {
    
    @Mock
    private EncryptInterceptor encryptInterceptor;
    
    @Mock
    private DecryptInterceptor decryptInterceptor;
    
    @Mock
    private MonitoringInterceptor<?> monitoringInterceptor;
    
    @Mock
    private CacheDiskInterceptor cacheDiskInterceptor;
    
    private RefreshableRedisTemplateImpl<String, String> template;
    
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    
    @Mock
    private RedisCallback<String> callback;
    
    @Mock
    private SessionCallback<String> sessionCallback;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        template = new RefreshableRedisTemplateImpl<>();
    }
    
    public void execute_TemplateInitialized_ThrowsException2()
            throws NoSuchFieldException, IllegalAccessException {
        setPrivateField(template, "initialized", true);
        
        assertThrows(IllegalArgumentException.class, () -> {
            template.execute(sessionCallback);
        });
    }
    
    
    public void setPrivateField(Object object, String fieldName, boolean value)
            throws NoSuchFieldException, IllegalAccessException {
        Field refreshingField = RefreshableRedisTemplateImpl.class.getDeclaredField(
                fieldName);
        refreshingField.setAccessible(true);
        
        refreshingField.set(object, value);
    }
    
    
    @Test
    public void testPostStart() {
        template.setConnectionFactory(mock(RedisConnectionFactory.class));
        template.afterPropertiesSet();
        template.postStart();
        assertTrue(template.isInitialized());
    }
    
    @Test
    public void testPreStop() {
        template.setConnectionFactory(mock(RedisConnectionFactory.class));
        template.afterPropertiesSet();
        template.preStop();
        assertFalse(template.isInitialized());
    }
    
    @Test
    public void testSetConnectionFactory() {
        RedisConnectionFactory connectionFactory = mock(
                RedisConnectionFactory.class);
        template.setConnectionFactory(connectionFactory);
        assertSame(connectionFactory, template.getConnectionFactory());
    }
    
    @Test
    public void testSetKeySerializer() {
        RedisSerializer<String> serializer = mock(RedisSerializer.class);
        template.setKeySerializer(serializer);
        assertSame(serializer,
                   ((InterceptorHandler<Object>) template.getKeySerializer()).getDelegate()
        );
    }
    
    @Test
    public void testSetValueSerializer() {
        RedisSerializer<String> serializer = mock(RedisSerializer.class);
        template.setValueSerializer(serializer);
        assertSame(serializer,
                   ((InterceptorHandler<Object>) template.getValueSerializer()).getDelegate()
        );
    }
    
    @Test
    public void testSetHashKeySerializer() {
        RedisSerializer<String> serializer = mock(RedisSerializer.class);
        template.setHashKeySerializer(serializer);
        assertSame(serializer,
                   ((InterceptorHandler<Object>) template.getHashKeySerializer()).getDelegate()
        );
    }
    
    @Test
    public void testSetHashValueSerializer() {
        RedisSerializer<String> serializer = mock(RedisSerializer.class);
        template.setHashValueSerializer(serializer);
        assertSame(serializer,
                   ((InterceptorHandler<Object>) template.getHashValueSerializer()).getDelegate()
        );
    }
    
    @Test
    public void testAfterPropertiesSet() {
        template.setConnectionFactory(mock(RedisConnectionFactory.class));
        template.afterPropertiesSet();
        assertTrue(template.isInitialized());
    }
    
    @Test
    public void testSetEncryptInterceptor() {
        template.setConnectionFactory(mock(RedisConnectionFactory.class));
        template.setEncryptInterceptor(encryptInterceptor);
        template.afterPropertiesSet();
        assertSame(encryptInterceptor,
                   ((InterceptorHandler<Object>) template.getValueSerializer()).getEncryptInterceptor()
        );
    }
    
    @Test
    public void testSetDecryptInterceptor() {
        template.setDecryptInterceptor(decryptInterceptor);
        template.setConnectionFactory(mock(RedisConnectionFactory.class));
        template.afterPropertiesSet();
        assertSame(decryptInterceptor,
                   ((InterceptorHandler<Object>) template.getValueSerializer()).getDecryptInterceptor()
        );
    }
    
    @Test
    public void testSetMonitoringInterceptor() {
        template.setMonitoringInterceptor(monitoringInterceptor);
        template.setConnectionFactory(mock(RedisConnectionFactory.class));
        template.afterPropertiesSet();
        assertSame(monitoringInterceptor,
                   ((InterceptorHandler<Object>) template.getKeySerializer()).getMonitoringInterceptor()
        );
    }
    
    @Test
    public void testSetCacheDiskInterceptor() {
        template.setCacheDiskInterceptor(cacheDiskInterceptor);
        assertSame(cacheDiskInterceptor, template.getCacheDiskInterceptor());
    }
    
    @Test
    public void testIsUsedHash() {
        template.setUsedHash(true);
        assertTrue(template.isUsedHash());
    }
    
    @Test
    public void testIsKeyEncrypt() {
        template.setKeyEncrypt(true);
        assertTrue(template.isKeyEncrypt());
    }
    
    @Test
    public void testIsValueMonitoring() {
        template.setValueMonitoring(true);
        assertTrue(template.isValueMonitoring());
    }
    
    @Test
    public void testIsHashMonitoring() {
        template.setHashMonitoring(true);
        assertTrue(template.isHashMonitoring());
    }
    
    @Test
    public void execute_TemplateNotInitialized_ThrowsException()
            throws NoSuchFieldException, IllegalAccessException {
        setPrivateField(template, "initialized", false);
        
        RedisCallback<String> callback = connection -> "result";
        
        assertThrows(CloudAppInvalidAccessException.class, () -> {
            template.execute(callback, false, false);
        });
    }
    
    @Test
    public void execute_TemplateInitialized_success()
            throws NoSuchFieldException, IllegalAccessException {
        setPrivateField(template, "initialized", true);
        
        RedisCallback<String> callback = connection -> "result";
        
        assertThrows(IllegalArgumentException.class, () -> {
            template.execute(callback, false, false);
        });
    }
    
    
    @Test
    public void executePipelined_TemplateNotInitialized_SessionCallBack_ThrowsException()
            throws NoSuchFieldException, IllegalAccessException {
        setPrivateField(template, "initialized", false);
        
        assertThrows(CloudAppInvalidAccessException.class, () -> {
            template.executePipelined(sessionCallback, null);
        });
    }
    
    @Test
    public void executePipelined_TemplateNotInitialized_SessionCallBack()
            throws NoSuchFieldException, IllegalAccessException {
        setPrivateField(template, "initialized", true);
        
        assertThrows(IllegalArgumentException.class, () -> {
            template.executePipelined(sessionCallback, null);
        });
    }
    
    @Test
    public void executePipelined_TemplateNotInitialized_RedisCallBack_ThrowsException()
            throws NoSuchFieldException, IllegalAccessException {
        setPrivateField(template, "initialized", false);
        
        assertThrows(CloudAppInvalidAccessException.class, () -> {
            template.executePipelined(callback, null);
        });
    }
    
    @Test
    public void executePipelined_TemplateNotInitialized_RedisCallBack()
            throws NoSuchFieldException, IllegalAccessException {
        setPrivateField(template, "initialized", true);
        
        assertThrows(IllegalArgumentException.class, () -> {
            template.executePipelined(callback, null);
        });
    }
    
    @Test
    public void execute_TemplateNotInitialized_ThrowsException2()
            throws NoSuchFieldException, IllegalAccessException {
        setPrivateField(template, "initialized", false);
        
        assertThrows(CloudAppInvalidAccessException.class, () -> {
            template.execute(sessionCallback);
        });
    }
    
    
}
