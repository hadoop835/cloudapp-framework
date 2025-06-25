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

import com.aliyun.oss.model.Bucket;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.model.Pairs;
import com.alibaba.cloudapp.util.RandomStringGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class OSSMultiPartsServiceSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            OSSMultiPartsServiceSit.class);
    
    @Autowired
    @Qualifier("ossMultiPartsService")
    private OSSMultiPartsService ossMultiPartsService;
    
    @Test
    public void testUploadObjects()
            throws CloudAppException, IOException, InterruptedException {
        String BUCKET_NAME =
                "testoss987" + RandomStringGenerator.generate(10);
        String objectName = "post.pdf";
        boolean exists = ossMultiPartsService.getDelegatingStorageClient()
                                             .doesBucketExist(BUCKET_NAME);
        
        if (!exists) {
            logger.info("Bucket {} does not exist, creating...", BUCKET_NAME);
            Bucket bucket = ossMultiPartsService.getDelegatingStorageClient()
                                                .createBucket(
                                                        BUCKET_NAME);
            Thread.sleep(5000);
        }
        
        InputStream inputStream1 =
                new ClassPathResource("post.pdf").getInputStream();
//        InputStream inputStream2 =
//                new ClassPathResource("post2.pdf").getInputStream();
        List<Pairs.Pair<String, InputStream>> objects = new ArrayList<>();
        objects.add(new Pairs.Pair<>("postpdf", inputStream1));
//        objects.add(new Pairs.Pair<>("postpdf2", inputStream2));
        Map<String, Integer> result = ossMultiPartsService.uploadObjects(
                BUCKET_NAME, objectName, objects);
        
        while (objects.size() != result.size()) {
            logger.info("Object {} is uploading...", objectName);
            Thread.sleep(1000);
        }
        
        logger.info("Upload objects result is: {}", result);
        
        ossMultiPartsService.getDelegatingStorageClient()
                            .deleteObject(BUCKET_NAME,
                                          objectName
                            );
        
        Thread.sleep(3000);
        ossMultiPartsService.getDelegatingStorageClient().deleteBucket(
                BUCKET_NAME);
        logger.info("bucket {} is deleting", BUCKET_NAME);
    }
    
    @Test
    public void testUploadBigFile()
            throws CloudAppException, InterruptedException {
        String BUCKET_NAME =
                "testoss987" + RandomStringGenerator.generate(10);
        String objectName = "bigpost.pdf";
        boolean exists = ossMultiPartsService.getDelegatingStorageClient()
                                             .doesBucketExist(BUCKET_NAME);
        
        if (!exists) {
            logger.info("Bucket {} does not exist, creating...", BUCKET_NAME);
            Bucket bucket = ossMultiPartsService.getDelegatingStorageClient()
                                                .createBucket(
                                                        BUCKET_NAME);
            Thread.sleep(5000);
        }
        
        Path path = null;
        try {
            path = new ClassPathResource("post.pdf").getFile().toPath();
        } catch (IOException e) {
            logger.error("Error getting file path", e);
        }
        boolean result = ossMultiPartsService.uploadBigFile(BUCKET_NAME,
                                                            objectName, path,
                                                            5 * 1024 * 1024
        );
        Thread.sleep(10000);
        logger.info("Upload big file result is: {}", result);
        
        ossMultiPartsService.getDelegatingStorageClient().deleteObject(
                BUCKET_NAME, objectName);
        Thread.sleep(3000);
        logger.info("Object {} is deleting", objectName);
        
        ossMultiPartsService.getDelegatingStorageClient().deleteBucket(
                BUCKET_NAME);
        Thread.sleep(3000);
        logger.info("bucket {} is deleting", BUCKET_NAME);
        
    }
    
}
