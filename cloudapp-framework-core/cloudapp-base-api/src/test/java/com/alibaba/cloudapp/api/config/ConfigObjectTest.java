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

package com.alibaba.cloudapp.api.config;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigObjectTest {
    
    private ConfigObject<String> configObjectUnderTest;
    
    @Before
    public void setUp() throws Exception {
        configObjectUnderTest = new ConfigObject.Builder<String>()
                .configName("configName")
                .refObject("refObject")
                .content("content")
                .build();
    }
    
    @Test
    public void testBuilder() {
        // Setup
        // Run the test
        final ConfigObject.Builder<String> result = ConfigObject.builder();
        
        // Verify the results
    }
    
    @Test
    public void testConfigNameGetterAndSetter() {
        final String configName = "configName";
        configObjectUnderTest.setConfigName(configName);
        assertEquals(configName, configObjectUnderTest.getConfigName());
    }
    
    @Test
    public void testContentGetterAndSetter() {
        final String content = "content";
        configObjectUnderTest.setContent(content);
        assertEquals(content, configObjectUnderTest.getContent());
    }
    
    @Test
    public void testRefObjectGetterAndSetter() {
        final Object refObject = "refObject";
        configObjectUnderTest.setRefObject(refObject);
        assertEquals(refObject, configObjectUnderTest.getRefObject());
    }
    
}
