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
import com.alibaba.cloudapp.model.Pairs;
import io.minio.ComposeObjectArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.UploadSnowballObjectsArgs;
import okhttp3.Headers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MinioMultiPartsServiceTest {
    
    @Mock
    private MinioClient client;
    @Mock
    private ClientProvider provider;
    
    private final String bucketName = "aaa";
    private final String objectName = "bbb";
    
    private MinioMultiPartsService minioMultiPartsServiceUnderTest;
    
    @Before
    public void setUp() throws Exception {
        when(provider.getClient()).thenReturn(client);
        when(client.bucketExists(any())).thenReturn(true);
        minioMultiPartsServiceUnderTest = new MinioMultiPartsService(
                provider);
    }
    
    @Test
    public void testUploadObjects() throws Exception {
        // Setup
        final List<Pairs.Pair<String, InputStream>> objects = Collections.singletonList(
                new Pairs.Pair<>(
                        "key", new ByteArrayInputStream(new byte[5 << 20])
                ));
        final Map<String, Integer> expectedResult = Collections.singletonMap(
                "key", 1
        );
        
        when(client.uploadSnowballObjects(any(UploadSnowballObjectsArgs.class)))
                .thenReturn(
                        new ObjectWriteResponse(
                                new Headers.Builder().build(), bucketName,
                                "", objectName, "", ""
                        )
                );
        
        when(client.composeObject(any(ComposeObjectArgs.class))).thenReturn(
                new ObjectWriteResponse(
                        new Headers.Builder().build(), bucketName, "",
                        objectName, "", ""
                )
        );
        
        // Run the test
        final Map<String, Integer> result = minioMultiPartsServiceUnderTest.uploadObjects(
                bucketName, objectName, objects);
        
        // Verify the results
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testUploadBigFile() throws Exception {
        // Setup
        File file = new File("filename.txt");
        if(!file.exists() && !file.createNewFile()) {
            final boolean result = minioMultiPartsServiceUnderTest.uploadBigFile(
                    bucketName, objectName, Paths.get("filename.txt"), 0);
            // Verify the results
            assertTrue(file.delete());
            assertTrue(result);
        }
    }
    
    @Test
    public void testGetDelegatingStorageClient() {
        // Setup
        
        // Run the test
        final MinioClient result = minioMultiPartsServiceUnderTest.getDelegatingStorageClient();
        
        // Verify the results
        assertEquals(client, result);
    }
    
}
