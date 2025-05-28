package io.cloudapp.starter.redis.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CloudAppRedisPropertiesTest {
    
    private CloudAppRedisProperties redisProperties;
    
    @BeforeEach
    void setUp() {
        redisProperties = new CloudAppRedisProperties();
    }
    
    @Test
    void testHashMonitoringGetterAndSetter() {
        final boolean hashMonitoring = false;
        redisProperties.setHashMonitoring(hashMonitoring);
        assertFalse(redisProperties.isHashMonitoring());
    }
    
    @Test
    void testValueMonitoringGetterAndSetter() {
        final boolean valueMonitoring = false;
        redisProperties.setValueMonitoring(valueMonitoring);
        assertFalse(redisProperties.isValueMonitoring());
    }
    
    @Test
    void testKeyEncryptGetterAndSetter() {
        final boolean keyEncrypt = false;
        redisProperties.setKeyEncrypt(keyEncrypt);
        assertFalse(redisProperties.isKeyEncrypt());
    }
    
    @Test
    void testUsedHashGetterAndSetter() {
        final boolean usedHash = false;
        redisProperties.setUsedHash(usedHash);
        assertFalse(redisProperties.isUsedHash());
    }
    
    @Test
    void testBaseGetterAndSetter() {
        final RedisProperties base = new RedisProperties();
        redisProperties.setBase(base);
        assertEquals(base, redisProperties.getBase());
    }
    
}
