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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class RedisConfigurationBuilderTest {

    @InjectMocks
    private RedisConfigurationBuilder builder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        builder = RedisConfigurationBuilder.builder();
    }

    @Test
    public void testBuildStandalone_ValidConfiguration_ReturnsConfiguration() {
        builder.host("localhost");
        builder.port(6379);
        builder.password("password");
        builder.database(0);

        RedisStandaloneConfiguration configuration = builder.buildStandalone();

        assertNotNull(configuration);
        assertEquals("localhost", configuration.getHostName());
        assertEquals(6379, configuration.getPort());
        assertEquals("password", new String(configuration.getPassword().get()));
        assertEquals(0, configuration.getDatabase());
    }

    @Test
    public void testBuildCluster_ValidConfiguration_ReturnsConfiguration() {
        builder.cluster(new RedisProperties.Cluster());
        builder.getCluster().setNodes(Arrays.asList("localhost:6379", "localhost:6380"));
        builder.password("password");
        builder.username("username");

        RedisClusterConfiguration configuration = builder.buildCluster();

        assertNotNull(configuration);
        assertEquals(2, configuration.getClusterNodes().size());
        assertTrue(configuration.getClusterNodes().contains(RedisNode.fromString("localhost:6379")));
        assertTrue(configuration.getClusterNodes().contains(RedisNode.fromString("localhost:6380")));
        assertEquals("password", new String(configuration.getPassword().get()));
        assertEquals("username", configuration.getUsername());
    }

    @Test
    public void testBuildSentinel_ValidConfiguration_ReturnsConfiguration() {
        builder.sentinel(new RedisProperties.Sentinel());
        builder.getSentinel().setNodes(Arrays.asList("localhost:26379", "localhost:26380"));
        builder.getSentinel().setMaster("mymaster");
        builder.password ( "password");
        builder.username( "username");
        builder.getSentinel().setUsername("sentinelUsername");
        builder.getSentinel().setPassword("sentinelPassword");

        RedisSentinelConfiguration configuration = builder.buildSentinel();

        assertNotNull(configuration);
        assertEquals(2, configuration.getSentinels().size());
        assertTrue(configuration.getSentinels().contains(RedisNode.fromString("localhost:26379")));
        assertTrue(configuration.getSentinels().contains(RedisNode.fromString("localhost:26380")));
        assertNotNull(configuration.getMaster());
        assertEquals("mymaster", configuration.getMaster().getName());
        assertEquals("password", new String(configuration.getPassword().get()));
        assertEquals("username", configuration.getUsername());
        assertEquals("sentinelUsername", configuration.getSentinelUsername());
        assertEquals("sentinelPassword", new String(configuration.getSentinelPassword().get()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildCluster_InvalidConfiguration_ThrowsException() {
        builder.buildCluster();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildSentinel_InvalidConfiguration_ThrowsException() {
        builder.buildSentinel();
    }

    @Test
    public void testIsCluster_WithClusterConfiguration_ReturnsTrue() {
        builder.cluster (new RedisProperties.Cluster());
        builder.getCluster().setNodes(Collections.singletonList("localhost:6379"));
        assertTrue(builder.isCluster());
    }

    @Test
    public void testIsCluster_WithoutClusterConfiguration_ReturnsFalse() {
        assertFalse(builder.isCluster());
    }

    @Test
    public void testIsSentinel_WithSentinelConfiguration_ReturnsTrue() {
        builder.sentinel(new RedisProperties.Sentinel());
        builder.getSentinel().setNodes(Collections.singletonList("localhost:26379"));
        assertTrue(builder.isSentinel());
    }

    @Test
    public void testIsSentinel_WithoutSentinelConfiguration_ReturnsFalse() {
        assertFalse(builder.isSentinel());
    }
}
