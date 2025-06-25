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

import com.alibaba.cloudapp.api.common.Pagination;
import com.alibaba.cloudapp.api.filestore.BucketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/bucket")
public class BucketController {
    
    @Autowired
    private BucketManager bucketManager;
    
    @RequestMapping("/list")
    public Collection listBuckets(
            @RequestParam(required = false) String prefix,
            @RequestParam(required = false) String resourceGroupId) {
        return bucketManager.listAllBucketsWithPrefix(prefix, resourceGroupId);
    }
    
    @RequestMapping("/create")
    public boolean createBucket(String bucketName) {
        return bucketManager.createBucket(bucketName);
    }
    
    @RequestMapping("/delete")
    public boolean deleteBucket(String bucketName) {
        return bucketManager.deleteBucket(bucketName);
    }
    
    @RequestMapping("/location")
    public String getBucketLocation(String bucketName) {
        return bucketManager.getBucketLocation(bucketName);
    }
    
    @RequestMapping("/listObjects")
    public Pagination<?> listObjects(
            String bucketName,
            @RequestParam(required = false) String nextMarker,
            @RequestParam(defaultValue = "10") int pageSize) {
        Pagination<?> paging = new Pagination<>();
        paging.setNextToken(nextMarker);
        paging.setMaxResults(pageSize);
        
        return bucketManager.listObjects(bucketName, paging);
    }
    
    @RequestMapping("/listTopNObjects")
    public Collection<?> listTopNObjects(String bucketName, int count) {
        return bucketManager.listTopNObjects(bucketName, count);
    }
    
    @RequestMapping("/listPagingBuckets")
    public Pagination<?> listPagingBuckets(
            @RequestParam(required = false) String prefix,
            @RequestParam(required = false) String resourceGroupId,
            @RequestParam(required = false) String nextMarker,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        Pagination<?> paging = new Pagination<>();
        paging.setNextToken(nextMarker);
        paging.setMaxResults(pageSize);
        
        return bucketManager.listPagingBuckets(prefix, resourceGroupId, paging);
    }
    
}
