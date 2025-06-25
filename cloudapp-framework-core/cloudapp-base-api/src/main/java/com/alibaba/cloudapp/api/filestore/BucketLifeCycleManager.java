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
package com.alibaba.cloudapp.api.filestore;


import com.alibaba.cloudapp.exeption.CloudAppException;

import java.util.Date;

/**
 * Bucket lifecycle interface
 */
public interface BucketLifeCycleManager<Client, Lifecycle> extends StorageClientAware<Client> {

    /**
     * Transit bucket to storage type with last access days, please refer to
     * the lifecycle of <a href="https://docs.aws.amazon.com/AmazonS3/latest/dev/object-lifecycle-mgmt.html">AWS S3</a>
     * and <a href="https://help.aliyun.com/zh/oss/developer-reference/lifecycle">Aliyun OSS</a>
     *
     * for example, The specified example applies to all Objects in the
     * 'examplebucket',  the Prefix specified as test/,
     * indicating that the Object matching the prefix test/ is no longer Convert
     * to Archive storage type after the last modification time exceeds 30 days.
     *
     * <pre>{@code
     *  transitToWithLastAccessDays("examplebucket", "test/", 'Archive', 30);
     * }
     * </pre>
     * @param bucketName Bucket name
     * @param objectPrefixName object prefix name
     * @param storageType The storage type, such as 'Standard', 'Standard-IA', 'Archive'
     * @param lastAccessDays last access days
     *
     * @throws CloudAppException exception
     */
    void transitToWithLastAccessDays(String bucketName,
                                     String objectPrefixName,
                                     String storageType,
                                     int lastAccessDays) throws CloudAppException;

    /**
     * Expire object with last access days, please refer to the lifecycle of
     * <a href="https://docs.aws.amazon.com/AmazonS3/latest/dev/object-lifecycle-mgmt.html">
     *     AWS S3</a> and <a href="https://help.aliyun.com/zh/oss/developer-reference/lifecycle">
     *  Aliyun OSS</a>
     *
     * for example, The specified example applies to all Objects in the
     * 'examplebucket', all objects (that is, the refix is empty), will be expired
     *  after the last modification time exceeds 30 days.
     *
     * <pre>{@code
     *  expireObjectsWithLastAccessDays("examplebucket", "", 30);
     * }
     * </pre>
     * @param bucketName Bucket name
     * @param objectPrefixName object prefix name
     * @param lastAccessDays last access days
     *
     * @throws CloudAppException exception
     */
    void expireObjectsWithLastAccessDays(String bucketName,
                                        String objectPrefixName,
                                        int lastAccessDays) throws CloudAppException;

    /**
     * Expire objects with create before, please refer to the lifecycle of
     * <a href="https://docs.aws.amazon.com/AmazonS3/latest/dev/object-lifecycle-mgmt.html">
     *     AWS S3</a> and <a href="https://help.aliyun.com/zh/oss/developer-reference/lifecycle">
     *  Aliyun OSS</a>
     *
     * for example, The specified example applies to all Objects in the
     * 'examplebucket', all objects (that is, the refix is empty), will be expired
     *  after the creation time is before 2020-01-01 00:00:00.
     *
     * <pre>{@code
     *  expireObjectsWithCreateBefore("examplebucket", "", new Date(2020, 1, 1));
     * }
     * </pre>
     * @param bucketName Bucket name
     * @param objectPrefixName object prefix name
     * @param createBefore create before
     *
     * @throws CloudAppException exception
     */
    void expireObjectsWithCreateBefore(String bucketName,
                                       String objectPrefixName,
                                       Date createBefore) throws CloudAppException;


    /**
     * Expire objects after version noncurrent days, please refer to the lifecycle of
     * <a href="https://docs.aws.amazon.com/AmazonS3/latest/dev/object-lifecycle-mgmt.html">
     *     AWS S3</a> and <a href="https://help.aliyun.com/zh/oss/developer-reference/lifecycle">
     *  Aliyun OSS</a>
     *
     * for example, The specified example applies to all Objects in the
     * 'examplebucket', all objects (that is, the refix is empty), will be expired
     *  after 30 days when the version became non-current.
     *
     * <pre>{@code
     *  expireObjectsAfterVersionNoncurrentDays("examplebucket", "", 30);
     * }
     * </pre>
     * @param bucketName Bucket name
     * @param objectPrefixName object prefix name
     * @param noncurrentDays noncurrent days
     *
     * @throws CloudAppException exception
     */
    void expireObjectsAfterVersionNoncurrentDays(String bucketName,
                                                 String objectPrefixName,
                                                 int noncurrentDays)
            throws CloudAppException;

    /**
     * Get bucket lifecycle rules
     * @param bucketName Bucket name
     * @return Lifecycle rules
     */
    Lifecycle getBucketLifeCycle(String bucketName) throws CloudAppException;

    /**
     * Delete bucket lifecycle rules
     *
     * @param bucketName Bucket name
     *
     * @return true if delete success.
     */
    boolean deleteBucketLifeCycle(String bucketName) throws CloudAppException;

}
