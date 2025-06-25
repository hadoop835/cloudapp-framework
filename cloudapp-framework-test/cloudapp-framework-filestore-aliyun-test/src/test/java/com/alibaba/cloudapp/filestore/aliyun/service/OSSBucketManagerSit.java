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

import com.aliyun.oss.model.OSSObjectSummary;
import com.alibaba.cloudapp.api.common.Pagination;
import com.alibaba.cloudapp.api.filestore.model.Bucket;
import com.alibaba.cloudapp.api.filestore.model.ObjectItem;
import com.alibaba.cloudapp.api.filestore.model.ObjectMetadata;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.util.RandomStringGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class OSSBucketManagerSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            OSSBucketManagerSit.class);
    
    private static final String RESOURCE_GROUP_ID = "rg-acfmz5d2gpm2raa";
    private static final String PREFIX = "";
    private static final String BUCKET_NAME = "testoss987";
    private static final String OBJECT_PATH = "test.txt";
    private static final String LOCATION = "oss-cn-shenzhen";
    private static final String TEST_BUCKET_NAME = "testbucket"
            + LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-ddHHmmss"));
    private static final String BUCKET_NAME_TO_DELETE =
            "buckettodelete123456789";
    @Autowired
    @Qualifier("ossBucketManager")
    private OSSBucketManager bucketManager;
    
    @Test
    public void testListAllBucketsWithPrefix() throws CloudAppException {
        logger.info("AK:" + System.getenv("AK") + "\n" + "SK:" + System.getenv(
                "SK"));
        List<Bucket<com.aliyun.oss.model.Bucket>> buckets = bucketManager
                .listAllBucketsWithPrefix(PREFIX, RESOURCE_GROUP_ID);
        buckets.stream()
               .map(Bucket::getName)
               .forEach(name -> logger.info("bucket name is: {}", name));
    }
    
    @Test
    public void testListPagingBuckets() throws CloudAppException {
        Pagination<Bucket<com.aliyun.oss.model.Bucket>> pagination = bucketManager.listPagingBuckets(
                PREFIX, RESOURCE_GROUP_ID,
                new Pagination<com.aliyun.oss.model.Bucket>().builder()
                                                             .maxResults(5)
                                                             .build()
        );
        
        pagination.getDataList().stream()
                  .map(Bucket::getName)
                  .forEach(name -> logger.info("bucket name is: {}", name));
    }
    
    
    @Test
    public void testCreateBucketByName()
            throws CloudAppException, InterruptedException {
        boolean bucketExist = bucketManager.getDelegatingStorageClient()
                                           .doesBucketExist(
                                                   BUCKET_NAME_TO_DELETE);
        String randomBucketName = "";
        
        if (bucketExist) {
            randomBucketName =
                    TEST_BUCKET_NAME + RandomStringGenerator.generate(10);
        } else {
            randomBucketName = TEST_BUCKET_NAME;
        }
        boolean result = bucketManager.createBucket(randomBucketName);
        Thread.sleep(5000);
        logger.info("create bucket {} result is: {}", randomBucketName,
                    result
        );
        bucketManager.deleteBucket(randomBucketName);
        Thread.sleep(1000);
        logger.info("bucket {} is deleted", randomBucketName);
    }
    
    @Test
    public void testCreateBucketByEntity()
            throws CloudAppException, InterruptedException {
        Bucket<com.aliyun.oss.model.Bucket> bucket = new Bucket<>();
        bucket.setName("bucket-by-test-entity-"
                               + LocalDateTime.now()
                                              .format(DateTimeFormatter.ofPattern(
                                                      "yyyyMMddHHmmss")));
        bucket.setDelegatingBucket(new com.aliyun.oss.model.Bucket());
        bucket.getDelegatingBucket().setLocation(LOCATION);
        Bucket result = bucketManager.createBucket(bucket);
        Thread.sleep(5000);
        logger.info("create bucket result is: {}", result.getName());
        bucketManager.deleteBucket(result.getName());
    }
    
    @Test
    public void testDeleteBucketByName()
            throws CloudAppException, InterruptedException {
        boolean bucketExist = bucketManager.getDelegatingStorageClient()
                                           .doesBucketExist(
                                                   BUCKET_NAME_TO_DELETE);
        
        if (!bucketExist) {
            logger.info("Bucket {} is not exist,creating it",
                        BUCKET_NAME_TO_DELETE
            );
            boolean result = bucketManager.createBucket(BUCKET_NAME_TO_DELETE);
            Thread.sleep(5000);
            logger.info("Bucket {} is created", BUCKET_NAME_TO_DELETE);
        }
        boolean result = bucketManager.deleteBucket(BUCKET_NAME_TO_DELETE);
        logger.info("delete bucket {} result is: {}", BUCKET_NAME_TO_DELETE,
                    result
        );
    }
    
    @Test
    public void testGetBucketLocation() throws CloudAppException {
        String location = bucketManager.getBucketLocation(BUCKET_NAME);
        logger.info("bucket {} location is: {}", BUCKET_NAME, location);
    }
    
    @Test
    public void testHeadObject() throws CloudAppException {
        ObjectMetadata objectMetadata = bucketManager.headObject(BUCKET_NAME,
                                                                 OBJECT_PATH
        );
        logger.info("bucket {} {} size is {} and etag is {}", BUCKET_NAME,
                    OBJECT_PATH,
                    objectMetadata.getSize(), objectMetadata.getEtag()
        );
    }
    
    @Test
    public void testHeadObjectByVersionId() throws CloudAppException {
        String versionId = "CAEQywEYgYDAhJOI140ZIiBhNTBjODFkZDU2YzM0ZmUxYmQzNjA0YmYxZGRmOTZlMg--";
        ObjectMetadata objectMetadata = bucketManager.headObject(BUCKET_NAME,
                                                                 OBJECT_PATH,
                                                                 versionId
        );
        logger.info("bucket {} {} size is {} and etag is {}", BUCKET_NAME,
                    OBJECT_PATH,
                    objectMetadata.getSize(), objectMetadata.getEtag()
        );
    }
    
    @Test
    public void testListTopNObjects() throws CloudAppException {
        Collection<ObjectItem<OSSObjectSummary>> objectItems = bucketManager.listTopNObjects(
                BUCKET_NAME, 2);
        objectItems.stream()
                   .map(ObjectItem::getObjectName)
                   .forEach(objectName -> logger.info("object name is: {}",
                                                      objectName
                   ));
    }
    
    @Test
    public void testListObjects() throws CloudAppException {
        Pagination<ObjectItem<OSSObjectSummary>> pagination = bucketManager.listObjects(
                BUCKET_NAME,
                new Pagination<OSSObjectSummary>().builder()
                                                  .maxResults(2)
                                                  .build()
        );
        pagination.getDataList().stream()
                  .map(ObjectItem::getObjectName)
                  .forEach(objectName -> logger.info("object name is: {}",
                                                     objectName
                  ));
    }
    
    @Test
    public void testDoesBucketExist() throws CloudAppException {
        logger.info("bucket {} exists is {}", BUCKET_NAME,
                    bucketManager.getDelegatingClient()
                                 .doesBucketExist(BUCKET_NAME)
        );
    }
    
}
