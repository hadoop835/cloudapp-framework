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
package com.alibaba.cloudapp.scheduler.schedulerx2;


import com.alibaba.fastjson2.JSON;
import com.aliyun.schedulerx220190430.Client;
import com.aliyun.schedulerx220190430.models.CreateAppGroupRequest;
import com.aliyun.schedulerx220190430.models.CreateAppGroupResponse;
import com.aliyun.schedulerx220190430.models.DeleteAppGroupRequest;
import com.aliyun.schedulerx220190430.models.DeleteAppGroupResponse;
import com.aliyun.schedulerx220190430.models.GetAppGroupRequest;
import com.aliyun.schedulerx220190430.models.GetAppGroupResponse;
import com.aliyun.schedulerx220190430.models.GetAppGroupResponseBody;
import com.aliyun.schedulerx220190430.models.ListGroupsRequest;
import com.aliyun.schedulerx220190430.models.ListGroupsResponse;
import com.aliyun.schedulerx220190430.models.ListGroupsResponseBody;
import com.aliyun.schedulerx220190430.models.UpdateAppGroupRequest;
import com.aliyun.schedulerx220190430.models.UpdateAppGroupResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.alibaba.cloudapp.api.scheduler.server.JobGroupManager;
import com.alibaba.cloudapp.api.scheduler.model.JobGroup;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchedulerX2JobGroupManager implements JobGroupManager<Client> {
    private static final Logger logger = LoggerFactory.getLogger(
            SchedulerX2JobGroupManager.class);
    
    private final Client client;
    private final String regionId;
    
    public SchedulerX2JobGroupManager(Client client) {
        this.client = client;
        this.regionId = client._regionId;
    }
    
    @Override
    public Long create(JobGroup obj)
            throws CloudAppException {
        try {
            Assert.hasText(obj.getNamespace(), "namespace is required");
            Assert.hasText(obj.getAppName(), "appName is required");
            Assert.hasText(obj.getGroupId(), "groupId is required");
            
            CreateAppGroupRequest req = new CreateAppGroupRequest()
                    .setRegionId(regionId)
                    .setNamespace(obj.getNamespace())
                    .setAppName(obj.getAppName())
                    .setGroupId(obj.getGroupId())
                    .setDescription(obj.getDescription())
                    ;
            
            if (logger.isDebugEnabled()) {
                logger.debug("Create request: {}", req.toString());
            }
            
            CreateAppGroupResponse resp = client.createAppGroupWithOptions(req,
                                                                           new RuntimeOptions()
            );
            
            if (logger.isDebugEnabled()) {
                logger.debug("Create response: {}", resp.toString());
            }
            
            return Optional.ofNullable(resp)
                           .map(CreateAppGroupResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getData())){
                                   return e.getData().getAppGroupId();
                               }
                               if (StringUtils.hasText(e.getMessage())){
                                   throw new CloudAppException(e.getMessage());
                               }
                               throw new CloudAppException("Unknown error, " +
                                                                   "resp: %s"
                                       , resp.toString());
                           })
                           .orElse(null);
            
        } catch (Exception e) {
            String msg = String.format(
                    "Create failed, jobGroup: %s",
                    JSON.toJSONString(obj)
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    @Override
    public JobGroup get(String namespace, String groupId)
            throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            Assert.hasText(groupId, "group id is required");
            
            GetAppGroupRequest req = new GetAppGroupRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    ;
            
            if (logger.isDebugEnabled()) {
                logger.debug("Get request: {}", req.toString());
            }
            
            GetAppGroupResponse resp = client.getAppGroup(req);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Get response: {}", resp.toString());
            }
            
            // TODO: 考虑提取成公共方法处理 by@澜田
            return Optional.ofNullable(resp)
                           .map(GetAppGroupResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getData())) {
                                   return e.getData();
                               }
                               if (StringUtils.hasText(e.getMessage())) {
                                   throw new CloudAppException(e.getMessage());
                               }
                               throw new CloudAppException("Unknown error, " +
                                                                   "resp: %s"
                                       , resp.toString());
                           })
                           .map(this::convertToJobGroup)
                           .orElse(new JobGroup());
            
        } catch (Exception e) {
            String msg = String.format(
                    "Get failed, namespace: %s, groupId: %s",
                    namespace, groupId
            );
            
            throw new CloudAppException(msg,
                                        "CloudApp.SchedulerX2Error",
                                        e
            );
        }
    }
    
    @Override
    public boolean delete(String namespace, String groupId)
            throws CloudAppException {
        try {
            Assert.hasText(groupId, "group id is required");
            DeleteAppGroupRequest req = new DeleteAppGroupRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    ;
            
            DeleteAppGroupResponse resp = client.deleteAppGroup(req);
            
            return Optional.ofNullable(resp)
                           .map(DeleteAppGroupResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getSuccess())) {
                                   return e.getSuccess();
                               }
                               if (StringUtils.hasText(e.getMessage())) {
                                   throw new CloudAppException(e.getMessage());
                               }
                               throw new CloudAppException("Unknown error, " +
                                                                   "resp: %s"
                                       , resp.toString());
                           })
                           .orElse(false);
            
        } catch (Exception e) {
            String msg = String.format(
                    "Delete failed, namespace: %s, groupId: %s",
                    namespace, groupId
            );
            
            throw new CloudAppException(msg,
                                        "CloudApp.SchedulerX2Error",
                                        e
            );
        }
    }
    
    @Override
    public boolean update(JobGroup obj)
            throws CloudAppException {
        try {
            Assert.notNull(obj, "job group is not null");
            Assert.hasText(obj.getGroupId(), "group id is required");
            Assert.hasText(obj.getAppName(), "app name is required");
            Assert.hasText(obj.getNamespace(), "namespace is required");
            
            UpdateAppGroupRequest req = new UpdateAppGroupRequest()
                    .setRegionId(regionId)
                    .setNamespace(obj.getNamespace())
                    .setDescription(obj.getDescription())
                    .setGroupId(obj.getGroupId())
                    ;
            
            UpdateAppGroupResponse resp = client.updateAppGroup(req);
            
            return Optional.ofNullable(resp)
                           .map(UpdateAppGroupResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getSuccess())) {
                                   return e.getSuccess();
                               }
                               if (StringUtils.hasText(e.getMessage())) {
                                   throw new CloudAppException(e.getMessage());
                               }
                               throw new CloudAppException("Unknown error, " +
                                                                   "resp: %s"
                                       , resp.toString());
                           })
                           .orElse(false);
            
        } catch (Exception e) {
            String msg = String.format(
                    "Update failed, job group: %s",
                    obj
            );
            
            throw new CloudAppException(msg,
                                        "CloudApp.SchedulerX2Error",
                                        e
            );
        }
    }
    
    @Override
    public List<JobGroup> list(String namespace, String name)
            throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            
            ListGroupsRequest req = new ListGroupsRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setAppGroupName(name)
                    ;
            
            ListGroupsResponse resp = client.listGroups(req);
            
            return Optional.ofNullable(resp)
                           .map(ListGroupsResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getData())) {
                                   return e.getData();
                               }
                               if (StringUtils.hasText(e.getMessage())) {
                                   throw new CloudAppException(e.getMessage());
                               }
                               throw new CloudAppException("Unknown error, " +
                                                                   "resp: %s"
                                       , resp.toString());
                           })
                           .map(ListGroupsResponseBody.ListGroupsResponseBodyData::getAppGroups)
                           .map(e -> e.stream()
                                   .map(this::convertToJobGroup)
                                   .collect(Collectors.toList())
                           )
                           .orElse(new ArrayList<>());
            
        } catch (Exception e) {
            String msg = String.format(
                    "List failed, namespace: %s, name: %s",
                    namespace, name
            );
            
            throw new CloudAppException(msg,
                                        "CloudApp.SchedulerX2Error",
                                        e
            );
        }
    }
    
    @Override
    public Client getDelegatingClient() {
        return this.client;
    }
    
    private JobGroup convertToJobGroup(GetAppGroupResponseBody.GetAppGroupResponseBodyData obj) {
        JobGroup jobGroup = new JobGroup();
        jobGroup.setDescription(obj.getDescription());
        jobGroup.setAppName(obj.getAppName());
        jobGroup.setGroupId(obj.getGroupId());
        
        return jobGroup;
    }
    
    private JobGroup convertToJobGroup(ListGroupsResponseBody.ListGroupsResponseBodyDataAppGroups obj) {
        JobGroup jobGroup = new JobGroup();
        jobGroup.setDescription(obj.getDescription());
        jobGroup.setAppName(obj.getAppName());
        jobGroup.setGroupId(obj.getGroupId());
        
        return jobGroup;
    }
}
