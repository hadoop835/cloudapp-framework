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

package com.alibaba.cloudapp.sequence.exception;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RedisExecutionExceptionTest {
    
    private RedisExecutionException redisExecutionExceptionUnderTest;
    
    @Before
    public void setUp() {
        redisExecutionExceptionUnderTest = new RedisExecutionException(
                "msg",
                new Exception("message")
        );
    }
    
    @Test
    public void testGetCode() {
        assertEquals("CloudApp.RedisError",
                     redisExecutionExceptionUnderTest.getCode()
        );
        assertNotNull(redisExecutionExceptionUnderTest.getMessage());
    }
    
    @Test
    public void testThrowRedisExecutionException() {
        // Arrange
        String invalidMessageFormat = "url";
        
        // Act & Assert
        Assert.assertThrows(RedisExecutionException.class, () -> {
            throw new RedisExecutionException(invalidMessageFormat);
        });
    }
    
    @Test
    public void testThrowRedisExecutionException_throw() {
        // Arrange
        String invalidMessageFormat = "url";
        
        // Act & Assert
        Assert.assertThrows(RedisExecutionException.class, () -> {
            throw new RedisExecutionException(invalidMessageFormat, new Exception());
        });
    }
    
}
