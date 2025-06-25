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

package com.alibaba.cloudapp.filestore.demo.service;

import com.alibaba.cloudapp.api.filestore.BucketManager;
import com.alibaba.cloudapp.api.filestore.MultiPartsService;
import com.alibaba.cloudapp.model.Pairs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MultiPartUploadDemoService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MultiPartUploadDemoService.class);

    @Autowired
    private MultiPartsService multiPartsService;

    @Autowired
    private BucketManager bucketManager;
    @Value("${BUCKET}")
    private String bucketName;

    public void uploadBigFile(String file, int maxSizePerPart) throws Exception {
        Path filePath = Paths.get(file);
        String fileName = filePath.getFileName().toString();
        multiPartsService.uploadBigFile(bucketName, fileName, filePath, maxSizePerPart);
    }

    public void uploadObjects(String filePath, int maxSizePerPart) {
        List<Pairs.Pair<String, InputStream>> objects = null;
        List<String> partFiles = null;
        try {
            partFiles =sliceFile(maxSizePerPart, filePath);

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

            Path path = Paths.get(filePath);
            String fileName = path.getFileName().toString();

            multiPartsService.uploadObjects(bucketName, fileName, objects);

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

    private List<String> sliceFile(int partSize, String filePath) throws Exception {
        FileChannel fileChannel = FileChannel.open(Paths.get(filePath));
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

    @Override
    public void afterPropertiesSet() throws Exception {
        String filePath = "src/test/resources/post.pdf";
        URL uri = this.getClass().getClassLoader().getResource("./");

        File file = uri == null ? new File("./")
                : new File(uri.toURI()).getParentFile().getParentFile();

        File tempFile = new File(file, filePath);

        String fileName = tempFile.getName();

        try {
            uploadBigFile(tempFile.getAbsolutePath(), 512 * 1024);
        } catch (Exception e) {
            logger.error("upload Error", e);
        }

        try {
            uploadObjects(tempFile.getAbsolutePath(), 512 * 1024);
        } catch (Exception e) {
            logger.error("upload Error", e);
        }
    }
}
