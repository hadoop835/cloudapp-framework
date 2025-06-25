package com.alibaba.cloudapp.starter.redis.configuration;

import com.alibaba.cloudapp.api.cache.interceptors.CacheDiskInterceptor;
import com.alibaba.cloudapp.api.cache.interceptors.DecryptInterceptor;
import com.alibaba.cloudapp.api.cache.interceptors.EncryptInterceptor;
import com.alibaba.cloudapp.api.cache.interceptors.MonitoringInterceptor;
import com.alibaba.cloudapp.redis.RefreshableRedisTemplateImpl;
import com.alibaba.cloudapp.starter.redis.properties.CloudAppRedisProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.mockito.Mockito.mock;

class CloudAppRedisConfigurationTest {
    
    private CloudAppRedisConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new CloudAppRedisConfiguration();
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
    void testEncryptInterceptor() {
        // Setup
        // Run the test
        final EncryptInterceptor result = configuration.encryptInterceptor();
        
        // Verify the results
    }
    
    @Test
    void testDecryptInterceptor() {
        // Setup
        // Run the test
        final DecryptInterceptor result = configuration.decryptInterceptor();
        
        // Verify the results
    }
    
    @Test
    void testMonitoringInterceptor() {
        // Setup
        // Run the test
        final MonitoringInterceptor<?> result = configuration.monitoringInterceptor();
        
        // Verify the results
    }
    
    @Test
    void testCacheDiskInterceptor() {
        // Setup
        // Run the test
        final CacheDiskInterceptor result = configuration.cacheDiskInterceptor();
        
        // Verify the results
    }
    
    @Test
    void testRefreshableRedisTemplate() {
        // Setup
        final RedisConnectionFactory factory = null;
        final CloudAppRedisProperties properties = new CloudAppRedisProperties();
        properties.setHashMonitoring(false);
        properties.setValueMonitoring(false);
        properties.setKeyEncrypt(false);
        properties.setUsedHash(false);
        final RedisProperties base = new RedisProperties();
        properties.setBase(base);
        
        final EncryptInterceptor encryptInterceptor = mock(EncryptInterceptor.class);
        final DecryptInterceptor decryptInterceptor = mock(DecryptInterceptor.class);
        final MonitoringInterceptor<?> monitoringInterceptor = mock(MonitoringInterceptor.class);
        final CacheDiskInterceptor cacheDiskInterceptor = mock(CacheDiskInterceptor.class);
        final RedisSerializer<?> keySerializer = RedisSerializer.java();
        final RedisSerializer<?> valueSerializer = RedisSerializer.java();
        final RedisSerializer<?> hashKeySerializer = RedisSerializer.java();
        final RedisSerializer<?> hashValueSerializer = RedisSerializer.java();
        
        // Run the test
        final RefreshableRedisTemplateImpl<?, ?> result = configuration.refreshableRedisTemplate(
                factory, properties, encryptInterceptor, decryptInterceptor,
                monitoringInterceptor, cacheDiskInterceptor, keySerializer,
                valueSerializer, hashKeySerializer, hashValueSerializer);
        
        // Verify the results
    }
    
}
