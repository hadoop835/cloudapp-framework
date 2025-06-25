package com.alibaba.cloudapp.api.cache.interceptors;

import org.junit.Before;
import org.junit.Test;

public class DecryptInterceptorTest {
    
    private DecryptInterceptor interceptor;
    
    @Before
    public void setUp() throws Exception {
        interceptor = new DecryptInterceptor() {
            
            @Override
            public byte[] intercept(byte[] param) {
                return new byte[0];
            }
        };
    }
    
    @Test
    public void testIntercept() {
        interceptor.intercept(new byte[0]);
    }
    
}
