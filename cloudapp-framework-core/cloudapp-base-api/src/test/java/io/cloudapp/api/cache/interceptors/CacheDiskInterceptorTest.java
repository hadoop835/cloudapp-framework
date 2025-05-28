package io.cloudapp.api.cache.interceptors;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class CacheDiskInterceptorTest {
    
    private CacheDiskInterceptor interceptor;
    
    @Before
    public void setUp() throws Exception {
        interceptor = new CacheDiskInterceptor() {
            
            @Override
            public void expire(byte[] key, long millis) {
            
            }
            
            @Override
            public void persist(byte[] key) {
            
            }
            
            @Override
            public void delete(byte[]... keys) {
            
            }
            
            @Override
            public void notifyChanged(Collection<byte[]> keys) {
            
            }
        };
    }
    
    @Test
    public void testNotifyChanged2() {
        // Setup
        // Run the test
        interceptor.notifyChanged("content".getBytes());
        
        // Verify the results
    }
    
    @Test
    public void testTemplateGetterAndSetter() {
        final RedisTemplate template = new RedisTemplate<>();
        interceptor.setDelegate(template);
        assertEquals(template, interceptor.getDelegate());
    }
    
}
