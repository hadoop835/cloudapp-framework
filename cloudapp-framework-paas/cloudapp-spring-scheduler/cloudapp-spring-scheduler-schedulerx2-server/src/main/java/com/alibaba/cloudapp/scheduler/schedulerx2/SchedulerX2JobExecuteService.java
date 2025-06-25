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

import com.aliyun.schedulerx220190430.Client;
import com.aliyun.schedulerx220190430.models.*;
import com.aliyun.teautil.models.RuntimeOptions;
import com.alibaba.cloudapp.api.scheduler.server.JobExecuteService;
import com.alibaba.cloudapp.api.scheduler.model.JobInstance;
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

public class SchedulerX2JobExecuteService implements JobExecuteService<Client> {
    private static final Logger logger = LoggerFactory.getLogger(
            SchedulerX2JobExecuteService.class);
    
    private final Client client;
    private final String regionId;
    
    public SchedulerX2JobExecuteService(Client client) {
        this.client = client;
        this.regionId = client._regionId;
    }
    
    
    @Override
    public Client getDelegatingClient() {
        return this.client;
    }
    
    @Override
    public boolean setSuccess(String namespace, String groupId, Long jobId, Long instanceId)
            throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            Assert.notNull(jobId, "jobId is required");
            Assert.hasText(groupId, "groupId is required");
            Assert.notNull(instanceId, "instanceId is required");
            
            SetJobInstanceSuccessRequest req = new SetJobInstanceSuccessRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    .setJobId(jobId)
                    .setJobInstanceId(instanceId)
                    ;
            
            if (logger.isDebugEnabled()) {
                logger.debug("Set success request: {}", req.toString());
            }
            
            SetJobInstanceSuccessResponse resp =
                    client.setJobInstanceSuccessWithOptions(req, new RuntimeOptions());
            
            if (logger.isDebugEnabled()) {
                logger.debug("Set success response: {}", resp.toString());
            }
            
