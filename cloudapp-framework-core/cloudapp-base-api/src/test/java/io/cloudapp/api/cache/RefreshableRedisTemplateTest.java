package io.cloudapp.api.cache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class RefreshableRedisTemplateTest {
    
    @Mock
    private RedisTemplate<String, String> mockTemplate;
    
    private RefreshableRedisTemplate<String, String> redisTemplate;
    
    @Before
    public void setUp() throws Exception {
        redisTemplate =
                new RefreshableRedisTemplate<String, String>(mockTemplate) {
            
            @Override
            public void postStart() {
            
            }
            
            @Override
            public void preStop() {
            
            }
            
            @Override
            public Boolean expire(String key, Duration timeout) {
                return null;
            }
            
            @Override
            public Boolean expireAt(String key, Instant expireAt) {
                return null;
            }
            
            @Override
            public void restore(String key,
                                byte[] value,
                                long timeToLive,
                                TimeUnit unit) {
                
            }
        };
    }
    
    @Test
    public void testTemplate() {
        
        redisTemplate.preStop();
        redisTemplate.postStart();
    }
    
}
