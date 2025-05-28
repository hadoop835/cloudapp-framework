package io.cloudapp.redis.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@RunWith(MockitoJUnitRunner.class)
public class DefaultEncryptInterceptorTest {
    
    private static final Logger logger = LoggerFactory.getLogger(
            DefaultEncryptInterceptorTest.class
    );
    
    private DefaultEncryptInterceptor interceptor;
    
    @Before
    public void setUp() throws Exception {
        String privateKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkE" +
                "AomiSYVliwJPXvbAzOB1CvcAhuTGFZbyIq5tL4CeD1bbcJNb0UZJ5fhg50F" +
                "xAdiOI+pGAaKpG6x5dcjsnBJtkkQIDAQABAkAJSp0Tr4IRBtd967NSde7k9" +
                "wJqG1iVZ6h61BZBy1QlVgrOiQw0IvMUcaGaFZZxtVUmXhiikayBJ97SVo+z" +
                "9mTNAiEA4KnNhXqLQsqUnpF5RIbCue64wgPBQbhGCas+mfjfhSsCIQC5D8v" +
                "dSW/zTExP5nfHj0IwarxXDvR45lk5JgC2jNcXMwIhAIZ7XnkuF7qNhVU+A3" +
                "dPq7Sc/5+zm7V5VwNmrHyCi1otAiEAl3S6IAzuNmyHOA0ikoxIW1+/bHCs3" +
                "BWJiB/2DXEOx8kCIQCmoMK0DhwDQt6qqe7TlIPC1tcrGN9WkF9XQCFnMkn0" +
                "jA==";
        
        KeySpec keySpec =
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        
        KeyFactory factory = KeyFactory.getInstance("RSA");
        
        PrivateKey pk = factory.generatePrivate(keySpec);
        interceptor = new DefaultEncryptInterceptor(
                pk, "RSA");
    }
    
    @Test
    public void testIntercept() {
        
        byte[] result = interceptor.intercept("content".getBytes());
        
        logger.debug("result:{}", Base64.getEncoder().encodeToString(result));
        
        assert result != null;
        
    }
    
}
