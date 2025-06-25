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
import com.aliyun.schedulerx220190430.models.CreateJobRequest;
import com.aliyun.schedulerx220190430.models.CreateJobResponse;
import com.aliyun.schedulerx220190430.models.DeleteJobRequest;
import com.aliyun.schedulerx220190430.models.DeleteJobResponse;
import com.aliyun.schedulerx220190430.models.DeleteJobResponseBody;
import com.aliyun.schedulerx220190430.models.ListJobsRequest;
import com.aliyun.schedulerx220190430.models.ListJobsResponse;
import com.aliyun.schedulerx220190430.models.ListJobsResponseBody;
import com.alibaba.cloudapp.api.scheduler.model.ExecuteMode;
import com.alibaba.cloudapp.api.scheduler.model.JobType;
import com.alibaba.cloudapp.api.scheduler.model.TimeType;
import com.alibaba.cloudapp.api.scheduler.worker.AbstractGlobalJobSyncManager;
import com.alibaba.cloudapp.api.scheduler.worker.GlobalJobMetadata;
import com.alibaba.cloudapp.api.scheduler.worker.JobGroupMetadata;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * Job Synchronization Manager
 * <p>
 * When the program starts: synchronize the job configuration
 * scanned locally to the SchedulerX2 server.
 * Before closing the program: clear the jobs on the server.
 */
public class GlobalJobSyncManager extends AbstractGlobalJobSyncManager<Client> {
    private static final Logger logger = LoggerFactory.getLogger(
            GlobalJobSyncManager.class);
    
    public GlobalJobSyncManager(Client client,
                                String regionId,
                                String namespace,
                                String groupId) {
        super(client, null, regionId, namespace, groupId);
    }
    
    @Override
    public String getUniqueSymbol(GlobalJobMetadata obj) {
        // The process method name is used as the job unique symbol,
        // it is the same as the registered bean name.
        return CommonConstants.getRegisterBeanNameFunc.apply(obj);
    }
    
    @Override
    public void createJobGroupAndSetIdToEnv(JobGroupMetadata obj)
            throws CloudAppException {
        // For scx2, the group is created on the page first,
        // so there is nothing to do.
    }
    
    
    public void createJob(GlobalJobMetadata obj) throws CloudAppException {
        try {
            CreateJobRequest req = new CreateJobRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    .setName(getUniqueSymbol(obj))
                    .setJobType(JobType.JAVA.getKey())
                    .setExecuteMode(obj.getExecuteMode())
                    // notes: this is full class name
                    .setClassName(CommonConstants.getClassNameFunc.apply(obj))
                    .setDescription(obj.getDescription())
                    ;
            if (StringUtils.hasText(obj.getCron())) {
                req.setTimeType(TimeType.CRON.getValue());
                req.setTimeExpression(obj.getCron());
                
            } else if (!longDefault.equals(obj.getFixedRate())) {
                req.setTimeType(TimeType.FIXED_RATE.getValue());
                req.setTimeExpression(String.valueOf(obj.getFixedRate()));
                
            } else if (!longDefault.equals(obj.getFixedDelay())) {
                // schedulerx2 uses second_delay instead of it, Its enum value is 4
                req.setTimeType(4);
                req.setTimeExpression(String.valueOf(obj.getFixedDelay()));
            }
            
            if (obj.getExecuteMode().equals(ExecuteMode.SHARDING.getKey()) &&
                    StringUtils.hasText(obj.getShardingParams())) {
                req.setParameters(obj.getShardingParams());
            }
            
            if (logger.isDebugEnabled()) {
                logger.debug("Create request: {}", req.toString());
            }
            
            CreateJobResponse resp = client.createJob(req);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Create response: {}", resp.toString());
            }
            
            Optional.ofNullable(resp)
                    .map(CreateJobResponse::getBody)
                    .map(e -> {
                        if (Objects.nonNull(e.getData())) {
                            return e.getData().getJobId();
                        }
                        if (StringUtils.hasText(e.getMessage())) {
                            throw new CloudAppException(e.getMessage());
                        }
                        throw new CloudAppException("Unknown error, " +
                                                            "resp: %s"
                                , resp.toString());
                    })
            ;
            
        } catch (Exception e) {
            String msg = String.format("Create failed, job: %s",
                                       JSON.toJSONString(obj)
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    /**
     * Find the first job id by job name
     *
     * @param jobName job name
     * @return job id, null if not found
     * @throws CloudAppException exception
     */
    public Long findFirstJobId(String jobName) throws CloudAppException {
        try {
            ListJobsRequest req = new ListJobsRequest()
                    .setRegionId(regionId)
                    .setNamespace(namespace)
                    .setGroupId(groupId)
                    .setJobName(jobName)
                    ;
            
            ListJobsResponse resp = client.listJobs(req);
            
            return Optional.ofNullable(resp)
                           .map(ListJobsResponse::getBody)
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
                           .map(ListJobsResponseBody.ListJobsResponseBodyData::getJobs)
                           .map(e -> {
                               if (!e.isEmpty()) {
                                   return e.get(0).getJobId();
                               }
                               return null;
                           })
                           .orElse(null);
            
        } catch (Exception e) {
            String msg = String.format(
                    "findFirstJobId failed, namespace: %s, groupId: %s, jobName: %s",
                    namespace, groupId, jobName
            );
            
            throw new CloudAppException(msg, "CloudApp.SchedulerX2Error", e);
        }
    }
    
    public boolean deleteJob(Long jobId) throws CloudAppException {
        try {
            
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
    
}
