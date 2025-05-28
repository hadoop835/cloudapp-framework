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

package io.cloudapp.filestore.demo.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.TagSet;
import io.cloudapp.api.filestore.StorageObjectService;
import io.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageObjectDemoService implements InitializingBean {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StorageObjectDemoService.class);

    private String bucketName = "test987654";
    
    private String targetBucketName = "test987654321";

    @Autowired
    private StorageObjectService storageObjectService;

    public void copyObject(String fileName, String copiedName) throws CloudAppException {
        boolean result = storageObjectService.copy(bucketName, fileName, targetBucketName,
                                                   copiedName, true);
        logger.info("Copy result: " + result);
    }

    public void putObject(String filePath) throws Exception{
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Path path =  Paths.get(filePath);
            String fileName = path.getFileName().toString();

            boolean result = storageObjectService.putObject(bucketName, fileName, fis);
            logger.info("Put result: " + result);

        } catch (Exception e) {
            throw new Exception("Error while uploading file", e);
        }
    }

    public void deleteObject(String fileName) throws Exception {
        boolean result = storageObjectService.deleteObject(bucketName, fileName);
        logger.info("Delete result: " + result);
    }

    public void listObjectVersions(String prefix, String keyMarker, int maxKeys) throws Exception {
        storageObjectService.listObjectVersions(bucketName, prefix, keyMarker, maxKeys)
                .forEach(e -> logger.info("listObjectVersions version info： {}", e));
    }

    public void deleteObjects(List<String> keys) throws Exception {
        storageObjectService.deleteObjects(bucketName, keys, false);
    }


    public void uploadObject(String fileFullPath, String fileName) {
        try (FileInputStream inputStream = new FileInputStream(fileFullPath)) {
            boolean result = storageObjectService.putObject(bucketName, fileName, inputStream);
            logger.info("createObject result: {}", result);
        } catch (FileNotFoundException e) {
            logger.error("File not found: {}", fileFullPath, e);
        } catch (CloudAppException e) {
            logger.error("Failed to upload object: {}", fileName, e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getObjectTagging(String fileName) {
        OSSClient ossClient = (OSSClient) storageObjectService.getDelegatingStorageClient();
        try  {
            TagSet tagSet = ossClient.getObjectTagging(bucketName, fileName);
            logger.info("tagSet: {}", tagSet);
        } catch (OSSException e) {
            logger.error("Error retrieving object tagging from OSS: {}", e.getMessage(), e);
        } catch (ClientException e) {
            logger.error("Error with client configuration: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String filePath = "src/test/resources/post.pdf";
        URL uri = this.getClass().getClassLoader().getResource("./");

        File file = uri == null ? new File("./")
                : new File(uri.toURI()).getParentFile().getParentFile();

        File tempFile = new File(file, filePath);

        String fileName = tempFile.getName();

        putObject(tempFile.getAbsolutePath());

        List<String> copyFiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String copyName = "copy" + i + "-" + fileName;
            copyObject(fileName, copyName);
            copyFiles.add(copyName);
        }

        listObjectVersions("copy", null, 10);

        deleteObjects(copyFiles);

        deleteObject(fileName);

        uploadObject(tempFile.getAbsolutePath(), fileName);
        getObjectTagging("test/test.txt");
    }
}
