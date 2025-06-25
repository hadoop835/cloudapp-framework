package com.alibaba.cloudapp.starter.redis.connection.client;

import com.alibaba.cloudapp.starter.redis.connection.pool.GenericObjectPoolCreator;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JedisClientConfigurationBuilderTest {
    
    @Mock
    private HostnameVerifier mockHostnameVerifier;
    @Mock
    private SSLSocketFactory mockSslSocketFactory;
    @Mock
    private SSLParameters mockSslParameters;
    @Mock
    private GenericObjectPoolCreator mockPoolCreator;
    
    private JedisClientConfigurationBuilder builder;
    
    @BeforeEach
    void setUp() {
        builder = new JedisClientConfigurationBuilder();
        builder.useSsl(false);
        builder.clientName("clientName");
        builder.hostnameVerifier(mockHostnameVerifier);
        builder.SSLSocketFactory(mockSslSocketFactory);
        builder.sslParameters(mockSslParameters);
        builder.poolCreator(mockPoolCreator);
    }
    
    @Test
    void testBuilder() {
        // Run the test
        final JedisClientConfigurationBuilder result = JedisClientConfigurationBuilder.builder();
        final Duration readTimeout = Duration.ofDays(0L);
        assertEquals(result, result.readTimeout(readTimeout));
        
        final Duration connectTimeout = Duration.ofDays(0L);
        assertEquals(result, result.connectTimeout(connectTimeout));
        
    }
    
    @Test
    void testReadTimeout() {
        // Setup
        final Duration readTimeout = Duration.ofDays(0L);
        
        // Run the test
        final JedisClientConfigurationBuilder result = builder.readTimeout(
                readTimeout);
        
        // Verify the results
    }
    
    @Test
    void testConnectTimeout() {
        // Setup
        final Duration connectTimeout = Duration.ofDays(0L);
        
        // Run the test
        final JedisClientConfigurationBuilder result = builder.connectTimeout(
                connectTimeout);
        
        // Verify the results
    }
    
    @Test
    void testBuild() {
        // Setup
        when(mockPoolCreator.usePooling()).thenReturn(false);
        
        // Configure GenericObjectPoolCreator.create(...).
        final GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxIdle(0);
        genericObjectPoolConfig.setMaxTotal(0);
        genericObjectPoolConfig.setMinIdle(0);
        when(mockPoolCreator.create()).thenReturn(genericObjectPoolConfig);
        
        // Run the test
        final DistributedJedisClientConfiguration result = builder.build();
        
        // Verify the results
    }
    
}
