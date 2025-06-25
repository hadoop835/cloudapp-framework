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

package com.alibaba.cloudapp.starter.config.properties;

import com.alibaba.cloudapp.starter.base.properties.RefreshableProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;


@ConfigurationProperties(prefix = AliyunConfigManagerProperties.PREFIX)
public class AliyunConfigManagerProperties extends RefreshableProperties {
    
    public static final String PREFIX = "io.cloudapp.config.aliyun.write";

    /**
     * nacos config group, group is config data meta info.
     */
    private String group = "DEFAULT_GROUP";

    /**
     * timeout for get config from nacos.
     */
    private int timeout = 3000;

    /**
     * the domain for execute the request, for example acm.cn-hangzhou.aliyuncs.com
     */
    private String domain = "";
    /**
     * Region ID where the application executing the API calls resides, for example cn-hangzhou
     */
    private String regionId = "";
    /**
     * http or https
     */
    private String protocol = "http";
    /**
     * AccessKey of the Alibaba Cloud primary or sub-account
     */
    private String accessKey = "";
    /**
     * SecretKey of the Alibaba Cloud primary or sub-account
     */
    private String secretKey = "";

    /**
     * The namespace where the configuration item is located.
     */
    private String namespaceId;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override public String toString() {
        return "AliyunConfigManagerProperties{" + "group='" + group + '\'' + ", timeout=" + timeout + ", domain='"
            + domain + '\'' + ", regionId='" + regionId + '\'' + ", protocol='" + protocol + '\'' + ", accessKey='"
            + accessKey + '\'' + ", secretKey='" + secretKey + '\'' + '}';
    }

    public Properties toProperties() {
        Properties properties = new Properties();
        properties.put("timeout", timeout);
        properties.put("group", group);
        properties.put("domain", domain);
        properties.put("regionId", regionId);
        properties.put("accessKey", accessKey);
        properties.put("secretKey", secretKey);
        properties.put("protocol", protocol);
        return properties;
    }

    public void validate() {
        if(StringUtils.isBlank(domain)) {
            throw new IllegalArgumentException("domain must not be blank!");
        }
        if(StringUtils.isBlank(regionId)) {
            throw new IllegalArgumentException("regionId must not be blank!");
        }
        if(StringUtils.isBlank(accessKey)) {
            throw new IllegalArgumentException("accessKey must not be blank!");
        }
        if(StringUtils.isBlank(secretKey)) {
            throw new IllegalArgumentException("secretKey must not be blank!");
        }
    }

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }
}
