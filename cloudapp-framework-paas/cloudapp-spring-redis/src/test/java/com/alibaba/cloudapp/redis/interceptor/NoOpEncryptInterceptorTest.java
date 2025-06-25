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

package com.alibaba.cloudapp.redis.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertArrayEquals;

@RunWith(MockitoJUnitRunner.class)
public class NoOpEncryptInterceptorTest {

    @InjectMocks
    private NoOpEncryptInterceptor noOpEncryptInterceptor;

    @Before
    public void setUp() {
        // Any setup can be placed here
    }

    @Test
    public void intercept_WithValidInput_ShouldReturnSameByteArray() {
        // Arrange
        byte[] input = "test".getBytes();
        byte[] expectedOutput = input.clone();

        // Act
        byte[] actualOutput = noOpEncryptInterceptor.intercept(input);

        // Assert
        assertArrayEquals("The encrypted byte array should match the input byte array", expectedOutput, actualOutput);
    }

    @Test
    public void intercept_WithEmptyInput_ShouldReturnSameByteArray() {
        // Arrange
        byte[] input = new byte[0];
        byte[] expectedOutput = input.clone();

        // Act
        byte[] actualOutput = noOpEncryptInterceptor.intercept(input);

        // Assert
        assertArrayEquals("The encrypted byte array should match the input byte array even if it's empty", expectedOutput, actualOutput);
    }

    @Test
    public void intercept_WithNullInput_ShouldReturnNull() {
        // Arrange
        byte[] input = null;
        byte[] expectedOutput = null;

        // Act
        byte[] actualOutput = noOpEncryptInterceptor.intercept(input);

        // Assert
        assertArrayEquals("The encrypted byte array should be null when input is null", expectedOutput, actualOutput);
    }
}
