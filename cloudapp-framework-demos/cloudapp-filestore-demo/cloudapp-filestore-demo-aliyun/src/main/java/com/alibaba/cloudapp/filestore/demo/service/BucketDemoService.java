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

package com.alibaba.cloudapp.filestore.demo.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.cloudapp.api.filestore.BucketManager;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class BucketDemoService {

    private static final Logger logger = LoggerFactory.getLogger(BucketDemoService.class);

    @Autowired
    private BucketManager bucketManager;

    private static final String BUCKET_NAME = "bucket-create" + new Random().nextInt(30);

    public void createAndDeleteBucket() throws CloudAppException {
        boolean result = bucketManager.createBucket(BUCKET_NAME);
        logger.info("create bucket: {}", JSON.toJSONString(result));

        String location = bucketManager.getBucketLocation(BUCKET_NAME);
        logger.info("bucket location: {}",  location);

        boolean result2 = bucketManager.deleteBucket(BUCKET_NAME);
        logger.info("delete bucket: {}", result2);
    }
    
    public void createAndDeleteBucket(String bucketName) throws CloudAppException {
        boolean result = bucketManager.createBucket(bucketName);
        logger.info("create bucket: {}", JSON.toJSONString(result));
        
        String location = bucketManager.getBucketLocation(bucketName);
        logger.info("bucket location: {}",  location);
        
        boolean result2 = bucketManager.deleteBucket(bucketName);
        logger.info("delete bucket: {}", result2);
    }
}
