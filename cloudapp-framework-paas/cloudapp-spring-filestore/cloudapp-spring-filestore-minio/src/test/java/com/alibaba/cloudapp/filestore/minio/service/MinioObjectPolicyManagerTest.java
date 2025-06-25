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
import io.minio.GetBucketPolicyArgs;
import io.minio.MinioClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MinioObjectPolicyManagerTest {
    
    @Mock
    private MinioClient client;
    @Mock
    private ClientProvider provider;
    
    private final String bucketName = "aaa";
    private final String objectName = "bbb";
    
    private MinioObjectPolicyManager minioObjectPolicyManagerUnderTest;
    
    @Before
    public void setUp() {
        when(provider.getClient()).thenReturn(client);
        minioObjectPolicyManagerUnderTest = new MinioObjectPolicyManager(
                provider);
    }
    
    @Test
    public void testGrantAccessPermissions() {
        assertThrows(CloudAppException.class,
                     () -> minioObjectPolicyManagerUnderTest.grantAccessPermissions(
                             bucketName, objectName, "accessAcl")
        );
    }
    
    @Test
    public void testGetObjectPolicy() throws Exception {
        // Setup
        when(client.getBucketPolicy(any(GetBucketPolicyArgs.class)))
                .thenReturn("Private");
        when(client.bucketExists(any())).thenReturn(true);
        // Run the test
        final String result = minioObjectPolicyManagerUnderTest.getObjectPolicy(
                bucketName, "objectName");
        
        // Verify the results
        assertEquals("Private", result);
    }
    
    @Test
    public void testDeleteObjectPolicy() {
        assertThrows(CloudAppException.class,
                     () -> minioObjectPolicyManagerUnderTest.deleteObjectPolicy(
                             bucketName, "objectName")
        );
    }
    
    @Test
    public void testGetDelegatingStorageClient() {
        // Setup
        // Run the test
        final MinioClient result = minioObjectPolicyManagerUnderTest.getDelegatingStorageClient();
        
        // Verify the results
        assertEquals(client, result);
    }
    
}
