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

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static io.cloudapp.model.Pairs.Pair;

/**
 * Multipart upload service
 */
public interface MultiPartsService<Client> extends StorageClientAware<Client> {

    /**
     * The max size of each part, maxSize per part should be less than 2G
     */
    long MAX_SIZE_PER_PART = 2L << 30;

    /**
     * Upload multiple parts of a big object, each part is including the
     * name and input stream.
     * This service usually used for large file upload. for alibaba cloud, please
     * refer to <a href="https://help.aliyun.com/zh/oss/user-guide/multipart-upload">
     *     Upload Large Files</a>, for minio please refer to
     *     <a href="https://min.io/docs/minio/linux/developers/java/API.html#uploadsnowballobjects-uploadsnowballobjectsargs-args">
     *         Upload Snowball Objects.
     *     </a>
     *
     * @param bucketName The bucket name
     * @param objectName The object name
     * @param objects    Upload objects
     *
     * @return the relation of part number and part etag
     *
     * @throws CloudAppException if any error
     */
    Map<String, Integer> uploadObjects(String bucketName,
                                       String objectName,
                                       List<Pair<String, InputStream>> objects)
            throws CloudAppException;

    /**
     * Upload a big file, this service usually used for large file upload.
     *
     * @param bucketName The bucket name
     * @param objectName The object name
     * @param filePath   The file path
     * @param maxSizePerPart The max size of each part, maxSize per part should
     *                       be less than 2G
     *
     * @return true if upload success, otherwise false
     *
     * @throws CloudAppException if any error
     */
    boolean uploadBigFile(String bucketName,
                          String objectName,
                          Path filePath,
                          int maxSizePerPart) throws CloudAppException;
}
