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
package io.cloudapp.config.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.acm.model.v20200206.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.ProtocolType;
import io.cloudapp.api.config.ConfigManager;
import io.cloudapp.api.config.ConfigObject;
import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.exeption.CloudAppInvalidArgumentsException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ch.qos.logback.core.spi.ComponentTracker.DEFAULT_TIMEOUT;
import static io.cloudapp.config.aliyun.AliyunConfigConstants.DEFAULT_GROUP;

public class AliyunConfigManager implements ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(
            AliyunConfigManager.class);
    
    public static final String CONFIGURATION_NOT_EXISTS =
            "ConfigurationNotExists";
    
    private DefaultAcsClient defaultAcsClient;
    
    private String namespaceId;
    
    private String defaultGroup;
    
    private int defaultTimeout;
    
    private ProtocolType protocolType;
    
    public AliyunConfigManager(DefaultAcsClient defaultAcsClient,
                               String namespaceId,
                               String defaultGroup,
                               int defaultTimeout,
                               String protocol) {
        
        this.defaultAcsClient = defaultAcsClient;
        this.namespaceId = namespaceId;
        this.defaultTimeout = defaultTimeout <= 10 ?
                DEFAULT_TIMEOUT : defaultTimeout;
        this.defaultGroup = StringUtils.isBlank(defaultGroup) ?
                DEFAULT_GROUP : defaultGroup;
        this.protocolType = "https".equalsIgnoreCase(protocol) ?
                ProtocolType.HTTPS : ProtocolType.HTTP;
    }
    
    @Override
    public Object getDelegatingClient() {
        return defaultAcsClient;
    }
    
    @Override
    public boolean deleteConfig(String configName)
            throws CloudAppException {
        ConfigObject<Object> configObject = ConfigObject.builder()
                                                        .configName(configName)
                                                        .build();
        return deleteConfig(configObject);
    }
    
    @Override
    public boolean publish(String configName, String content)
            throws CloudAppException {
        ConfigObject<Object> configObject = ConfigObject.builder()
                                                        .configName(configName)
                                                        .content(content)
                                                        .build();
        return publishConfig(configObject);
    }
    
    private String toString(String code, String message, String requestId) {
        StringBuilder sb = new StringBuilder();
        sb.append("{code=").append(code).append(",")
          .append("message=").append(message).append(",")
          .append("requestId=").append(requestId)
          .append("}");
        
        return sb.toString();
    }
    
    private boolean isSuccess(String code) {
        return "ok".equalsIgnoreCase(code);
    }
    
    @Override
    public List<Boolean> publishConfigs(Collection configs)
            throws CloudAppException {
        List<Boolean> result = new ArrayList<>();
        for (Object item : configs) {
            result.add(publishConfig((ConfigObject) item));
        }
        return result;
    }
    
    @Override
    public boolean publishConfig(ConfigObject object)
            throws CloudAppException {
        String configName = object.getConfigName();
        NacosConfigReadService.ConfigFullName configFullName =
                NacosConfigReadService.parse(configName, defaultGroup);
        if (isExists(configName)) {
            return updateConfig(object, configName, configFullName);
        } else {
            return createConfig(object, configName, configFullName);
        }
    }
    
    private boolean createConfig(ConfigObject object,
                                 String configName,
                                 NacosConfigReadService.ConfigFullName configFullName) {
        CreateConfigurationRequest request = createConfigurationRequest(
                object, configFullName);
        try {
            CreateConfigurationResponse response = defaultAcsClient.getAcsResponse(
                    request);
            if (isSuccess(response.getCode())) {
                return true;
            }
            String res = toString(response.getCode(),
                                  response.getMessage(),
                                  response.getRequestId()
            );
            logger.warn("fail create config,request arg is " +
                                "configName={},content={},response={}",
                        configName,
                        object.getContent(),
                        res
            );
            return false;
        } catch (ClientException e) {
            throw new CloudAppException(
                    "fail create config,configName=" + configName,
                    AliyunConfigConstants.CLOUD_APP_NACOS_PUBLISH_ERROR, e
            );
        }
    }
    
    CreateConfigurationRequest createConfigurationRequest(ConfigObject object,
                                                          NacosConfigReadService.ConfigFullName configFullName) {
        CreateConfigurationRequest request = new CreateConfigurationRequest();
        
        request.setSysProtocol(protocolType);
        request.setSysConnectTimeout(defaultTimeout);
        request.setSysReadTimeout(defaultTimeout);
        request.setNamespaceId(namespaceId);
        request.setGroup(configFullName.groupName);
        request.setDataId(configFullName.dataId);
        if (object.getContent() instanceof String) {
            request.setContent((String) object.getContent());
            request.setType("text");
        } else if (object.getContent() instanceof DeployConfigurationRequest) {
            DeployConfigurationRequest requestObject = (DeployConfigurationRequest) object.getContent();
            request.setContent(requestObject.getContent());
            request.setAppName(requestObject.getAppName());
            request.setDesc(requestObject.getDesc());
            request.setTags(requestObject.getTags());
            request.setType(requestObject.getType());
        } else {
            throw new CloudAppInvalidArgumentsException(
                    "content of ConfigObject " +
                            "is neither of type String nor DeployConfigurationRequest!");
        }
        return request;
    }
    
    private boolean isExists(String configName) {
        NacosConfigReadService.ConfigFullName configFullName =
                NacosConfigReadService.parse(configName, defaultGroup);
        
        DescribeConfigurationRequest request = new DescribeConfigurationRequest();
        request.setSysProtocol(protocolType);
        request.setSysConnectTimeout(defaultTimeout);
        request.setSysReadTimeout(defaultTimeout);
        request.setNamespaceId(namespaceId);
        request.setGroup(configFullName.groupName);
        request.setDataId(configFullName.dataId);
        
        try {
            DescribeConfigurationResponse response =
                    defaultAcsClient.getAcsResponse(request);
            if (response.getConfiguration() == null) {
                return false;
            }
            return isSuccess(response.getCode());
        } catch (ClientException e) {
            String code = e.getErrCode();
            if (code != null && code.contains(CONFIGURATION_NOT_EXISTS)) {
                return false;
            }
            throw new CloudAppException(
                    "fail get config,configName=" + configFullName, e);
        }
    }
    
    private boolean updateConfig(ConfigObject object,
                                 String configName,
                                 NacosConfigReadService.ConfigFullName configFullName) {
        final DeployConfigurationRequest request = deployConfigurationRequest(
                object, configFullName);
        try {
            DeployConfigurationResponse res = defaultAcsClient.getAcsResponse(
                    request);
            if (isSuccess(res.getCode())) {
                return true;
            }
            String msg = toString(res.getCode(), res.getMessage(),
                                  res.getRequestId()
            );
            logger.warn("fail publish config,request arg is " +
                                "configName={},content={},response is {}",
                        configName,
                        object.getContent(),
                        msg
            );
            return false;
        } catch (ClientException e) {
            String code = e.getErrCode();
            if (code != null && code.contains(CONFIGURATION_NOT_EXISTS)) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "try to update the config as it returned as not " +
                                    "existed, configName={}, res code={}",
                            configName,
                            code
                    );
                }
                return createConfig(object, configName, configFullName);
            }
            throw new CloudAppException(
                    "fail publish config,configName=" + configName,
                    AliyunConfigConstants.CLOUD_APP_NACOS_PUBLISH_ERROR, e
            );
        }
    }
    
    DeployConfigurationRequest deployConfigurationRequest(ConfigObject object,
                                                          NacosConfigReadService.ConfigFullName configFullName) {
        DeployConfigurationRequest request = new DeployConfigurationRequest();
        request.setSysProtocol(protocolType);
        request.setSysConnectTimeout(defaultTimeout);
        request.setSysReadTimeout(defaultTimeout);
        request.setNamespaceId(namespaceId);
        request.setGroup(configFullName.groupName);
        request.setDataId(configFullName.dataId);
        
        if (object.getContent() instanceof String) {
            request.setContent((String) object.getContent());
            request.setType("text");
        } else if (object.getContent() instanceof DeployConfigurationRequest) {
            DeployConfigurationRequest requestObject = (DeployConfigurationRequest) object.getContent();
            request.setContent(requestObject.getContent());
            request.setAppName(requestObject.getAppName());
            request.setDesc(requestObject.getDesc());
            request.setTags(requestObject.getTags());
            request.setType(requestObject.getType());
        } else {
            throw new CloudAppInvalidArgumentsException(
                    "content of ConfigObject is neither of type String nor" +
                            " DeployConfigurationRequest!");
        }
        return request;
    }
    
    @Override
    public List<Boolean> deleteConfigs(Collection configs)
            throws CloudAppException {
        List<Boolean> result = new ArrayList<>();
        for (Object item : configs) {
            if (item instanceof ConfigObject) {
                result.add(deleteConfig((ConfigObject) item));
            } else if (item instanceof String) {
                result.add(deleteConfig((String) item));
            } else {
                String typeOfElement = item == null ? null :
                        item.getClass().toString();
                throw new CloudAppInvalidArgumentsException("not support type" +
                                                                    " " + typeOfElement);
            }
        }
        return result;
    }
    
    @Override
    public boolean deleteConfig(ConfigObject config) throws CloudAppException {
        NacosConfigReadService.ConfigFullName configFullName =
                NacosConfigReadService.parse(config.getConfigName(),
                                             defaultGroup
                );
        DeleteConfigurationRequest request = new DeleteConfigurationRequest();
        request.setSysProtocol(protocolType);
        request.setSysConnectTimeout(defaultTimeout);
        request.setSysReadTimeout(defaultTimeout);
        request.setNamespaceId(namespaceId);
        request.setGroup(configFullName.groupName);
        request.setDataId(configFullName.dataId);
        try {
            DeleteConfigurationResponse response = defaultAcsClient.getAcsResponse(
                    request);
            if (isSuccess(response.getCode())) {
                return true;
            }
            String msg = toString(response.getCode(),
                                  response.getMessage(),
                                  response.getRequestId()
            );
            logger.warn("fail delete config,request arg is " +
                                "configName={},content={},response is {}",
                        config.getConfigName(),
                        config.getContent(),
                        msg
            );
            return false;
        } catch (ClientException e) {
            String code = e.getErrCode();
            if (code != null && code.contains(CONFIGURATION_NOT_EXISTS)) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "fail to delete config as it returned as not existed," +
                                    " configName={}, res code={}",
                            config.getConfigName(), code
                    );
                }
                return false;
            }
            throw new CloudAppException(
                    "fail delete config,configName=" + config.getConfigName(),
                    AliyunConfigConstants.CLOUD_APP_NACOS_DELETE_ERROR, e
            );
        }
    }
    
}
