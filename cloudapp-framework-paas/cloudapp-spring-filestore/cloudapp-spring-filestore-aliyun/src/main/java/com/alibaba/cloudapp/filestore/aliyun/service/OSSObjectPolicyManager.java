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
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectAcl;
import com.aliyun.oss.model.SetObjectAclRequest;
import com.aliyun.oss.model.VoidResult;
import com.alibaba.cloudapp.api.filestore.ObjectPolicyManager;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.filestore.aliyun.util.OSSExceptionConverter;
import com.alibaba.cloudapp.filestore.aliyun.util.OSSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSSObjectPolicyManager implements ObjectPolicyManager<OSS> {

    private static final Logger logger = LoggerFactory.getLogger(OSSObjectPolicyManager.class);

    private final OSS oss;

    public OSSObjectPolicyManager(OSS oss) {
        this.oss = oss;
    }

    @Override
    public OSS getDelegatingStorageClient() {
        return oss;
    }

    @Override
    public void grantAccessPermissions(String bucketName, String objectName, String accessAcl)
            throws CloudAppException {
        try {

            OSSUtil.checkBucketExists(oss, bucketName);

            SetObjectAclRequest request = new SetObjectAclRequest(bucketName, objectName);
            request.setCannedACL(CannedAccessControlList.parse(accessAcl));

            VoidResult result = oss.setObjectAcl(request);

            if (logger.isDebugEnabled()) {
                logger.debug("grant object access permissions result: {}",
                        JSON.toJSONString(result));
            }

        } catch (ClientException ex) {
            logger.error("grant object access permissions error, " +
                            "bucketName: {}, objectName: {}, accessAcl: {}",
                    bucketName, objectName, accessAcl);

            throw OSSExceptionConverter.convert(ex);
        }
    }

    @Override
    public String getObjectPolicy(String bucketName, String objectName) throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            ObjectAcl result = oss.getObjectAcl(bucketName, objectName);

            if(logger.isDebugEnabled()) {
                logger.debug("get object policy result: {}",
                        JSON.toJSONString(result));
            }

            return result.getPermission().toString();

        } catch (ClientException ex) {
            logger.error("Get bucket policy error, bucketName: {}, objectName: {}",
                    bucketName, objectName);
            throw OSSExceptionConverter.convert(ex);
        }
    }

    @Override
    public boolean deleteObjectPolicy(String bucketName, String objectName) throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            SetObjectAclRequest request = new SetObjectAclRequest(bucketName, objectName);
            request.setCannedACL(CannedAccessControlList.Default);

            VoidResult result = oss.setObjectAcl(request);

            if(logger.isDebugEnabled()) {
                logger.debug("Delete bucket policy result: {}", JSON.toJSONString(result));
            }

            return result.getResponse() == null || result.getResponse().isSuccessful();

        } catch (ClientException ex) {
            logger.error("Delete bucket policy error, bucketName: {}, objectName: {}", bucketName, objectName);
            throw OSSExceptionConverter.convert(ex);
        }
    }
}
