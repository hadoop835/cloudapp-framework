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

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class SecurityUtilTest {
    
    @Test
    public void testSafeMask1() {
        // Setup
        final Properties properties = new Properties();
        final Properties expectedResult = new Properties();
        
        properties.setProperty("password", "password");
        properties.setProperty("username", "username");
        properties.setProperty("user", "name");
        
        expectedResult.setProperty("password", "pass****");
        expectedResult.setProperty("username", "user****");
        expectedResult.setProperty("user", "na**");
        
        // Run the test
        final Properties result = SecurityUtil.safeMask(properties);
        
        // Verify the results
        assertEquals(expectedResult, result);
    }
    
}
