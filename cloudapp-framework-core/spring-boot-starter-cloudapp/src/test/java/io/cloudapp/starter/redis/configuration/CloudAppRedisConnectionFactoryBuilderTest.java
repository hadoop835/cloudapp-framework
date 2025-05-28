package io.cloudapp.starter.redis.configuration;

import io.cloudapp.starter.redis.properties.CloudAppRedisProperties;
import io.cloudapp.starter.redis.refresh.RedisRefreshableComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;

class CloudAppRedisConnectionFactoryBuilderTest {
    
    private CloudAppRedisConnectionFactoryBuilder factoryBuilder;
    
    @BeforeEach
    void setUp() {
        factoryBuilder = new CloudAppRedisConnectionFactoryBuilder();
    }
    
    @Test
    void testGetComponent() {
        // Setup
        final CloudAppRedisProperties properties = new CloudAppRedisProperties();
        properties.setHashMonitoring(false);
        properties.setValueMonitoring(false);
        properties.setKeyEncrypt(false);
        properties.setUsedHash(false);
        final RedisProperties base = new RedisProperties();
        properties.setBase(base);
        
        // Run the test
        final RedisRefreshableComponent result = factoryBuilder.getComponent(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testJedisConnectionFactory() {
        // Setup
        final CloudAppRedisProperties cloudAppRedisProperties = new CloudAppRedisProperties();
        cloudAppRedisProperties.setHashMonitoring(false);
        cloudAppRedisProperties.setValueMonitoring(false);
        cloudAppRedisProperties.setKeyEncrypt(false);
        cloudAppRedisProperties.setUsedHash(false);
        final RedisProperties base = new RedisProperties();
        cloudAppRedisProperties.setBase(base);
        final RedisRefreshableComponent comp = new RedisRefreshableComponent(
                cloudAppRedisProperties);
        
        // Run the test
        final RedisConnectionFactory result = factoryBuilder.jedisConnectionFactory(
                comp);
        
        // Verify the results
    }
    
}
