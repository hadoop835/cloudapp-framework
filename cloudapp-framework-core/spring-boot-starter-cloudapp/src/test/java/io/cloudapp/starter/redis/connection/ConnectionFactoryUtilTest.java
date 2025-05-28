package io.cloudapp.starter.redis.connection;

import io.cloudapp.starter.redis.RedisConnectionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

class ConnectionFactoryUtilTest {
    
    @Test
    void testGetRedisConnectionFactoryBuilder() {
        // Setup
        final RedisProperties properties = new RedisProperties();
        properties.setDatabase(0);
        properties.setUrl("url");
        properties.setHost("host");
        properties.setUsername("username");
        properties.setClientType(RedisProperties.ClientType.LETTUCE);
        
        // Run the test
        final RedisConnectionFactoryBuilder result = ConnectionFactoryUtil
                .getRedisConnectionFactoryBuilder(properties);
        
        // Verify the results
    }
    
}
