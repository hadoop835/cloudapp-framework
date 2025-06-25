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
package com.alibaba.cloudapp.filestore.demo.controller;

import com.alibaba.cloudapp.api.filestore.BucketLifeCycleManager;
import com.alibaba.cloudapp.util.JsonUtil;
import io.minio.messages.LifecycleConfiguration;
import io.minio.messages.LifecycleRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bucket/lifecycle")
public class BucketLifeCycleController {
    
    private static final Logger logger =
            LoggerFactory.getLogger(BucketLifeCycleController.class);
    
    @Autowired
    private BucketLifeCycleManager bucketLifeCycleManager;
    
    @RequestMapping("/create")
    public void createBucketLifeCycle(String bucketName,
                                      String prefixName,
                                      int days) {
        bucketLifeCycleManager.expireObjectsAfterVersionNoncurrentDays(
                bucketName, prefixName, days);
    }
    
    @RequestMapping("/delete")
    public void deleteBucketLifeCycle(String bucketName) {
        bucketLifeCycleManager.deleteBucketLifeCycle(bucketName);
    }
    
    @RequestMapping("/transit")
    public void transitToWithLastAccessDays(String bucketName,
                                            String prefixName,
                                            String storageType,
                                            int days) {
        bucketLifeCycleManager.transitToWithLastAccessDays(bucketName,
                                                           prefixName,
                                                           storageType, days
        );
    }
    
    @RequestMapping("/list")
    public List<?> listBucketLifeCycle(String bucketName) {
        LifecycleConfiguration object = (LifecycleConfiguration) bucketLifeCycleManager.getBucketLifeCycle(
                bucketName);
        if (object == null) {
            logger.info("No bucket life cycle found");
            return null;
        }
        List<LifecycleRule> rules = object.rules();
        return JsonUtil.toJSONObject(rules);
    }
    
}
