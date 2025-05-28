package io.cloudapp.redis.interceptor;

import org.junit.Before;
import org.junit.Test;

import java.util.Base64;

public class SM2EncryptInterceptorTest {
    
    private SM2EncryptInterceptor interceptor;
    
    @Before
    public void setUp() throws Exception {
        String PUBLIC_KEY = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEuqQo8miDKVdGliE9yZcL" +
                "etujqWa1khmeGPCm0GUIIGKF42VLJKH0XlGXRgDFl6d4oxIo" +
                "wvO6p6tUjZrhgaO2Iw==";
        interceptor = new SM2EncryptInterceptor(
                Base64.getDecoder().decode(PUBLIC_KEY));
    }
    
    @Test
    public void testIntercept() {
        // Setup
        // Run the test
        final byte[] result = interceptor.intercept("content".getBytes());
        
        // Verify the results
        assert result != null;
    }
    
}
