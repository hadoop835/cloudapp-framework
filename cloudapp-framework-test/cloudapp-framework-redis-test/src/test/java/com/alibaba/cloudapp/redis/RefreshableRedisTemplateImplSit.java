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
package com.alibaba.cloudapp.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RefreshableRedisTemplateImplSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            RefreshableRedisTemplateImplSit.class);
    
    @Autowired
    RefreshableRedisTemplateImpl refreshableRedisTemplateImpl;
    
    @Test
    public void testTemplate() {
        Object test = refreshableRedisTemplateImpl.opsForValue().get("test");
        logger.info("test value is : " + test);
    }
    
    @Test
    public void testExecute_RedisCallback_exposeConnection_pipeline() {
        refreshableRedisTemplateImpl.execute(new RedisCallback() {
            
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                Object test = refreshableRedisTemplateImpl.opsForValue().get(
                        "test");
                logger.info("test value is : " + test);
                return test;
            }
        }, false, false);
    }
    
    @Test
    public void testExecute_SessionCallback() {
        refreshableRedisTemplateImpl.execute(new SessionCallback() {
            
            @Override
            public Object execute(RedisOperations operations)
                    throws DataAccessException {
                Object test = refreshableRedisTemplateImpl.opsForValue().get(
                        "test");
                logger.info("test value is : " + test);
                return test;
            }
        });
    }
    
    @Test
    public void testExecutePipelined_SessionCallback_RedisSerializer() {
        refreshableRedisTemplateImpl.executePipelined(new SessionCallback() {
            
            @Override
            public Object execute(RedisOperations operations)
                    throws DataAccessException {
                Object test = refreshableRedisTemplateImpl.opsForValue().get(
                        "test");
                logger.info("test value is : " + test);
                return test;
            }
        }, null);
    }
    
    @Test
    public void testExecutePipelined_RedisCallback_RedisSerializer() {
        refreshableRedisTemplateImpl.executePipelined(new RedisCallback() {
            
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                Object test = refreshableRedisTemplateImpl.opsForValue().get(
                        "test");
                logger.info("test value is : " + test);
                return null;
            }
        }, null);
    }
    
    @Test
    public void testExecuteWithStickyConnection_RedisCallback() {
        refreshableRedisTemplateImpl.executeWithStickyConnection(
                new RedisCallback() {
                    
                    @Override
                    public Object doInRedis(RedisConnection connection)
                            throws DataAccessException {
                        Object test = refreshableRedisTemplateImpl.opsForValue()
                                                                  .get(
                                                                          "test");
                        logger.info("test value is : " + test);
                        return null;
                    }
                });
    }
    
}
