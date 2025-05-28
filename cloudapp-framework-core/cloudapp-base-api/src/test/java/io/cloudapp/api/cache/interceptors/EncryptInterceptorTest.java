package io.cloudapp.api.cache.interceptors;

import org.junit.Before;
import org.junit.Test;

public class EncryptInterceptorTest {
    
    private EncryptInterceptor interceptor;
    
    @Before
    public void setUp() throws Exception {
        interceptor = new EncryptInterceptor() {
            
            @Override
            public byte[] intercept(byte[] param) {
                return new byte[0];
            }
        };
    }
    
    @Test
    public void testIntercept() {
        interceptor.intercept(null);
    }
    
}
