package io.cloudapp.starter.redis.connection.client;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.resource.ClientResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class DistributedLettuceClientConfigurationTest {
    
    @Mock
    private ClientOptions mockClientOptions;
    @Mock
    private ClientResources mockClientResources;
    @Mock
    private ReadFrom mockReadFrom;
    
    private DistributedLettuceClientConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new DistributedLettuceClientConfiguration(
                false, false, false, "clientName", Duration.ofDays(0L),
                Duration.ofDays(0L), Duration.ofDays(0L), mockClientOptions,
                mockClientResources, mockReadFrom);
    }
    
    @Test
    void testIsUseSsl() {
        assertFalse(configuration.isUseSsl());
    }
    
    @Test
    void testIsVerifyPeer() {
        assertFalse(
                configuration.isVerifyPeer());
    }
    
    @Test
    void testIsStartTls() {
        assertFalse(
                configuration.isStartTls());
    }
    
    @Test
    void testGetClientResources() {
        final Optional<ClientResources> result = configuration.getClientResources();
    }
    
    @Test
    void testGetClientOptions() {
        final Optional<ClientOptions> result = configuration.getClientOptions();
    }
    
    @Test
    void testGetClientName() {
        assertEquals(Optional.of("clientName"),
                     configuration.getClientName()
        );
    }
    
    @Test
    void testGetReadFrom() {
        final Optional<ReadFrom> result = configuration.getReadFrom();
    }
    
    @Test
    void testGetCommandTimeout() {
        assertEquals(Duration.ofDays(0L),
                     configuration.getCommandTimeout()
        );
    }
    
    @Test
    void testGetTimeout() {
        assertEquals(Duration.ofDays(0L),
                     configuration.getTimeout()
        );
    }
    
    @Test
    void testGetShutdownTimeout() {
        assertEquals(Duration.ofDays(0L),
                     configuration.getShutdownTimeout()
        );
    }
    
    @Test
    void testGetShutdownQuietPeriod() {
        assertEquals(Duration.ofDays(0L),
                     configuration.getShutdownQuietPeriod()
        );
    }
    
}