            return Optional.ofNullable(resp)
                           .map(SetJobInstanceSuccessResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getSuccess())){
                                   return e.getSuccess();
                               }
                               if (StringUtils.hasText(e.getMessage())){
                                   throw new CloudAppException(e.getMessage());
                               }
                               throw new CloudAppException("Unknown error, " +
                                                                   "resp: %s"
                                       , resp.toString());
                           })
                           .orElse(false);
            
        } catch (Exception e) {
            String msg = String.format(
                    "Set success failed, namespace: %s, groupId: %s, jobId: %s, instanceId: %s",
                    namespace, groupId, jobId, instanceId
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    @Override
    public boolean retry(String namespace, String groupId, Long jobId, Long instanceId)
            throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            Assert.notNull(jobId, "jobId is required");
            Assert.hasText(groupId, "groupId is required");
            Assert.notNull(instanceId, "instanceId is required");
            
            RetryJobInstanceRequest req = new RetryJobInstanceRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    .setJobId(jobId)
                    .setJobInstanceId(instanceId)
                    ;
            
            if (logger.isDebugEnabled()) {
                logger.debug("Retry request: {}", req.toString());
            }
            
            RetryJobInstanceResponse resp =
                    client.retryJobInstanceWithOptions(req, new RuntimeOptions());
            
            if (logger.isDebugEnabled()) {
                logger.debug("Retry response: {}", resp.toString());
            }
            
            return Optional.ofNullable(resp)
                           .map(RetryJobInstanceResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getSuccess())){
                                   return e.getSuccess();
                               }
                               if (StringUtils.hasText(e.getMessage())){
                                   throw new CloudAppException(e.getMessage());
                               }
                               throw new CloudAppException("Unknown error, " +
                                                                   "resp: %s"
                                       , resp.toString());
                           })
                           .orElse(false);
            
        } catch (Exception e) {
            String msg = String.format(
                    "Retry failed, namespace: %s, groupId: %s, jobId: %s, instanceId: %s",
                    namespace, groupId, jobId, instanceId
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    @Override
    public boolean stop(String namespace, String groupId, Long jobId, Long instanceId)
            throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            Assert.notNull(jobId, "jobId is required");
            Assert.hasText(groupId, "groupId is required");
            Assert.notNull(instanceId, "instanceId is required");
            
            StopInstanceRequest req = new StopInstanceRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    .setJobId(jobId)
                    .setInstanceId(instanceId)
                    ;
            
            if (logger.isDebugEnabled()) {
                logger.debug("Stop request: {}", req.toString());
            }
            
            StopInstanceResponse resp = client.stopInstanceWithOptions(req, new RuntimeOptions());
            
            if (logger.isDebugEnabled()) {
                logger.debug("Stop response: {}", resp.toString());
            }
            
            return Optional.ofNullable(resp)
                           .map(StopInstanceResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getSuccess())){
                                   return e.getSuccess();
                               }
                               if (StringUtils.hasText(e.getMessage())){
                                   throw new CloudAppException(e.getMessage());
                               }
                               throw new CloudAppException("Unknown error, " +
                                                                   "resp: %s"
                                       , resp.toString());
                           })
                           .orElse(false);
            
        } catch (Exception e) {
            String msg = String.format(
                    "Stop failed, namespace: %s, groupId: %s, jobId: %s, instanceId: %s",
                    namespace, groupId, jobId, instanceId
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    @Override
    public List<JobInstance> list(String namespace, String groupId, Long jobId)
            throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            Assert.notNull(jobId, "jobId is required");
            Assert.hasText(groupId, "groupId is required");
            
            GetJobInstanceListRequest req = new GetJobInstanceListRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    .setJobId(jobId)
                    ;
            
            GetJobInstanceListResponse resp = client.getJobInstanceList(req);
            
            return Optional.ofNullable(resp)
                           .map(GetJobInstanceListResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getData())){
                                   return e.getData();
                               }
                               if (StringUtils.hasText(e.getMessage())){
                                   throw new CloudAppException(e.getMessage());
                               }
                               throw new CloudAppException("Unknown error, " +
                                                                   "resp: %s"
                                       , resp.toString());
                           })
                           .map(GetJobInstanceListResponseBody.GetJobInstanceListResponseBodyData::getJobInstanceDetails)
                           .map(e -> e.stream()
                                  .map(this::convertToJobInstance)
                                  .collect(Collectors.toList())
                           )
                           .orElse(new ArrayList<>());
            
        } catch (Exception e) {
            String msg = String.format("List failed, namespace: %s, groupId: %s, jobId: %s",
                    namespace, groupId, jobId);
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    @Override
    public JobInstance get(String namespace,
                           String groupId,
                           Long jobId,
                           Long instanceId)
            throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            Assert.hasText(groupId, "groupId is required");
            Assert.notNull(jobId, "jobId is required");
            Assert.notNull(instanceId, "instanceId is required");
            
            GetJobInstanceRequest req = new GetJobInstanceRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    .setJobId(jobId)
                    .setJobInstanceId(instanceId)
                    ;
            
            if(logger.isDebugEnabled()){
                logger.debug("Get request: {}", req.toString());
            }
            
            GetJobInstanceResponse resp = client.getJobInstance(req);
            
            if(logger.isDebugEnabled()){
                logger.debug("Get response: {}", resp.toString());
            }
            
            return Optional.ofNullable(resp)
                           .map(GetJobInstanceResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getData())){
                                   return e.getData();
                               }
                               if (StringUtils.hasText(e.getMessage())){
                                   throw new CloudAppException(e.getMessage());
                               }
                               throw new CloudAppException("Unknown error, " +
                                                                   "resp: %s"
                                       , resp.toString());
                           })
                           .map(e -> convertToJobInstance(e.getJobInstanceDetail()))
                           .orElse(null);
            
        } catch (Exception e){
            String msg = String.format("Get failed, namespace: %s, jobId: %s, instanceId: %s",
                    namespace, jobId, instanceId);
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    private JobInstance convertToJobInstance(GetJobInstanceListResponseBody.GetJobInstanceListResponseBodyDataJobInstanceDetails obj) {
        JobInstance jobInstance = new JobInstance();
        jobInstance.setExecutor(obj.getExecutor());
        jobInstance.setInstanceId(obj.getInstanceId());
        jobInstance.setResult(obj.getResult());
        jobInstance.setScheduleTime(obj.getScheduleTime());
        jobInstance.setStartTime(obj.getStartTime());
        jobInstance.setStatus(obj.getStatus());
        jobInstance.setEndTime(obj.getEndTime());
        jobInstance.setWorkAddr(obj.getWorkAddr());
        jobInstance.setJobId(obj.getJobId());
        jobInstance.setTriggerType(obj.getTriggerType());
        jobInstance.setProgress(obj.getProgress());
        jobInstance.setDataTime(obj.getDataTime());
        
        return jobInstance;
    }
    
    private JobInstance convertToJobInstance(GetJobInstanceResponseBody.GetJobInstanceResponseBodyDataJobInstanceDetail obj) {
        JobInstance jobInstance = new JobInstance();
        jobInstance.setExecutor(obj.getExecutor());
        jobInstance.setInstanceId(obj.getInstanceId());
        jobInstance.setResult(obj.getResult());
        jobInstance.setScheduleTime(obj.getScheduleTime());
        jobInstance.setStartTime(obj.getStartTime());
        jobInstance.setStatus(obj.getStatus());
        jobInstance.setEndTime(obj.getEndTime());
        jobInstance.setWorkAddr(obj.getWorkAddr());
        jobInstance.setJobId(obj.getJobId());
        jobInstance.setTriggerType(obj.getTriggerType());
        jobInstance.setParameters(obj.getParameters());
        jobInstance.setProgress(obj.getProgress());
        jobInstance.setDataTime(obj.getDataTime());
        
        return jobInstance;
    }
}
