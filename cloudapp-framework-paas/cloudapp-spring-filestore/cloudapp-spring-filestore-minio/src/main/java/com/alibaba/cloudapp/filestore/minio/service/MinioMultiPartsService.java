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

import com.alibaba.cloudapp.api.filestore.MultiPartsService;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.exeption.CloudAppInvalidArgumentsException;
import com.alibaba.cloudapp.filestore.minio.ClientProvider;
import com.alibaba.cloudapp.filestore.minio.util.MinioExceptionConverter;
import com.alibaba.cloudapp.filestore.minio.util.MinioUtil;
import com.alibaba.cloudapp.model.Pairs;
import io.minio.*;
import io.minio.messages.DeleteObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MinioMultiPartsService implements MultiPartsService<MinioClient> {
    
    private static final Logger logger = LoggerFactory.getLogger(
            MinioMultiPartsService.class);
    
    private final ClientProvider clientProvider;
    
    private static final Long MIN_PART_SIZE = 5L << 20;
    private static final Long MAX_PART_SIZE = 1L << 30;
    
    public MinioMultiPartsService(ClientProvider client) {
        this.clientProvider = client;
    }
    
    @Override
    public Map<String, Integer> uploadObjects(String bucketName,
                                              String objectName,
                                              List<Pairs.Pair<String, InputStream>> objects)
            throws CloudAppException {
        try {
            if (!StringUtils.hasText(objectName)) {
                throw new CloudAppInvalidArgumentsException(
                        "ObjectName should not be empty.");
            }
            
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            List<SnowballObject> snowballObjects = objects.stream().map(
                    e -> {
                        try {
                            return new SnowballObject(
                                    e.getKey(),
                                    e.getValue(),
                                    e.getValue().available(),
                                    null
                            );
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
            ).collect(Collectors.toList());
            
            ObjectWriteResponse response =
                    clientProvider.getClient().uploadSnowballObjects(
                            UploadSnowballObjectsArgs.builder()
                                                     .bucket(bucketName)
                                                     .objects(snowballObjects)
                                                     .build());
            
            if (logger.isDebugEnabled()) {
                logger.debug("upload objects response: {}", response);
            }
            
            List<ComposeSource> sourceObjectList =
                    objects.stream()
                           .map(e -> ComposeSource.builder()
                                                  .bucket(bucketName)
                                                  .object(e.getKey())
                                                  .build())
                           .collect(Collectors.toList());
            
            clientProvider.getClient().composeObject(
                    ComposeObjectArgs.builder()
                                     .bucket(bucketName)
                                     .object(objectName)
                                     .sources(sourceObjectList)
                                     .build());
            AtomicInteger i = new AtomicInteger(1);
            
            clientProvider.getClient().removeObjects(
                    RemoveObjectsArgs
                            .builder()
                            .bucket(bucketName)
                            .objects(objects.stream()
                                            .map(e -> new DeleteObject(
                                                    e.getKey(),
                                                    response.versionId()
                                            ))
                                            .collect(Collectors.toList()))
                            .build());
            return objects.stream().collect(Collectors.toMap(
                    Pairs.Pair::getKey,
                    e -> i.getAndIncrement()
            ));
        } catch (Exception e) {
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public boolean uploadBigFile(String bucketName,
                                 String objectName,
                                 Path filePath,
                                 int maxSizePerPart) throws CloudAppException {
        File file = filePath.toFile();
        
        try (FileInputStream inputStream = new FileInputStream(file)) {
            if (!StringUtils.hasText(objectName)) {
                throw new CloudAppInvalidArgumentsException(
                        "ObjectName should not be empty.");
            }
            
            if (maxSizePerPart < MIN_PART_SIZE || maxSizePerPart > MAX_PART_SIZE) {
                throw new CloudAppInvalidArgumentsException(
                        "MaxSizePerPart should be between " +
                                MIN_PART_SIZE + " and " + MAX_PART_SIZE);
            }
            
            logger.info("Starting to upload a big file to " +
                                "bucket: {}, object: {}, file: {}, size: {}",
                        bucketName, objectName, filePath, maxSizePerPart
            );
            
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            long size = file.length();
            
            clientProvider.getClient().putObject(
                    PutObjectArgs
                            .builder()
                            .stream(inputStream, size, maxSizePerPart)
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            
            return true;
        } catch (Exception ex) {
            logger.error(
                    "Failed to upload big file, bucketName: {}, objectName: {}",
                    bucketName, objectName
            );
            
            throw MinioExceptionConverter.convert(ex);
        }
    }
    
    @Override
    public MinioClient getDelegatingStorageClient() {
        return clientProvider.getClient();
    }
    
}
