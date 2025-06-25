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
package com.alibaba.cloudapp.api.cache.interceptors;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Collection;

public abstract class CacheDiskInterceptor {

    public RedisTemplate template;

    /**
     * Set key to expire at {@code millis}
     * @param key key
     * @param millis expiredTime
     */
    public abstract void expire(byte[] key, long millis);

    /**
     * remove key expire time
     */
    public abstract void persist(byte[] key);

    /**
     * delete keys
     * @param keys -
     */
    public abstract void delete(byte[]... keys);

    /**
     * Notify when value changes
     * @param keys set of key
     */
    public abstract void notifyChanged(Collection<byte[]> keys);

    /**
     * Notify when value changes
     * @param keys  key array
     */
    public void notifyChanged(byte[]... keys) {
        notifyChanged((Arrays.asList(keys)));
    }

    /**
     * Set up a delegate to get changed data
     * @param template redis template
     */
    public void setDelegate(RedisTemplate template) {
        this.template = template;
    }

    /**
     * Get delegate
     */
    public RedisTemplate getDelegate() {
        return template;
    }

}
