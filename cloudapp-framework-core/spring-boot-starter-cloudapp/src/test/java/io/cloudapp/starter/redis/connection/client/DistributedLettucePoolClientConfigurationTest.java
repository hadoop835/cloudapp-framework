package io.cloudapp.starter.redis.connection.client;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.resource.ClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DistributedLettucePoolClientConfigurationTest {
    
    @Mock
    private LettuceClientConfiguration mockConfig;
    @Mock
    private GenericObjectPoolConfig mockPoolConfig;
    
    private DistributedLettucePoolClientConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new DistributedLettucePoolClientConfiguration(
                mockConfig, mockPoolConfig);
    }
    
    @Test
    void testGetClientConfiguration() {
        assertEquals(mockConfig,
                     configuration.getClientConfiguration()
        );
    }
    
    @Test
    void testGetPoolConfig() {
        assertEquals(mockPoolConfig,
                     configuration.getPoolConfig()
        );
    }
    
    @Test
    void testIsUseSsl() {
        // Setup
        when(mockConfig.isUseSsl()).thenReturn(false);
        
        // Run the test
        final boolean result = configuration.isUseSsl();
        
        // Verify the results
        assertFalse(result);
    }
    
    @Test
    void testIsUseSsl_LettuceClientConfigurationReturnsTrue() {
        // Setup
        when(mockConfig.isUseSsl()).thenReturn(true);
        
        // Run the test
        final boolean result = configuration.isUseSsl();
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    void testIsVerifyPeer() {
        // Setup
        when(mockConfig.isVerifyPeer()).thenReturn(false);
        
        // Run the test
        final boolean result = configuration.isVerifyPeer();
        
        // Verify the results
        assertFalse(result);
    }
    
    @Test
    void testIsVerifyPeer_LettuceClientConfigurationReturnsTrue() {
        // Setup
        when(mockConfig.isVerifyPeer()).thenReturn(true);
        
        // Run the test
        final boolean result = configuration.isVerifyPeer();
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    void testIsStartTls() {
        // Setup
        when(mockConfig.isStartTls()).thenReturn(false);
        
        // Run the test
        final boolean result = configuration.isStartTls();
        
        // Verify the results
        assertFalse(result);
    }
    
    @Test
    void testIsStartTls_LettuceClientConfigurationReturnsTrue() {
        // Setup
        when(mockConfig.isStartTls()).thenReturn(true);
        
        // Run the test
        final boolean result = configuration.isStartTls();
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    void testGetClientResources() {
        // Setup
        // Configure LettuceClientConfiguration.getClientResources(...).
        final Optional<ClientResources> clientResources = Optional.of(
                ClientResources.builder().build());
        when(mockConfig.getClientResources()).thenReturn(clientResources);
        
        // Run the test
        final Optional<ClientResources> result = configuration.getClientResources();
        
        // Verify the results
    }
    
    @Test
    void testGetClientResources_LettuceClientConfigurationReturnsAbsent() {
        // Setup
        when(mockConfig.getClientResources()).thenReturn(Optional.empty());
        
        // Run the test
        final Optional<ClientResources> result = configuration.getClientResources();
        
        // Verify the results
        assertEquals(Optional.empty(), result);
    }
    
    @Test
    void testGetClientOptions() {
        // Setup
        // Configure LettuceClientConfiguration.getClientOptions(...).
        final Optional<ClientOptions> clientOptions = Optional.of(
                ClientOptions.builder()
                             .timeoutOptions(TimeoutOptions.builder().build())
                             .build());
        when(mockConfig.getClientOptions()).thenReturn(clientOptions);
        
        // Run the test
        final Optional<ClientOptions> result = configuration.getClientOptions();
        
        // Verify the results
    }
    
    @Test
    void testGetClientOptions_LettuceClientConfigurationReturnsAbsent() {
        // Setup
        when(mockConfig.getClientOptions()).thenReturn(Optional.empty());
        
        // Run the test
        final Optional<ClientOptions> result = configuration.getClientOptions();
        
        // Verify the results
        assertEquals(Optional.empty(), result);
    }
    
    @Test
    void testGetClientName() {
        // Setup
        when(mockConfig.getClientName()).thenReturn(Optional.of("value"));
        
        // Run the test
        final Optional<String> result = configuration.getClientName();
        
        // Verify the results
        assertEquals(Optional.of("value"), result);
    }
    
    @Test
    void testGetClientName_LettuceClientConfigurationReturnsAbsent() {
        // Setup
        when(mockConfig.getClientName()).thenReturn(Optional.empty());
        
        // Run the test
        final Optional<String> result = configuration.getClientName();
        
        // Verify the results
        assertEquals(Optional.empty(), result);
    }
    
    @Test
    void testGetReadFrom() {
        // Setup
        // Configure LettuceClientConfiguration.getReadFrom(...).
        final Optional<ReadFrom> readFrom = Optional.of(mock(ReadFrom.class));
        when(mockConfig.getReadFrom()).thenReturn(readFrom);
        
        // Run the test
        final Optional<ReadFrom> result = configuration.getReadFrom();
        
        // Verify the results
    }
    
    @Test
    void testGetReadFrom_LettuceClientConfigurationReturnsAbsent() {
        // Setup
        when(mockConfig.getReadFrom()).thenReturn(Optional.empty());
        
        // Run the test
        final Optional<ReadFrom> result = configuration.getReadFrom();
        
        // Verify the results
        assertEquals(Optional.empty(), result);
    }
    
    @Test
    void testGetCommandTimeout() {
        // Setup
        final Duration expectedResult = Duration.ofDays(0L);
        when(mockConfig.getCommandTimeout()).thenReturn(Duration.ofDays(0L));
        
        // Run the test
        final Duration result = configuration.getCommandTimeout();
        
        // Verify the results
        assertEquals(expectedResult, result);
    }
    
    @Test
    void testGetShutdownTimeout() {
        // Setup
        final Duration expectedResult = Duration.ofDays(0L);
        when(mockConfig.getShutdownTimeout()).thenReturn(Duration.ofDays(0L));
        
        // Run the test
        final Duration result = configuration.getShutdownTimeout();
        
        // Verify the results
        assertEquals(expectedResult, result);
    }
    
    @Test
    void testGetShutdownQuietPeriod() {
        // Setup
        final Duration expectedResult = Duration.ofDays(0L);
        when(mockConfig.getShutdownQuietPeriod())
                .thenReturn(Duration.ofDays(0L));
        
        // Run the test
        final Duration result = configuration.getShutdownQuietPeriod();
        
        // Verify the results
        assertEquals(expectedResult, result);
    }
    
}
