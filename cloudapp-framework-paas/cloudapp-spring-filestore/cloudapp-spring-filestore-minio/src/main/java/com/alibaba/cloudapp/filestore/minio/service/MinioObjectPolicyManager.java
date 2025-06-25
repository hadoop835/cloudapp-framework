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

import com.alibaba.cloudapp.api.filestore.ObjectPolicyManager;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.filestore.minio.ClientProvider;
import com.alibaba.cloudapp.filestore.minio.util.MinioExceptionConverter;
import com.alibaba.cloudapp.filestore.minio.util.MinioUtil;
import io.minio.GetBucketPolicyArgs;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinioObjectPolicyManager implements ObjectPolicyManager<MinioClient> {
    
    private static final Logger logger =
            LoggerFactory.getLogger(MinioObjectPolicyManager.class);
    
    private final ClientProvider clientProvider;
    
    public MinioObjectPolicyManager(ClientProvider client) {
        this.clientProvider = client;
    }
    
    @Override
    public void grantAccessPermissions(String bucketName,
                                       String objectName,
                                       String accessAcl)
            throws CloudAppException {
        
        throw new CloudAppException("product not support grant object acl");
    }
    
    @Override
    public String getObjectPolicy(String bucketName, String objectName)
            throws CloudAppException {
        try {
            MinioUtil.checkBucketExists(clientProvider.getClient(), bucketName);
            
            return clientProvider.getClient().getBucketPolicy(GetBucketPolicyArgs.builder()
                                                                     .bucket(bucketName).build());
            
        } catch (Exception e) {
            logger.error("Get object policy error");
            throw MinioExceptionConverter.convert(e);
        }
    }
    
    @Override
    public boolean deleteObjectPolicy(String bucketName, String objectName)
            throws CloudAppException {
        
        throw new CloudAppException("product not support delete object acl");
    }
    
    @Override
    public MinioClient getDelegatingStorageClient() {
        return clientProvider.getClient();
    }
    
}
