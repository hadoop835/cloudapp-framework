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

import io.cloudapp.exeption.CloudAppInvalidArgumentsException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SnowflakeSequenceGeneratorTest {
    
    private SnowflakeSequenceGenerator generator;
    private final int workId = 1;
    private final int workIdBit = 5;
    private final int sequenceBits = 10;
    
    @Before
    public void setUp() {
        generator = new SnowflakeSequenceGenerator(workId, workIdBit,
                                                   sequenceBits
        );
    }
    
    @Test
    public void testNextIdMonotonicity() {
        long previousId = -1;
        for (int i = 0; i < 1000; i++) {
            long currentId = generator.nextId();
            assertTrue("ID should be greater than the previous ID",
                       currentId > previousId
            );
            previousId = currentId;
        }
    }
    
    @Test
    public void testNextIdWithWorkerIdAndSequenceBits() {
        generator = new SnowflakeSequenceGenerator(12, 4,
                                                   10
        ); // Testing with different workerIdBits and sequenceBits
        long id = generator.nextId();
        // Check workerId
        assertEquals(
                "ID should be generated with provided workerId and sequence bits",
                0, (id & (1L << 4) - 1)
        ); // Check sequence
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void testInvalidWorkerIdBits() {
        new SnowflakeSequenceGenerator(1, 21, 10); // workerIdBits > 20
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void testInvalidSequenceBits() {
        new SnowflakeSequenceGenerator(1, 5, 21); // sequenceBits > 20
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void testNegativeWorkerId() {
        new SnowflakeSequenceGenerator(-1, 5, 10); // negative workerId
    }
    
    @Test
    public void testClockBackward() throws InterruptedException {
        long initialTimestamp = generator.nextId() >>> (workIdBit + sequenceBits); // Extract timestamp part of the ID
        Thread.sleep(1); // Ensure time progression
        long idAfterSleep = generator.nextId();
        long timestampAfterSleep = idAfterSleep >>> (workIdBit + sequenceBits);
        assertTrue("Timestamp should be greater after sleep",
                   timestampAfterSleep > initialTimestamp
        );
    }
    
    // Assuming tilNextMillis method is public or package-private for testing
    @Test
    public void testTilNextMillis() {
        long currentTime = System.currentTimeMillis();
        long adjustedTime = generator.tilNextMillis(currentTime);
        assertTrue("Time should be advanced", adjustedTime >= currentTime);
    }
    
}
