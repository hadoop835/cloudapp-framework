package com.alibaba.cloudapp.redis.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDecryptInterceptorTest {
    
    private static final Logger logger = LoggerFactory.getLogger(
            DefaultDecryptInterceptorTest.class
    );
    
    private DefaultDecryptInterceptor interceptor;
    
    @Before
    public void setUp() throws Exception {
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKJokmFZYsCT172w" +
                "MzgdQr3AIbkxhWW8iKubS+Ang9W23CTW9FGSeX4YOdBcQHYjiPqRgGiqRus" +
                "eXXI7JwSbZJECAwEAAQ==";
        
        
        KeyFactory factory = KeyFactory.getInstance("RSA");
        KeySpec keySpec = new X509EncodedKeySpec(
                Base64.getDecoder().decode(publicKey));
        PublicKey pk = factory.generatePublic(keySpec);
        interceptor = new DefaultDecryptInterceptor(pk, "RSA");
    }
    
    @Test
    public void testIntercept() {
        String content = "ISaPauJsAFgwu9fTK0enGBT5aGLqVxsnHJyhvrKXVhYjz" +
                "/IDlRaCyNRQoMWcbznsZzMllWEJxZpR1JI5O2iViA==";
        byte[] result = interceptor.intercept(
                Base64.getDecoder().decode(content)
        );
        
        logger.debug("result:{}", new String(result));
        
        assert "content".equals(new String(result));
        
    }
    
}
