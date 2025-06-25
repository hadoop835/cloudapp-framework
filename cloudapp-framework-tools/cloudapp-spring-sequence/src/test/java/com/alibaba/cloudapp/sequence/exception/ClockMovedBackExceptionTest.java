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

package com.alibaba.cloudapp.sequence.exception;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClockMovedBackExceptionTest {

    private static final String TEST_MESSAGE_FORMAT = "Clock moved back from %s to %s";
    private static final String START_TIME = "10:00";
    private static final String END_TIME = "09:00";

    @Before
    public void setUp() {
        // Any setup can be placed here if needed in future
    }

    @Test
    public void testClockMovedBackException_ConstructorWithValidArguments_ShouldInitializeCorrectly() {
        // Arrange
        String expectedMessage = String.format(TEST_MESSAGE_FORMAT, START_TIME, END_TIME);

        // Act
        ClockMovedBackException exception = new ClockMovedBackException(TEST_MESSAGE_FORMAT, START_TIME, END_TIME);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals("CloudApp.ClockMoved", exception.getCode());
    }

    @Test
    public void testClockMovedBackException_ConstructorWithInvalidArguments_ShouldThrowIllegalArgumentException() {
        // Arrange
        String invalidMessageFormat = "%s ~ %s";

        // Act & Assert
        Assert.assertThrows(ClockMovedBackException.class, () -> {
            throw new ClockMovedBackException(invalidMessageFormat, START_TIME,
                                              END_TIME
            );
        });
    }
}
