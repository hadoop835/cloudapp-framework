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
import com.alibaba.cloudapp.exeption.CloudAppInvalidAccessException;
import com.alibaba.cloudapp.exeption.CloudAppInvalidRequestException;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Convert any exception to CloudAppException
 */
public class MinioExceptionConverter {
    
    public static CloudAppException convert(CloudAppException e) {
        return e;
    }
    
    public static CloudAppException convert(Throwable ex) {
        if (ex instanceof ErrorResponseException
                || ex instanceof InvalidResponseException
                || ex instanceof ServerException
        ) {
            return new CloudAppInvalidRequestException(
                    ex.getMessage(), ex.getClass().getSimpleName(), ex
            );
        }
        
        if (ex instanceof InvalidKeyException || ex instanceof NoSuchAlgorithmException) {
            return new CloudAppInvalidAccessException(
                    ex.getMessage(), ex.getClass().getSimpleName(), ex
            );
        }
        
        if (ex instanceof MinioException) {
            MinioException me = (MinioException) ex;
            return new CloudAppException(me.getMessage(), me.httpTrace(), ex);
        }
        
        return new CloudAppException(ex.getMessage(), ex);
    }
    
}
