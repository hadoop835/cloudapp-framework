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

package io.cloudapp.filestore.minio;

import io.cloudapp.filestore.minio.service.MinioMultiPartsService;
import io.cloudapp.model.Pairs;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.errors.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MinioMultiPartsServiceSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            MinioMultiPartsServiceSit.class);
    
    private static final String BUCKET_NAME = "test987654";
    
    @Autowired
    MinioMultiPartsService minioMultiPartsService;
    
    @Test
    public void uploadObjects_bucketName_objectName_objects() {
        String objectName = "post.pdf";
        boolean exists = false;
        try {
            exists = minioMultiPartsService.getDelegatingStorageClient()
                                           .bucketExists(
                                                   BucketExistsArgs.builder()
                                                                   .bucket(BUCKET_NAME)
                                                                   .build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        
        if (!exists) {
            logger.info("Bucket {} does not exist, creating...", BUCKET_NAME);
            try {
                minioMultiPartsService.getDelegatingStorageClient().makeBucket(
                        MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
                Thread.sleep(5000);
            } catch (ErrorResponseException e) {
                e.printStackTrace();
            } catch (InsufficientDataException e) {
                e.printStackTrace();
            } catch (InternalException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidResponseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (XmlParserException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        InputStream inputStream1 =
                null;
        try {
            inputStream1 = new ClassPathResource("post.pdf").getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        InputStream inputStream2 =
//                new ClassPathResource("post2.pdf").getInputStream();
        List<Pairs.Pair<String, InputStream>> objects = new ArrayList<>();
        objects.add(new Pairs.Pair<>("postpdf", inputStream1));
//        objects.add(new Pairs.Pair<>("postpdf2", inputStream2));
        Map<String, Integer> result = minioMultiPartsService.uploadObjects(
                BUCKET_NAME, objectName, objects);
        
        while (objects.size() != result.size()) {
            logger.info("Object {} is uploading...", objectName);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        logger.info("Upload objects result is: {}", result);
        
    }
    
    @Test
    public void uploadBigFile_bucketName_objectName_filePath_maxSizePerPart() {
        String objectName = "bigpost.pdf";
        boolean exists = false;
        try {
            exists = minioMultiPartsService.getDelegatingStorageClient()
                                           .bucketExists(
                                                   BucketExistsArgs.builder()
                                                                   .bucket(BUCKET_NAME)
                                                                   .build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        
        if (!exists) {
            logger.info("Bucket {} does not exist, creating...", BUCKET_NAME);
            try {
                minioMultiPartsService.getDelegatingStorageClient()
                                      .makeBucket(MakeBucketArgs.builder()
                                                                .bucket(BUCKET_NAME)
                                                                .build());
                Thread.sleep(5000);
            } catch (ErrorResponseException e) {
                e.printStackTrace();
            } catch (InsufficientDataException e) {
                e.printStackTrace();
            } catch (InternalException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidResponseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (XmlParserException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        Path path = null;
        try {
            path = new ClassPathResource("post.pdf").getFile().toPath();
        } catch (IOException e) {
            logger.error("Error getting file path", e);
        }
        boolean result = minioMultiPartsService.uploadBigFile(BUCKET_NAME,
                                                              objectName, path,
                                                              5 * 1024 * 1024
        );
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Upload big file result is: {}", result);
        
    }
    
    @Test
    public void getDelegatingStorageClient() {
        Assert.assertNotNull(
                minioMultiPartsService.getDelegatingStorageClient());
    }
    
}
