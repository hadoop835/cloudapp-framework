package io.cloudapp.starter.redis.connection.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;

class GenericObjectPoolCreatorTest {
    
    private GenericObjectPoolCreator poolCreator;
    
    @BeforeEach
    void setUp() {
        final RedisProperties.Pool pool = new RedisProperties.Pool();
        pool.setEnabled(false);
        pool.setMaxIdle(0);
        pool.setMinIdle(0);
        pool.setMaxActive(0);
        pool.setMaxWait(Duration.ofDays(0L));
        pool.setTimeBetweenEvictionRuns(Duration.ofDays(0L));
        poolCreator = new GenericObjectPoolCreator(pool);
    }
    
    @Test
    void testCreate() {
        // Setup
        // Run the test
        final GenericObjectPoolConfig result = poolCreator.create();
        
        // Verify the results
    }
    
    @Test
    void testUsePooling() {
        // Setup
        // Run the test
        final boolean result = poolCreator.usePooling();
        
        // Verify the results
        assertFalse(result);
    }
    
}
