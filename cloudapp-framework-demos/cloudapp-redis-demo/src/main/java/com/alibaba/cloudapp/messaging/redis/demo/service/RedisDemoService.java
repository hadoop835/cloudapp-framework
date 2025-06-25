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
package com.alibaba.cloudapp.messaging.redis.demo.service;

import com.alibaba.cloudapp.redis.RefreshableRedisTemplateImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RedisDemoService implements InitializingBean {

    @Autowired
    @Qualifier("refreshableRedisTemplate")
    private RefreshableRedisTemplateImpl redisTemplate;


    @Override
    public void afterPropertiesSet() {
        redisTemplate.opsForValue().set("testValue", "test");
        System.out.println(redisTemplate.opsForValue().get("testValue"));

        redisTemplate.opsForList().rightPushAll("testList", "test", "test2", "test3");
        System.out.println(redisTemplate.opsForList().range("testList", 0, -1));

        redisTemplate.opsForGeo().add("testGeo", new Point(1.1, 2.2), "test");
        System.out.println(redisTemplate.opsForGeo().radius("testGeo", "test", 10));

        redisTemplate.opsForHash().put("testHash", "test", "test2");
        System.out.println(redisTemplate.opsForHash().get("testHash", "test"));

        redisTemplate.opsForSet().add("testSet", "test", "test2");
        System.out.println(redisTemplate.opsForSet().members("testSet"));

        redisTemplate.opsForHyperLogLog().add("testLog", Collections.singletonMap("test", "test2"));
        System.out.println(redisTemplate.opsForHyperLogLog().size("testLog"));

        RecordId id = redisTemplate.opsForStream().add("testStream", Collections.singletonMap("test", "test2"));

        redisTemplate.opsForValue().getAndDelete("testValue");
        redisTemplate.opsForList().leftPop("testList", 3);
        redisTemplate.opsForGeo().remove("testGeo", "test");
        redisTemplate.opsForHash().delete("testHash", "test");
        redisTemplate.opsForSet().remove("testSet", "test", "test2");
        redisTemplate.opsForHyperLogLog().delete("testLog");
        redisTemplate.opsForStream().delete("testStream", id);

    }
}
