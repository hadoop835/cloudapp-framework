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

package com.alibaba.cloudapp.config.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.acm.model.v20200206.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.ProtocolType;
import com.alibaba.cloudapp.api.config.ConfigObject;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.exeption.CloudAppInvalidArgumentsException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.alibaba.cloudapp.config.aliyun.AliyunConfigManager.CONFIGURATION_NOT_EXISTS;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AliyunConfigManagerTest {
    
    @Mock
    DefaultAcsClient defaultAcsClient;
    private String namespaceId = "test";
    private String defaultGroup = "DEFAULT_GROUP";
    private int defaultTimeout = 3000;
    private AliyunConfigManager aliyunConfigManager;
    private DeleteConfigurationResponse normalDeleteConfigurationResponse =
            new DeleteConfigurationResponse();
    
    @Before
    public void before() {
        aliyunConfigManager = new AliyunConfigManager(defaultAcsClient,
                                                      namespaceId,
                                                      defaultGroup,
                                                      defaultTimeout,
                                                      "http"
        );
        normalDeleteConfigurationResponse.setCode("ok");
    }
    
    @Test
    public void getDelegatingClient() {
        Assert.assertNotNull(aliyunConfigManager.getDelegatingClient());
    }
    
    @Test
    public void test_deleteConfig() throws ClientException {
        AtomicReference reqReference = new AtomicReference();
        Mockito.when(defaultAcsClient.getAcsResponse(
                       ArgumentMatchers.argThat(req -> {
                           reqReference.set(req);
                           return true;
                       })))
               .thenReturn(normalDeleteConfigurationResponse);
        boolean result = aliyunConfigManager.deleteConfig("/groupa/datab");
        Assert.assertTrue(result);
        DeleteConfigurationRequest request = (DeleteConfigurationRequest) reqReference.get();
        verifyDeleteRequest(request);
    }
    
    @Test
    public void test_deleteConfig_response_not_ok() throws ClientException {
        DeleteConfigurationResponse failResponse =
                new DeleteConfigurationResponse();
        failResponse.setCode("500");
        AtomicReference reqReference = new AtomicReference();
        Mockito.when(defaultAcsClient.getAcsResponse(
                       ArgumentMatchers.argThat(req -> {
                           reqReference.set(req);
                           return true;
                       })))
               .thenReturn(failResponse);
        boolean result = aliyunConfigManager.deleteConfig("/groupa/datab");
        Assert.assertFalse(result);
        DeleteConfigurationRequest request = (DeleteConfigurationRequest) reqReference.get();
        verifyDeleteRequest(request);
    }
    
    private void verifyDeleteRequest(DeleteConfigurationRequest request) {
        assertEquals(namespaceId, request.getNamespaceId());
        assertEquals(ProtocolType.HTTP, request.getSysProtocol());
        assertEquals("groupa", request.getGroup());
        assertEquals("datab", request.getDataId());
        assertEquals(defaultTimeout, request.getSysConnectTimeout().intValue());
        assertEquals(defaultTimeout, request.getSysReadTimeout().intValue());
    }
    
    @Test
    public void test_deleteConfig_should_return_false_when_ClientException_with_errorcode_not_exists()
            throws ClientException {
        AtomicReference reqReference = new AtomicReference();
        Mockito.when(defaultAcsClient.getAcsResponse(
                ArgumentMatchers.argThat(req -> {
                    reqReference.set(req);
                    return true;
                }))).thenThrow(
                new ClientException(CONFIGURATION_NOT_EXISTS, ""));
        boolean result = aliyunConfigManager.deleteConfig("/groupa/datab");
        Assert.assertFalse(result);
    }
    
    @Test
    public void test_deleteConfig_should_throw_CloudAppException_when_ClientException_with_errorcode_not_exists()
            throws ClientException {
        AtomicReference reqReference = new AtomicReference();
        Mockito.when(defaultAcsClient.getAcsResponse(
                ArgumentMatchers.argThat(req -> {
                    reqReference.set(req);
                    return true;
                }))).thenThrow(new ClientException("other.exception", ""));
        try {
            aliyunConfigManager.deleteConfig("/groupa/datab");
            fail("should throw CloudAppException");
        } catch (CloudAppException e) {
            assertEquals(AliyunConfigConstants.CLOUD_APP_NACOS_DELETE_ERROR,
                         e.getCode()
            );
        }
        
    }
    
    @Test
    public void test_publish_when_config_not_exists() throws ClientException {
        AtomicReference describeConfigurationRequestRef = new AtomicReference<>();
        AtomicReference createConfigurationRequest = new AtomicReference<>();
        Mockito.when(defaultAcsClient.getAcsResponse(
                       ArgumentMatchers.argThat(req -> {
                           if (DescribeConfigurationRequest.class.isAssignableFrom(
                                   req.getClass())) {
                               describeConfigurationRequestRef.set(req);
                           } else {
                               createConfigurationRequest.set(req);
                           }
                           return true;
                       })))
               .thenAnswer(invocation -> {
                   Object req = invocation.getArgument(0);
                   if (DescribeConfigurationRequest.class.isAssignableFrom(
                           req.getClass())) {
                       throw new ClientException(CONFIGURATION_NOT_EXISTS, "");
                   } else {
                       return makeSuccessCreateResponse();
                   }
               });
        String content = "content";
        boolean result = aliyunConfigManager.publish("/groupa/datab", content);
        Assert.assertTrue(result);
        verifyDescribeConfigurationRequest(
                (DescribeConfigurationRequest) describeConfigurationRequestRef.get());
        verifyCreateConfigurationRequest(content,
                                         (CreateConfigurationRequest) createConfigurationRequest.get()
        );
    }
    
    
    @Test
    public void test_publish_when_config_exists() throws ClientException {
        AtomicReference describeConfigurationRequestRef = new AtomicReference<>();
        AtomicReference deployConfigurationRequest =
                new AtomicReference<>();
        Mockito.when(defaultAcsClient.getAcsResponse(
                       ArgumentMatchers.argThat(req -> {
                           if (DescribeConfigurationRequest.class.isAssignableFrom(
                                   req.getClass())) {
                               describeConfigurationRequestRef.set(req);
                           } else {
                               deployConfigurationRequest.set(req);
                           }
                           return true;
                       })))
               .thenAnswer(invocation -> {
                   Object req = invocation.getArgument(0);
                   if (DescribeConfigurationRequest.class.isAssignableFrom(
                           req.getClass())) {
                       return makeSuccessDescribeResponse();
                   } else {
                       return makeSuccessDeployResponse();
                   }
               });
        
        String content = "content";
        boolean result = aliyunConfigManager.publish("/groupa/datab", content);
        Assert.assertTrue(result);
        verifyDescribeConfigurationRequest(
                (DescribeConfigurationRequest) describeConfigurationRequestRef.get());
        verifyDeployConfigurationRequest(content,
                                         (DeployConfigurationRequest) deployConfigurationRequest.get()
        );
    }
    
    @Test
    public void test_publish_when_create_response_fail()
            throws ClientException {
        AtomicReference describeConfigurationRequestRef = new AtomicReference<>();
        AtomicReference createConfigurationRequest = new AtomicReference<>();
        Mockito.when(defaultAcsClient.getAcsResponse(
                       ArgumentMatchers.argThat(req -> {
                           if (DescribeConfigurationRequest.class.isAssignableFrom(
                                   req.getClass())) {
                               describeConfigurationRequestRef.set(req);
                           } else {
                               createConfigurationRequest.set(req);
                           }
                           return true;
                       })))
               .thenAnswer(invocation -> {
                   Object req = invocation.getArgument(0);
                   if (DescribeConfigurationRequest.class.isAssignableFrom(
                           req.getClass())) {
                       throw new ClientException(CONFIGURATION_NOT_EXISTS, "");
                   } else {
                       CreateConfigurationResponse response =
                               new CreateConfigurationResponse();
                       response.setCode("fail");
                       return response;
                   }
               });
        String content = "content";
        assertFalse(aliyunConfigManager.publish("/groupa/datab", content));
        verifyDescribeConfigurationRequest(
                (DescribeConfigurationRequest) describeConfigurationRequestRef.get());
        verifyCreateConfigurationRequest(content,
                                         (CreateConfigurationRequest) createConfigurationRequest.get()
        );
    }
    
    @Test
    public void test_publish_when_create_throw_ClientException()
            throws ClientException {
        AtomicReference describeConfigurationRequestRef = new AtomicReference<>();
        AtomicReference createConfigurationRequest = new AtomicReference<>();
        Mockito.when(defaultAcsClient.getAcsResponse(
                       ArgumentMatchers.argThat(req -> {
                           if (DescribeConfigurationRequest.class.isAssignableFrom(
                                   req.getClass())) {
                               describeConfigurationRequestRef.set(req);
                           } else {
                               createConfigurationRequest.set(req);
                           }
                           return true;
                       })))
               .thenAnswer(invocation -> {
                   Object req = invocation.getArgument(0);
                   if (DescribeConfigurationRequest.class.isAssignableFrom(
                           req.getClass())) {
                       throw new ClientException(CONFIGURATION_NOT_EXISTS, "");
                   } else {
                       throw new ClientException("unknown exception", "");
                   }
               });
        String content = "content";
        try {
            aliyunConfigManager.publish("/groupa/datab", content);
            fail("should throw  CloudAppException");
        } catch (CloudAppException cloudAppException) {
            assertEquals(AliyunConfigConstants.CLOUD_APP_NACOS_PUBLISH_ERROR,
                         cloudAppException.getCode()
            );
        }
        verifyDescribeConfigurationRequest(
                (DescribeConfigurationRequest) describeConfigurationRequestRef.get());
        verifyCreateConfigurationRequest(content,
                                         (CreateConfigurationRequest) createConfigurationRequest.get()
        );
    }
    
    @Test
    public void test_publish_when_deploy_throw_CloudAppException()
            throws ClientException {
        AtomicReference describeConfigurationRequestRef = new AtomicReference<>();
        AtomicReference deployConfigurationRequest =
                new AtomicReference<>();
        Mockito.when(defaultAcsClient.getAcsResponse(
                       ArgumentMatchers.argThat(req -> {
                           if (DescribeConfigurationRequest.class.isAssignableFrom(
                                   req.getClass())) {
                               describeConfigurationRequestRef.set(req);
                           } else {
                               deployConfigurationRequest.set(req);
                           }
                           return true;
                       })))
               .thenAnswer(invocation -> {
                   Object req = invocation.getArgument(0);
                   if (DescribeConfigurationRequest.class.isAssignableFrom(
                           req.getClass())) {
                       return makeSuccessDescribeResponse();
                   } else {
                       throw new ClientException("unknown exception", "");
                   }
               });
        
        String content = "content";
        try {
            aliyunConfigManager.publish("/groupa/datab", content);
            fail("should throw  CloudAppException");
        } catch (CloudAppException cloudAppException) {
            assertEquals(AliyunConfigConstants.CLOUD_APP_NACOS_PUBLISH_ERROR,
                         cloudAppException.getCode()
            );
        }
        verifyDescribeConfigurationRequest(
                (DescribeConfigurationRequest) describeConfigurationRequestRef.get());
        verifyDeployConfigurationRequest(content,
                                         (DeployConfigurationRequest) deployConfigurationRequest.get()
        );
    }
    
    @Test
    public void test_publish_when_deploy_response_fail()
            throws ClientException {
        AtomicReference describeConfigurationRequestRef = new AtomicReference<>();
        AtomicReference deployConfigurationRequest =
                new AtomicReference<>();
        Mockito.when(defaultAcsClient.getAcsResponse(
                       ArgumentMatchers.argThat(req -> {
                           if (DescribeConfigurationRequest.class.isAssignableFrom(
                                   req.getClass())) {
                               describeConfigurationRequestRef.set(req);
                           } else {
                               deployConfigurationRequest.set(req);
                           }
                           return true;
                       })))
               .thenAnswer(invocation -> {
                   Object req = invocation.getArgument(0);
                   if (DescribeConfigurationRequest.class.isAssignableFrom(
                           req.getClass())) {
                       return makeSuccessDescribeResponse();
                   } else {
                       DeployConfigurationResponse response = new DeployConfigurationResponse();
                       response.setCode("fail");
                       return response;
                   }
               });
        
        String content = "content";
        assertFalse(aliyunConfigManager.publish("/groupa/datab", content));
        verifyDescribeConfigurationRequest(
                (DescribeConfigurationRequest) describeConfigurationRequestRef.get());
        verifyDeployConfigurationRequest(content,
                                         (DeployConfigurationRequest) deployConfigurationRequest.get()
        );
    }
    
    @Test
    public void test_createConfigurationRequest_when_content_is_DeployConfigurationRequest()
            throws ClientException {
        String configName = "/groupa/datab";
        DeployConfigurationRequest content = new DeployConfigurationRequest();
        content.setContent("content");
        content.setAppName("appName");
        content.setDesc("desc");
        content.setTags("tag1,tag2");
        content.setType("yaml");
        ConfigObject<Object> configObject = ConfigObject.builder()
                                                        .configName(configName)
                                                        .content(content)
                                                        .build();
        NacosConfigReadService.ConfigFullName configFullName = NacosConfigReadService.parse(
                configName, defaultGroup);
        CreateConfigurationRequest configurationRequest = aliyunConfigManager.createConfigurationRequest(
                configObject,
                configFullName
        );
        assertEquals(content.getContent(), configurationRequest.getContent());
        assertEquals(content.getAppName(), configurationRequest.getAppName());
        assertEquals(content.getDesc(), configurationRequest.getDesc());
        assertEquals(content.getTags(), configurationRequest.getTags());
        assertEquals(content.getType(), configurationRequest.getType());
    }
    
    @Test
    public void test_deployConfigurationRequest_when_content_is_DeployConfigurationRequest()
            throws ClientException {
        String configName = "/groupa/datab";
        DeployConfigurationRequest content = new DeployConfigurationRequest();
        content.setContent("content");
        content.setAppName("appName");
        content.setDesc("desc");
        content.setTags("tag1,tag2");
        content.setType("yaml");
        ConfigObject<Object> configObject = ConfigObject.builder()
                                                        .configName(configName)
                                                        .content(content)
                                                        .build();
        NacosConfigReadService.ConfigFullName configFullName = NacosConfigReadService.parse(
                configName, defaultGroup);
        DeployConfigurationRequest configurationRequest =
                aliyunConfigManager.deployConfigurationRequest(
                        configObject,
                        configFullName
                );
        assertEquals(content.getContent(), configurationRequest.getContent());
        assertEquals(content.getAppName(), configurationRequest.getAppName());
        assertEquals(content.getDesc(), configurationRequest.getDesc());
        assertEquals(content.getTags(), configurationRequest.getTags());
        assertEquals(content.getType(), configurationRequest.getType());
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void test_deployConfigurationRequest_when_content_is_unknown_type() {
        String configName = "/groupa/datab";
        ConfigObject<Object> configObject = ConfigObject.builder()
                                                        .configName(configName)
                                                        .content(
                                                                new HashMap<>())
                                                        .build();
        NacosConfigReadService.ConfigFullName configFullName = NacosConfigReadService.parse(
                configName, defaultGroup);
        aliyunConfigManager.deployConfigurationRequest(
                configObject,
                configFullName
        );
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void test_createConfigurationRequest_when_content_is_unknown_type() {
        String configName = "/groupa/datab";
        ConfigObject<Object> configObject = ConfigObject.builder()
                                                        .configName(configName)
                                                        .content(
                                                                new HashMap<>())
                                                        .build();
        NacosConfigReadService.ConfigFullName configFullName = NacosConfigReadService.parse(
                configName, defaultGroup);
        aliyunConfigManager.createConfigurationRequest(
                configObject,
                configFullName
        );
    }
    
    private CreateConfigurationResponse makeSuccessCreateResponse() {
        CreateConfigurationResponse createResponse =
                new CreateConfigurationResponse();
        createResponse.setCode("ok");
        return createResponse;
    }
    
    private DeployConfigurationResponse makeSuccessDeployResponse() {
        DeployConfigurationResponse deployResponse =
                new DeployConfigurationResponse();
        deployResponse.setCode("ok");
        return deployResponse;
    }
    
    private DescribeConfigurationResponse makeSuccessDescribeResponse() {
        DescribeConfigurationResponse describeResponse =
                new DescribeConfigurationResponse();
        describeResponse.setCode("ok");
        DescribeConfigurationResponse.Configuration configuration = new DescribeConfigurationResponse.Configuration();
        configuration.setContent("content");
        describeResponse.setConfiguration(configuration);
        return describeResponse;
    }
    
    private void verifyDescribeConfigurationRequest(DescribeConfigurationRequest request) {
        assertEquals(namespaceId, request.getNamespaceId());
        assertEquals(ProtocolType.HTTP, request.getSysProtocol());
        assertEquals("groupa", request.getGroup());
        assertEquals("datab", request.getDataId());
        assertEquals(defaultTimeout, request.getSysConnectTimeout().intValue());
        assertEquals(defaultTimeout, request.getSysReadTimeout().intValue());
    }
    
    private void verifyCreateConfigurationRequest(String content,
                                                  CreateConfigurationRequest request) {
        assertEquals(namespaceId, request.getNamespaceId());
        assertEquals(ProtocolType.HTTP, request.getSysProtocol());
        assertEquals("groupa", request.getGroup());
        assertEquals("datab", request.getDataId());
        assertEquals(defaultTimeout, request.getSysConnectTimeout().intValue());
        assertEquals(defaultTimeout, request.getSysReadTimeout().intValue());
        assertEquals(content, request.getContent());
        assertEquals("text", request.getType());
    }
    
    private void verifyDeployConfigurationRequest(String content,
                                                  DeployConfigurationRequest request) {
        assertEquals(namespaceId, request.getNamespaceId());
        assertEquals(ProtocolType.HTTP, request.getSysProtocol());
        assertEquals("groupa", request.getGroup());
        assertEquals("datab", request.getDataId());
        assertEquals(defaultTimeout, request.getSysConnectTimeout().intValue());
        assertEquals(defaultTimeout, request.getSysReadTimeout().intValue());
        assertEquals(content, request.getContent());
        assertEquals("text", request.getType());
    }
    
    @Test
    public void publishConfigs() throws ClientException {
        AtomicReference describeConfigurationRequestRef = new AtomicReference<>();
        AtomicReference createConfigurationRequest = new AtomicReference<>();
        Mockito.when(defaultAcsClient.getAcsResponse(
                       ArgumentMatchers.argThat(req -> {
                           if (DescribeConfigurationRequest.class.isAssignableFrom(
                                   req.getClass())) {
                               describeConfigurationRequestRef.set(req);
                           } else {
                               createConfigurationRequest.set(req);
                           }
                           return true;
                       })))
               .thenAnswer(invocation -> {
                   Object req = invocation.getArgument(0);
                   if (DescribeConfigurationRequest.class.isAssignableFrom(
                           req.getClass())) {
                       throw new ClientException(CONFIGURATION_NOT_EXISTS, "");
                   } else {
                       return makeSuccessCreateResponse();
                   }
               });
        String configName = "/groupa/datab";
        String content = "content";
        ConfigObject<Object> configObject = ConfigObject.builder()
                                                        .configName(configName)
                                                        .content(content)
                                                        .build();
        aliyunConfigManager.publishConfigs(Arrays.asList(configObject));
        verifyDescribeConfigurationRequest(
                (DescribeConfigurationRequest) describeConfigurationRequestRef.get());
        verifyCreateConfigurationRequest(content,
                                         (CreateConfigurationRequest) createConfigurationRequest.get()
        );
    }
    
    @Test
    public void deleteConfigs_with_ConfigObject() throws ClientException {
        AtomicReference reqReference = new AtomicReference();
        Mockito.when(defaultAcsClient.getAcsResponse(
                       ArgumentMatchers.argThat(req -> {
                           reqReference.set(req);
                           return true;
                       })))
               .thenReturn(normalDeleteConfigurationResponse);
        ConfigObject<Object> configObject = ConfigObject.builder()
                                                 .configName("/groupa/datab")
                                                 .build();
        aliyunConfigManager.deleteConfigs(Arrays.asList(configObject));
        DeleteConfigurationRequest request = (DeleteConfigurationRequest) reqReference.get();
        verifyDeleteRequest(request);
    }
    
    @Test
    public void deleteConfigs_with_string() throws ClientException {
        AtomicReference reqReference = new AtomicReference();
        Mockito.when(defaultAcsClient.getAcsResponse(
                       ArgumentMatchers.argThat(req -> {
                           reqReference.set(req);
                           return true;
                       })))
               .thenReturn(normalDeleteConfigurationResponse);
        aliyunConfigManager.deleteConfigs(Arrays.asList("/groupa/datab"));
        DeleteConfigurationRequest request = (DeleteConfigurationRequest) reqReference.get();
        verifyDeleteRequest(request);
    }
    
    @Test(expected = CloudAppInvalidArgumentsException.class)
    public void deleteConfigs_with_un_support_type() throws ClientException {
        aliyunConfigManager.deleteConfigs(Arrays.asList(new HashMap()));
    }
}