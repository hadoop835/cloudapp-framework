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
package com.alibaba.cloudapp.filestore.minio.util;

import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.exeption.CloudAppInvalidRequestException;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioUtil {
    
    /**
     * check bucket exists
     *
     * @param client     client
     * @param bucketName bucketName
     * @throws CloudAppException when bucket not exists, or invalid name
     */
    public static void checkBucketExists(MinioClient client, String bucketName)
            throws CloudAppException {
        if (bucketName == null) {
            throw new CloudAppInvalidRequestException(
                    "Bucket name is required");
        }
        
        BucketExistsArgs args = BucketExistsArgs.builder()
                                                .bucket(bucketName)
                                                .build();
        boolean bucketExists;
        try {
            bucketExists = client.bucketExists(args);
        } catch (ErrorResponseException |
                 InsufficientDataException |
                 InternalException |
                 InvalidKeyException |
                 InvalidResponseException |
                 IOException |
                 NoSuchAlgorithmException |
                 ServerException |
                 XmlParserException e) {
            throw MinioExceptionConverter.convert(e);
        }
        
        if (!bucketExists) {
            throw new CloudAppInvalidRequestException(
                    "Bucket " + bucketName + " does not exist");
        }
    }
    
    /**
     * check object exists
     *
     * @param client     client
     * @param bucketName bucketName
     * @throws CloudAppException when bucket not exists, or invalid name
     */
    public static boolean checkObjectExists(
            MinioClient client, String bucketName, String objectName
    ) throws CloudAppException {
        if (!StringUtils.hasText(bucketName)) {
            throw new CloudAppInvalidRequestException(
                    "Bucket name is required");
        }
        if (!StringUtils.hasText(objectName)) {
            throw new CloudAppInvalidRequestException(
                    "Object name is required");
        }
        
        StatObjectArgs statObjectArgs = StatObjectArgs
                .builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        
        boolean exists = false;
        try {
            StatObjectResponse resp = client.statObject(statObjectArgs);
            
            if (StringUtils.hasText(resp.etag()) && !resp.deleteMarker()) {
                exists = true;
            }
        } catch (ErrorResponseException e) {
            if (!e.errorResponse().code().equals("NoSuchKey")) {
                throw MinioExceptionConverter.convert(e);
            }
        } catch (ServerException |
                 InsufficientDataException |
                 IOException |
                 NoSuchAlgorithmException |
                 InvalidKeyException |
                 InvalidResponseException |
                 XmlParserException |
                 InternalException e) {
            throw MinioExceptionConverter.convert(e);
        }
        return exists;
    }
    
}
