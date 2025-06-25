package com.alibaba.cloudapp.redis.interceptor;

import com.alibaba.cloudapp.api.cache.interceptors.EncryptInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.PrivateKey;

public class DefaultEncryptInterceptor extends EncryptInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(
            DefaultEncryptInterceptor.class
    );
    
    private final PrivateKey privateKey;
    private final String transformation;
    
    public DefaultEncryptInterceptor(PrivateKey privateKey, String transformation) {
        this.privateKey = privateKey;
        this.transformation = transformation;
    }
    
    @Override
    public byte[] intercept(byte[] param) {
        if (param == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(param);
        } catch (Exception e) {
            logger.error("encrypt error, error:{}", e.getMessage());
            return param;
        }
    }
    
}
