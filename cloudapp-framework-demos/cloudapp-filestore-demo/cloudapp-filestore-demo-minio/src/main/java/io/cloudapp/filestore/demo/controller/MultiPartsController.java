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

import io.cloudapp.api.filestore.MultiPartsService;
import io.cloudapp.model.Pairs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/multipart")
public class MultiPartsController {
    
    @Autowired
    private MultiPartsService multiPartsService;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    private static final Logger logger = LoggerFactory.getLogger(MultiPartsController.class);
    
    private final String FILE_PATH = "post.pdf";
    
    @RequestMapping("/objects")
    public void upload(String bucketName, String objectName) {
        List<Pairs.Pair<String, InputStream>> objects = null;
        List<String> partFiles = null;
        try {
            partFiles = sliceFile( 5 * 1024 * 1024, FILE_PATH);
            
            AtomicInteger index = new AtomicInteger();
            objects = partFiles.stream().map(e -> {
                InputStream inputStream;
                try {
                    inputStream = new FileInputStream(e);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                index.getAndIncrement();
                return new Pairs.Pair<>(String.valueOf(index.get()), inputStream);
            }).collect(Collectors.toList());
            
            multiPartsService.uploadObjects(bucketName, objectName, objects);
            
        } catch (Exception e) {
            logger.error("upload error", e);
        } finally {
            if(objects != null) {
                objects.forEach(e -> {
                    try {
                        e.getValue().close();
                    } catch (IOException ex) {
                        logger.error("close error", ex);
                    }
                });
            }
            if(partFiles != null) {
                partFiles.forEach(e -> new File(e).delete());
            }
        }
        
    }
    
    @RequestMapping("/bigfile")
    public void uploadBigFile(String bucketName, String objectName) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + FILE_PATH);
        Path filePath = resource.getFile().toPath();
        multiPartsService.uploadBigFile(bucketName, objectName, filePath,
                                        5 * 1024 * 1024);
    }
    
    
    private List<String> sliceFile(int partSize, String filePath) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + filePath);
        FileChannel fileChannel = FileChannel.open(resource.getFile().toPath());
        long fileSize = fileChannel.size();
        int parts = (int) Math.ceil((double) fileSize / partSize);
        
        // Slice the file
        List<String> partFiles = new ArrayList<>();
        for (int i = 0; i < parts; i++) {
            long start = (long) i * partSize;
            long size = Math.min(partSize, fileSize - start);
            
            // Create a file to store the slice
            String sliceFilePath = filePath + ".part" + i;
            try (RandomAccessFile sliceFile = new RandomAccessFile(sliceFilePath, "rw");
                 FileChannel sliceChannel = sliceFile.getChannel()) {
                
                // Copy the slice from the original file
                fileChannel.transferTo(start, size, sliceChannel);
                
            }
            partFiles.add(sliceFilePath);
        }
        
        fileChannel.close();
        return partFiles;
    }
}
