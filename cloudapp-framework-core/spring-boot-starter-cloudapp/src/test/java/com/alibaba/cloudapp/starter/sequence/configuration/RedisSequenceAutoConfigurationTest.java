package com.alibaba.cloudapp.starter.sequence.configuration;

import com.alibaba.cloudapp.sequence.service.RedisSequenceGenerator;
import com.alibaba.cloudapp.starter.sequence.properties.CloudAppSequenceProperties;
import com.alibaba.cloudapp.starter.sequence.properties.RedisSequenceProperties;
import com.alibaba.cloudapp.starter.sequence.properties.SnowflakeProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

class RedisSequenceAutoConfigurationTest {
    
    private RedisSequenceAutoConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new RedisSequenceAutoConfiguration();
    }
    
    @Test
    void testKeySerializer() {
        // Setup
        // Run the test
        final StringRedisSerializer result = configuration.keySerializer();
        
        // Verify the results
    }
    
    @Test
    void testValueSerializer() {
        // Setup
        // Run the test
        final JdkSerializationRedisSerializer result = configuration.valueSerializer();
        
        // Verify the results
    }
    
    @Test
    void testHashKeySerializer() {
        // Setup
        // Run the test
        final StringRedisSerializer result = configuration.hashKeySerializer();
        
        // Verify the results
    }
    
    @Test
    void testHashValueSerializer() {
        // Setup
        // Run the test
        final JdkSerializationRedisSerializer result = configuration.hashValueSerializer();
        
        // Verify the results
    }
    
    @Test
    void testGetRedisTemplate() {
        // Setup
        final RedisConnectionFactory factory = null;
        final RedisSerializer<?> keySerializer = RedisSerializer.java();
        final RedisSerializer<?> valueSerializer = RedisSerializer.java();
        final RedisSerializer<?> hashKeySerializer = RedisSerializer.java();
        final RedisSerializer<?> hashValueSerializer = RedisSerializer.java();
        
        // Run the test
        final RedisTemplate<?, ?> result = configuration.getRedisTemplate(
                factory, keySerializer, valueSerializer, hashKeySerializer,
                hashValueSerializer);
        
        // Verify the results
    }
    
    @Test
    void testGetRedisSequence() {
        // Setup
        final CloudAppSequenceProperties properties = new CloudAppSequenceProperties();
        final SnowflakeProperties snowflake = new SnowflakeProperties();
        snowflake.setWorkerId(0L);
        properties.setSnowflake(snowflake);
        final RedisSequenceProperties redis = new RedisSequenceProperties();
        redis.setSequenceName("queueName");
        redis.setStep(0L);
        properties.setRedis(redis);
        
        final RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(null);
        redisTemplate.setKeySerializer(RedisSerializer.java());
        redisTemplate.setValueSerializer(RedisSerializer.java());
        redisTemplate.setHashKeySerializer(RedisSerializer.java());
        redisTemplate.setHashValueSerializer(RedisSerializer.java());
        
        // Run the test
        final RedisSequenceGenerator result = configuration.getRedisSequence(
                properties, redisTemplate);
        
        // Verify the results
    }
    
}
