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

package com.alibaba.cloudapp.starter.redis.connection;

import com.alibaba.cloudapp.sequence.exception.IllegalRedisUrlException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class RedisUrlPropertyResolverTest {

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAssignment_ValidProperties_SetsPropertiesCorrectly() {
        RedisProperties properties = new RedisProperties();
        RedisUrlPropertyResolver resolver = new RedisUrlPropertyResolver(
                "redis://user:pass@localhost:6379/0?timeout=10s&database=0");
        resolver.assignment(properties);

        assertEquals("localhost", properties.getHost());
        assertEquals(6379, properties.getPort());
        assertEquals("user", properties.getUsername());
        assertEquals("pass", properties.getPassword());
        assertEquals(Duration.ofSeconds(10), properties.getTimeout());
        assertEquals(0, properties.getDatabase());
        assertFalse(properties.isSsl());
    }

    @Test(expected = IllegalRedisUrlException.class)
    public void testConstructor_InvalidScheme_ThrowsException() {
        new RedisUrlPropertyResolver("http://localhost:6379");
    }

    @Test(expected = IllegalRedisUrlException.class)
    public void testConstructor_InvalidUri_ThrowsException() {
        new RedisUrlPropertyResolver(
                "redis://:password@]host[:port][/database][?[timeout=timeout[d|h|m|s|ms|us|ns]");
    }

    @Test
    public void testAssignment_NoDatabaseInUrl_SetsDatabaseFromPath() {
        RedisProperties properties = new RedisProperties();
        RedisUrlPropertyResolver resolver = new RedisUrlPropertyResolver("redis://localhost:6379/1");
        resolver.assignment(properties);
        assertEquals(1, properties.getDatabase());
    }

    @Test
    public void testAssignment_NoTimeoutInUrl_SetsTimeoutFromQuery() {
        RedisProperties properties = new RedisProperties();
        RedisUrlPropertyResolver resolver = new RedisUrlPropertyResolver("redis://localhost:6379/0?timeout=10s");
        resolver.assignment(properties);
        assertEquals(Duration.ofSeconds(10), properties.getTimeout());
    }
}
