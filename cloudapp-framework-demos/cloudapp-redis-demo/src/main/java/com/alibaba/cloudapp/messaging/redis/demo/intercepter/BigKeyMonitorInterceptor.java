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
package com.alibaba.cloudapp.messaging.redis.demo.intercepter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.cloudapp.api.cache.interceptors.MonitoringInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BigKeyMonitorInterceptor extends MonitoringInterceptor<Object> {

    private static final int MAX_KEY_SIZE = 10;
    private static final Logger logger = LoggerFactory.getLogger(BigKeyMonitorInterceptor.class);

    @Override
    public boolean needMonitoring(byte[] value) {
        return value != null && value.length > MAX_KEY_SIZE;
    }

    @Override
    public Void intercept(Object param) {
        logger.warn("big key found, current key class: {}, value:{}",
                param.getClass(), JSON.toJSONString(param));
        return null;
    }
}
