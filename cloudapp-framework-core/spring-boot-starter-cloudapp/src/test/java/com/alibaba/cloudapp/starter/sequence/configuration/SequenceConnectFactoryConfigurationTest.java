package com.alibaba.cloudapp.starter.sequence.configuration;

import com.alibaba.cloudapp.starter.sequence.properties.CloudAppSequenceProperties;
import com.alibaba.cloudapp.starter.sequence.properties.RedisSequenceProperties;
import com.alibaba.cloudapp.starter.sequence.refresh.RedisSequenceRefreshComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;

class SequenceConnectFactoryConfigurationTest {
    
    private SequenceConnectFactoryConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new SequenceConnectFactoryConfiguration();
    }
    
    @Test
    void testGetRedisSequenceRefreshComponent() {
        // Setup
        final CloudAppSequenceProperties properties = new CloudAppSequenceProperties();
        final RedisSequenceProperties redis = new RedisSequenceProperties();
        redis.setDatabase(0);
        redis.setHost("host");
        redis.setPassword("password");
        redis.setPort(0);
        redis.setSequenceName("redis");
        redis.setStep(1L);
        redis.setUsername("username");
        
        properties.setRedis(redis);
        
        // Run the test
        final RedisSequenceRefreshComponent result = configuration.getRedisSequenceRefreshComponent(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testJedisConnectionFactory() {
        // Setup
        final CloudAppSequenceProperties properties =
                new CloudAppSequenceProperties();
        final RedisSequenceProperties redis = new RedisSequenceProperties();
        redis.setDatabase(0);
        redis.setHost("host");
        redis.setPassword("password");
        redis.setPort(0);
        redis.setSequenceName("redis");
        redis.setStep(1L);
        redis.setUsername("username");
        
        properties.setRedis(redis);
        final RedisSequenceRefreshComponent comp = new RedisSequenceRefreshComponent(
                properties);
        
        // Run the test
        final RedisConnectionFactory result = configuration.jedisConnectionFactory(
                comp);
        
        // Verify the results
    }
    
}
