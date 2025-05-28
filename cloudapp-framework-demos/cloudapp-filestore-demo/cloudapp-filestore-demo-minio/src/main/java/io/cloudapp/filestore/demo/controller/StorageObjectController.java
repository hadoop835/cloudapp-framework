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
import io.cloudapp.util.JsonUtil;
import io.minio.GetObjectTagsArgs;
import io.minio.MinioClient;
import io.minio.messages.Tags;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/object")
public class StorageObjectController {
    
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(
            StorageObjectController.class);
    
    @Autowired
    private StorageObjectService storageObjectService;
    
    @RequestMapping("get")
    public void getObject(String bucketName, String path,
                          HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition",
                           "attachment; filename=\"" + path + "\""
        );
        response.setStatus(HttpServletResponse.SC_OK);
        
        try (OutputStream out = response.getOutputStream();
             InputStream in = storageObjectService.getObject(bucketName,
                                                             path
             )) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            logger.error("get file error", e);
        }
    }
    
    @RequestMapping("getObjectTag")
    public void getObjectTag() {
        MinioClient client = (MinioClient) storageObjectService.getDelegatingStorageClient();
        GetObjectTagsArgs args = GetObjectTagsArgs.builder()
                                                  .bucket("test987")
                                                  .object("test.txt")
                                                  .build();
        Tags tags = null;
        try {
            tags = client.getObjectTags(args);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        Map<String, String> map = tags.get();
        map.forEach((k, v) -> logger.info("{} : {}", k, v));
    }
    
    @RequestMapping("delete")
    public boolean deleteObject(String bucketName, String path) {
        return storageObjectService.deleteObject(bucketName, path);
    }
    
    @RequestMapping("put")
    public boolean putObject(String bucketName, String path)
            throws IOException {
        byte[] bts = new byte[5 << 20];
        ByteArrayInputStream body = new ByteArrayInputStream(bts);
        boolean result = storageObjectService.putObject(bucketName, path, body);
        body.close();
        return result;
    }
    
    @RequestMapping("listVersions")
    public Collection<?> listObjectVersions(String bucketName, String path) {
        return JsonUtil.toJSONObject(
                storageObjectService.listObjectVersions(bucketName, path,
                                                        "991a535f-47f6-40ca-b85d-2858be743fbf",
                                                        10
                ));
    }
    
    @RequestMapping("listTop10")
    public Collection<?> listTop10LatestObjectVersions(String bucketName,
                                                       String path) {
        return JsonUtil.toJSONObject(
                storageObjectService.listTop10LatestObjectVersions(bucketName,
                                                                   path
                ));
    }
    
    @RequestMapping("restore")
    public boolean restoreObject(String bucketName, String path) {
        return storageObjectService.restoreObject(bucketName, path, 1,
                                                  "Standard"
        );
    }
    
    @RequestMapping("copy")
    public boolean copyObject(String bucketName,
                              String sourcePath,
                              String targetBucket,
                              String targetPath) {
        return storageObjectService.copy(bucketName, sourcePath,
                                         targetBucket, targetPath,true
        );
    }
    
    @RequestMapping("deleteObjects")
    public boolean deleteObjects(String bucketName, String paths) {
        List<String> objects = Arrays.asList(paths.split(","));
        return storageObjectService.deleteObjects(bucketName, objects, true);
    }
    
}
