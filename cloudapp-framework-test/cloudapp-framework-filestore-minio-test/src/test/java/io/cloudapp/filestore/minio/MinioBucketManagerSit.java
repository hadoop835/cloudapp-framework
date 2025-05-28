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

import com.alibaba.fastjson2.JSONObject;
import io.cloudapp.api.common.Pagination;
import io.cloudapp.api.filestore.model.Bucket;
import io.cloudapp.api.filestore.model.ObjectItem;
import io.cloudapp.api.filestore.model.ObjectMetadata;
import io.cloudapp.exeption.CloudAppInvalidRequestException;
import io.cloudapp.filestore.minio.service.MinioBucketManager;
import io.cloudapp.util.RandomStringGenerator;
import io.minio.BucketExistsArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MinioBucketManagerSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            MinioBucketManagerSit.class);
    
    private static final String bucketName = "test987654";
    
    @Autowired
    MinioBucketManager minioBucketManager;
    
    @Test
    public void listAllBucketsWithPrefix_prefix_resourceGroupId() {
        List<Bucket<io.minio.messages.Bucket>> bucketList = minioBucketManager.listAllBucketsWithPrefix(
                "test", null);
        bucketList.stream().forEach(
                bucket -> logger.info("bucket: {}", bucket.getName()));
    }
    
    @Test
    public void listPagingBuckets_prefix_resourceGroupId_pagingMaker() {
        Pagination<Bucket<io.minio.messages.Bucket>> pagingMaker =
                new Pagination<Bucket<io.minio.messages.Bucket>>().builder()
                                                                  .maxResults(
                                                                          10)
                                                                  .build();
        Assert.assertThrows(CloudAppInvalidRequestException.class, () -> {
            minioBucketManager.listPagingBuckets(
                    "test", null, pagingMaker);
        });
    }
    
    @Test
    public void createBucket_bucketName() {
        boolean bucket =
                minioBucketManager.createBucket(
                        "testbucket" + RandomStringGenerator.generate(3));
        logger.info("create bucket result: {}", bucket);
    }
    
    @Test
    public void createBucket_Bucket() {
        Bucket<io.minio.messages.Bucket> bucket = new Bucket<>();
        bucket.setName("testbucket" + RandomStringGenerator.generate(3));
        Bucket<io.minio.messages.Bucket> minioBucketManagerBucket = minioBucketManager.createBucket(
                bucket);
        logger.info("create bucket result: {}",
                    minioBucketManagerBucket.getName()
        );
    }
    
    
    @Test
    public void getBucketLocation_bucketName() {
        Assert.assertThrows(CloudAppInvalidRequestException.class, () -> {
            minioBucketManager.getBucketLocation(bucketName);
        });
    }
    
    @Test
    public void headObject_bucketName_objectName() {
        ObjectMetadata<StatObjectResponse> statObjectResponseObjectMetadata = minioBucketManager.headObject(
                bucketName, "test.txt");
        logger.info("object metadat etag is: {}, size is {}",
                    statObjectResponseObjectMetadata.getEtag(),
                    statObjectResponseObjectMetadata.getSize()
        );
    }
    
    @Test
    public void headObject_bucketName_objectName_versionId() {
        ObjectMetadata<StatObjectResponse> statObjectResponseObjectMetadata = minioBucketManager.headObject(
                bucketName, "test.txt", "");
        logger.info("object metadat etag is: {}, size is {}",
                    statObjectResponseObjectMetadata.getEtag(),
                    statObjectResponseObjectMetadata.getSize()
        );
    }
    
    @Test
    public void listTopNObjects_bucketName_count() {
        Collection<ObjectItem<Item>> objectItems = minioBucketManager.listTopNObjects(
                bucketName, 10);
        if (objectItems != null && !objectItems.isEmpty()) {
            objectItems.stream().forEach(
                    objectItem -> logger.info("object item: {}",
                                              JSONObject.toJSONString(
                                                      objectItem)
                    ));
        } else {
            logger.info("object item is empty");
        }
    }
    
    @Test
    public void listObjects_bucketName_pageMarker() {
        Pagination<ObjectItem<Item>> pageMarker =
                new Pagination<ObjectItem<Item>>().builder()
                                                  .maxResults(10)
                                                  .build();
        Pagination<ObjectItem<Item>> objectItemPagination = minioBucketManager.listObjects(
                bucketName, pageMarker);
        if (objectItemPagination != null && !objectItemPagination.getDataList()
                                                                 .isEmpty()) {
            objectItemPagination.getDataList().stream().forEach(
                    objectItem -> logger.info("object item: {}",
                                              JSONObject.toJSONString(
                                                      objectItem)
                    ));
        } else {
            logger.info("object item is empty");
        }
    }
    
    @Test
    public void deleteBucket_bucketName() {
        String bucket = "testbucket" + RandomStringGenerator.generate(3);
        try {
            boolean bucketExists = minioBucketManager.getDelegatingClient()
                                                     .bucketExists(
                                                             BucketExistsArgs.builder()
                                                                             .bucket(bucket)
                                                                             .build());
            if (!bucketExists) {
                minioBucketManager.createBucket(bucket);
                Thread.sleep(5000);
            }
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
        boolean deleteBucket = minioBucketManager.deleteBucket(bucket);
        logger.info("delete bucket result: {}", deleteBucket);
    }
    
    @Test
    public void getDelegatingStorageClient() {
        Assert.assertNotNull(minioBucketManager.getDelegatingStorageClient());
    }
    
}
