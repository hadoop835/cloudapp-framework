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

package io.cloudapp.filestore.demo.service;

import io.cloudapp.api.filestore.BucketManager;
import io.cloudapp.exeption.CloudAppException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BucketServiceTest {

    private static final String BUCKET_NAME = "mostin2-bucket";

    @Mock
    private BucketManager bucketManager;

    @InjectMocks
    private BucketDemoService bucketService;

    @Test
    public void testCreateAndDeleteBucket_Success() throws CloudAppException {
        // Setup
        when(bucketManager.createBucket(BUCKET_NAME)).thenReturn(true);
        when(bucketManager.getBucketLocation(BUCKET_NAME)).thenReturn("US");
        when(bucketManager.deleteBucket(BUCKET_NAME)).thenReturn(true);

        // Execution
        bucketService.createAndDeleteBucket();

        // Verification
        verify(bucketManager, times(1)).createBucket(BUCKET_NAME);
        verify(bucketManager, times(1)).getBucketLocation(BUCKET_NAME);
        verify(bucketManager, times(1)).deleteBucket(BUCKET_NAME);
    }

    @Test(expected = CloudAppException.class)
    public void testCreateAndDeleteBucket_FailureOnCreation() throws CloudAppException {
        // Setup
        when(bucketManager.createBucket(BUCKET_NAME)).thenThrow(new CloudAppException("Error creating bucket"));

        // Execution
        bucketService.createAndDeleteBucket();

        // Verification
        verify(bucketManager, times(1)).createBucket(BUCKET_NAME);
        verify(bucketManager, never()).getBucketLocation(BUCKET_NAME);
        verify(bucketManager, never()).deleteBucket(BUCKET_NAME);
    }

}
