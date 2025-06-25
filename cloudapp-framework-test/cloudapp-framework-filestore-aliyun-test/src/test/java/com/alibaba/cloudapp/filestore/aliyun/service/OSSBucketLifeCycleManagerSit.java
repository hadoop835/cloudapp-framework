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

import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.model.LifecycleRule;
import com.aliyun.oss.model.StorageClass;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OSSBucketLifeCycleManagerSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            OSSBucketLifeCycleManagerSit.class);
    
    private static final String BUCKET_NAME = "testoss987";
    private static final String OBJECT_PATH = "te/";
    
    @Autowired
    @Qualifier("ossBucketLifeCycleManager")
    private OSSBucketLifeCycleManager ossBucketLifeCycleManager;
    
    @Before
    public void init() throws InterruptedException {
        boolean bucketExist = ossBucketLifeCycleManager.getDelegatingStorageClient()
                                                       .doesBucketExist(
                                                               BUCKET_NAME);
        
        if (!bucketExist) {
            ossBucketLifeCycleManager.getDelegatingStorageClient().createBucket(
                    BUCKET_NAME);
            Thread.sleep(3000);
            logger.info("Bucket {} is not exist,creating", BUCKET_NAME);
        }
    }
    
    @Test
    public void testGetBucketLifeCycle() throws CloudAppException {
        List<LifecycleRule> rules = ossBucketLifeCycleManager.getBucketLifeCycle(
                BUCKET_NAME);
        rules = rules == null ? Collections.emptyList() : rules;
        rules.forEach(rule -> logger.info("rule: {}", JSON.toJSONString(rule)));
    }
    
    @Test
    public void testExpireObjectsAfterVersionNoncurrentDays()
            throws CloudAppException {
        ossBucketLifeCycleManager.expireObjectsAfterVersionNoncurrentDays(
                BUCKET_NAME, "te5/", 1);
    }
    
    @Test
    public void testExpireObjectsWithCreateBefore() throws CloudAppException {
        Date date = DateUtil.parseStringToDate("2024-09-01 08:12:00",
                                               "yyyy-MM-dd HH:mm:ss"
        );
        ossBucketLifeCycleManager.expireObjectsWithCreateBefore(BUCKET_NAME,
                                                                "te3/", date
        );
        logger.info(
                "create lifecycle rule expireObjectsWithCreateBefore success");
    }
    
    @Test
    public void testExpireObjectsWithLastAccessDays() throws CloudAppException {
        ossBucketLifeCycleManager.expireObjectsWithLastAccessDays(BUCKET_NAME,
                                                                  "te4/", 1
        );
    }
    
    @Test
    public void testTransitToWithLastAccessDays() throws CloudAppException {
        ossBucketLifeCycleManager.transitToWithLastAccessDays(BUCKET_NAME,
                                                              OBJECT_PATH,
                                                              StorageClass.IA.toString(),
                                                              7
        );
    }
    
    @Test
    public void testDeleteBucketLifeCycle() throws CloudAppException {
        boolean result = ossBucketLifeCycleManager.deleteBucketLifeCycle(
                BUCKET_NAME);
        logger.info("delete bucket lifecycle result is: {}", result);
    }
    
}
