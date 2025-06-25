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

package com.alibaba.cloudapp.filestore.aliyun.util;

import com.aliyun.oss.OSS;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.exeption.CloudAppInvalidRequestException;

public class OSSUtil {

    /**
     * check bucket exists
     * @param oss client
     * @param bucketName bucketName
     * @throws CloudAppException when bucket not exists, or invalid name
     */
    public static void checkBucketExists(OSS oss, String bucketName)
            throws CloudAppException {
        if(bucketName == null) {
            throw new CloudAppInvalidRequestException("Bucket name is required");
        }

        if (!oss.doesBucketExist(bucketName)) {
            throw new CloudAppInvalidRequestException(
                    "Bucket " + bucketName + " does not exist");
        }
    }

}
