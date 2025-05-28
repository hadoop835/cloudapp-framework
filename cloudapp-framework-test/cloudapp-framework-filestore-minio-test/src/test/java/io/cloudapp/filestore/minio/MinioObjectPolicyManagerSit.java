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

package io.cloudapp.filestore.minio;

import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.filestore.minio.service.MinioObjectPolicyManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MinioObjectPolicyManagerSit {
    
    private static final Logger logger =
            LoggerFactory.getLogger(MinioObjectPolicyManagerSit.class);
    
    private static final String BUCKET_NAME = "test987654";
    
    @Autowired
    MinioObjectPolicyManager minioObjectPolicyManager;
    
    @Test
    public void grantAccessPermissions_bucketName_objectName_accessAcl() {
        Assert.assertThrows(CloudAppException.class, () -> {
            minioObjectPolicyManager.grantAccessPermissions(BUCKET_NAME,
                                                            "test.txt",
                                                            "public"
            );
        });
    }
    
    @Test
    public void getObjectPolicy_bucketName_objectName() {
        String objectPolicy = minioObjectPolicyManager.getObjectPolicy(
                BUCKET_NAME, "test.txt");
        if (objectPolicy != null && !objectPolicy.isEmpty()) {
            logger.info("object policy: {}", objectPolicy);
        }
    }
    
    
    @Test
    public void getDelegatingStorageClient() {
        Assert.assertNotNull(
                minioObjectPolicyManager.getDelegatingStorageClient());
    }
    
    @Test
    public void deleteObjectPolicy_bucketName_objectName() {
        Assert.assertThrows(CloudAppException.class, () -> {
            boolean deleteObjectPolicy = minioObjectPolicyManager.deleteObjectPolicy(
                    BUCKET_NAME,
                    "test.txt"
            );
        });
    }
    
}
