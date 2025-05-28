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

package io.cloudapp.sequence.demo.configuration;


import io.cloudapp.api.sequence.SequenceGenerator;
import io.cloudapp.sequence.service.RedisSequenceGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class MultiQueueConfiguration {

    @Bean("firstQueue")
    public SequenceGenerator getFirstQueue(
            RedisTemplate<Object, Object> redisTemplate
    ) {
        return new RedisSequenceGenerator(redisTemplate, "firstQueue", 1L);
    }

    @Bean("secondQueue")
    public RedisSequenceGenerator getSecondQueue(
            RedisTemplate<Object, Object> redisTemplate
    ) {
        return new RedisSequenceGenerator(redisTemplate, "secondQueue", 1L);
    }

}
