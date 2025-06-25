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

package com.alibaba.cloudapp.filestore.minio;

import com.alibaba.cloudapp.filestore.minio.service.MinioStorageObjectService;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MinioStorageObjectServiceSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            MinioStorageObjectServiceSit.class);
    
    private static final String bucketName = "test987654";
    private static final String targetBucketName = "test987654321";
    private static final String sourcePath = "test.txt";
    private static final String targetPath = "test2.txt";
    
    @Autowired
    MinioStorageObjectService minioStorageObjectService;
    
    @Test
    public void getDelegatingStorageClient() {
        Assert.assertNotNull(
                minioStorageObjectService.getDelegatingStorageClient());
        
        InputStream body = null;
        try {
            body =
                    new ClassPathResource("post.pdf").getInputStream();
            long size = body.available();
            Map headers = new HashMap<>();
//            valid storage class
            headers.put("x-amz-storage-class", "REDUCED_REDUNDANCY");
//            valid storage class
//            headers.put("x-amz-storage-class", "STANDARD");
//            Invalid storage class
//            headers.put("x-amz-storage-class", "REDUCED");
//            Invalid storage class
//            headers.put("x-amz-storage-class", "GLACIER");
            PutObjectArgs args = PutObjectArgs.builder()
                                              .bucket(bucketName)
                                              .object("post.pdf")
                                              .stream(body, size, size)
                                              .contentType("application/pdf")
                                              .extraHeaders(headers)
                                              .build();
            ObjectWriteResponse objectWriteResponse = minioStorageObjectService.getDelegatingStorageClient()
                                                                               .putObject(
                                                                                       args);
            logger.info("versionId is : " + objectWriteResponse.versionId());
            
        } catch (FileNotFoundException e) {
            logger.error("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        }
        
    }
    
    @Test
    public void copy_bucketName_sourcePath_targetPath_override() {
//        boolean copy = minioStorageObjectService.copy(bucketName, sourcePath,
//                                                      targetPath, true
//        );
//        logger.info("copy result: {} when override is true", copy);
        
        boolean copy1 = minioStorageObjectService.copy(bucketName, sourcePath
                ,targetBucketName,
                                                       targetPath, false
        );
//        boolean copy1 = minioStorageObjectService.copy(targetBucketName,
//                                                       targetPath, bucketName, sourcePath
//                ,true
//        );
        logger.info("copy result: {} when override is false", copy1);
    }
    
    @Test
    public void getObject_buckName_path() {
        InputStream object = minioStorageObjectService.getObject(bucketName,
                                                                 sourcePath
        );
        
        String targetDirectory = "D:\\test\\";
        Path directoryPath = Paths.get(targetDirectory);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (Exception e) {
                logger.error("Failed to create directory: " + e.getMessage());
                return;
            }
        }
        
        if (object != null) {
            logger.info("File get successfully");
            Path targetPath = Paths.get("D:\\test\\" + "test.txt");
            try {
                Files.copy(object, targetPath,
                           StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Test
    public void putObject_bucketName_path_InputStream_contentType() {
        InputStream body = null;
        try {
            body =
                    new ClassPathResource("post.pdf").getInputStream();
        } catch (FileNotFoundException e) {
            logger.error("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean putObject = minioStorageObjectService.putObject(bucketName,
                                                                "post.pdf",
                                                                body,
                                                                "application/pdf"
        );
        logger.info("put object result: {}", putObject);
    }
    
    @Test
    public void listObjectVersions_buckName_path_sinceVersion_count() {
        Collection<String> listObjectVersions = minioStorageObjectService.listObjectVersions(
                bucketName, "test.txt", "b91d27e8-7835-4f56-bc46-76dae66af068",
                10
        );
        logger.info("list object versions count is: {}",
                    listObjectVersions.size()
        );
        listObjectVersions.stream().forEach(
                version -> logger.info("version: {}", version));
    }
    
    @Test
    public void restoreObject_buckName_path_days_tier() {
        boolean restoreObject = minioStorageObjectService.restoreObject(
                bucketName,
                "post.pdf",
                1,
                "Expedited"
        );
        logger.info("restore object result: {}", restoreObject);
    }
    
    @Test
    public void deleteObject_buckName_path() {
        boolean deleteObject = minioStorageObjectService.deleteObject(
                bucketName,
                targetPath
        );
        logger.info("delete object result: {}", deleteObject);
    }
    
    @Test
    public void deleteObjects_buckName_Objects_checkDeleteAll() {
        Collection<String> objects = new ArrayList();
        objects.add(targetPath);
        boolean deleteObjects = minioStorageObjectService.deleteObjects(
                bucketName,
                objects,
                true
        );
        logger.info("delete object result: {}", deleteObjects);
        
        
        boolean deleteObjects1 = minioStorageObjectService.deleteObjects(
                bucketName,
                objects,
                false
        );
        logger.info("delete object result: {}", deleteObjects1);
        
        
    }
    
}
