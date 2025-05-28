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

package io.cloudapp.sequence.service;

import io.cloudapp.api.sequence.SequenceGenerator;
import io.cloudapp.sequence.Constants;
import io.cloudapp.sequence.exception.RedisExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisSequenceGenerator implements SequenceGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RedisSequenceGenerator.class);

    public RedisTemplate<Object, Object> redisTemplate;
    private String queueName;
    private Long step;

    public RedisSequenceGenerator(RedisTemplate<Object, Object> redisTemplate) {
        this(redisTemplate, Constants.DEFAULT_QUEUE_NAME);
    }

    public RedisSequenceGenerator(RedisTemplate<Object, Object> redisTemplate,
                                  String queueName) {
        this(redisTemplate, queueName, 1L);
    }

    public RedisSequenceGenerator(RedisTemplate<Object, Object> redisTemplate,
                                  String queueName,
                                  Long step) {
        if (logger.isDebugEnabled()) {
            logger.debug("RedisSequenceGenerator init, queueName: {}, step: {}",
                    queueName, step);
        }

        this.redisTemplate = redisTemplate;
        setStep(step);
        setQueueName(queueName);
    }

    public RedisSequenceGenerator(RedisTemplate<Object, Object> redisTemplate,
                                  Long step) {
        this(redisTemplate, Constants.DEFAULT_QUEUE_NAME, step);
    }

    @Override
    public Long nextId() {
        try {
            return this.redisTemplate
                    .opsForValue()
                    .increment(getQueueName(), getStep());
        } catch (Exception e) {
            throw new RedisExecutionException("Invoke nextId() error.", e);
        }
    }

    public Long nextId(String queueName, Long step) {
        try {
            return this.redisTemplate
                    .opsForValue()
                    .increment(queueName, step);
        } catch (Exception e) {
            throw new RedisExecutionException("Invoke nextId() error.", e);
        }
    }

    public RedisTemplate<Object, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName == null || queueName.isEmpty() ? Constants.DEFAULT_QUEUE_NAME : queueName;
    }

    public Long getStep() {
        return step;
    }

    public void setStep(Long step) {
        this.step = step == null || step <= 0 ? 1L : step;
    }

}
