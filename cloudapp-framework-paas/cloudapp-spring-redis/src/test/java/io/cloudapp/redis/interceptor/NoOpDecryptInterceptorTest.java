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

package io.cloudapp.redis.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertArrayEquals;

@RunWith(MockitoJUnitRunner.class)
public class NoOpDecryptInterceptorTest {

    @InjectMocks
    private NoOpDecryptInterceptor decryptInterceptor;

    @Before
    public void setUp() {
        decryptInterceptor = new NoOpDecryptInterceptor();
    }

    @Test
    public void intercept_ShouldReturnSameByteArray_WhenIntercepting() {
        byte[] input = "test".getBytes();
        byte[] result = decryptInterceptor.intercept(input);
        assertArrayEquals(input, result);
    }

    @Test
    public void intercept_ShouldHandleNullArray() {
        byte[] input = null;
        byte[] result = decryptInterceptor.intercept(input);
        assertArrayEquals(input, result);
    }

    @Test
    public void intercept_ShouldHandleEmptyArray() {
        byte[] input = new byte[0];
        byte[] result = decryptInterceptor.intercept(input);
        assertArrayEquals(input, result);
    }
}
