package io.cloudapp.starter.redis.connection.client;

import io.cloudapp.starter.redis.connection.pool.GenericObjectPoolCreator;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LettuceClientPoolingConfigurationBuilderTest {
    
    @Mock
    private LettuceClientConfiguration mockClientConfiguration;
    @Mock
    private GenericObjectPoolCreator mockPoolCreator;
    
    private LettuceClientPoolingConfigurationBuilder builder;
    
    @BeforeEach
    void setUp() {
        builder = new LettuceClientPoolingConfigurationBuilder();
        builder.clientConfiguration(mockClientConfiguration);
        builder.poolCreator(mockPoolCreator);
    }
    
    @Test
    void testBuild() {
        // Setup
        // Configure GenericObjectPoolCreator.create(...).
        final GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxIdle(0);
        genericObjectPoolConfig.setMaxTotal(0);
        genericObjectPoolConfig.setMinIdle(0);
        when(mockPoolCreator.create()).thenReturn(genericObjectPoolConfig);
        
        // Run the test
        final DistributedLettucePoolClientConfiguration result = builder.build();
        
        // Verify the results
    }
    
}
