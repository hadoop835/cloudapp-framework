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

package com.alibaba.cloudapp.filestore.aliyun.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.common.comm.ServiceClient;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import com.alibaba.cloudapp.api.common.Pagination;
import com.alibaba.cloudapp.api.filestore.model.Bucket;
import com.alibaba.cloudapp.api.filestore.model.ObjectItem;
import com.alibaba.cloudapp.api.filestore.model.ObjectMetadata;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OSSBucketManagerTest {

    private static final String BUCKET_NAME = "test-bucket";
    private static final String PREFIX = "prefix";
    private static final String RESOURCE_GROUP_ID = "resource-group-id";
    private static final String LOCATION = "location";
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @Mock
    private OSS oss;

    private OSSBucketManager bucketManager;

    @Before
    public void setUp() {
        bucketManager = new OSSBucketManager(oss);
    }

    @Test
    public void listAllBucketsWithPrefix_ValidInputs_ReturnsBuckets() throws CloudAppException {
        // Arrange
        BucketList bucketList = new BucketList();
        bucketList.setBucketList(Arrays.asList(
                new com.aliyun.oss.model.Bucket("bucket1"),
                new com.aliyun.oss.model.Bucket("bucket2")
        ));

        when(oss.listBuckets(any())).thenReturn(bucketList);

        // Act
        List<Bucket<com.aliyun.oss.model.Bucket>> buckets = bucketManager
                .listAllBucketsWithPrefix(PREFIX, RESOURCE_GROUP_ID);

        // Assert
        assertEquals(2, buckets.size());
        verify(oss, times(1)).listBuckets(any(ListBucketsRequest.class));
    }

    @Test
    public void createBucket_ValidBucketName_CreatesBucket() throws CloudAppException {
        // Arrange
        com.aliyun.oss.model.Bucket bucket = new com.aliyun.oss.model.Bucket();
        bucket.setName(BUCKET_NAME);
        bucket.setRequestId("requestId");
        ResponseMessage response = new ResponseMessage(new ServiceClient.Request());
        response.setStatusCode(200);
        bucket.setResponse(response);

        when(oss.createBucket(any(CreateBucketRequest.class))).thenReturn(bucket);

        // Act
        boolean result = bucketManager.createBucket(BUCKET_NAME);

        // Assert
        assertTrue(result);
        verify(oss, times(1)).createBucket(any(CreateBucketRequest.class));
    }

    @Test
    public void listPagingBuckets_ValidInputs_ReturnsPagingBuckets() throws CloudAppException {
        // Arrange
        Pagination pagination = new Pagination<>();
        pagination.setMaxResults(100);
        BucketList bucketList = new BucketList();
        bucketList.setBucketList(Arrays.asList(
                new com.aliyun.oss.model.Bucket("bucket1"),
                new com.aliyun.oss.model.Bucket("bucket2")
        ));

        when(oss.listBuckets(any(ListBucketsRequest.class))).thenReturn(bucketList);

        // Act
        Pagination<Bucket<com.aliyun.oss.model.Bucket>> buckets = bucketManager
                .listPagingBuckets(PREFIX, RESOURCE_GROUP_ID, pagination);

        // Assert
        assertEquals(2, buckets.getDataList().size());
        verify(oss, times(1)).listBuckets(any(ListBucketsRequest.class));
    }

    @Test
    public void createBucket_ValidBucket_ReturnsBucket() throws CloudAppException {
        // Arrange
        Bucket<com.aliyun.oss.model.Bucket> bucket = new Bucket<>();
        bucket.setName(BUCKET_NAME);
        com.aliyun.oss.model.Bucket delegatingBucket = new com.aliyun.oss.model.Bucket(BUCKET_NAME);
        delegatingBucket.setLocation(LOCATION);
        bucket.setDelegatingBucket(delegatingBucket);

        when(oss.createBucket(any(CreateBucketRequest.class))).thenReturn(delegatingBucket);

        // Act
        Bucket<com.aliyun.oss.model.Bucket> result = bucketManager.createBucket(bucket);

        // Assert
        assertNotNull(result);
        verify(oss, times(1)).createBucket(any(CreateBucketRequest.class));
    }

    @Test
    public void deleteBucket_ValidBucketName_DeletesBucket() throws CloudAppException {
        // Arrange
        VoidResult voidResult = new VoidResult();
        ResponseMessage response = new ResponseMessage(new ServiceClient.Request());
        response.setStatusCode(200);
        voidResult.setResponse(response);

        when(oss.deleteBucket(any(String.class))).thenReturn(voidResult);

        // Act
        boolean result = bucketManager.deleteBucket(BUCKET_NAME);

        // Assert
        assertTrue(result);
        verify(oss, times(1)).deleteBucket(any(String.class));
    }

    @Test
    public void getBucketLocation_ValidBucketName_ReturnsLocation() throws CloudAppException {
        // Arrange
        when(oss.getBucketLocation(any(String.class))).thenReturn(LOCATION);

        // Act
        String result = bucketManager.getBucketLocation(BUCKET_NAME);

        // Assert
        assertEquals(LOCATION, result);
        verify(oss, times(1)).getBucketLocation(any(String.class));
    }

    @Test
    public void headObject_ValidInputs_ReturnsObjectMetadata() throws CloudAppException {
        // Arrange
        com.aliyun.oss.model.ObjectMetadata metadata = new com.aliyun.oss.model.ObjectMetadata();
        metadata.setHeader(OSSHeaders.ETAG, "etag");

        when(oss.headObject(any(HeadObjectRequest.class))).thenReturn(metadata);
        when(oss.doesBucketExist(any(String.class))).thenReturn(true);

        // Act
        ObjectMetadata result = bucketManager.headObject(BUCKET_NAME, "path");

        // Assert
        assertNotNull(result);
        assertEquals("etag", result.getEtag());
        verify(oss, times(1)).headObject(any(HeadObjectRequest.class));
    }

    @Test
    public void headObject_ValidInputsWithVersionId_ReturnsObjectMetadata() throws CloudAppException {
        // Arrange
        com.aliyun.oss.model.ObjectMetadata metadata = new com.aliyun.oss.model.ObjectMetadata();
        metadata.setHeader(OSSHeaders.ETAG, "etag");

        when(oss.headObject(any(HeadObjectRequest.class))).thenReturn(metadata);
        when(oss.doesBucketExist(any(String.class))).thenReturn(true);

        // Act
        ObjectMetadata result = bucketManager.headObject(BUCKET_NAME, "path", "versionId");

        // Assert
        assertNotNull(result);
        assertEquals("etag", result.getEtag());
        verify(oss, times(1)).headObject(any(HeadObjectRequest.class));
    }

    @Test
    public void listTopNObjects_ValidInputs_ReturnsObjectItems() throws CloudAppException {
        // Arrange
        ObjectListing objectListing = new ObjectListing();
        OSSObjectSummary object1 = new OSSObjectSummary();
        object1.setKey("object1");
        OSSObjectSummary object2 = new OSSObjectSummary();
        object1.setKey("object2");
        objectListing.setObjectSummaries(Arrays.asList(object1, object2));

        when(oss.listObjects(any(ListObjectsRequest.class))).thenReturn(objectListing);
        when(oss.doesBucketExist(any(String.class))).thenReturn(true);

        // Act
        Collection<ObjectItem<OSSObjectSummary>> result = bucketManager.listTopNObjects(BUCKET_NAME, 2);

        // Assert
        assertEquals(2, result.size());
        verify(oss, times(1)).listObjects(any(ListObjectsRequest.class));
    }

    @Test
    public void listObjects_ValidInputs_ReturnsPagination() throws CloudAppException {
        // Arrange
        Pagination pagination = new Pagination<>();
        pagination.setMaxResults(100);
        ObjectListing objectListing = new ObjectListing();
        OSSObjectSummary object1 = new OSSObjectSummary();
        object1.setKey("object1");
        OSSObjectSummary object2 = new OSSObjectSummary();
        object1.setKey("object2");
        objectListing.setObjectSummaries(Arrays.asList(object1, object2));

        when(oss.listObjects(any(ListObjectsRequest.class))).thenReturn(objectListing);
        when(oss.doesBucketExist(any(String.class))).thenReturn(true);

        // Act
        Pagination<ObjectItem<OSSObjectSummary>> result = bucketManager.listObjects(BUCKET_NAME, pagination);

        // Assert
        assertEquals(2, result.getDataList().size());
        verify(oss, times(1)).listObjects(any(ListObjectsRequest.class));
    }

    @Test(expected = CloudAppException.class)
    public void listAllBucketsWithPrefix_WhenClientExceptionThrown_ThrowsCloudAppException()
            throws CloudAppException {
        // Arrange
        when(oss.listBuckets(any(ListBucketsRequest.class))).thenThrow(new ClientException("ClientException", null));

        // Act
        bucketManager.listAllBucketsWithPrefix(PREFIX, RESOURCE_GROUP_ID);
    }

    @Test(expected = CloudAppException.class)
    public void createBucket_WhenClientExceptionThrown_ThrowsCloudAppException() throws CloudAppException {
        // Arrange
        when(oss.createBucket(any(CreateBucketRequest.class))).thenThrow(new ClientException("ClientException", null));

        // Act
        bucketManager.createBucket(BUCKET_NAME);
    }

    @Test(expected = CloudAppException.class)
    public void listPagingBuckets_WhenClientExceptionThrown_ThrowsCloudAppException() throws CloudAppException {
        // Arrange
        when(oss.listBuckets(any(ListBucketsRequest.class))).thenThrow(new ClientException("ClientException", null));

        // Act
        Pagination<Bucket> pagination = new Pagination<>();
        pagination.setMaxResults(1000);
        bucketManager.listPagingBuckets(PREFIX, RESOURCE_GROUP_ID, pagination);
    }

    @Test(expected = CloudAppException.class)
    public void createBucket_WhenBucketAlreadyExists_ThrowsCloudAppException() throws CloudAppException {
        // Arrange
        when(oss.createBucket(any(CreateBucketRequest.class)))
                .thenThrow(new ClientException("BucketAlreadyExists", null));

        // Act
        bucketManager.createBucket(BUCKET_NAME);
    }

    @Test(expected = CloudAppException.class)
    public void deleteBucket_WhenClientExceptionThrown_ThrowsCloudAppException() throws CloudAppException {
        // Arrange
        when(oss.deleteBucket(any(String.class))).thenThrow(new ClientException("ClientException", null));

        // Act
        bucketManager.deleteBucket(BUCKET_NAME);
    }

    @Test(expected = CloudAppException.class)
    public void getBucketLocation_WhenClientExceptionThrown_ThrowsCloudAppException() throws CloudAppException {
        // Arrange
        when(oss.getBucketLocation(any(String.class))).thenThrow(new ClientException("ClientException", null));

        // Act
        bucketManager.getBucketLocation(BUCKET_NAME);
    }

    @Test(expected = CloudAppException.class)
    public void headObject_WhenClientExceptionThrown_ThrowsCloudAppException() throws CloudAppException {
        // Arrange
        when(oss.headObject(any(HeadObjectRequest.class))).thenThrow(new ClientException("ClientException", null));
        when(oss.doesBucketExist(any(String.class))).thenReturn(true);

        // Act
        bucketManager.headObject(BUCKET_NAME, "path");
    }

    @Test(expected = CloudAppException.class)
    public void headObject_WhenClientExceptionThrownWithVersionId_ThrowsCloudAppException() throws CloudAppException {
        // Arrange
        when(oss.headObject(any(HeadObjectRequest.class))).thenThrow(new ClientException("ClientException", null));
        when(oss.doesBucketExist(any(String.class))).thenReturn(true);

        // Act
        bucketManager.headObject(BUCKET_NAME, "path", "versionId");
    }

    @Test(expected = CloudAppException.class)
    public void listTopNObjects_WhenClientExceptionThrown_ThrowsCloudAppException() throws CloudAppException {
        // Arrange
        when(oss.listObjects(any(ListObjectsRequest.class))).thenThrow(new ClientException("ClientException", null));
        when(oss.doesBucketExist(any(String.class))).thenReturn(true);

        // Act
        bucketManager.listTopNObjects(BUCKET_NAME, 2);
    }

    @Test(expected = CloudAppException.class)
    public void listObjects_WhenClientExceptionThrown_ThrowsCloudAppException() throws CloudAppException {
        // Arrange
        when(oss.listObjects(any(ListObjectsRequest.class))).thenThrow(new ClientException("ClientException", null));
        when(oss.doesBucketExist(any(String.class))).thenReturn(true);

        // Act
        Pagination<ObjectItem> pagination = new Pagination<>();
        pagination.setMaxResults(1000);

        Pagination<ObjectItem<OSSObjectSummary>> result = bucketManager.listObjects(BUCKET_NAME, pagination);
    }
}
