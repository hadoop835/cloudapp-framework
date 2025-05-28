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
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.common.comm.ServiceClient;
import com.aliyun.oss.model.ObjectAcl;
import com.aliyun.oss.model.ObjectPermission;
import com.aliyun.oss.model.SetObjectAclRequest;
import com.aliyun.oss.model.VoidResult;
import io.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OSSObjectPolicyManagerTest {

    @Mock
    private OSS oss;

    private OSSObjectPolicyManager manager;

    private final String bucketName = "test-bucket";
    private final String objectName = "test-object";
    private final String accessAcl = "public-read";

    private VoidResult voidResult;

    @Before
    public void setUp() {
        manager = new OSSObjectPolicyManager(oss);

        voidResult = new VoidResult();
        ResponseMessage message = new ResponseMessage(new ServiceClient.Request());
        message.setStatusCode(200);
        voidResult.setResponse(message);

        doReturn(true).when(oss).doesBucketExist(anyString());
    }

    @Test
    public void testGrantAccessPermissions() throws CloudAppException {
        when(oss.setObjectAcl(any(SetObjectAclRequest.class))).thenReturn(voidResult);

        manager.grantAccessPermissions(bucketName, objectName, accessAcl);

        // Verify the method call with expected parameters
        Mockito.verify(oss).setObjectAcl(any(SetObjectAclRequest.class));
    }

    @Test(expected = CloudAppException.class)
    public void testGrantAccessPermissionsWithException() throws CloudAppException {
        // Simulate an exception scenario
        manager.grantAccessPermissions(null, objectName, accessAcl);
    }

    @Test
    public void testGetObjectPolicy() throws CloudAppException {
        // Assuming the method exists and can be mocked properly
        ObjectAcl acl = new ObjectAcl();
        acl.setPermission(ObjectPermission.parsePermission(accessAcl));

        when(oss.getObjectAcl(bucketName, objectName)).thenReturn(acl);

        String policy = manager.getObjectPolicy(bucketName, objectName);

        assertEquals(accessAcl, policy);
    }

    @Test(expected = RuntimeException.class)
    public void testGetObjectPolicyWithException() throws CloudAppException {
        // Setup a mock response for getObjectAcl which throws an exception
        when(oss.getObjectAcl(bucketName, objectName)).thenThrow(new RuntimeException("Mock exception"));

        manager.getObjectPolicy(bucketName, objectName);
    }

    @Test
    public void testDeleteObjectPolicy() throws CloudAppException {
        when(oss.setObjectAcl(any(SetObjectAclRequest.class))).thenReturn(voidResult);

        boolean result = manager.deleteObjectPolicy(bucketName, objectName);

        assertTrue(result);
        Mockito.verify(oss).setObjectAcl(any(SetObjectAclRequest.class));
    }

    @Test(expected = CloudAppException.class)
    public void testDeleteObjectPolicyWithException() throws CloudAppException {
        // Simulate an exception scenario
        manager.deleteObjectPolicy(null, objectName);
    }
}
