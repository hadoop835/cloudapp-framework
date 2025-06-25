package com.alibaba.cloudapp.redis.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

public class ZucEncryptInterceptorTest {
    
    private static final Logger log = LoggerFactory.getLogger(
            ZucEncryptInterceptorTest.class
    );
    
    private ZucEncryptInterceptor interceptor;
    
    @Before
    public void setUp() throws Exception {
        interceptor = new ZucEncryptInterceptor(
                "1q2w3e4r5t6y7u8i".getBytes(), "qwertyui12345678".getBytes());
    }
    
    @Test
    public void testIntercept() {
        // Setup
        // Run the test
        final byte[] result = interceptor.intercept("content".getBytes());
        
        // Verify the results
        log.debug("result: {}", Base64.getEncoder().encodeToString(result));
        
        assert result != null;
        
    }
    
}
