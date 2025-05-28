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

package io.cloudapp.api.filestore.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BucketTest {
    
    private Bucket<String> bucketUnderTest;
    
    @Before
    public void setUp() throws Exception {
        bucketUnderTest = new Bucket.Builder<String>()
                .creationDate("creationDate")
                .region("region")
                .name("name")
                .delegatingBucket("delegatingBucket")
                .build();
    }
    
    @Test
    public void testBuilder() {
        // Setup
        // Run the test
        final Bucket.Builder<String> result = Bucket.builder();
        
        // Verify the results
    }
    
    @Test
    public void testCreationDateGetterAndSetter() {
        final String creationDate = "creationDate";
        bucketUnderTest.setCreationDate(creationDate);
        assertEquals(creationDate, bucketUnderTest.getCreationDate());
    }
    
    @Test
    public void testRegionGetterAndSetter() {
        final String region = "region";
        bucketUnderTest.setRegion(region);
        assertEquals(region, bucketUnderTest.getRegion());
    }
    
    @Test
    public void testNameGetterAndSetter() {
        final String name = "name";
        bucketUnderTest.setName(name);
        assertEquals(name, bucketUnderTest.getName());
    }
    
    @Test
    public void testDelegatingBucketGetterAndSetter() {
        final String delegatingBucket = "delegatingBucket";
        bucketUnderTest.setDelegatingBucket(delegatingBucket);
        assertEquals(delegatingBucket, bucketUnderTest.getDelegatingBucket());
    }
    
}
