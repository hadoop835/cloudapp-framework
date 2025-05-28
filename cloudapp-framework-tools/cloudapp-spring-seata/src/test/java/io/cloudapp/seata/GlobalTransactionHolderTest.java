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

package io.cloudapp.seata;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GlobalTransactionHolderTest {
    
    private GlobalTransactionHolder globalTransactionHolderUnderTest;
    
    @Before
    public void setUp() {
        globalTransactionHolderUnderTest = new GlobalTransactionHolder();
    }
    
    @Test
    public void testCountGetterAndSetter() {
        final long count = 0L;
        globalTransactionHolderUnderTest.setCount(count);
        assertEquals(count, globalTransactionHolderUnderTest.getCount());
    }
    
    @Test
    public void testSucceedCountGetterAndSetter() {
        final long succeedCount = 0L;
        globalTransactionHolderUnderTest.setSucceedCount(succeedCount);
        assertEquals(succeedCount,
                     globalTransactionHolderUnderTest.getSucceedCount()
        );
    }
    
    @Test
    public void testFailedCountGetterAndSetter() {
        final long failedCount = 0L;
        globalTransactionHolderUnderTest.setFailedCount(failedCount);
        assertEquals(failedCount,
                     globalTransactionHolderUnderTest.getFailedCount()
        );
    }
    
    @Test
    public void testRunningGaugeGetterAndSetter() {
        final long runningGauge = 0L;
        globalTransactionHolderUnderTest.setRunningGauge(runningGauge);
        assertEquals(runningGauge,
                     globalTransactionHolderUnderTest.getRunningGauge()
        );
    }
    
    @Test
    public void testMaxRunningTimeGetterAndSetter() {
        final long maxRunningTime = 0L;
        globalTransactionHolderUnderTest.setMaxRunningTime(maxRunningTime);
        assertEquals(maxRunningTime,
                     globalTransactionHolderUnderTest.getMaxRunningTime()
        );
    }
    
    @Test
    public void testMinRunningTimeGetterAndSetter() {
        final long minRunningTime = 0L;
        globalTransactionHolderUnderTest.setMinRunningTime(minRunningTime);
        assertEquals(minRunningTime,
                     globalTransactionHolderUnderTest.getMinRunningTime()
        );
    }
    
}
