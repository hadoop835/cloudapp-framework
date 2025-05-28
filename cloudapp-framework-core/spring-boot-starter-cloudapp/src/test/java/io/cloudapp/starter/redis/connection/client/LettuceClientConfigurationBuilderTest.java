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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LettuceClientConfigurationBuilderTest {
    
    @Mock
    private ClientResources mockClientResources;
    @Mock
    private ClientOptions mockClientOptions;
    @Mock
    private ReadFrom mockReadFrom;
    
    private LettuceClientConfigurationBuilder builder;
    
    @BeforeEach
    void setUp() {
        builder = new LettuceClientConfigurationBuilder();
        builder.useSsl(false);
        builder.verifyPeer(false);
        builder.startTls(false);
        builder.clientName("clientName");
        builder.shutdownQuietPeriod(
                Duration.ofDays(0L));
        builder.clientResources(
                mockClientResources);
        builder.clientOptions(
                mockClientOptions);
        builder.readFrom(mockReadFrom);
    }
    
    @Test
    void testBuilder() {
        // Run the test
        final LettuceClientConfigurationBuilder result = LettuceClientConfigurationBuilder.builder();
        final Duration timeout = Duration.ofDays(0L);
        assertEquals(result, result.timeout(timeout));
        
        final Duration shutdownTimeout = Duration.ofDays(0L);
        assertEquals(result, result.shutdownTimeout(shutdownTimeout));
    }
    
    @Test
    void testTimeout() {
        // Setup
        final Duration timeout = Duration.ofDays(0L);
        
        // Run the test
        final LettuceClientConfigurationBuilder result = builder.timeout(
                timeout);
        
        // Verify the results
    }
    
    @Test
    void testShutdownTimeout() {
        // Setup
        final Duration shutdownTimeout = Duration.ofDays(0L);
        
        // Run the test
        final LettuceClientConfigurationBuilder result = builder.shutdownTimeout(
                shutdownTimeout);
        
        // Verify the results
    }
    
    @Test
    void testBuild() {
        // Setup
        // Run the test
        final DistributedLettuceClientConfiguration result = builder.build();
        
        // Verify the results
    }
    
}
