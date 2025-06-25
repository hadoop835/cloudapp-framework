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
package com.alibaba.cloudapp.starter.scheduler.properties;

import com.alibaba.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = SchedulerX2WorkerProperties.PREFIX)
public class SchedulerX2WorkerProperties extends RefreshableProperties {
    
    public static final String PREFIX = "io.cloudapp.scheduler.schedulerx2-worker";
    
    /**
     * Enable SchedulerX2 worker, default false
     */
    private boolean enabled = false;
    
    /**
     * The endpoint of the OpenAPI.
     */
    private String openAPIEndpoint;
    
    /**
     * SchedulerX2 access key id
     */
    private String accessKeyId;
    
    /**
     * SchedulerX2 access key secret
     */
    private String accessKeySecret;
    
    /**
     * SchedulerX2 endpoint
     */
    private String endpoint;
    
    /**
     * SchedulerX2 domainName, if it is a private cloud env, need it
     */
    private String domainName;

    /**
     * SchedulerX2 region id
     */
    private String regionId;
    
    /**
     * The namespace of the job.
     */
    private String namespace;
    
    /**
     * The group id of the appGroup.
     */
    private String groupId;
    
    /**
     * The app key of the appGroup.
     */
    private String appKey;
    
    public String getNamespace() {
        return namespace;
    }
    
    public SchedulerX2WorkerProperties setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public SchedulerX2WorkerProperties setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }
    
    public String getAppKey() {
        return appKey;
    }
    
    public SchedulerX2WorkerProperties setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public SchedulerX2WorkerProperties setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    
    public String getAccessKeyId() {
        return accessKeyId;
    }
    
    public SchedulerX2WorkerProperties setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        return this;
    }
    
    public String getAccessKeySecret() {
        return accessKeySecret;
    }
    
    public SchedulerX2WorkerProperties setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
        return this;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public SchedulerX2WorkerProperties setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }
    
    public String getDomainName() {
        return domainName;
    }
    
    public SchedulerX2WorkerProperties setDomainName(String domainName) {
        this.domainName = domainName;
        return this;
    }
    
    public String getRegionId() {
        return regionId;
    }
    
    public SchedulerX2WorkerProperties setRegionId(String regionId) {
        this.regionId = regionId;
        return this;
    }
    
    public String getOpenAPIEndpoint() {
        return openAPIEndpoint;
    }
    
    public SchedulerX2WorkerProperties setOpenAPIEndpoint(String openAPIEndpoint) {
        this.openAPIEndpoint = openAPIEndpoint;
        return this;
    }
    
}
