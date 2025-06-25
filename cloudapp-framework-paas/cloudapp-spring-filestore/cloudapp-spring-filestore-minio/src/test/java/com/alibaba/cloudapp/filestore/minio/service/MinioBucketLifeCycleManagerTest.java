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

import com.alibaba.cloudapp.filestore.minio.ClientProvider;
import io.minio.DeleteBucketLifecycleArgs;
import io.minio.MinioClient;
import io.minio.SetBucketLifecycleArgs;
import io.minio.messages.LifecycleConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MinioBucketLifeCycleManagerTest {
    
    @Mock
    private MinioClient client;
    @Mock
    private ClientProvider provider;
    
    private final String bucketName = "aaa";
    
    private MinioBucketLifeCycleManager lifeCycleManager;
    
    @Before
    public void setUp() throws Exception {
        when(provider.getClient()).thenReturn(client);
        when(client.bucketExists(any())).thenReturn(true);
        
        lifeCycleManager = new MinioBucketLifeCycleManager(provider);
    }
    
    @Test
    public void testTransitToWithLastAccessDays() throws Exception {
        // Setup
        doNothing().when(client)
                   .setBucketLifecycle(any(SetBucketLifecycleArgs.class));
        
        // Run the test
        lifeCycleManager.transitToWithLastAccessDays(
                bucketName, "objectPrefixName", "storageType", 0);
        
        // Verify the results
    }
    
    @Test
    public void testExpireObjectsWithLastAccessDays() throws Exception {
        // Setup
        doNothing().when(client)
                   .setBucketLifecycle(any(SetBucketLifecycleArgs.class));
        
        // Run the test
        lifeCycleManager.expireObjectsWithLastAccessDays(
                bucketName, "objectPrefixName", 1);
        
        // Verify the results
    }
    
    @Test
    public void testExpireObjectsWithCreateBefore() throws Exception {
        // Setup
        doNothing().when(client)
                   .setBucketLifecycle(any(SetBucketLifecycleArgs.class));
        
        // Run the test
        lifeCycleManager.expireObjectsWithCreateBefore(
                bucketName, "objectPrefixName",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()
        );
        
        // Verify the results
    }
    
    @Test
    public void testExpireObjectsAfterVersionNoncurrentDays() throws Exception {
        // Setup
        doNothing().when(client)
                .setBucketLifecycle(any(SetBucketLifecycleArgs.class));
        
        // Run the test
        lifeCycleManager.expireObjectsAfterVersionNoncurrentDays(
                bucketName, "objectPrefixName", 0);
        
        // Verify the results
    }
    
    @Test
    public void testGetBucketLifeCycle() throws Exception {
        // Setup
        LifecycleConfiguration configuration = mock(LifecycleConfiguration.class);
        when(client.getBucketLifecycle(any())).thenReturn(configuration);
        
        // Run the test
        final LifecycleConfiguration result = lifeCycleManager.getBucketLifeCycle(
                bucketName);
        
        // Verify the results
        assertEquals(configuration, result);
    }
    
    @Test
    public void testDeleteBucketLifeCycle() throws Exception {
        // Setup
        doNothing().when(client)
                .deleteBucketLifecycle(any(DeleteBucketLifecycleArgs.class));
        
        // Run the test
        final boolean result = lifeCycleManager.deleteBucketLifeCycle(
                bucketName);
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    public void testGetDelegatingStorageClient() {
        // Setup
        
        // Run the test
        final MinioClient result = lifeCycleManager.getDelegatingStorageClient();
        
        // Verify the results
        assertEquals(client, result);
    }
    
}
