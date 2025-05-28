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

package io.cloudapp.filestore.minio.util;

import io.cloudapp.exeption.CloudAppException;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.InvalidResponseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MinioUtilTest {
    
    @Mock
    private MinioClient client;
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void testCheckBucketExists() throws Exception {
        // Setup
        when(client.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);
        
        // Run the test
        MinioUtil.checkBucketExists(client, "aaa");
        
        // Verify the results
    }
    
    @Test
    public void testCheckBucketExists_ThrowsCloudAppException()
            throws Exception {
        // Setup
        // Setup
        when(client.bucketExists(any(BucketExistsArgs.class)))
                .thenThrow(new InvalidResponseException(
                        500, "errorMessage", "errorResponse", "123"
                ));
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> MinioUtil.checkBucketExists(client, "aaa")
        );
    }
    
}
