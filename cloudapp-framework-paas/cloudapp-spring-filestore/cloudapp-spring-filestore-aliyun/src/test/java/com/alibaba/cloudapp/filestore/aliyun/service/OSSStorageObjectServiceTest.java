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

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.common.comm.ServiceClient;
import com.aliyun.oss.model.*;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OSSStorageObjectServiceTest {
    
    @Mock
    private OSS oss;
    
    private OSSStorageObjectService service;
    
    @Before
    public void setUp() {
        service = new OSSStorageObjectService(oss);
    }
    
    @Test
    public void copy_WhenBucketDoesNotExist_ShouldThrowException() {
        String bucketName = "bucket";
        String sourcePath = "source";
        String targetPath = "target";
        String targetBucket = "targetBucket";
        boolean override = false;
        
        doReturn(false).when(oss).doesBucketExist(bucketName);
        
        assertThrows(CloudAppException.class,
                     () -> service.copy(
                             bucketName, sourcePath, targetBucket, targetPath,
                             override
                     )
        );
    }
    
    @Test
    public void copy_WhenTargetAlreadyExistsAndOverrideIsFalse_ShouldThrowException() {
        String bucketName = "bucket";
        String sourcePath = "source";
        String targetPath = "target";
        String targetBucket = "targetBucket";
        boolean override = false;
        
        doReturn(true).when(oss).doesBucketExist(bucketName);
        doReturn(true).when(oss).doesBucketExist(targetBucket);
        doReturn(true).when(oss).doesObjectExist(bucketName, targetPath);
        
        assertThrows(CloudAppException.class, () ->
                service.copy(bucketName, sourcePath, targetBucket, targetPath,
                             override));
    }
    
    @Test
    public void copy_WhenSuccessful_ShouldReturnTrue() {
        String bucketName = "bucket";
        String sourcePath = "source";
        String targetPath = "target";
        String targetBucket = "targetBucket";
        boolean override = true;
        
        CopyObjectResult objectResult = new CopyObjectResult();
        ResponseMessage message = new ResponseMessage(
                new ServiceClient.Request());
        message.setStatusCode(200);
        objectResult.setResponse(message);
        
        doReturn(true).when(oss).doesBucketExist(bucketName);
        doReturn(true).when(oss).doesBucketExist(targetBucket);
        doReturn(objectResult).when(oss).copyObject(
                any(CopyObjectRequest.class));
        
        boolean result = service.copy(
                bucketName, sourcePath, targetBucket, targetPath, override
        );
        
        assertTrue(result);
        verify(oss, times(1)).copyObject(any(CopyObjectRequest.class));
    }
    
    @Test
    public void deleteObject_WhenBucketDoesNotExist_ShouldThrowException() {
        String bucketName = "bucket";
        String path = "path";
        
        doReturn(false).when(oss).doesBucketExist(bucketName);
        
        assertThrows(CloudAppException.class,
                     () -> service.deleteObject(bucketName, path)
        );
    }
    
    @Test
    public void deleteObject_WhenSuccessful_ShouldReturnTrue() {
        String bucketName = "bucket";
        String path = "path";
        
        VoidResult objectResult = new VoidResult();
        ResponseMessage message = new ResponseMessage(
                new ServiceClient.Request());
        message.setStatusCode(200);
        objectResult.setResponse(message);
        
        doReturn(true).when(oss).doesBucketExist(bucketName);
        doReturn(objectResult).when(oss).deleteObject(bucketName, path);
        
        boolean result = service.deleteObject(bucketName, path);
        
        assertTrue(result);
        verify(oss, times(1)).deleteObject(bucketName, path);
    }
    
    @Test
    public void deleteObjects_WhenBucketDoesNotExist_ShouldThrowException() {
        String bucketName = "bucket";
        Collection<String> objects = Collections.singletonList("path");
        
        doReturn(false).when(oss).doesBucketExist(bucketName);
        
        assertThrows(CloudAppException.class,
                     () -> service.deleteObjects(bucketName, objects, false)
        );
    }
    
    @Test
    public void deleteObjects_WhenSuccessful_ShouldReturnTrue() {
        String bucketName = "bucket";
        List<String> objects = Arrays.asList("path1", "path2");
        
        DeleteObjectsResult objectResult = new DeleteObjectsResult();
        ResponseMessage message = new ResponseMessage(
                new ServiceClient.Request());
        message.setStatusCode(200);
        objectResult.setResponse(message);
        objectResult.setDeletedObjects(objects);
        
        doReturn(true).when(oss).doesBucketExist(bucketName);
        doReturn(objectResult).when(oss).deleteObjects(
                any(DeleteObjectsRequest.class));
        
        boolean result = service.deleteObjects(bucketName, objects, false);
        
        assertTrue(result);
        verify(oss, times(1)).deleteObjects(any(DeleteObjectsRequest.class));
    }
    
    @Test
    public void getObject_WhenBucketDoesNotExist_ShouldThrowException() {
        String bucketName = "bucket";
        String path = "path";
        
        doReturn(false).when(oss).doesBucketExist(bucketName);
        
        assertThrows(CloudAppException.class,
                     () -> service.getObject(bucketName, path)
        );
    }
    
    @Test
    public void getObject_WhenSuccessful_ShouldReturnInputStream() {
        String bucketName = "bucket";
        String path = "path";
        InputStream expectedStream = new ByteArrayInputStream(
                "content".getBytes());
        
        OSSObject ossObject = new OSSObject();
        ossObject.setObjectContent(expectedStream);
        ossObject.setBucketName(bucketName);
        ossObject.setKey(path);
        
        doReturn(true).when(oss).doesBucketExist(bucketName);
        doReturn(ossObject).when(oss).getObject(any(GetObjectRequest.class));
        
        InputStream result = service.getObject(bucketName, path);
        
        assertNotNull(result);
        assertEquals(expectedStream, result);
        verify(oss, times(1)).getObject(any(GetObjectRequest.class));
    }
    
    @Test
    public void putObject_WhenBucketDoesNotExist_ShouldThrowException() {
        String bucketName = "bucket";
        String path = "path";
        InputStream body = new ByteArrayInputStream("content".getBytes());
        String contentType = "text/plain";
        
        doReturn(false).when(oss).doesBucketExist(bucketName);
        
        assertThrows(CloudAppException.class, () ->
                service.putObject(bucketName, path, body, contentType));
    }
    
    @Test
    public void putObject_WhenSuccessful_ShouldReturnTrue() {
        String bucketName = "bucket";
        String path = "path";
        InputStream body = new ByteArrayInputStream("content".getBytes());
        String contentType = "text/plain";
        
        PutObjectResult putObjectResult = new PutObjectResult();
        ResponseMessage message = new ResponseMessage(
                new ServiceClient.Request());
        message.setStatusCode(200);
        putObjectResult.setResponse(message);
        
        doReturn(true).when(oss).doesBucketExist(bucketName);
        doReturn(putObjectResult).when(oss).putObject(
                any(PutObjectRequest.class));
        
        boolean result = service.putObject(bucketName, path, body, contentType);
        
        assertTrue(result);
        verify(oss, times(1)).putObject(any(PutObjectRequest.class));
    }
    
    @Test
    public void listObjectVersions_WhenBucketDoesNotExist_ShouldThrowException() {
        String bucketName = "bucket";
        String path = "path";
        String sinceVersion = "sinceVersion";
        int count = 10;
        
        assertThrows(CloudAppException.class, () ->
                service.listObjectVersions(bucketName, path, sinceVersion,
                                           count
                ));
    }
    
    @Test
    public void listObjectVersions_WhenSuccessful_ShouldReturnCollection() {
        String bucketName = "bucket";
        String path = "path";
        String sinceVersion = "sinceVersion";
        int count = 10;
        
        doReturn(true).when(oss).doesBucketExist(bucketName);
        when(oss.listVersions(any(ListVersionsRequest.class)))
                .thenReturn(new VersionListing());
        
        Collection<String> result = service.listObjectVersions(bucketName, path,
                                                               sinceVersion,
                                                               count
        );
        
        assertNotNull(result);
        verify(oss, times(1)).listVersions(any(ListVersionsRequest.class));
    }
    
    @Test
    public void restoreObject_WhenBucketDoesNotExist_ShouldThrowException() {
        String bucketName = "bucket";
        String path = "path";
        int days = 7;
        String tier = "Standard";
        
        doReturn(false).when(oss).doesBucketExist(bucketName);
        
        assertThrows(CloudAppException.class, () ->
                service.restoreObject(bucketName, path, days, tier));
    }
    
    @Test
    public void restoreObject_WhenSuccessful_ShouldReturnTrue() {
        String bucketName = "bucket";
        String path = "path";
        int days = 7;
        String tier = "Standard";
        
        RestoreObjectResult restoreObjectResult = new RestoreObjectResult(200);
        ResponseMessage message = new ResponseMessage(
                new ServiceClient.Request());
        message.setStatusCode(200);
        restoreObjectResult.setResponse(message);
        
        doReturn(true).when(oss).doesBucketExist(bucketName);
        doReturn(restoreObjectResult).when(oss).restoreObject(
                any(String.class),
                any(String.class),
                any(RestoreConfiguration.class)
        );
        
        boolean result = service.restoreObject(bucketName, path, days, tier);
        
        assertTrue(result);
        verify(oss, times(1)).restoreObject(
                any(String.class),
                any(String.class),
                any(RestoreConfiguration.class)
        );
    }
    
}
