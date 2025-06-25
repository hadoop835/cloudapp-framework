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

import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.filestore.minio.ClientProvider;
import io.minio.*;
import io.minio.messages.DeleteError;
import okhttp3.Headers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MinioStorageObjectServiceTest {
    
    @Mock
    private ClientProvider provider;
    @Mock
    private MinioClient client;
    
    private MinioStorageObjectService minioStorageObjectServiceUnderTest;
    
    @Before
    public void setUp() throws Exception {
        when(provider.getClient()).thenReturn(client);
        when(client.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);
        minioStorageObjectServiceUnderTest = new MinioStorageObjectService(
                provider);
    }
    
    @Test
    public void testGetDelegatingStorageClient() {
        // Setup
        // Configure ClientProvider.getClient(...).
        final MinioClient spyMinioClient = spy(
                MinioClient.builder().endpoint("http://localhost").build());
        when(provider.getClient()).thenReturn(spyMinioClient);
        
        // Run the test
        final MinioClient result = minioStorageObjectServiceUnderTest.getDelegatingStorageClient();
        
        // Verify the results
        assertEquals(spyMinioClient, result);
    }
    
    @Test
    public void testCopy() throws Exception {
        // Setup
        when(client.copyObject(any(CopyObjectArgs.class)))
                .thenReturn(mock(ObjectWriteResponse.class));
        when(client.statObject(any(StatObjectArgs.class)))
                .thenReturn(mock(StatObjectResponse.class));
        // Run the test
        final boolean result = minioStorageObjectServiceUnderTest.copy(
                "aaaa", "sourcePath",
                "bbb", "targetPath", false);
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    public void testCopy_false() throws Exception {
        StatObjectResponse resp = mock(StatObjectResponse.class);
        when(resp.etag()).thenReturn("etag");
        when(resp.deleteMarker()).thenReturn(false);
        
        // Setup
        when(client.statObject(any(StatObjectArgs.class)))
                .thenReturn(resp);
        // Run the test
        final boolean result = minioStorageObjectServiceUnderTest.copy(
                "aaaa", "sourcePath",
                "bbb", "targetPath", false);
        
        // Verify the results
        assertFalse(result);
    }
    
    @Test
    public void testDeleteObject() throws Exception {
        // Setup
        doNothing().when(client).removeObject(any(RemoveObjectArgs.class));
        // Run the test
        final boolean result = minioStorageObjectServiceUnderTest.deleteObject(
                "aaaa", "path");
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    public void testDeleteObjects() {
        Iterable<Result<DeleteError>> spyMinioClient = Collections.emptyList();
        // Setup
        when(client.removeObjects(any(RemoveObjectsArgs.class)))
                .thenReturn(spyMinioClient);
        
        // Run the test
        final boolean result = minioStorageObjectServiceUnderTest.deleteObjects(
                "aaaa", Collections.singletonList("value"), false);
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    public void testGetObject() throws Exception {
        // Setup
        ByteArrayInputStream body = new ByteArrayInputStream(
                "content".getBytes());
        Headers headers = new Headers.Builder().build();
        String bucketName = "aaaa";
        String path = "path";
        
        GetObjectResponse response = new GetObjectResponse(
                headers, bucketName, null, path, body);
        
        when(client.getObject(any(GetObjectArgs.class)))
                .thenReturn(response);
        // Run the test
        final InputStream result = minioStorageObjectServiceUnderTest.getObject(
                bucketName, path);
        
        StringBuilder builder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int len = result.read(buffer);
        while (len >= 0) {
            builder.append(new String(buffer, 0, len));
            len = result.read(buffer);
        }
        
        result.close();
        body.close();
        
        // Verify the results
        assertEquals("content", builder.toString());
    }
    
    @Test
    public void testPutObject() throws Exception {
        // Setup
        final InputStream body = new ByteArrayInputStream(new byte[5 << 20]);
        
        when(client.putObject(any(PutObjectArgs.class))).thenReturn(null);
        
        // Run the test
        final boolean result = minioStorageObjectServiceUnderTest.putObject(
                "aaaa", "path", body, "contentType");
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    public void testPutObject_BrokenBody() throws Exception {
        // Setup
        try (final InputStream body =
                     new ByteArrayInputStream(new byte[5 << 20])) {
            
            when(client.putObject(any(PutObjectArgs.class))).thenThrow(
                    new IOException());
            
            // Run the test
            assertThrows(CloudAppException.class,
                         () -> minioStorageObjectServiceUnderTest.putObject(
                                 "aaaa", "path", body, "contentType")
            );
        }
    }
    
    @Test
    public void testListObjectVersions() {
        // Setup
        when(client.listObjects(any(ListObjectsArgs.class))).thenReturn(
                Collections.emptyList());
        // Run the test
        final Collection<String> result = minioStorageObjectServiceUnderTest.listObjectVersions(
                "aaaa", "path", "sinceVersion", 0);
        
        // Verify the results
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testRestoreObject() throws Exception {
        // Setup
        doNothing().when(client).restoreObject(any(RestoreObjectArgs.class));
        // Run the test
        final boolean result = minioStorageObjectServiceUnderTest.restoreObject(
                "aaaa", "path", 1, "Standard");
        
        // Verify the results
        assertTrue(result);
    }
    
}
