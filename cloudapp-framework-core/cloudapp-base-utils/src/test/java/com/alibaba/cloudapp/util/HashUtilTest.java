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

package com.alibaba.cloudapp.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class HashUtilTest {

    private byte[] emptyByteArray;
    private byte[] singleByteArray;
    private byte[] multiByteArray;

    @Before
    public void setUp() {
        emptyByteArray = new byte[0];
        singleByteArray = new byte[]{65};
        multiByteArray = new byte[]{65, 66, 67};
    }

    @Test
    public void crc32Code_EmptyByteArray_ReturnsZero() {
        assertEquals(0L, HashUtil.crc32Code(emptyByteArray));
    }

    @Test
    public void crc32Code_SingleByteArray_ReturnsExpectedCrc32() {
        long expectedCrc32 = 3554254475L; // This value is based on the expected CRC32 checksum for the byte array [65]
        assertEquals(expectedCrc32, HashUtil.crc32Code(singleByteArray));
    }

    @Test
    public void crc32Code_MultiByteArray_ReturnsExpectedCrc32() {
        long expectedCrc32 = 2743272264L; // This value is based on the expected CRC32 checksum for the byte array [65, 66, 67]
        assertEquals(expectedCrc32, HashUtil.crc32Code(multiByteArray));
    }

    @Test
    public void crc32Code_NullByteArray_ThrowsNullPointerException() {
        assertThrows(AssertionError.class, () -> HashUtil.crc32Code(null));
    }

}
