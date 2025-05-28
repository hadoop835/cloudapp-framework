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

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import io.cloudapp.api.config.ConfigReadService;
import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.exeption.CloudAppInvalidArgumentsException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static io.cloudapp.config.aliyun.AliyunConfigConstants.*;

public class NacosConfigReadService implements ConfigReadService {
    
    private static final Logger logger = LoggerFactory.getLogger(
            NacosConfigReadService.class);
    private final long defaultTimeout;
    private final String defaultGroup;
    private final ConfigService nacosConfigService;
    
    public NacosConfigReadService(ConfigService nacosConfigService) {
        this(DEFAULT_TIMEOUT, DEFAULT_GROUP, nacosConfigService);
    }
    
    public NacosConfigReadService(long defaultTimeout,
                                  String defaultGroup,
                                  ConfigService nacosConfigService) {
        this.defaultTimeout = defaultTimeout <= 10 ?
                DEFAULT_TIMEOUT : defaultTimeout;
        
        this.defaultGroup = StringUtils.isBlank(defaultGroup) ?
                DEFAULT_GROUP : defaultGroup;
        
        this.nacosConfigService = nacosConfigService;
    }
    
    @Override
    public Object getDelegatingClient() {
        return nacosConfigService;
    }
    
    @Override
    public String getConfig(String configName) throws CloudAppException {
        checkConfigName(configName);
        try {
            ConfigFullName fullName = parse(configName, defaultGroup);
            return nacosConfigService.getConfig(
                    fullName.dataId, fullName.groupName, defaultTimeout);
        } catch (NacosException e) {
            throw new CloudAppException(
                    "fail getConfig with args configName= " + configName,
                    CLOUD_APP_NACOS_GET_CONFIG_ERROR, e
            );
        }
    }
    
    class NacosListener implements Listener {
        String configName;
        Consumer delegateListener;
        
        public NacosListener(String configName, Consumer delegateListener) {
            this.configName = configName;
            this.delegateListener = delegateListener;
        }
        
        @Override
        public Executor getExecutor() {
            return null;
        }
        
        @Override
        public void receiveConfigInfo(String s) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "receiveConfigInfo: {} for {}",
                        s, configName
                );
            }
            delegateListener.accept(s);
        }
        
    }
    
    @Override
    public String getAndListen(String configName, Consumer listener)
            throws CloudAppException {
        checkConfigName(configName);
        if (listener == null) {
            throw new CloudAppInvalidArgumentsException(
                    "listener must not null or empty!");
        }
        try {
            return getAndListen0(configName, listener);
        } catch (NacosException e) {
            String msg = String.format(
                    "fail getAndListen with args configName= %s,listener=%s",
                    configName, listener
            );
            throw new CloudAppException(msg, CLOUD_APP_NACOS_WATCH_ERROR, e);
        }
    }
    
    private String getAndListen0(String configName, Consumer listener)
            throws NacosException {
        ConfigFullName fullName = parse(configName, defaultGroup);
        String content = nacosConfigService.getConfig(fullName.dataId,
                                                      fullName.groupName,
                                                      defaultTimeout
        );
        nacosConfigService.addListener(fullName.dataId, fullName.groupName,
                                       new NacosListener(configName,
                                                         listener
                                       )
        );
        return content;
    }
    
    @Override
    public Object getConfig(String configName, Class cls)
            throws CloudAppException {
        checkConfigName(configName);
        if (cls == null) {
            throw new CloudAppInvalidArgumentsException("cls must not null!");
        }
        
        try {
            ConfigFullName fullName = parse(configName, defaultGroup);
            String content = nacosConfigService.getConfig(fullName.dataId,
                                                          fullName.groupName,
                                                          defaultTimeout
            );
            if (content == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "configName={} may not found as content is null",
                            configName
                    );
                }
                return null;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("get config,configName={}, content is {}",
                             configName, content
                );
            }
            if (String.class.isAssignableFrom(cls)) {
                return content;
            }
            return JSON.parseObject(content, cls);
        } catch (NacosException e) {
            String msg = String.format(
                    "fail getConfig with args configName=%s,cls=%s",
                    configName, cls
            );
            throw new CloudAppException(msg, CLOUD_APP_NACOS_GET_CONFIG_ERROR,
                                        e
            );
        }
    }
    
    private void checkConfigName(String configName) {
        if (configName == null || StringUtils.isBlank(configName)) {
            throw new CloudAppInvalidArgumentsException(
                    "configName must not null or blank!");
        }
    }
    
    static ConfigFullName parse(String dataId, String defaultGroup) {
        dataId = dataId.trim();
        if (dataId.startsWith(DATA_ID_SEPARATOR_CHAR)) {
            dataId = dataId.substring(1);
        }
        
        String groupName = defaultGroup;
        int groupFlagIndex = dataId.lastIndexOf(DATA_ID_SEPARATOR_CHAR);
        if (groupFlagIndex > 0) {
            groupName = dataId.substring(0, groupFlagIndex);
            dataId = dataId.substring(groupFlagIndex + 1);
        }
        return new ConfigFullName(dataId, groupName);
    }
    
    public static final class ConfigFullName {
        String dataId;
        String groupName;
        
        public ConfigFullName(String dataId, String groupName) {
            this.dataId = dataId;
            this.groupName = groupName;
        }
        
    }
    
}
