package com.alibaba.cloudapp.starter.redis.connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LettuceConnectionFactoryBuilderTest {
    
    @Mock
    private RedisProperties mockRedisProperties;
    
    private LettuceConnectionFactoryBuilder factoryBuilder;
    
    @BeforeEach
    void setUp() {
        factoryBuilder = new LettuceConnectionFactoryBuilder(
                mockRedisProperties);
    }
    
    @Test
    void testInitConnectFactory() {
        // Setup
        // Configure RedisProperties.getLettuce(...).
        final RedisProperties.Lettuce lettuce = new RedisProperties.Lettuce();
        lettuce.setShutdownTimeout(Duration.ofDays(0L));
        when(mockRedisProperties.getLettuce()).thenReturn(lettuce);
        
        when(mockRedisProperties.getUrl()).thenReturn(
                "redis://password@host:6379/8?timeout=PT3000S"
        );
        when(mockRedisProperties.getClientName()).thenReturn("clientName");
        when(mockRedisProperties.isSsl()).thenReturn(false);
        when(mockRedisProperties.getTimeout()).thenReturn(Duration.ofDays(0L));
        
        // Configure RedisProperties.getCluster(...).
        final RedisProperties.Cluster cluster = new RedisProperties.Cluster();
        cluster.setNodes(Collections.singletonList("node:6379"));
        cluster.setMaxRedirects(0);
        when(mockRedisProperties.getCluster()).thenReturn(cluster);
        
        when(mockRedisProperties.getDatabase()).thenReturn(0);
        when(mockRedisProperties.getHost()).thenReturn("host");
        when(mockRedisProperties.getPassword()).thenReturn("password");
        when(mockRedisProperties.getPort()).thenReturn(0);
        
        // Configure RedisProperties.getSentinel(...).
        final RedisProperties.Sentinel sentinel = new RedisProperties.Sentinel();
        sentinel.setMaster("master");
        sentinel.setNodes(Collections.singletonList("value:6379"));
        sentinel.setUsername("username");
        sentinel.setPassword("password");
        when(mockRedisProperties.getSentinel()).thenReturn(sentinel);
        
        when(mockRedisProperties.getUsername()).thenReturn("username");
        
        // Run the test
        factoryBuilder.initConnectFactory();
        
        assertThrows(IllegalStateException.class,
                     () -> factoryBuilder.destroy());
        // Verify the results
    }
    
    @Test
    void testGetConnectionFactory() {
        final RedisConnectionFactory result = factoryBuilder.getConnectionFactory();
    }
    
    @Test
    void testDestroy_ThrowsException() {
        // Setup
        // Run the test
        assertThrows(Exception.class,
                     () -> factoryBuilder.destroy()
        );
    }
    
    @Test
    void testAfterPropertiesSet() {
        // Setup
        // Configure RedisProperties.getLettuce(...).
        final RedisProperties.Lettuce lettuce = new RedisProperties.Lettuce();
        lettuce.setShutdownTimeout(Duration.ofDays(0L));
        when(mockRedisProperties.getLettuce()).thenReturn(lettuce);
        
        when(mockRedisProperties.getUrl()).thenReturn(
                "redis://password@host:6379/9?timeout=PT3000S"
        );
        when(mockRedisProperties.getClientName()).thenReturn("clientName");
        when(mockRedisProperties.isSsl()).thenReturn(false);
        when(mockRedisProperties.getTimeout()).thenReturn(Duration.ofDays(0L));
        
        when(mockRedisProperties.getDatabase()).thenReturn(0);
        when(mockRedisProperties.getHost()).thenReturn("host");
        when(mockRedisProperties.getPassword()).thenReturn("password");
        when(mockRedisProperties.getPort()).thenReturn(0);
        
        // Configure RedisProperties.getSentinel(...).
        final RedisProperties.Sentinel sentinel = new RedisProperties.Sentinel();
        sentinel.setMaster("master");
        sentinel.setNodes(Collections.singletonList("value:6379"));
        sentinel.setUsername("username");
        sentinel.setPassword("password");
        when(mockRedisProperties.getSentinel()).thenReturn(sentinel);
        
        when(mockRedisProperties.getUsername()).thenReturn("username");
        
        // Run the test
        factoryBuilder.afterPropertiesSet();
        
        // Verify the results
    }
    
}
