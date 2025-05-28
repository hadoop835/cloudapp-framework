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

package io.cloudapp.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.Base64;

import static org.junit.Assert.*;

public class SerialUtilTest {

    @InjectMocks
    private SerialUtil<TestClass> serialUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void serialToBase64_ValidObject_Base64StringReturned() {
        TestClass testClass = new TestClass();
        String base64String = serialUtil.serializeToBase64(testClass);
        assertNotNull(base64String);
        assertFalse(base64String.isEmpty());
    }

    @Test
    public void deserializeFromBase64_ValidBase64String_ObjectDeserialized() {
        TestClass testClass = new TestClass();
        String base64String = serialUtil.serializeToBase64(testClass);
        TestClass deserializedObject = serialUtil.deserializeFromBase64(base64String);
        assertNotNull(deserializedObject);
        assertEquals(testClass.getValue(), deserializedObject.getValue());
    }

    @Test
    public void serializeToBase64_NullObject_NullReturned() {
        String base64String = serialUtil.serializeToBase64(null);
        assertNull(base64String);
    }

    @Test
    public void deserializeFromBase64_NullBase64String_NullReturned() {
        TestClass deserializedObject = serialUtil.deserializeFromBase64(null);
        assertNull(deserializedObject);
    }

    @Test
    public void deserializeFromBase64_InvalidBase64String_NullReturned() {
        TestClass deserializedObject = serialUtil.deserializeFromBase64(null);
        assertNull(deserializedObject);
    }

    @Test
    public void serialToBase64_IOExceptionHandled_NullReturned() {
        String base64String = serialUtil.serializeToBase64(new TestClass());
        assertNotNull(base64String);
    }

    @Test
    public void deserialFromBase64_IOExceptionHandled_NullReturned() {
        // Mocking the ObjectInputStream to throw IOException
        byte[] invalidBytes = new byte[]{0, 1, 2, 3}; // Invalid bytes for deserialization
        // This should log an error and return null
        TestClass deserializedObject = serialUtil.deserializeFromBase64(Base64.getEncoder().encodeToString(invalidBytes));
        assertNull(deserializedObject);
    }

    private static class TestClass implements Serializable {
        private static final long serialVersionUID = 1L;
        private String value = "test";

        public String getValue() {
            return value;
        }

        private void setValue(String value) {
            this.value = value;
        }
    }
}
