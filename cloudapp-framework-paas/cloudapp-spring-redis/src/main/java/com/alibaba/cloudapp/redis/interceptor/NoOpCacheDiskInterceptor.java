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
package com.alibaba.cloudapp.redis.interceptor;

import com.alibaba.cloudapp.api.cache.interceptors.CacheDiskInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class NoOpCacheDiskInterceptor extends CacheDiskInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(
            NoOpCacheDiskInterceptor.class);

    @Override
    public void expire(byte[] key, long millis) {
        logger.debug("expire key");
    }

    @Override
    public void persist(byte[] key) {
        logger.debug("persist key");
    }

    @Override
    public void delete(byte[]... keys) {
        logger.debug("delete keys");
    }

    @Override
    public void notifyChanged(Collection<byte[]> keys) {
        logger.debug("notifyChanged keys");
    }

}
