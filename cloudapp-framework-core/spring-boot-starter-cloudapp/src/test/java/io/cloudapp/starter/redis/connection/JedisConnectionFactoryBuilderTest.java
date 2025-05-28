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

package io.cloudapp.starter.redis.connection;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JedisConnectionFactoryBuilderTest {

    private JedisConnectionFactoryBuilder factoryBuilder;

    @Before
    public void setUp() {
        factoryBuilder = new JedisConnectionFactoryBuilder(new RedisProperties());
    }

    @Test
    public void build_ShouldCreateJedisConnectionFactory_WhenValidParametersProvided() {
        factoryBuilder.initConnectFactory();
        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory)
                factoryBuilder.getConnectionFactory();
        
        assertNotNull(jedisConnectionFactory);
        assertTrue(jedisConnectionFactory instanceof JedisConnectionFactory);
    }

    @Test(expected = NullPointerException.class)
    public void build_ShouldThrowNullPointerException_WhenInvalidParametersProvided() {
        factoryBuilder = new JedisConnectionFactoryBuilder(null);
        factoryBuilder.afterPropertiesSet();
    }

}
