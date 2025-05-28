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

package io.cloudapp.starter.redis.connection.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.util.ClassUtils;

public class GenericObjectPoolCreator {

    private static final boolean COMMONS_POOL2_AVAILABLE =
            ClassUtils.isPresent("org.apache.commons.pool2.ObjectPool",
            GenericObjectPoolCreator.class.getClassLoader());

    private static final Logger logger = LoggerFactory.getLogger(GenericObjectPoolCreator.class);

    private final RedisProperties.Pool pool;

    public GenericObjectPoolCreator(RedisProperties.Pool pool) {
        if (logger.isDebugEnabled()) {
            logger.debug("GenericObjectPoolCreator, pool available: {}", COMMONS_POOL2_AVAILABLE);
        }

        if(pool == null && COMMONS_POOL2_AVAILABLE) {
            this.pool = new RedisProperties.Pool();
        } else {
            this.pool = pool;
        }
    }

    /**
     * create new GenericObjectPoolConfig
     */
    public GenericObjectPoolConfig create() {
        if(!usePooling()) {
            logger.warn("Ignore creating pool as it is configured disabled.");
            return null;
        }

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMinIdle(pool.getMinIdle()); //Minimum number of idle
        config.setMaxIdle(pool.getMaxIdle()); //Maximum number of idle
        config.setMaxTotal(pool.getMaxActive()); //Maximum number of connections

        if (pool.getMaxWait() != null) {
            config.setMaxWait(pool.getMaxWait());//Maximum waiting time in milliseconds (ms)
        }

        if (pool.getTimeBetweenEvictionRuns() != null) {
            config.setTimeBetweenEvictionRuns(pool.getTimeBetweenEvictionRuns());
        }

        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestOnCreate(true);

        return config;
    }

    public boolean usePooling() {
        return pool != null && pool.getEnabled() != null ?
                pool.getEnabled() :  COMMONS_POOL2_AVAILABLE;
    }

}
