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

package com.alibaba.cloudapp.api.filestore.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectMetadataTest {
    
    private ObjectMetadata<String> objectMetadataUnderTest;
    
    @Before
    public void setUp() throws Exception {
        objectMetadataUnderTest = new ObjectMetadata.Builder<String>()
                .etag("etag")
                .size(0)
                .delegatingMetadata("delegationMetadata")
                .build();
    }
    
    @Test
    public void testEtagGetterAndSetter() {
        final String etag = "etag";
        objectMetadataUnderTest.setEtag(etag);
        assertEquals(etag, objectMetadataUnderTest.getEtag());
    }
    
    @Test
    public void testSizeGetterAndSetter() {
        final int size = 0;
        objectMetadataUnderTest.setSize(size);
        assertEquals(size, objectMetadataUnderTest.getSize());
    }
    
    @Test
    public void testDelegationMetadataGetterAndSetter() {
        final String delegationMetadata = "delegationMetadata";
        objectMetadataUnderTest.setDelegationMetadata(delegationMetadata);
        assertEquals(delegationMetadata,
                     objectMetadataUnderTest.getDelegationMetadata()
        );
    }
    
    @Test
    public void testBuilder() {
        // Setup
        // Run the test
        final ObjectMetadata.Builder<String> result = ObjectMetadata.builder();
        
        // Verify the results
    }
    
}
