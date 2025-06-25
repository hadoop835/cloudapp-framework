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

import com.alibaba.cloudapp.api.filestore.StorageObjectService;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.filestore.minio.ClientProvider;
import com.alibaba.cloudapp.filestore.minio.util.MinioExceptionConverter;
import com.alibaba.cloudapp.filestore.minio.util.MinioUtil;
import io.minio.*;
import io.minio.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MinioStorageObjectService implements StorageObjectService<MinioClient> {
    
    private static final Logger logger = LoggerFactory.getLogger(
            MinioStorageObjectService.class);
    
    private final ClientProvider clientProvider;
    
    public MinioStorageObjectService(ClientProvider client) {
        this.clientProvider = client;
    }
    
    @Override
    public MinioClient getDelegatingStorageClient() {
        return clientProvider.getClient();
    }
    
    @Override
    public boolean copy(String sourceBucket,
                        String sourcePath,
                        String targetBucket,
                        String targetPath,
                        boolean override) throws CloudAppException {
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), sourceBucket);
            
            MinioUtil.checkBucketExists(clientProvider.getClient(), targetBucket);
    
    
            if (!override && MinioUtil.checkObjectExists(
                    clientProvider.getClient(), targetBucket, targetPath
            )) {
                return false;
            }
    
            CopyObjectArgs args = CopyObjectArgs
                    .builder()
                    .bucket(targetBucket)
                    .object(targetPath)
                    .source(CopySource.builder()
                                      .bucket(sourceBucket)
                                      .object(sourcePath)
                                      .build())
                    .taggingDirective(Directive.REPLACE)
                    .metadataDirective(Directive.REPLACE)
                    .build();
    
            ObjectWriteResponse response =
                    clientProvider.getClient().copyObject(args);
    
            if (logger.isDebugEnabled()) {
                logger.debug("Copy object from {} to {}, response: {}",
                             sourcePath, targetPath, response
                );
            }
    
            return true;
        } catch (Exception ex) {
            logger.error(
                    "Failed to copy object, bucketName: {}, sourcePath: {}, targetPath: {}",
                    sourceBucket, sourcePath, targetPath
            );
            
            throw MinioExceptionConverter.convert(ex);
        }
    }
    
    @Override
    public boolean deleteObject(String bucketName, String path)
            throws CloudAppException {
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            RemoveObjectArgs args = RemoveObjectArgs
                    .builder()
                    .object(path)
                    .bucket(bucketName)
                    .build();
            
            clientProvider.getClient().removeObject(args);
            
            if (logger.isDebugEnabled()) {
                logger.debug("remove object named {}, bucket of {}",
                             path, bucketName
                );
            }
            
            return true;
        } catch (Exception ex) {
            logger.error(
                    "Failed to remove object, bucketName: {}, object: {}",
                    bucketName, path
            );
            
            throw MinioExceptionConverter.convert(ex);
        }
    }
    
    @Override
    public boolean deleteObjects(String bucketName,
                                 Collection<String> objects,
                                 boolean checkDeleteAll)
            throws CloudAppException {
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            RemoveObjectsArgs args = RemoveObjectsArgs
                    .builder()
                    .bucket(bucketName)
                    .objects(objects.stream().map(
                            DeleteObject::new
                    ).collect(Collectors.toList()))
                    .build();
            
            Iterable<Result<DeleteError>> response =
                    clientProvider.getClient().removeObjects(args);
            
            boolean allDelete = true;
            
            Iterator<Result<DeleteError>> iterator = response.iterator();
            if (iterator.hasNext()) {
                allDelete = false;
                
                while (iterator.hasNext()) {
                    Result<DeleteError> result = iterator.next();
                    logger.warn("Delete object {} error: {}",
                                result.get().objectName(),
                                result.get().message()
                    );
                }
            }
            
            return !checkDeleteAll || allDelete;
        } catch (Exception ex) {
            logger.error("delete many objects error");
            
            throw MinioExceptionConverter.convert(ex);
        }
    }
    
    @Override
    public InputStream getObject(String bucketName, String path)
            throws CloudAppException {
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            GetObjectArgs args = GetObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(path)
                    .build();
            
            return clientProvider.getClient().getObject(args);
        } catch (Exception ex) {
            logger.error("get object error");
            throw MinioExceptionConverter.convert(ex);
        }
    }
    
    @Override
    public boolean putObject(String bucketName,
                             String path,
                             InputStream body,
                             String contentType) throws CloudAppException {
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            long size = body.available();
            
            PutObjectArgs args = PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(path)
                    .stream(body, size, size)
                    .build();
            
            ObjectWriteResponse result = clientProvider.getClient().putObject(args);
            
            if (logger.isDebugEnabled()) {
                logger.debug("put object result: {}", result);
            }
            
            return true;
        } catch (Exception ex) {
            logger.error("put object error");
            throw MinioExceptionConverter.convert(ex);
        }
    }
    
    @Override
    public Collection<String> listObjectVersions(String bucketName,
                                                 String path,
                                                 String sinceVersion,
                                                 int count)
            throws CloudAppException {
        try{
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            Iterable<Result<Item>> results = clientProvider.getClient().listObjects(
                    ListObjectsArgs.builder()
                                   .bucket(bucketName)
                                   .prefix(path)
                                   .includeVersions(true)
                                   .build());
            
            List<Item> items = StreamSupport
                    .stream(results.spliterator(), false)
                    .map(result -> {
                        try {
                            return result.get();
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(result -> result != null && result.objectName().equals(path))
                    .collect(Collectors.toList());
            
            Item item = null;
            if(StringUtils.hasText(sinceVersion)) {
                item = items.stream().filter(
                        e -> e.versionId().equals(sinceVersion)
                ).findFirst().orElse(null);
            }
            
            ZonedDateTime since = item == null ? null : item.lastModified();
            
            return items.stream().filter(
                    e -> since == null || since.isBefore(e.lastModified())
            ).map(Item::versionId).collect(Collectors.toList());
            
        } catch (Exception ex) {
            logger.error("list object versions error");
            throw MinioExceptionConverter.convert(ex);
        }
    }
    
    @Override
    public boolean restoreObject(String bucketName,
                                 String path,
                                 int days,
                                 String tier) throws CloudAppException {
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            RestoreRequest request = null;
            if (StringUtils.hasText(tier)) {
                Tier tierEnum = Tier.fromString(tier);
                GlacierJobParameters parameters = new GlacierJobParameters(
                        tierEnum);
                
                request = new RestoreRequest(days, parameters, tierEnum,
                                             null,
                                             null,
                                             null
                );
            }
            
            RestoreObjectArgs args = RestoreObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(path)
                    .request(request)
                    .build();
            
            clientProvider.getClient().restoreObject(args);
            return true;
            
        } catch (Exception ex) {
            logger.error("restore object error");
            throw MinioExceptionConverter.convert(ex);
        }
    }
    
}
