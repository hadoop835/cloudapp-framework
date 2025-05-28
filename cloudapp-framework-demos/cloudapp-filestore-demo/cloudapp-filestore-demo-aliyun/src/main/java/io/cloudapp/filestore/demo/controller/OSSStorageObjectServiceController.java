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

package io.cloudapp.filestore.demo.controller;

import io.cloudapp.api.filestore.StorageObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
public class OSSStorageObjectServiceController {
    
    private final static Logger logger =
            LoggerFactory.getLogger(OSSStorageObjectServiceController.class);
    
    @Autowired
    StorageObjectService storageObjectService;
    
    private String bucketName = "gxrtestoss";
    
    private String path = "test.txt";
    
    @RequestMapping("/putObject")
    public boolean putObject_bucketName_path_content(String bucketName,
    String path, InputStream content) throws Exception {
        boolean putObject = storageObjectService.putObject(bucketName, path, content);
        logger.info("Put result : {}.", putObject);
        return putObject;
    }
    
    @RequestMapping("/putObject")
    public boolean putObject_content(InputStream content) throws Exception {
        boolean putObject = storageObjectService.putObject(bucketName, path, content);
        logger.info("Put result : {}.", putObject);
        return putObject;
    }}
