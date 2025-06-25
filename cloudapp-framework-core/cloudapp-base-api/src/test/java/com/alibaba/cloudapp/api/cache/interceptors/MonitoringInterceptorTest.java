package com.alibaba.cloudapp.api.cache.interceptors;

import org.junit.Before;
import org.junit.Test;

public class MonitoringInterceptorTest {
    
    private MonitoringInterceptor<String> interceptor;
    
    @Before
    public void setUp() throws Exception {
        interceptor = new MonitoringInterceptor<String>() {
            
            @Override
            public boolean needMonitoring(byte[] value) {
                return false;
            }
            
            @Override
            public Void intercept(String param) {
                return null;
            }
        };
    }
    
    @Test
    public void testIntercept() {
        interceptor.intercept("");
    }
}
