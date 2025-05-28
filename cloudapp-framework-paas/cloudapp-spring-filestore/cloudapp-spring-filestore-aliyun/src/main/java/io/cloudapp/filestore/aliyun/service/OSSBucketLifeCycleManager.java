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

import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSErrorCode;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.LifecycleRule;
import com.aliyun.oss.model.SetBucketLifecycleRequest;
import com.aliyun.oss.model.StorageClass;
import com.aliyun.oss.model.VoidResult;
import io.cloudapp.api.filestore.BucketLifeCycleManager;
import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.filestore.aliyun.util.OSSExceptionConverter;
import io.cloudapp.filestore.aliyun.util.OSSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OSSBucketLifeCycleManager implements
        BucketLifeCycleManager<OSS, List<LifecycleRule>> {

    private static final Logger logger =
            LoggerFactory.getLogger(OSSBucketLifeCycleManager.class);

    private final OSS oss;

    public OSSBucketLifeCycleManager(OSS oss) {
        this.oss = oss;
    }

    /**
     * Transit to with last access days
     * @param bucketName Bucket name
     * @param objectPrefixName object prefix name
     * @param storageType The storage type, such as 'Standard', 'Standard-IA', 'Archive'
     * @param lastAccessDays last access days
     *
     * @throws CloudAppException occurs error
     */
    @Override
    public void transitToWithLastAccessDays(String bucketName,
                                            String objectPrefixName,
                                            String storageType,
                                            int lastAccessDays)
            throws CloudAppException {

        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            if (logger.isDebugEnabled()) {
                logger.debug(
                        "expireObjectsAfterVersionNonCurrentDays " +
                                "bucketName: {}, objectPrefixName: {}" +
                                ", storageType: {}, lastAccessDays: {}",
                        bucketName, objectPrefixName, storageType, lastAccessDays);
            }

            String ruleId = "Prefix-" + objectPrefixName + "-transitLastAccessDays";

            LifecycleRule lifecycleRule = new LifecycleRule(
                    ruleId,
                    objectPrefixName,
                    LifecycleRule.RuleStatus.Enabled);

            lifecycleRule.setStorageTransition(Collections.singletonList(
                    new LifecycleRule.StorageTransition(
                            lastAccessDays,
                            StorageClass.parse(storageType)
                    )
            ));

            SetBucketLifecycleRequest request = mergeToCurrentRules(bucketName,
                    lifecycleRule);

            VoidResult result = oss.setBucketLifecycle(request);

            tryLoggingIt(result);

        } catch (Exception e) {
            logger.error("Set expire objects after version non current days error");
            throw OSSExceptionConverter.convert(e);
        }
    }

    private static void tryLoggingIt(VoidResult result) {
        if (logger.isDebugEnabled()) {
            logger.debug("expireObjectsAfterVersionNonCurrentDays result: {}",
                    JSON.toJSONString(result));
        }
    }

    /**
     * Expire objects with create before
     * @param bucketName Bucket name
     * @param objectPrefixName object prefix name
     * @param lastAccessDays last access days
     *
     * @throws CloudAppException occurs error
     */
    @Override
    public void expireObjectsWithLastAccessDays(String bucketName,
                                                String objectPrefixName,
                                                int lastAccessDays)
            throws CloudAppException {

        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            if (logger.isDebugEnabled()) {
                logger.debug("expireObjectsAfterVersionNonCurrentDays " +
                        "bucketName: {}, " +
                        "objectPrefixName: {}," +
                        " lastAccessDays: {}",
                        bucketName, objectPrefixName, lastAccessDays);
            }

            String ruleId = "Prefix-" + objectPrefixName + "-expireLastAccessDays";

            LifecycleRule lifecycleRule = new LifecycleRule(
                    ruleId,
                    objectPrefixName,
                    LifecycleRule.RuleStatus.Enabled);
            lifecycleRule.setExpirationDays(lastAccessDays);

            SetBucketLifecycleRequest request = mergeToCurrentRules(bucketName, lifecycleRule);

            VoidResult result = oss.setBucketLifecycle(request);

            tryLoggingIt(result);

        } catch (Exception e) {
            logger.error("Set expire objects after version non current days error", e);
            throw OSSExceptionConverter.convert(e);
        }
    }

    /**
     * Expire objects with create before
     * @param bucketName Bucket name
     * @param objectPrefixName object prefix name
     * @param createBefore create before
     *
     * @throws CloudAppException occurs error
     */
    @Override
    public void expireObjectsWithCreateBefore(String bucketName,
                                              String objectPrefixName,
                                              Date createBefore)
            throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            if (logger.isDebugEnabled()) {
                logger.debug("expireObjectsAfterVersionNonCurrentDays " +
                        "bucketName: {}, " +
                        "objectPrefixName: {}, " +
                        "createBefore: {}",
                        bucketName, objectPrefixName, createBefore);
            }

            String ruleId = "Prefix-" + objectPrefixName + "-expireCreateBefore";

            LifecycleRule lifecycleRule = new LifecycleRule(
                    ruleId,
                    objectPrefixName,
                    LifecycleRule.RuleStatus.Enabled);

            lifecycleRule.setCreatedBeforeDate(convertDateUTZToZeroClock(createBefore));

            SetBucketLifecycleRequest request = mergeToCurrentRules(bucketName, lifecycleRule);

            VoidResult result = oss.setBucketLifecycle(request);

            tryLoggingIt(result);

        } catch (Exception e) {
            logger.error("Expire objects after version non current days error", e);
            throw OSSExceptionConverter.convert(e);
        }
    }

    /**
     * add lifecycle rule, which set expire objects after version noncurrent days
     * @param bucketName Bucket name
     * @param objectPrefixName object prefix name
     * @param noncurrentDays noncurrent days
     *
     * @throws CloudAppException occurs error
     */
    @Override
    public void expireObjectsAfterVersionNoncurrentDays(String bucketName,
                                                        String objectPrefixName,
                                                        int noncurrentDays)
            throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            if (logger.isDebugEnabled()) {
                logger.debug("expireObjectsAfterVersionNoncurrentDays " +
                        "bucketName: {}, " +
                        "objectPrefixName: {}," +
                        "noncurrentDays: {}",
                        bucketName, objectPrefixName, noncurrentDays);
            }

            String ruleId = "Prefix" + objectPrefixName + "expireAfterNonCurrentDays";
            LifecycleRule lifecycleRule = new LifecycleRule();
            lifecycleRule.setId(ruleId);
            lifecycleRule.setStatus(LifecycleRule.RuleStatus.Enabled);
            lifecycleRule.setPrefix(objectPrefixName);
            lifecycleRule.setNoncurrentVersionExpiration(
                    new LifecycleRule.NoncurrentVersionExpiration(noncurrentDays));

            SetBucketLifecycleRequest request = mergeToCurrentRules(bucketName, lifecycleRule);

            VoidResult result = oss.setBucketLifecycle(request);

            tryLoggingIt(result);
        } catch (Exception e) {
            logger.error("Set expire objects after version noncurrent days error", e);
            throw OSSExceptionConverter.convert(e);
        }
    }

    private SetBucketLifecycleRequest mergeToCurrentRules(
            String bucketName, LifecycleRule lifecycleRule) {
        List<LifecycleRule> rules = getBucketLifeCycle(bucketName);

        if (logger.isDebugEnabled()) {
            logger.debug(bucketName + " current  rules: {}", JSON.toJSONString(rules));
        }

        if (rules != null && !rules.isEmpty()) {
            rules.add(lifecycleRule);
            rules = rules.stream()
                    .map(e -> e.getId().equals(lifecycleRule.getId()) ? lifecycleRule : e)
                    .distinct()
                    .collect(Collectors.toList());

        } else {
            rules = Collections.singletonList(lifecycleRule);
        }

        SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(
                bucketName);
        request.setLifecycleRules(rules);
        return request;
    }

    /**
     * Get bucket life cycle rules
     *
     * @param bucketName bucket name
     * @return rules
     * @throws CloudAppException occurs error
     */
    @Override
    public List<LifecycleRule> getBucketLifeCycle(String bucketName)
            throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            List<LifecycleRule> lifecycleRules = oss.getBucketLifecycle(bucketName);

            if (logger.isDebugEnabled()) {
                logger.debug("getBucketLifeCycle result: {}",
                        JSON.toJSONString(lifecycleRules));
            }

            return lifecycleRules;

        } catch (ClientException ex) {
            logger.error("Get bucket life cycle rules error");

            throw OSSExceptionConverter.convert(ex);
        } catch (OSSException ex) {

            if(OSSErrorCode.NO_SUCH_LIFECYCLE.equals(ex.getErrorCode())) {
//                logger.warn("No such lifecycle rules, bucketName: {}", bucketName);
                return null;
            }

            throw OSSExceptionConverter.convert(ex);
        }
    }

    /**
     * Delete bucket life cycle rules
     *
     * @param bucketName bucket name
     * @return true if delete success
     * @throws CloudAppException occurs error
     */
    @Override
    public boolean deleteBucketLifeCycle(String bucketName) throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            VoidResult result = oss.deleteBucketLifecycle(bucketName);

            if (logger.isDebugEnabled()) {
                logger.debug("deleteBucketLifeCycle result: {}",
                        JSON.toJSONString(result));
            }

            return result.getResponse() == null ||
                    result.getResponse().isSuccessful();

        } catch (ClientException ex) {
            logger.error("Delete bucket life cycle rules error");
            throw OSSExceptionConverter.convert(ex);
        }
    }

    /**
     * Get delegating storage client
     *
     * @return delegating storage client
     */
    @Override
    public OSS getDelegatingStorageClient() {
        return oss;
    }

    private Date convertDateUTZToZeroClock(Date date) {
        ZonedDateTime localDate = date.toInstant()
                .atZone(ZoneId.systemDefault());

        Instant startDate = localDate.toOffsetDateTime()
                .atZoneSameInstant(ZoneId.of("UTC"))
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toInstant();

        return Date.from(startDate);
    }
}
