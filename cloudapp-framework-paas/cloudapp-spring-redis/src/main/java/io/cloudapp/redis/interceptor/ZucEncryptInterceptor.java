package io.cloudapp.redis.interceptor;

import io.cloudapp.api.cache.interceptors.EncryptInterceptor;
import org.bouncycastle.crypto.engines.Zuc128Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class ZucEncryptInterceptor extends EncryptInterceptor {
    
    private final Zuc128Engine zucEngine;
    
    public ZucEncryptInterceptor(byte[] key, byte[] iv) {
        if(key == null || iv == null) {
            throw new IllegalArgumentException("key and iv must not be null");
        }
        if(key.length != 16 || iv.length != 16) {
            throw new IllegalArgumentException("key and iv must be 16 bytes");
        }
        
        this.zucEngine = new Zuc128Engine();
        
        KeyParameter kp = new KeyParameter(key);
        ParametersWithIV parameters = new ParametersWithIV(kp, iv);
        zucEngine.init(true, parameters);
    }
    
    @Override
    public byte[] intercept(byte[] data) {
        
        byte[] result = new byte[data.length];
        
        zucEngine.processBytes(data, 0, data.length, result, 0);
        
        return result;
    }
    
}
