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

import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.filestore.minio.service.MinioBucketLifeCycleManager;
import io.minio.messages.LifecycleConfiguration;
import io.minio.messages.LifecycleRule;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MinioBucketLifeCycleManagerSit {
    
    private static final Logger logger = LoggerFactory
            .getLogger(MinioBucketLifeCycleManagerSit.class);
    
    private static final String bucketName = "test987654";
    
    @Autowired
    MinioBucketLifeCycleManager minioBucketLifeCycleManager;
    
    //    storageType is MinIO tier
    @Test
    public void transitToWithLastAccessDays_bucketName_objectPrefixName_storageType_lastAccessDays() {
        try {
            minioBucketLifeCycleManager.transitToWithLastAccessDays(bucketName,
                                                                    "testDir",
                                                                    "PLAY",
                                                                    1
            );
            logger.info("transitToWithLastAccessDays is successful");
        } catch (CloudAppException e) {
            Assert.fail(e.getMessage());
        }
    }
    
    @Test
    public void expireObjectsWithLastAccessDays_bucketName_objectPrefixName_lastAccessDays() {
        try {
            minioBucketLifeCycleManager.expireObjectsWithLastAccessDays(
                    bucketName,
                    "testDir",
                    10
            );
            logger.info("expireObjectsWithLastAccessDays is successful");
        } catch (CloudAppException e) {
            Assert.fail(e.getMessage());
        }
    }
    
    @Test
    public void expireObjectsWithCreateBefore_bucketName_objectPrefixName_createBefore() {
        try {
            
            ZonedDateTime midnightGmt = LocalDateTime.of(2023, 10, 1, 0, 0)
                                                     .atZone(ZoneId.of("GMT"));
            Date date = Date.from(midnightGmt.toInstant());
            
            minioBucketLifeCycleManager.expireObjectsWithCreateBefore(
                    bucketName,
                    "testDir",
                    date
            );
            logger.info("expireObjectsWithCreateBefore is successful");
        } catch (CloudAppException e) {
            Assert.fail(e.getMessage());
        }
    }
    
    @Test
    public void expireObjectsAfterVersionNoncurrentDays_bucketName_objectPrefixName_noncurrentDays() {
        try {
            minioBucketLifeCycleManager.expireObjectsAfterVersionNoncurrentDays(
                    bucketName, "testDir", 10);
            logger.info("expireObjectsAfterVersionNoncurrentDays is " +
                                "successful");
        } catch (CloudAppException e) {
            Assert.fail(e.getMessage());
        }
    }
    
    @Test
    public void getBucketLifeCycle_bucketName() {
        LifecycleConfiguration bucketLifeCycle = minioBucketLifeCycleManager.getBucketLifeCycle(
                bucketName);
        List<LifecycleRule> rules = bucketLifeCycle.rules();
        rules.stream().forEach(rule -> logger.info("{} lifecycle is {}",
                                                   bucketName, rule
        ));
    }
    
    @Test
    public void getDelegatingStorageClient() {
        Assert.assertNotNull(
                minioBucketLifeCycleManager.getDelegatingStorageClient());
    }
    
    @Test
    public void deleteBucketLifeCycle_bucketName() {
        try {
            minioBucketLifeCycleManager.deleteBucketLifeCycle(bucketName);
            logger.info("deleteBucketLifeCycle is successful");
        } catch (CloudAppException e) {
            Assert.fail(e.getMessage());
        }
    }
    
}
