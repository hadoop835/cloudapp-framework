package io.cloudapp.redis.interceptor;

import org.junit.Before;
import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.assertEquals;

public class ZucDecryptInterceptorTest {
    
    private ZucDecryptInterceptor interceptor;
    
    @Before
    public void setUp() throws Exception {
        interceptor = new ZucDecryptInterceptor(
                "1q2w3e4r5t6y7u8i".getBytes(), "qwertyui12345678".getBytes());
    }
    
    @Test
    public void testIntercept() {
        // Setup
        byte[] content = Base64.getDecoder().decode("wwqDaJevhw==");
        // Run the test
        final byte[] result = interceptor.intercept(content);
        
        // Verify the results
        assertEquals("content", new String(result));
    }
    
}
