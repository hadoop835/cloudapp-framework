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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ObjectItemTest {
    
    private ObjectItem<String> objectItemUnderTest;
    
    @Before
    public void setUp() throws Exception {
        objectItemUnderTest = new ObjectItem.Builder<String>()
                .objectName("objectName")
                .size(0L)
                .lastModified(new Date())
                .delegatingMetadata("delegatingMetadata")
                .versionId("versionId")
                .isLatest(false)
                .build();
        objectItemUnderTest.setObjectName("objectName");
    }
    
    @Test
    public void testBuilder() {
        // Setup
        // Run the test
        final ObjectItem.Builder<String> result = ObjectItem.builder();
        
        // Verify the results
    }
    
    @Test
    public void testEtagGetterAndSetter() {
        final String etag = "etag";
        objectItemUnderTest.setEtag(etag);
        assertEquals(etag, objectItemUnderTest.getEtag());
    }
    
    @Test
    public void testGetObjectName() {
        assertEquals("objectName", objectItemUnderTest.getObjectName());
    }
    
    @Test
    public void testEncodingTypeGetterAndSetter() {
        final String encodingType = "encodingType";
        objectItemUnderTest.setEncodingType(encodingType);
        assertEquals(encodingType, objectItemUnderTest.getEncodingType());
    }
    
    @Test
    public void testSizeGetterAndSetter() {
        final long size = 0L;
        objectItemUnderTest.setSize(size);
        assertEquals(size, objectItemUnderTest.getSize());
    }
    
    @Test
    public void testLastModifiedGetterAndSetter() {
        final Date lastModified = new GregorianCalendar(2020, Calendar.JANUARY,
                                                        1
        ).getTime();
        objectItemUnderTest.setLastModified(lastModified);
        assertEquals(lastModified, objectItemUnderTest.getLastModified());
    }
    
    @Test
    public void testVersionIdGetterAndSetter() {
        final String versionId = "versionId";
        objectItemUnderTest.setVersionId(versionId);
        assertEquals(versionId, objectItemUnderTest.getVersionId());
    }
    
    @Test
    public void testIsLatestGetterAndSetter() {
        final boolean isLatest = false;
        objectItemUnderTest.setLatest(isLatest);
        assertFalse(objectItemUnderTest.isLatest());
    }
    
    @Test
    public void testDelegatingObjectItemGetterAndSetter() {
        final String delegatingObjectItem = "delegatingObjectItem";
        objectItemUnderTest.setDelegatingObjectItem(delegatingObjectItem);
        assertEquals(delegatingObjectItem,
                     objectItemUnderTest.getDelegatingObjectItem()
        );
    }
    
}
