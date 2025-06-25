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

import com.alibaba.cloudapp.api.filestore.model.ObjectMetadata;
import com.alibaba.cloudapp.api.common.Pagination;
import com.alibaba.cloudapp.api.filestore.BucketManager;
import com.alibaba.cloudapp.api.filestore.model.Bucket;
import com.alibaba.cloudapp.api.filestore.model.ObjectItem;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.exeption.CloudAppInvalidRequestException;
import com.alibaba.cloudapp.filestore.minio.ClientProvider;
import com.alibaba.cloudapp.filestore.minio.util.MinioExceptionConverter;
import com.alibaba.cloudapp.filestore.minio.util.MinioUtil;
import io.minio.*;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MinioBucketManager implements BucketManager<MinioClient, io.minio.messages.Bucket,
        StatObjectResponse, Item> {
    
    private static final Logger logger = LoggerFactory.getLogger(MinioBucketManager.class);
    
    private final ClientProvider clientProvider;
    
    public MinioBucketManager(ClientProvider client) {
        this.clientProvider = client;
    }
    
    @Override
    public List<Bucket<io.minio.messages.Bucket>> listAllBucketsWithPrefix(
            String prefix,
            String resourceGroupId) throws CloudAppException {
        
        try {
            List<io.minio.messages.Bucket> buckets =
                    clientProvider.getClient().listBuckets();
            
            return buckets == null ? null : buckets.stream().filter(
                    e -> !StringUtils.hasText(prefix) || e.name().startsWith(prefix)
            ).map(e -> {
                Bucket<io.minio.messages.Bucket> bucket = new Bucket<>();
                bucket.setName(e.name());
                bucket.setCreationDate(e.creationDate().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ));
                bucket.setDelegatingBucket(e);
                return bucket;
            }).collect(Collectors.toList());
            
        } catch (Exception e) {
            logger.error("list all bucket error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public Pagination<Bucket<io.minio.messages.Bucket>> listPagingBuckets(
            String prefix,
            String resourceGroupId,
            Pagination<Bucket<io.minio.messages.Bucket>> pagingMaker)
            throws CloudAppException {
        
        throw new CloudAppInvalidRequestException("Not support paging now");
    }
    
    @Override
    public boolean createBucket(String bucketName) throws CloudAppException {
        try {
            MakeBucketArgs args = MakeBucketArgs
                    .builder()
                    .bucket(bucketName)
                    .build();
            clientProvider.getClient().makeBucket(args);
            
            return true;
            
        } catch (Exception e) {
            logger.error("create bucket error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public Bucket<io.minio.messages.Bucket> createBucket(Bucket<io.minio.messages.Bucket> bucket)
            throws CloudAppException {
        try {
            MakeBucketArgs args = MakeBucketArgs
                    .builder()
                    .bucket(bucket.getName())
                    .build();
            clientProvider.getClient().makeBucket(args);
            
            return bucket;
            
        } catch (Exception e) {
            logger.error("create bucket error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public boolean deleteBucket(String bucketName) throws CloudAppException {
        try {
            RemoveBucketArgs args = RemoveBucketArgs
                    .builder()
                    .bucket(bucketName)
                    .build();
            clientProvider.getClient().removeBucket(args);
            
            return true;
            
        } catch (Exception e) {
            logger.error("delete bucket error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public String getBucketLocation(String bucketName)
            throws CloudAppException {
        throw new CloudAppInvalidRequestException(
                "Not support get bucket location yet");
    }
    
    @Override
    public ObjectMetadata<StatObjectResponse> headObject(
            String bucketName,
            String objectName) throws CloudAppException {
        
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            StatObjectArgs args = StatObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build();
            StatObjectResponse response = clientProvider.getClient().statObject(args);
            
            ObjectMetadata<StatObjectResponse> objectMetadata
                    = new ObjectMetadata<>();
            objectMetadata.setEtag(response.etag());
            objectMetadata.setSize((int) response.size());
            objectMetadata.setDelegationMetadata(response);
            
            return objectMetadata;
            
        } catch (Exception e) {
            logger.error("head object error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public ObjectMetadata<StatObjectResponse> headObject(
            String bucketName,
            String objectName,
            String versionId) throws CloudAppException {
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            StatObjectArgs args = StatObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .versionId(versionId)
                    .build();
            
            StatObjectResponse response = clientProvider.getClient().statObject(args);
            
            ObjectMetadata<StatObjectResponse> objectMetadata
                    = new ObjectMetadata<>();
            objectMetadata.setEtag(response.etag());
            objectMetadata.setSize((int) response.size());
            objectMetadata.setDelegationMetadata(response);
            
            return objectMetadata;
            
        } catch (Exception e) {
            logger.error("head object error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public Collection<ObjectItem<Item>> listTopNObjects(String bucketName,
                                                        int count)
            throws CloudAppException {
        
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            ListObjectsArgs args = ListObjectsArgs
                    .builder()
                    .bucket(bucketName)
                    .prefix("")
                    .maxKeys(count)
                    .build();
            
            Iterable<Result<Item>> response =
                    clientProvider.getClient().listObjects(args);
            
            return StreamSupport
                    .stream(response.spliterator(), false)
                    .limit(count)
                    .map(MinioBucketManager::convertObjectItem)
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            logger.error("list bucket topN object error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    private static ObjectItem<Item> convertObjectItem(Result<Item> result) {
        try {
            Item item = result.get();
            
            if(item == null) {
                return null;
            }
            
            Date lastModified = null;
            if(item.lastModified() != null) {
                Instant instant = item.lastModified().toInstant();
                lastModified = Date.from(instant);
            }
            
            return ObjectItem.<Item>builder()
                             .objectName(item.objectName())
                             .size(item.size())
                             .lastModified(lastModified)
                             .versionId(item.versionId())
                             .isLatest(false)
                             .delegatingMetadata(item)
                             .build();
        } catch (Exception e) {
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public Pagination<ObjectItem<Item>> listObjects(String bucketName,
                                                    Pagination<ObjectItem<Item>> pageMarker)
            throws CloudAppException {
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            ListObjectsArgs args = ListObjectsArgs
                    .builder()
                    .bucket(bucketName)
                    .startAfter(pageMarker.getNextToken())
                    .maxKeys(pageMarker.getMaxResults())
                    .build();
            
            Iterable<Result<Item>> response =
                    clientProvider.getClient().listObjects(args);
            
            List<ObjectItem<Item>> list = StreamSupport
                    .stream(response.spliterator(), false)
                    .limit(pageMarker.getMaxResults())
                    .map(MinioBucketManager::convertObjectItem)
                    .collect(Collectors.toList());
            
            pageMarker.setDataList(list);
            if(list.size() >=  pageMarker.getMaxResults()) {
                pageMarker.setNextToken(
                        list.get(list.size() - 1).getObjectName());
            }
            pageMarker.setHasNext(list.size() >= pageMarker.getMaxResults());
            
            return pageMarker;
            
        } catch (Exception e) {
            logger.error("list objects the bucket of {} error", bucketName);
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public MinioClient getDelegatingStorageClient() {
        return clientProvider.getClient();
    }
    
}
