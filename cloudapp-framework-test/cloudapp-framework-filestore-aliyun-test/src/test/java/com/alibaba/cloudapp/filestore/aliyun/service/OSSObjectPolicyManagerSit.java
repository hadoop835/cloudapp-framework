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

import com.aliyun.oss.model.CannedAccessControlList;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class OSSObjectPolicyManagerSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            OSSObjectPolicyManagerSit.class);
    
    private static final String BUCKET_NAME = "testoss987";
    private static final String OBJECT_NAME = "test.txt";
    
    @Autowired
    @Qualifier("ossBucketPolicyManager")
    private OSSObjectPolicyManager ossObjectPolicyManager;
    
    @Before
    public void init() throws InterruptedException {
        boolean bucketExist = ossObjectPolicyManager.getDelegatingStorageClient()
                                                    .doesBucketExist(
                                                            BUCKET_NAME);
        
        if (!bucketExist) {
            ossObjectPolicyManager.getDelegatingStorageClient().createBucket(
                    BUCKET_NAME);
            Thread.sleep(3000);
            logger.info("Bucket {} is not exist,creating", BUCKET_NAME);
        }
    }
    
    @Test
    public void testGetPolicy() throws CloudAppException {
        String objectPolicy = ossObjectPolicyManager.getObjectPolicy(
                BUCKET_NAME, OBJECT_NAME);
        logger.info("object {} policy: {}", OBJECT_NAME, objectPolicy);
    }
    
    @Test
    public void testGrantAccessPermissions() throws CloudAppException {
        ossObjectPolicyManager.grantAccessPermissions(BUCKET_NAME, OBJECT_NAME,
                                                      CannedAccessControlList.Private.toString()
        );
    }
    
    @Test
    public void testDeleteObjectPolicy() throws CloudAppException {
        boolean result = ossObjectPolicyManager.deleteObjectPolicy(BUCKET_NAME,
                                                                   OBJECT_NAME
        );
        logger.info("delete object {} policy result is: {}", OBJECT_NAME,
                    result
        );
    }
    
}
