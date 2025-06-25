package com.alibaba.cloudapp.redis.interceptor;

import com.alibaba.cloudapp.api.cache.interceptors.DecryptInterceptor;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

public class SM2DecryptInterceptor extends DecryptInterceptor {
    
    private static final Logger log =
            LoggerFactory.getLogger(SM2DecryptInterceptor.class);
    
    private SM2Engine engine;
    
    public SM2DecryptInterceptor(ECPrivateKeyParameters parameters) {
        SM2Engine engine = new SM2Engine();
        engine.init(false, parameters);
    }
    
    public SM2DecryptInterceptor(byte[] key)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        if (key == null) {
            throw new RuntimeException("key must be not null !");
        }
        
        KeySpec keySpec = new PKCS8EncodedKeySpec(key);
        
        final Provider provider = new BouncyCastleProvider();
        KeyFactory factory = KeyFactory.getInstance("EC", provider);
        
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        ECPrivateKeyParameters parameters = (ECPrivateKeyParameters)
                ECUtil.generatePrivateKeyParameter(privateKey);
        
        engine = new SM2Engine();
        engine.init(false, parameters);
    }
    
    @Override
    public byte[] intercept(byte[] data) {
        try {
            return engine.processBlock(data, 0, data.length);
        } catch (Exception e) {
            log.error("encrypt error, error:{}", e.getMessage(), e);
            return data;
        }
    }
    
}
