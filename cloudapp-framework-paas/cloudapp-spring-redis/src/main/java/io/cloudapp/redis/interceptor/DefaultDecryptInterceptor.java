package io.cloudapp.redis.interceptor;

import io.cloudapp.api.cache.interceptors.DecryptInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.PublicKey;

public class DefaultDecryptInterceptor extends DecryptInterceptor {
    
    private static final Logger log =
            LoggerFactory.getLogger(DefaultDecryptInterceptor.class);
    
    private final PublicKey publicKey;
    private final String transformation;
    
    public DefaultDecryptInterceptor(PublicKey publicKey, String transformation) {
        this.publicKey = publicKey;
        this.transformation = transformation;
    }
    
    @Override
    public byte[] intercept(byte[] param) {
        if (param == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(param);
        } catch (Exception e) {
            log.error("encrypt error, error:{}", e.getMessage(), e);
            return param;
        }
    }
    
}
