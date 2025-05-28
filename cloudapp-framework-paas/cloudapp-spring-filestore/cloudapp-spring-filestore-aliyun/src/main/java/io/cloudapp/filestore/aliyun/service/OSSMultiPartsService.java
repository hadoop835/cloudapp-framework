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
package io.cloudapp.filestore.aliyun.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import io.cloudapp.api.filestore.MultiPartsService;
import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.exeption.CloudAppInvalidArgumentsException;
import io.cloudapp.filestore.aliyun.util.OSSExceptionConverter;
import io.cloudapp.filestore.aliyun.util.OSSUtil;
import io.cloudapp.model.Pairs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OSSMultiPartsService implements MultiPartsService<OSS> {

    private static final Logger logger = LoggerFactory.getLogger(OSSMultiPartsService.class);

    private final OSS oss;


    public OSSMultiPartsService(OSS oss) {
        this.oss = oss;
    }

    @Override
    public OSS getDelegatingStorageClient() {
        return oss;
    }

    /**
     * Upload objects as multiple objects
     * @param bucketName The bucket name
     * @param objectName The object name
     * @param objects    Upload objects
     *
     * @return upload result
     */
    @Override
    public Map<String, Integer> uploadObjects(String bucketName,
                                              String objectName,
                                              List<Pairs.Pair<String, InputStream>> objects)
            throws CloudAppException {
        String uploadId = null;
        Map<String, Integer> partMapper = new HashMap<>();
        try {
            if (! StringUtils.hasText(objectName)) {
                throw new CloudAppInvalidArgumentsException(
                        "ObjectName should not be empty.");
            }

            OSSUtil.checkBucketExists(oss, bucketName);

            // init part request
            final AtomicInteger PART_NUMBER = new AtomicInteger(1);
            List<UploadPartRequest> partRequestList = objects.stream().map(
                    e -> {
                        UploadPartRequest request = new UploadPartRequest();
                        request.setBucketName(bucketName);
                        request.setPartNumber(PART_NUMBER.getAndIncrement());
                        request.setInputStream(e.getValue());
                        request.setKey(objectName);

                        partMapper.put(e.getKey(), request.getPartNumber());
                        return request;
                    }
            ).collect(Collectors.toList());

            // init multipart upload
            InitiateMultipartUploadResult initResult =
                    initiateMultipartUpload(bucketName, objectName);

            uploadId = initResult.getUploadId();

            // upload part
            List<PartETag> eTagList = partRequestList.stream().map(e -> {
                e.setUploadId(initResult.getUploadId());
                try {
                    UploadPartResult uploadPartResult = oss.uploadPart(e);

                    if(logger.isDebugEnabled()) {
                        logger.debug("Upload part {} success",
                                uploadPartResult.getPartNumber());
                    }

                    return new PartETag(uploadPartResult.getPartNumber(),
                            uploadPartResult.getETag());
                } catch (Exception ex) {
                    throw new CloudAppException("Failed to upload part", ex);
                }
            }).collect(Collectors.toList());

            // complete multipart upload
            CompleteMultipartUploadResult completeResult =  oss.completeMultipartUpload(
                    new CompleteMultipartUploadRequest(
                            bucketName,
                            objectName,
                            initResult.getUploadId(),
                            eTagList
            ));

            return partMapper;
        } catch (Exception ex) {
            if(uploadId != null) {
                // abort multipart upload
                logger.info("Abort multipart objects upload {}", uploadId);
                oss.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, objectName, uploadId));
            }

            throw OSSExceptionConverter.convert(ex);
        }
    }

    private InitiateMultipartUploadResult initiateMultipartUpload(
            String bucketName,
            String objectName) throws CloudAppException {
        InitiateMultipartUploadRequest initRequest =
                new InitiateMultipartUploadRequest(bucketName, objectName);

        InitiateMultipartUploadResult initResult = oss.initiateMultipartUpload(initRequest);

        if (initResult == null || initResult.getUploadId() == null) {
            throw new CloudAppException("Failed to initiate multipart upload",
                    "CloudApp.OSSInitUploadFailed");
        }
        return initResult;
    }

    /**
     * Upload big file as multiple objects
     * @param bucketName The bucket name
     * @param objectName The object name
     * @param filePath   The file path
     * @param maxSizePerPart The max size of each part, maxSize per part should
     *                       be less than 2G
     *
     * @return upload result
     * @throws CloudAppException if any error occurs
     */
    @Override
    public boolean uploadBigFile(String bucketName,
                                 String objectName,
                                 Path filePath,
                                 int maxSizePerPart)
            throws CloudAppException {
        String uploadId = null;
        FileInputStream inputStream = null;

        try {
            if (! StringUtils.hasText(objectName)) {
                throw new CloudAppInvalidArgumentsException(
                        "ObjectName should not be empty.");
            }

            logger.info("Starting to upload a big file to " +
                    "bucket: {}, object: {}, file: {}, size: {}",
                    bucketName, objectName, filePath.toString(), maxSizePerPart);

            OSSUtil.checkBucketExists(oss, bucketName);

            File file = filePath.toFile();
            long size = file.length();

            int partCount = (int) ((size + maxSizePerPart - 1) / maxSizePerPart);

            InitiateMultipartUploadResult initResult =
                    initiateMultipartUpload(bucketName, objectName);

            uploadId = initResult.getUploadId();

            List<PartETag> etagList = new ArrayList<>(partCount);

            for (int i = 0; i < partCount; i++) {
                UploadPartRequest request = new UploadPartRequest();
                request.setBucketName(bucketName);
                request.setPartNumber(i + 1);
                request.setUploadId(initResult.getUploadId());
                request.setKey(objectName);
                request.setPartSize(Math.min(size, maxSizePerPart));

                inputStream = new FileInputStream(file);

                inputStream.skip(i * (long) maxSizePerPart);
                request.setInputStream(inputStream);

                UploadPartResult uploadPartResult = oss.uploadPart(request);

                logger.info("port number: {} clientCheckSum: {}, serverCheckSum: {}",
                        request.getPartNumber(),
                        uploadPartResult.getClientCRC(),
                        uploadPartResult.getServerCRC());


                etagList.add(new PartETag(request.getPartNumber(), uploadPartResult.getETag()));

                size = size - maxSizePerPart;
                inputStream.close();
            }

            CompleteMultipartUploadResult completeResult =  oss.completeMultipartUpload(
                    new CompleteMultipartUploadRequest(
                            bucketName,
                            objectName,
                            initResult.getUploadId(),
                            etagList
                    ));

           return true;
        } catch (Exception ex) {
            logger.error("Failed to upload big file, bucketName: {}, objectName: {}",
                    bucketName, objectName);

            // abort multipart upload
            if(uploadId != null) {
                logger.info("Abort multipart big file upload {}", uploadId);
                oss.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, objectName, uploadId));
            }

            throw OSSExceptionConverter.convert(ex);
        } finally {
            // close input stream
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Failed to close input stream", e);
                }
            }
        }
    }
}
