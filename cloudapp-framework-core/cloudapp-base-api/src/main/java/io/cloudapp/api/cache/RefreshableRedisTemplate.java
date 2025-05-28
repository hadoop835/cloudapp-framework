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
package io.cloudapp.api.cache;

import io.cloudapp.api.cache.interceptors.Interceptor;
import io.cloudapp.api.common.ComponentLifeCycle;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;

public abstract class RefreshableRedisTemplate<K, V>
        extends RedisTemplate<K, V> implements ComponentLifeCycle {

    private Collection<Interceptor> beforeSendInterceptors;

    private Collection<Interceptor> afterReceivedInterceptors;


    public RefreshableRedisTemplate() {
        super();
    }

    public RefreshableRedisTemplate(RedisTemplate<K, V> template) {
        super();
        setConnectionFactory(template.getConnectionFactory());
        setDefaultSerializer(template.getValueSerializer());
        setKeySerializer(template.getKeySerializer());
        setHashKeySerializer(template.getHashKeySerializer());
        setHashValueSerializer(template.getHashValueSerializer());
        setStringSerializer(template.getStringSerializer());
    }


}
