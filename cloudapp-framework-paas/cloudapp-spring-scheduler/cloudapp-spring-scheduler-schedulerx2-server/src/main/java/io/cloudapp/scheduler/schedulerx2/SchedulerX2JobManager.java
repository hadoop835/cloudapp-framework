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
package io.cloudapp.scheduler.schedulerx2;

import com.alibaba.fastjson2.JSON;
import com.aliyun.schedulerx220190430.Client;
import com.aliyun.schedulerx220190430.models.*;
import io.cloudapp.api.scheduler.server.JobManager;
import io.cloudapp.api.scheduler.model.Job;
import io.cloudapp.api.scheduler.model.JobType;
import io.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchedulerX2JobManager implements JobManager<Client> {
    private static final Logger logger = LoggerFactory.getLogger(
            SchedulerX2JobManager.class);
    
    private final Client client;
    private final String regionId;
    
    public SchedulerX2JobManager(Client client) {
        this.client = client;
        this.regionId = client._regionId;
    }
    
    @Override
    public Long create(Job obj) throws CloudAppException {
        try {
            Assert.hasText(obj.getGroupId(), "groupId is required");
            Assert.hasText(obj.getNamespace(), "namespace is required");
            Assert.hasText(obj.getName(), "name is required");
            Assert.hasText(obj.getExecuteMode(), "executeMode is required");
            
            
            CreateJobRequest req = new CreateJobRequest()
                    .setRegionId(regionId)
                    .setGroupId(obj.getGroupId())
                    .setName(obj.getName())
                    .setJobType(obj.getJobType().getKey())
                    .setExecuteMode(obj.getExecuteMode())
                    .setParameters(obj.getParameters())
                    .setTimeExpression(obj.getTimeExpression())
                    .setTimeType(obj.getTimeType().getValue())
                    .setMaxAttempt(obj.getMaxAttempt())
                    .setAttemptInterval(obj.getAttemptInterval())
                    .setClassName(obj.getClassName())
                    .setContent(obj.getContent())
                    .setNamespace(obj.getNamespace())
//                    .setContactInfo(obj.getContacts())
                    .setDescription(obj.getDescription())
                    ;
            
            if (logger.isDebugEnabled()) {
                logger.debug("Create request: {}", req.toString());
            }
            
            CreateJobResponse resp = client.createJob(req);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Create response: {}", resp.toString());
            }
            
            return Optional.ofNullable(resp)
                           .map(CreateJobResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getData())){
                                   return e.getData().getJobId();
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
            String msg = String.format("Create failed, job: %s",
                                       JSON.toJSONString(obj)
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    @Override
    public boolean update(Job obj) throws CloudAppException {
        try {
            Assert.notNull(obj, "job group is not null");
            Assert.hasText(obj.getGroupId(), "group id is required");
            Assert.notNull(obj.getJobId(), "job id is required");
            Assert.hasText(obj.getNamespace(), "namespace is required");
            Assert.notNull(obj.getTimeType(), "timeType is required");
            
            UpdateJobRequest req = new UpdateJobRequest()
                    .setRegionId(regionId)
                    .setNamespace(obj.getNamespace())
                    .setGroupId(obj.getGroupId())
                    .setJobId(obj.getJobId())
                    .setName(obj.getName())
                    .setExecuteMode(obj.getExecuteMode())
                    .setParameters(obj.getParameters())
                    .setTimeExpression(obj.getTimeExpression())
                    .setTimeType(obj.getTimeType().getValue())
                    .setMaxAttempt(obj.getMaxAttempt())
                    .setDescription(obj.getDescription())
                    .setAttemptInterval(obj.getAttemptInterval())
                    .setClassName(obj.getClassName())
                    .setContent(obj.getContent())
//                    .setContacts(obj.getContacts())
                    ;
            
            UpdateJobResponse resp = client.updateJob(req);
            
            return Optional.ofNullable(resp)
                           .map(UpdateJobResponse::getBody)
                           .map(UpdateJobResponseBody::getSuccess)
                           .orElse(false);
        } catch (Exception e) {
            String msg = String.format("Update failed, job group: %s", obj);
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    @Override
    public boolean delete(String namespace, String groupId, Long jobId)
            throws CloudAppException {
        try {
            Assert.hasText(groupId, "group id is required");
            Assert.notNull(jobId, "job id is required");
            Assert.hasText(namespace, "namespace is required");
            
            DeleteJobRequest req = new DeleteJobRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    .setJobId(jobId)
                    ;
            
            DeleteJobResponse resp = client.deleteJob(req);
            
            return Optional.ofNullable(resp)
                           .map(DeleteJobResponse::getBody)
                           .map(DeleteJobResponseBody::getSuccess)
                           .orElse(false);
            
        } catch (Exception e) {
            String msg = String.format(
                    "Delete failed, namespace: %s, groupId: %s, jobId: %s",
                    namespace, groupId, jobId
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }

    @Override
    public Job get(String namespace, String groupId, Long jobId)
            throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            Assert.hasText(groupId, "group id is required");
            Assert.notNull(jobId, "job id is required");
            
            GetJobInfoRequest req = new GetJobInfoRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    .setJobId(jobId)
                    ;
                    
            if (logger.isDebugEnabled()) {
                logger.debug("Get request: {}", req.toString());
            }
            
            GetJobInfoResponse resp = client.getJobInfo(req);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Get response: {}", resp.toString());
            }
            
            return Optional.ofNullable(resp)
                           .map(GetJobInfoResponse::getBody)
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
                           .map(e -> convertToJob(e.getJobConfigInfo()))
                           .orElse(null);
            
        }catch (Exception e){
            String msg = String.format(
                    "Get failed, namespace: %s, groupId: %s, jobId: %s",
                    namespace, groupId, jobId
            );

            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
   
    @Override
    public List<Job> list(String namespace,
                          String groupId,
                          String jobName,
                          String status) throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            Assert.hasText(groupId, "group id is required");
            
            ListJobsRequest req = new ListJobsRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    ;
            if(StringUtils.hasText(jobName)){
                req.setJobName(jobName);
            }
            if(StringUtils.hasText(status)){
                req.setStatus(status);
            }
            
            ListJobsResponse resp = client.listJobs(req);
            
            return Optional.ofNullable(resp)
                           .map(ListJobsResponse::getBody)
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
                           .map(ListJobsResponseBody.ListJobsResponseBodyData::getJobs)
                           .map(e -> e.stream()
                                      .map(this::convertToJob)
                                      .collect(Collectors.toList())
                           )
                           .orElse(new ArrayList<>());
            
        } catch (Exception e) {
            String msg = String.format(
                    "List failed, namespace: %s, groupId: %s, jobName: %s, " + "status: %s",
                    namespace, groupId, jobName, status
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    @Override
    public boolean disable(String namespace, String groupId, Long jobId)
            throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            Assert.notNull(jobId, "job id is required");
            
            DisableJobRequest req = new DisableJobRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setJobId(jobId)
                    .setGroupId(groupId)
                    ;
            
            DisableJobResponse resp = client.disableJob(req);
            
            return Optional.ofNullable(resp)
                           .map(DisableJobResponse::getBody)
                           .map(DisableJobResponseBody::getSuccess)
                           .orElse(false);
            
        } catch (Exception e) {
            String msg = String.format(
                    "Disable failed, namespace: %s, groupId: %s, jobId: %s",
                    namespace, groupId, jobId
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    @Override
    public boolean enable(String namespace, String groupId, Long jobId)
            throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            Assert.notNull(jobId, "job id is required");
            
            EnableJobRequest req = new EnableJobRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setJobId(jobId)
                    .setGroupId(groupId)
                    ;
            
            EnableJobResponse resp = client.enableJob(req);
            
            return Optional.ofNullable(resp)
                           .map(EnableJobResponse::getBody)
                           .map(EnableJobResponseBody::getSuccess)
                           .orElse(false);
            
        } catch (Exception e) {
            String msg = String.format(
                    "Enable failed, namespace: %s, groupId: %s, jobId: %s",
                    namespace, groupId, jobId
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    
    @Override
    public Long execute(String namespace, String groupId, Long jobId,
                        String params)
            throws CloudAppException {
        try {
            Assert.hasText(namespace, "namespace is required");
            Assert.notNull(jobId, "jobId is required");
            Assert.hasText(groupId, "groupId is required");
            
            ExecuteJobRequest req = new ExecuteJobRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    .setJobId(jobId)
                    .setInstanceParameters(params)
                    ;
            
            if (logger.isDebugEnabled()) {
                logger.debug("Execute request: {}", req.toString());
            }
            
            ExecuteJobResponse resp = client.executeJob(req);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Execute response: {}", resp.toString());
            }
            
            return Optional.ofNullable(resp)
                           .map(ExecuteJobResponse::getBody)
                           .map(e -> {
                               if (Objects.nonNull(e.getData())){
                                   return e.getData().getJobInstanceId();
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
                    "Execute failed, namespace: %s, groupId: %s, jobId: %s",
                    namespace, groupId, jobId
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    @Override
    public Client getDelegatingClient() {
        return this.client;
    }
    
    private Job convertToJob(GetJobInfoResponseBody.GetJobInfoResponseBodyDataJobConfigInfo obj){
        Job job = new Job();
        job.setJobId(obj.getJobId());
        job.setName(obj.getName());
        job.setDescription(obj.getDescription());
        job.setClassName(obj.getClassName());
        job.setContent(obj.getContent());
        job.setExecuteMode(obj.getExecuteMode());
        job.setJobType(JobType.getByKey(obj.getJobType()));
        job.setMaxAttempt(obj.getMaxAttempt());
        job.setParameters(obj.getParameters());
        job.setAttemptInterval(obj.getAttemptInterval());
        
        return job;
    }
    
    private Job convertToJob(ListJobsResponseBody.ListJobsResponseBodyDataJobs obj){
        Job job = new Job();
        job.setJobId(obj.getJobId());
        job.setName(obj.getName());
        job.setDescription(obj.getDescription());
        job.setClassName(obj.getClassName());
        job.setContent(obj.getContent());
        job.setExecuteMode(obj.getExecuteMode());
        job.setJobType(JobType.getByKey(obj.getJobType()));
        job.setMaxAttempt(obj.getMaxAttempt());
        job.setParameters(obj.getParameters());
        job.setAttemptInterval(obj.getAttemptInterval());
        
        return job;
    }
    
}
