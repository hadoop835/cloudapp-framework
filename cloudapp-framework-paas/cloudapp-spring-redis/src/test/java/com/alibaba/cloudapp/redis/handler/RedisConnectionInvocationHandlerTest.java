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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.StringRedisConnection;

import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RedisConnectionInvocationHandlerTest {
    
    private RedisConnection delegate;
    @Mock
    private CacheDiskInterceptor interceptor;
    
    private RedisConnectionInvocationHandler invocationHandler;
    
    @Test
    public void testInvoke() throws Throwable {
        delegate = mock(StringRedisConnection.class);
        final String key = "key";
        invocationHandler = new RedisConnectionInvocationHandler(
                delegate, interceptor);
        // Setup
        final Method method = StringRedisConnection.class.getMethod(
                "set", String.class, String.class);
        
        // Run the test
        final Object result = invocationHandler.invoke(
                "proxy", method, new Object[]{key, "value"});
        
        // Verify the results
        verify(interceptor).notifyChanged(any(List.class));
    }
    
    @Test
    public void testInvokeDestination() throws Throwable {
        delegate = mock(StringRedisConnection.class);
        final String key = "key";
        invocationHandler = new RedisConnectionInvocationHandler(
                delegate, interceptor);
        // Setup
        //String lMove(String sourceKey, String destinationKey, Direction from, Direction to);
        final Method method = StringRedisConnection.class.getMethod(
                "lMove", String.class, String.class,
                RedisListCommands.Direction.class,
                RedisListCommands.Direction.class
        );
        
        RedisListCommands.Direction from = RedisListCommands.Direction.LEFT;
        RedisListCommands.Direction to = RedisListCommands.Direction.RIGHT;
        
        
        // Run the test
        final Object result = invocationHandler.invoke(
                "proxy", method, new Object[]{key, "value", from, to});
        
        // Verify the results
        verify(interceptor).notifyChanged(any(List.class));
    }
    
    @Test
    public void testInvokeKeys() throws Throwable {
        delegate = mock(StringRedisConnection.class);
        final String[] keys = new String[]{
                "key1", "key2", "key3", "key4"
        };
        invocationHandler = new RedisConnectionInvocationHandler(
                delegate, interceptor);
        // Setup
        //String lMove(String sourceKey, String destinationKey, Direction from, Direction to);
        final Method method = StringRedisConnection.class.getMethod(
                "del", String[].class
        );
        
        RedisListCommands.Direction from = RedisListCommands.Direction.LEFT;
        RedisListCommands.Direction to = RedisListCommands.Direction.RIGHT;
        
        
        // Run the test
        final Object result = invocationHandler.invoke(
                "proxy", method, new Object[]{keys});
        
        // Verify the results
        verify(interceptor).notifyChanged(any(List.class));
    }
    
    @Test
    public void testInvokeDst() throws Throwable {
        delegate = mock(StringRedisConnection.class);
        final String src = "key1";
        final String dst = "key2";
        invocationHandler = new RedisConnectionInvocationHandler(
                delegate, interceptor);
        // Setup
        //String lMove(String sourceKey, String destinationKey, Direction from, Direction to);
        final Method method = StringRedisConnection.class.getMethod(
                "rPopLPush", String.class, String.class
        );
        
        // Run the test
        final Object result = invocationHandler.invoke(
                "proxy", method, new Object[]{src, dst});
        
        // Verify the results
        verify(interceptor).notifyChanged(any(List.class));
    }
    
    @Test
    public void testInvokeByte() throws Throwable {
        delegate = mock(RedisConnection.class);
        final byte[] key = "key".getBytes();
        invocationHandler = new RedisConnectionInvocationHandler(
                delegate, interceptor);
        // Setup
        final Method method = RedisConnection.class.getMethod(
                "set", byte[].class, byte[].class);
        
        // Run the test
        final Object result = invocationHandler.invoke(
                "proxy", method, new Object[]{key, "value".getBytes()});
        
        // Verify the results
        verify(interceptor).notifyChanged(any(List.class));
    }
    
    @Test
    public void testInvokeByteDestination() throws Throwable {
        delegate = mock(RedisConnection.class);
        final byte[] key = "key".getBytes();
        invocationHandler = new RedisConnectionInvocationHandler(
                delegate, interceptor);
        // Setup
        //String lMove(String sourceKey, String destinationKey, Direction from, Direction to);
        final Method method = RedisConnection.class.getMethod(
                "lMove", byte[].class, byte[].class,
                RedisListCommands.Direction.class,
                RedisListCommands.Direction.class
        );
        
        RedisListCommands.Direction from = RedisListCommands.Direction.LEFT;
        RedisListCommands.Direction to = RedisListCommands.Direction.RIGHT;
        
        
        // Run the test
        final Object result = invocationHandler.invoke(
                "proxy", method, new Object[]{key, "value".getBytes(), from, to});
        
        // Verify the results
        verify(interceptor).notifyChanged(any(List.class));
    }
    
    @Test
    public void testInvokeByteKeys() throws Throwable {
        delegate = mock(StringRedisConnection.class);
        final byte[][] keys = new byte[][]{
                "key1".getBytes(), "key2".getBytes(),
                "key3".getBytes(), "key4".getBytes()
        };
        invocationHandler = new RedisConnectionInvocationHandler(
                delegate, interceptor);
        // Setup
        //String lMove(String sourceKey, String destinationKey, Direction from, Direction to);
        final Method method = RedisConnection.class.getMethod(
                "del", byte[][].class
        );
        
        RedisListCommands.Direction from = RedisListCommands.Direction.LEFT;
        RedisListCommands.Direction to = RedisListCommands.Direction.RIGHT;
        
        
        // Run the test
        final Object result = invocationHandler.invoke(
                "proxy", method, new Object[]{keys});
        
        // Verify the results
        verify(interceptor).notifyChanged(any(List.class));
    }
    
    @Test
    public void testInvokeByteDst() throws Throwable {
        delegate = mock(RedisConnection.class);
        final byte[] src = "key1".getBytes();
        final byte[] dst = "key2".getBytes();
        invocationHandler = new RedisConnectionInvocationHandler(
                delegate, interceptor);
        // Setup
        //String lMove(String sourceKey, String destinationKey, Direction from, Direction to);
        final Method method = RedisConnection.class.getMethod(
                "rPopLPush", byte[].class, byte[].class
        );
        
        // Run the test
        final Object result = invocationHandler.invoke(
                "proxy", method, new Object[]{src, dst});
        
        // Verify the results
        verify(interceptor).notifyChanged(any(List.class));
    }
    
}
