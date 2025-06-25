package com.alibaba.cloudapp.starter.redis.connection.client;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class DistributedJedisClientConfigurationTest {
    
    @Mock
    private GenericObjectPoolConfig mockPoolConfig;
    @Mock
    private SSLParameters mockSslParameters;
    @Mock
    private SSLSocketFactory mockSslSocketFactory;
    @Mock
    private HostnameVerifier mockHostnameVerifier;
    
    private DistributedJedisClientConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new DistributedJedisClientConfiguration(
                false, false, mockPoolConfig, "clientName", Duration.ofDays(0L),
                Duration.ofDays(0L), mockSslParameters, mockSslSocketFactory,
                mockHostnameVerifier);
    }
    
    @Test
    void testIsUseSsl() {
        assertFalse(configuration.isUseSsl());
    }
    
    @Test
    void testGetSslSocketFactory() {
        final Optional<SSLSocketFactory> result = configuration.getSslSocketFactory();
    }
    
    @Test
    void testGetSslParameters() {
        final Optional<SSLParameters> result = configuration.getSslParameters();
    }
    
    @Test
    void testGetHostnameVerifier() {
        final Optional<HostnameVerifier> result = configuration.getHostnameVerifier();
    }
    
    @Test
    void testIsUsePooling() {
        assertFalse(
                configuration.isUsePooling());
    }
    
    @Test
    void testGetPoolConfig() {
        final Optional<GenericObjectPoolConfig> result = configuration.getPoolConfig();
    }
    
    @Test
    void testGetClientName() {
        assertEquals(Optional.of("clientName"),
                     configuration.getClientName()
        );
    }
    
    @Test
    void testGetReadTimeout() {
        assertEquals(Duration.ofDays(0L),
                     configuration.getReadTimeout()
        );
    }
    
    @Test
    void testGetConnectTimeout() {
        assertEquals(Duration.ofDays(0L),
                     configuration.getConnectTimeout()
        );
    }
    
}
