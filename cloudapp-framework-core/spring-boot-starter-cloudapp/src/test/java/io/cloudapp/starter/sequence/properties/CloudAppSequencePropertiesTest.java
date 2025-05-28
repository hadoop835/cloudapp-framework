package io.cloudapp.starter.sequence.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CloudAppSequencePropertiesTest {
    
    private CloudAppSequenceProperties properties;
    
    @Mock
    private ApplicationContext applicationContext;
    
    @BeforeEach
    void setUp() {
        properties = new CloudAppSequenceProperties();
        properties.setApplicationContext(applicationContext);
    }
    
    @Test
    void testSnowflakeGetterAndSetter() {
        final SnowflakeProperties snowflake = new SnowflakeProperties();
        properties.setSnowflake(snowflake);
        assertEquals(snowflake,
                     properties.getSnowflake()
        );
    }
    
    @Test
    void testRedisGetterAndSetter() {
        final RedisSequenceProperties redis = new RedisSequenceProperties();
        properties.setRedis(redis);
        assertEquals(redis, properties.getRedis());
    }
    
    @Test
    void testAfterConstruct() {
        // Setup
        // Run the test
        properties.afterConstruct();
        
        // Verify the results
    }
    
}
