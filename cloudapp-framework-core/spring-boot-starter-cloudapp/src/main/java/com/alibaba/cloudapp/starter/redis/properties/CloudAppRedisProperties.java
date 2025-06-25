/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.alibaba.cloudapp.starter.redis.properties;

import com.alibaba.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

@ConfigurationProperties(CloudAppRedisProperties.PREFIX)
public class CloudAppRedisProperties extends RefreshableProperties {
    
    public static final String PREFIX = "io.cloudapp.redis";

    /**
     * Whether to monitor hash type
     */
    private boolean hashMonitoring = false;
    /**
     * Whether to monitor value
     */
    private boolean valueMonitoring = false;
    /**
     * Whether to encrypt key
     */
    private boolean keyEncrypt = true;
    /**
     * Whether to encrypt and decrypt hash type keys and values
     */
    private boolean usedHash = false;
    
    private RedisProperties base;
    
    public boolean isHashMonitoring() {
        return hashMonitoring;
    }

    public void setHashMonitoring(boolean hashMonitoring) {
        this.hashMonitoring = hashMonitoring;
    }

    public boolean isValueMonitoring() {
        return valueMonitoring;
    }

    public void setValueMonitoring(boolean valueMonitoring) {
        this.valueMonitoring = valueMonitoring;
    }

    public boolean isKeyEncrypt() {
        return keyEncrypt;
    }

    public void setKeyEncrypt(boolean keyEncrypt) {
        this.keyEncrypt = keyEncrypt;
    }

    public boolean isUsedHash() {
        return usedHash;
    }

    public void setUsedHash(boolean usedHash) {
        this.usedHash = usedHash;
    }
    
    public RedisProperties getBase() {
        return base;
    }
    
    public void setBase(RedisProperties base) {
        this.base = base;
    }
}
