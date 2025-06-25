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


import com.alibaba.cloudapp.api.filestore.model.Bucket;
import com.alibaba.cloudapp.api.filestore.model.ObjectMetadata;
import com.alibaba.cloudapp.api.common.Pagination;
import com.alibaba.cloudapp.api.filestore.model.ObjectItem;
import com.alibaba.cloudapp.exeption.CloudAppException;

import java.util.Collection;
import java.util.List;

/**
 * Bucket Management API
 *
 * @param <Client> The underlying client type
 * @param <B>      The underlying bucket type
 * @param <M>      The underlying object meta type
 * @param <O>      The underlying object item type
 *
 */
public interface BucketManager<Client, B, M, O> extends StorageClientAware<Client> {

    /**
     * Get all of available buckets start with a prefix without a pagination.
     *
     * @param prefix          The buckets starting with the prefix, return all
     *                        buckets if this is empty.
     * @param resourceGroupId The resource group id, for cloud provider only.
     * @return Bucket list
     */
    List<Bucket<B>> listAllBucketsWithPrefix(String prefix,
                                             String resourceGroupId)
            throws CloudAppException;

    /**
     * Get the available buckets with paging.
     *
     * @param prefix           the buckets starting with the prefix, try list
     *                         all buckets if this is empty.
     * @param resourceGroupId  The resource group id, for cloud provider only.
     * @param pagingMaker      The requesting paging info, only the first page
     *                         if this is empty.
     * @return Pagination result with a list of Buckets
     */
    Pagination<Bucket<B>> listPagingBuckets(String prefix,
                                            String resourceGroupId,
                                            Pagination<Bucket<B>> pagingMaker)
            throws CloudAppException;

    /**
     * Create a new bucket
     *
     * @param bucketName Bucket name, e.g: bucketName=my-bucket
     *
     * @return true if the bucket is created successfully, false otherwise
     */
    boolean createBucket(String bucketName) throws CloudAppException;

    /**
     * Create a new bucket
     * @param bucket Bucket information
     * @return Bucket information
     */
    Bucket<B> createBucket(Bucket<B> bucket) throws CloudAppException;

    /**
     * Delete a bucket
     *
     * @param bucketName Bucket name
     * @return true if the bucket is deleted successfully, false otherwise
     */
    boolean deleteBucket(String bucketName) throws CloudAppException;

    /**
     * Get bucket location information
     * @param bucketName Bucket name
     *
     * @return location information, e,g: oss-cn-hangzhou, oss-cn-shanghai, etc.
     */
    String getBucketLocation(String bucketName) throws CloudAppException;

    /**
     * Get object metadata,
     * AlibabaCloud Reference: https://api.aliyun.com/document/Oss/2019-05-17/GetObjectMeta ,
     * S3 Reference: https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObjectAttributes.html
     *
     * @param bucketName The bucket name
     * @param objectName The object name
     *
     * @return The metadata
     */
    ObjectMetadata<M> headObject(String bucketName, String objectName)
            throws CloudAppException;

    /**
     * Get the metadata of the specified version object
     *
     * @param bucketName The bucket name
     * @param objectName The object name
     * @param versionId  If bucket version control is enabled, this parameter
     *                   is required
     *
     * @return metadata
     */
    ObjectMetadata<M> headObject(String bucketName,
                                 String objectName,
                                 String versionId)
            throws CloudAppException;

    /**
     * List objects
     *
     * @param bucketName The bucket name
     * @param count       The number of objects to list
     *
     * @return The list of objects
     */
    Collection<ObjectItem<O>> listTopNObjects(String bucketName, int count)
            throws CloudAppException;

    /**
     * List objects with paging.
     *
     * @param bucketName The bucket name
     * @param pageMarker  The paging info
     *
     * @return The paging result
     */
    Pagination<ObjectItem<O>> listObjects(String bucketName,
                                          Pagination<ObjectItem<O>> pageMarker)
            throws CloudAppException;
}
