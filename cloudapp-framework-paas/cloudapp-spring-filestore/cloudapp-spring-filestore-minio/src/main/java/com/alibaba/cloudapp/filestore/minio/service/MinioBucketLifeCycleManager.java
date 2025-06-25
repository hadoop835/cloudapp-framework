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

import com.alibaba.cloudapp.api.filestore.BucketLifeCycleManager;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.filestore.minio.ClientProvider;
import com.alibaba.cloudapp.filestore.minio.util.MinioExceptionConverter;
import com.alibaba.cloudapp.filestore.minio.util.MinioUtil;
import io.minio.DeleteBucketLifecycleArgs;
import io.minio.GetBucketLifecycleArgs;
import io.minio.MinioClient;
import io.minio.SetBucketLifecycleArgs;
import io.minio.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MinioBucketLifeCycleManager implements BucketLifeCycleManager<MinioClient, LifecycleConfiguration> {
    
    private static final Logger logger = LoggerFactory
            .getLogger(MinioBucketLifeCycleManager.class);
    
    private final ClientProvider clientProvider;
    
    public MinioBucketLifeCycleManager(ClientProvider client) {
        this.clientProvider = client;
    }
    
    @Override
    public void transitToWithLastAccessDays(String bucketName,
                                            String objectPrefixName,
                                            String storageType,
                                            int lastAccessDays)
            throws CloudAppException {
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            if (logger.isDebugEnabled()) {
                logger.debug("transitToWithLastAccessDays bucketName: {}, " +
                                     "objectPrefixName: {}, storageType: {}, lastAccessDays: {}",
                             bucketName, objectPrefixName, storageType,
                             lastAccessDays
                );
            }
            
            String ruleId = "Prefix-" + objectPrefixName + "-transitLastAccessDays";
            ZonedDateTime datetime = null;
            Transition transition = new Transition(datetime, lastAccessDays,
                                                   storageType
            );
            
            List<LifecycleRule> rules = new LinkedList<>();
            rules.add(
                    new LifecycleRule(
                            Status.ENABLED, null,
                            null, new RuleFilter(objectPrefixName),
                            ruleId, null,
                            null, transition
                    ));
            
            LifecycleConfiguration config = new LifecycleConfiguration(rules);
            
            
            SetBucketLifecycleArgs lifecycleRule = SetBucketLifecycleArgs
                    .builder()
                    .bucket(bucketName)
                    .config(config)
                    .build();
            
            clientProvider.getClient().setBucketLifecycle(lifecycleRule);
            
        } catch (Exception e) {
            logger.error("Set expire objects transit to with last access day " +
                                 "error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public void expireObjectsWithLastAccessDays(String bucketName,
                                                String objectPrefixName,
                                                int lastAccessDays)
            throws CloudAppException {
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            if (logger.isDebugEnabled()) {
                logger.debug("expireObjectsWithLastAccessDays bucketName: " +
                                     "{}, objectPrefixName: {}, lastAccessDays: {}",
                             bucketName, objectPrefixName, lastAccessDays
                );
            }
            
            String ruleId = "Prefix-" + objectPrefixName + "-expireObjectsWithLastAccessDays";
            ZonedDateTime datetime = null;
            
            List<LifecycleRule> rules = new LinkedList<>();
            Expiration expiration = new Expiration(datetime, lastAccessDays,
                                                   null
            );
            rules.add(
                    new LifecycleRule(
                            Status.ENABLED, null,
                            expiration, new RuleFilter(objectPrefixName),
                            ruleId, null,
                            null, null
                    ));
            
            LifecycleConfiguration config = new LifecycleConfiguration(rules);
            
            
            SetBucketLifecycleArgs lifecycleRule = SetBucketLifecycleArgs
                    .builder()
                    .bucket(bucketName)
                    .config(config)
                    .build();
            
            clientProvider.getClient().setBucketLifecycle(lifecycleRule);
            
        } catch (Exception e) {
            logger.error("Set expire objects with last access days error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public void expireObjectsWithCreateBefore(String bucketName,
                                              String objectPrefixName,
                                              Date createBefore)
            throws CloudAppException {
        
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            if (logger.isDebugEnabled()) {
                logger.debug("expireObjectsWithCreateBefore bucketName: " +
                                     "{}, objectPrefixName: {}, createBefore: {}",
                             bucketName, objectPrefixName, createBefore
                );
            }
            
            String ruleId = "Prefix-" + objectPrefixName + "-expireObjectsWithCreateBefore";
            ZonedDateTime datetime = createBefore.toInstant().atZone(
                    ZoneId.systemDefault());
            
            List<LifecycleRule> rules = new LinkedList<>();
            Expiration expiration = new Expiration(datetime, null, null);
            
            rules.add(
                    new LifecycleRule(
                            Status.ENABLED, null,
                            expiration, new RuleFilter(objectPrefixName),
                            ruleId, null,
                            null, null
                    ));
            
            LifecycleConfiguration config = new LifecycleConfiguration(rules);
            
            SetBucketLifecycleArgs lifecycleRule = SetBucketLifecycleArgs
                    .builder()
                    .bucket(bucketName)
                    .config(config)
                    .build();
            
            clientProvider.getClient().setBucketLifecycle(lifecycleRule);
            
        } catch (Exception e) {
            logger.error("Set expire objects with create before error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public void expireObjectsAfterVersionNoncurrentDays(String bucketName,
                                                        String objectPrefixName,
                                                        int noncurrentDays)
            throws CloudAppException {
        
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            if (logger.isDebugEnabled()) {
                logger.debug("expireObjectsWithCreateBefore bucketName: " +
                                     "{}, objectPrefixName: {}, noncurrentDays: {}",
                             bucketName, objectPrefixName, noncurrentDays
                );
            }
            
            String ruleId = "Prefix-" + objectPrefixName + "-expireObjectsAfterVersionNoncurrentDays";
            ZonedDateTime datetime = null;
            
            List<LifecycleRule> rules = new LinkedList<>();
            
            NoncurrentVersionExpiration expiration = new NoncurrentVersionExpiration(
                    noncurrentDays
            );
            
            rules.add(
                    new LifecycleRule(
                            Status.ENABLED, null,
                            null, new RuleFilter(objectPrefixName),
                            ruleId, expiration,
                            null, null
                    ));
            
            LifecycleConfiguration config = new LifecycleConfiguration(rules);
            
            SetBucketLifecycleArgs lifecycleRule = SetBucketLifecycleArgs
                    .builder()
                    .bucket(bucketName)
                    .config(config)
                    .build();
            
            clientProvider.getClient().setBucketLifecycle(lifecycleRule);
            
        } catch (Exception e) {
            logger.error(
                    "Set expire objects after version non current days error");
            throw MinioExceptionConverter.convert(e);
        }
        
    }
    
    @Override
    public LifecycleConfiguration getBucketLifeCycle(String bucketName)
            throws CloudAppException {
        
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            GetBucketLifecycleArgs args = GetBucketLifecycleArgs
                    .builder()
                    .bucket(bucketName)
                    .build();
            return clientProvider.getClient().getBucketLifecycle(args);
            
        } catch (Exception e) {
            logger.error(
                    "Set expire objects after version non current days error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public boolean deleteBucketLifeCycle(String bucketName)
            throws CloudAppException {
        
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            DeleteBucketLifecycleArgs args = DeleteBucketLifecycleArgs
                    .builder()
                    .bucket(bucketName)
                    .build();
            clientProvider.getClient().deleteBucketLifecycle(args);
            
            return true;
            
        } catch (Exception e) {
            logger.error(
                    "Set expire objects after version non current days error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public MinioClient getDelegatingStorageClient() {
        return clientProvider.getClient();
    }
    
}
