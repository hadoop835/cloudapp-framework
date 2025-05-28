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
package io.cloudapp.api.filestore;


import io.cloudapp.exeption.CloudAppException;

/**
 * Bucket ACL interface
 */
public interface ObjectPolicyManager<Client> extends StorageClientAware<Client> {

    /**
     * Grant access permissions to object, e.g. "public-read", "private",
     * "public-read-write"
     *
     * @param bucketName Bucket name
     * @param objectName The object name
     * @param accessAcl  Access Granted ACL String, e.g. "public-read",
     *                   "private", "public-read-write"
     */
    void grantAccessPermissions(String bucketName, String objectName, String accessAcl)
            throws CloudAppException;

    /**
     * Get bucket access permissions
     *
     * @param bucketName Bucket name
     * @param objectName The object name
     * @return Access Control Permission List
     * @throws CloudAppException exception
     */
    String getObjectPolicy(String bucketName, String objectName)
            throws CloudAppException;

    /**
     * Delete bucket policy
     *
     * @param bucketName deleting bucket name.
     * @param objectName object name.
     * @return true if success.
     */
    boolean deleteObjectPolicy(String bucketName, String objectName)
            throws CloudAppException;

}
