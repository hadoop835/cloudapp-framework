package com.alibaba.cloudapp.redis.interceptor;

import com.alibaba.cloudapp.api.cache.interceptors.EncryptInterceptor;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SM2EncryptInterceptor extends EncryptInterceptor {
    
    private static final Logger logger =
            LoggerFactory.getLogger(SM2EncryptInterceptor.class);
    
    private final SM2Engine engine;
    
    public SM2EncryptInterceptor(byte[] key)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        
        if(key == null) {
            throw new RuntimeException("key must be not null !");
        }
        
        
        final Provider provider = new BouncyCastleProvider();
        KeyFactory factory = KeyFactory.getInstance("EC", provider);
        
        KeySpec keySpec = new X509EncodedKeySpec(key);
        ECPublicKey publicKey = (ECPublicKey) factory.generatePublic(keySpec);
        ParametersWithRandom parameters = new ParametersWithRandom(
                ECUtil.generatePublicKeyParameter(publicKey),
                new SecureRandom()
        );
        
        this.engine = new SM2Engine();
        this.engine.init(true, parameters);
    }
    
    public SM2EncryptInterceptor(ECPublicKeyParameters parameters) {
        this.engine = new SM2Engine();
        this.engine.init(true, parameters);
    }
    
    @Override
    public byte[] intercept(byte[] data) {
        try {
            return engine.processBlock(data, 0, data.length);
        } catch (InvalidCipherTextException e) {
            logger.error("encrypt error, error:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
}
