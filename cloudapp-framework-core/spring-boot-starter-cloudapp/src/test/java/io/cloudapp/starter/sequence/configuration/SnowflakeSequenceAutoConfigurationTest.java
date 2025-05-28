package io.cloudapp.starter.sequence.configuration;

import io.cloudapp.sequence.service.SnowflakeSequenceGenerator;
import io.cloudapp.starter.sequence.properties.CloudAppSequenceProperties;
import io.cloudapp.starter.sequence.properties.SnowflakeProperties;
import io.cloudapp.starter.sequence.refresh.SnowflakeSequenceRefreshComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnowflakeSequenceAutoConfigurationTest {
    
    private SnowflakeSequenceAutoConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new SnowflakeSequenceAutoConfiguration();
    }
    
    @Test
    void testGetComponent() {
        // Setup
        final CloudAppSequenceProperties properties = new CloudAppSequenceProperties();
        final SnowflakeProperties snowflake = new SnowflakeProperties();
        snowflake.setWorkerId(0L);
        snowflake.setWorkerIdBits(0L);
        snowflake.setSequenceBits(0L);
        snowflake.setEnabled(false);
        properties.setSnowflake(snowflake);
        
        // Run the test
        final SnowflakeSequenceRefreshComponent result = configuration.getComponent(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testGetRedisSequence() {
        // Setup
        final CloudAppSequenceProperties properties = new CloudAppSequenceProperties();
        final SnowflakeProperties snowflake = new SnowflakeProperties();
        snowflake.setWorkerId(0L);
        snowflake.setWorkerIdBits(0L);
        snowflake.setSequenceBits(0L);
        snowflake.setEnabled(false);
        properties.setSnowflake(snowflake);
        final SnowflakeSequenceRefreshComponent component = new SnowflakeSequenceRefreshComponent(
                properties);
        
        // Run the test
        final SnowflakeSequenceGenerator result = configuration.getRedisSequence(
                component);
        
        // Verify the results
    }
    
}
