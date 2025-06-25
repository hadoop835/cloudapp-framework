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

package com.alibaba.cloudapp.filestore.aliyun.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.common.comm.ServiceClient;
import com.aliyun.oss.model.LifecycleRule;
import com.aliyun.oss.model.SetBucketLifecycleRequest;
import com.aliyun.oss.model.StorageClass;
import com.aliyun.oss.model.VoidResult;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OSSBucketLifeCycleManagerTest {

    @Mock
    private OSS oss;

    private OSSBucketLifeCycleManager manager;

    private final String bucketName = "test-bucket";
    private final String objectPrefixName = "test-object-prefix";

    private VoidResult voidResult;

    @Before
    public void setUp() {
        manager = new OSSBucketLifeCycleManager(oss);

        voidResult = new VoidResult();
        ResponseMessage message = new ResponseMessage(new ServiceClient.Request());
        message.setStatusCode(200);
        voidResult.setResponse(message);

        doReturn(true).when(oss).doesBucketExist(anyString());
    }

    @Test
    public void testTransitToWithLastAccessDays() throws CloudAppException {
        LifecycleRule expectedRule = new LifecycleRule();
        expectedRule.setPrefix(objectPrefixName);
        expectedRule.setStatus(LifecycleRule.RuleStatus.Enabled);
        expectedRule.setStorageTransition(Collections.singletonList(
                new LifecycleRule.StorageTransition(30, StorageClass.IA)
        ));

        doReturn(voidResult).when(oss).setBucketLifecycle(any(SetBucketLifecycleRequest.class));

        String storageType = "IA";
        manager.transitToWithLastAccessDays(bucketName, objectPrefixName, storageType, 30);

        // Validate the interaction or the outcome
        Mockito.verify(oss).setBucketLifecycle(any(SetBucketLifecycleRequest.class));
    }

    @Test
    public void testExpireObjectsWithLastAccessDays() throws CloudAppException {
        LifecycleRule expectedRule = new LifecycleRule();
        expectedRule.setPrefix(objectPrefixName);
        expectedRule.setStatus(LifecycleRule.RuleStatus.Enabled);
        expectedRule.setExpirationDays(60);

        doReturn(voidResult).when(oss).setBucketLifecycle(any(SetBucketLifecycleRequest.class));

        manager.expireObjectsWithLastAccessDays(bucketName, objectPrefixName, 60);

        // Validate the interaction or the outcome
        Mockito.verify(oss).setBucketLifecycle(any(SetBucketLifecycleRequest.class));
    }

    @Test
    public void testExpireObjectsWithCreateBefore() throws CloudAppException {
        Date createBefore = new Date();

        LifecycleRule expectedRule = new LifecycleRule();
        expectedRule.setPrefix(objectPrefixName);
        expectedRule.setStatus(LifecycleRule.RuleStatus.Enabled);
        expectedRule.setCreatedBeforeDate(createBefore);

        doReturn(voidResult).when(oss).setBucketLifecycle(any(SetBucketLifecycleRequest.class));

        manager.expireObjectsWithCreateBefore(bucketName, objectPrefixName, createBefore);

        // Validate the interaction or the outcome
        Mockito.verify(oss).setBucketLifecycle(any(SetBucketLifecycleRequest.class));
    }

    @Test
    public void testExpireObjectsAfterVersionNoncurrentDays() throws CloudAppException {
        LifecycleRule expectedRule = new LifecycleRule();
        expectedRule.setPrefix(objectPrefixName);
        expectedRule.setStatus(LifecycleRule.RuleStatus.Enabled);
        expectedRule.setNoncurrentVersionExpiration(new LifecycleRule.NoncurrentVersionExpiration(7));

        doReturn(voidResult).when(oss).setBucketLifecycle(any(SetBucketLifecycleRequest.class));

        manager.expireObjectsAfterVersionNoncurrentDays(bucketName, objectPrefixName, 7);

        // Validate the interaction or the outcome
        Mockito.verify(oss).setBucketLifecycle(any(SetBucketLifecycleRequest.class));
    }

    @Test
    public void testGetBucketLifeCycle() throws CloudAppException {
        List<LifecycleRule> expectedRules = Collections.singletonList(
                new LifecycleRule("existing-id", objectPrefixName, LifecycleRule.RuleStatus.Enabled)
        );
        when(oss.getBucketLifecycle(bucketName)).thenReturn(expectedRules);

        List<LifecycleRule> actualRules = manager.getBucketLifeCycle(bucketName);

        assertEquals(expectedRules, actualRules);
    }

    @Test
    public void testDeleteBucketLifeCycle() throws CloudAppException {
        doReturn(voidResult).when(oss).deleteBucketLifecycle(bucketName);

        boolean result = manager.deleteBucketLifeCycle(bucketName);

        assertTrue(result);
        Mockito.verify(oss).deleteBucketLifecycle(bucketName);
    }
}
