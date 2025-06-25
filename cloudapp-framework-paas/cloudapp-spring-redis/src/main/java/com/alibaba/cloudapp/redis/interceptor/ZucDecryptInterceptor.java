package com.alibaba.cloudapp.redis.interceptor;

import com.alibaba.cloudapp.api.cache.interceptors.DecryptInterceptor;
import org.bouncycastle.crypto.engines.Zuc128Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class ZucDecryptInterceptor extends DecryptInterceptor {
    
    private final Zuc128Engine zucEngine;
    
    public ZucDecryptInterceptor(byte[] key, byte[] iv) {
        if(key == null || iv == null) {
            throw new IllegalArgumentException("key and iv must not be null");
        }
        if(key.length != 16 || iv.length != 16) {
            throw new IllegalArgumentException("key and iv must be 16 bytes");
        }
        
        this.zucEngine = new Zuc128Engine();
        
        KeyParameter kp = new KeyParameter(key);
        ParametersWithIV parameters = new ParametersWithIV(kp, iv);
        zucEngine.init(false, parameters);
    }
    
    @Override
    public byte[] intercept(byte[] data) {
        
        byte[] result = new byte[data.length];
        
        zucEngine.processBytes(data, 0, data.length, result, 0);
        
        return result;
    }
    
}
