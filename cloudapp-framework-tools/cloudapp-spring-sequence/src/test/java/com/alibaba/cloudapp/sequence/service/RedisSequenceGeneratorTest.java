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

package com.alibaba.cloudapp.sequence.service;

import com.alibaba.cloudapp.sequence.exception.RedisExecutionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedisSequenceGeneratorTest {

    @Mock
    private RedisTemplate<Object, Object> redisTemplate;

    @Mock
    private ValueOperations<Object, Object> valueOperations;

    @InjectMocks
    private RedisSequenceGenerator redisSequenceGenerator;

    @Before
    public void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testNextIdIncrementsCorrectly() {
        when(valueOperations.increment(anyString(), anyLong())).thenReturn(1L, 2L, 3L);
        assertEquals(Long.valueOf(1L), redisSequenceGenerator.nextId());
        assertEquals(Long.valueOf(2L), redisSequenceGenerator.nextId());
        assertEquals(Long.valueOf(3L), redisSequenceGenerator.nextId());
    }

    @Test
    public void testNextIdWithCustomQueueNameAndStep() {
        final String customQueueName = "testQueue";
        final long customStep = 2;
        when(valueOperations.increment(anyString(), anyLong())).thenReturn(1L, 3L, 5L);

        assertEquals(Long.valueOf(1L), redisSequenceGenerator.nextId(customQueueName, customStep));
        assertEquals(Long.valueOf(3L), redisSequenceGenerator.nextId(customQueueName, customStep));
        assertEquals(Long.valueOf(5L), redisSequenceGenerator.nextId(customQueueName, customStep));
    }

    @Test(expected = RedisExecutionException.class)
    public void testNextIdThrowsRedisConnectException() {
        when(valueOperations.increment(anyString(), anyLong())).thenThrow(new RuntimeException("Redis connection error"));
        redisSequenceGenerator.nextId();
        fail("Expected RedisConnectException to be thrown");
    }

    @Test(expected = RedisExecutionException.class)
    public void testNextIdWithCustomQueueNameAndStepThrowsRedisConnectException() {
        final String customQueueName = "testQueue";
        final long customStep = 2;
        when(valueOperations.increment(anyString(), anyLong())).thenThrow(new RuntimeException("Redis connection error"));

        redisSequenceGenerator.nextId(customQueueName, customStep);
        fail("Expected RedisConnectException to be thrown");
    }
}
