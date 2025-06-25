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

package com.alibaba.cloudapp.sequence.service;

import com.alibaba.cloudapp.api.sequence.SequenceGenerator;
import com.alibaba.cloudapp.exeption.CloudAppInvalidArgumentsException;
import com.alibaba.cloudapp.sequence.exception.ClockMovedBackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class SnowflakeSequenceGenerator implements SequenceGenerator {

    private static final Logger logger = LoggerFactory.getLogger(
            SnowflakeSequenceGenerator.class);

    private static final long EPOCH =
            Instant.parse("2000-01-01T00:00:00Z").toEpochMilli();

    private long workerId = 1;
    private long workerIdBits = 5L;
    private long sequenceBits = 10L;
    private final long MAX_TOTAL_BITS = 63;

    private long MAX_WORKER_ID;
    private long MAX_SEQUENCE;
    private long WORK_ID_SHIFT;
    private long TIMESTAMP_LEFT_SHIFT;

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeSequenceGenerator() {
        initShiftAndMarks();
    }

    public SnowflakeSequenceGenerator(long workerId,
                                      long workerIdBits,
                                      long sequenceBits) {
        
        this.workerId = workerId;
        this.workerIdBits = workerIdBits;
        this.sequenceBits = sequenceBits;
        
        initShiftAndMarks();

        if(workerId > MAX_WORKER_ID || workerId < 0) {
            throw new CloudAppInvalidArgumentsException(
                    String.format("Worker Id %d can't be greater than %d or less than 0",
                    workerId, MAX_WORKER_ID));
        }

        if(sequenceBits > 20 || sequenceBits < 0) {
            throw new CloudAppInvalidArgumentsException(
                    String.format("Sequence bits %d can't be greater than %d or less than 0",
                    sequenceBits, 20));
        }

        if(workerIdBits > 20 || workerIdBits < 0) {
            throw new CloudAppInvalidArgumentsException(
                    String.format("Worker Id bits %d can't be greater than %d or less than 0",
                    workerIdBits, 20));
        }
        
        if(workerIdBits + sequenceBits > 22) {
            throw new CloudAppInvalidArgumentsException(
                    String.format("The sum of worker Id bits and sequence " +
                                          "bits cannot exceed %d digits", 22));
        }

    }

    private void initShiftAndMarks() {
        if (logger.isTraceEnabled()) {
            logger.trace("Initializing SnowflakeSequenceGenerator with " +
                    "workerId: {}, workerIdBits: {}, sequenceBits: {}",
                    workerId,workerIdBits, sequenceBits);
        }

        this.MAX_WORKER_ID = ~(-1L << workerIdBits);
        this.WORK_ID_SHIFT = sequenceBits;
        this.MAX_SEQUENCE =  ~(-1L << sequenceBits);
        this.TIMESTAMP_LEFT_SHIFT = sequenceBits + workerIdBits;
    }


    public synchronized Long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new ClockMovedBackException(
                    String.format("Clock moved backwards. Refusing to generate id" +
                            " for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // When seq is 0, it means that seq will be randomized
                // starting from the next millisecond.
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT) |
                (workerId << WORK_ID_SHIFT) | sequence;
    }

    /**
     * Get the next milliseconds
     *
     * @param lastTimestamp Current milliseconds
     * @return milliseconds
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }

        return timestamp;
    }

    /**
     * Returns the current time in milliseconds. Note that while the unit of
     * time of the return value is a millisecond, the granularity of the value
     * depends on the underlying operating system and may be larger.
     * For example, many operating systems measure time in units of tens of
     * milliseconds.
     *
     * @return millisecond
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
    
}
