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

package com.alibaba.cloudapp.filestore.minio.service;

import com.alibaba.cloudapp.api.common.Pagination;
import com.alibaba.cloudapp.api.filestore.model.ObjectItem;
import com.alibaba.cloudapp.api.filestore.model.ObjectMetadata;
import com.alibaba.cloudapp.exeption.CloudAppInvalidRequestException;
import com.alibaba.cloudapp.filestore.minio.ClientProvider;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MinioBucketManagerTest {
    
    @Mock
    private MinioClient client;
    @Mock
    private ClientProvider provider;
    
    private final String bucketName = "aaa";
    private final String objectName = "bbb";
    
    private MinioBucketManager minioBucketManagerUnderTest;
    
    @Before
    public void setUp() throws Exception {
        when(provider.getClient()).thenReturn(client);
        when(client.bucketExists(any())).thenReturn(true);
        
        minioBucketManagerUnderTest = new MinioBucketManager(provider);
    }
    
    @Test
    public void testListAllBucketsWithPrefix() throws Exception {
        // Setup
        when(client.listBuckets()).thenReturn(Collections.emptyList());
        
        // Run the test
        final List<com.alibaba.cloudapp.api.filestore.model.Bucket<Bucket>> result = minioBucketManagerUnderTest.listAllBucketsWithPrefix(
                "prefix", "resourceGroupId");
        
        // Verify the results
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testListPagingBuckets() {
        assertThrows(CloudAppInvalidRequestException.class,
                     () -> minioBucketManagerUnderTest.listPagingBuckets(
                             "prefix", "resourceGroupId", new Pagination<>())
        );
    }
    
    @Test
    public void testCreateBucket1() throws Exception {
        // Setup
        doNothing().when(client).makeBucket(any(MakeBucketArgs.class));
        
        // Run the test
        final boolean result = minioBucketManagerUnderTest.createBucket(
                bucketName);
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    public void testCreateBucket2() throws Exception {
        // Setup
        final com.alibaba.cloudapp.api.filestore.model.Bucket<Bucket> bucket = new com.alibaba.cloudapp.api.filestore.model.Bucket<>();
        bucket.setCreationDate("creationDate");
        bucket.setRegion("region");
        bucket.setName("name");
        bucket.setDelegatingBucket(new Bucket());
        
        doNothing().when(client).makeBucket(any(MakeBucketArgs.class));
        
        // Run the test
        final com.alibaba.cloudapp.api.filestore.model.Bucket<Bucket> result = minioBucketManagerUnderTest.createBucket(
                bucket);
        
        // Verify the results
        assertEquals("name", result.getName());
    }
    
    @Test
    public void testDeleteBucket() throws Exception {
        // Setup
        doNothing().when(client).removeBucket(any(RemoveBucketArgs.class));
        
        // Run the test
        final boolean result = minioBucketManagerUnderTest.deleteBucket(
                bucketName);
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    public void testGetBucketLocation() {
        assertThrows(CloudAppInvalidRequestException.class,
                     () -> minioBucketManagerUnderTest.getBucketLocation(
                             bucketName)
        );
    }
    
    @Test
    public void testHeadObject1() throws Exception {
        
        // Setup
        final StatObjectResponse moc = mock(StatObjectResponse.class);
        
        when(client.statObject(any(StatObjectArgs.class))).thenReturn(moc);
        
        // Run the test
        final ObjectMetadata<StatObjectResponse> result = minioBucketManagerUnderTest.headObject(
                bucketName, "objectName");
        
        // Verify the results
        assertEquals(moc, result.getDelegationMetadata());
    }
    
    @Test
    public void testHeadObject2() throws Exception {
        // Setup
        final StatObjectResponse moc = mock(StatObjectResponse.class);
        when(client.statObject(any(StatObjectArgs.class))).thenReturn(moc);
        
        // Run the test
        final ObjectMetadata<StatObjectResponse> result = minioBucketManagerUnderTest.headObject(
                bucketName, "objectName", "versionId");
        
        // Verify the results
        assertEquals(moc, result.getDelegationMetadata());
    }
    
    @Test
    public void testListTopNObjects() {
        // Setup
        when(client.listObjects(any(ListObjectsArgs.class)))
                .thenReturn(Collections.emptyList());
        
        // Run the test
        final Collection<ObjectItem<Item>> result = minioBucketManagerUnderTest.listTopNObjects(
                bucketName, 10);
        
        // Verify the results
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testListObjects() {
        // Setup
        final Pagination<ObjectItem<Item>> pageMarker = new Pagination<>();
        pageMarker.setDataList(Collections.emptyList());
        pageMarker.setMaxResults(10);
        pageMarker.setHasNext(false);
        pageMarker.setNextToken("nextToken");
        
        when(client.listObjects(any(ListObjectsArgs.class))).thenReturn(Collections.emptyList());
        
        
        // Run the test
        final Pagination<ObjectItem<Item>> result = minioBucketManagerUnderTest.listObjects(
                bucketName, pageMarker);
        
        // Verify the results
        assertEquals(Collections.emptyList(), result.getDataList());
    }
    
    @Test
    public void testGetDelegatingStorageClient() {
        // Setup
        
        // Run the test
        final MinioClient result = minioBucketManagerUnderTest.getDelegatingStorageClient();
        
        // Verify the results
        assertEquals(client, result);
    }
    
}
