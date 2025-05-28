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
package io.cloudapp.api.scheduler.worker;

import io.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.List;
import java.util.Objects;

/**
 * Job Synchronization Manager
 * <p>
 * When the program starts: synchronize the job configuration
 * scanned locally to the server.
 * Before closing the program: clear the jobs on the server.
 *
 * @param <C> client type
 */
public abstract class AbstractGlobalJobSyncManager<C> implements
        SmartInitializingSingleton, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(
            AbstractGlobalJobSyncManager.class);
    
    public Long longDefault = 0L;
    public C client;
    public String regionId;
    public String namespace;
    public String groupId;
    public JobGroupMetadata jobGroup;
    List<GlobalJobMetadata> distributedJobs =
            AbstractGlobalJobScanner.getGlobalJobs();
    
    public AbstractGlobalJobSyncManager(C client,
                                        JobGroupMetadata jobGroup,
                                        String regionId,
                                        String namespace,
                                        String groupId) {
        this.client = client;
        this.jobGroup = jobGroup;
        this.regionId = regionId;
        this.namespace = namespace;
        this.groupId = groupId;
    }
    
    /**
     * Jobs marked as "auto-create" will be created
     * from the server after the program is starts.
     * if the job already exists, it will not be created again.
     */
    @Override
    public void afterSingletonsInstantiated() {
        // create job group
        createJobGroupAndSetIdToEnv(jobGroup);
        
        
        // create jobs
        distributedJobs.stream()
                       .filter(GlobalJobMetadata::isAutoCreateJob)
                       .forEach(e -> {
                           String uniqueSymbol = getUniqueSymbol(e);
                           
                           if (Objects.nonNull(findFirstJobId(uniqueSymbol))) {
                               logger.info("Job: {} already exists, skip...",
                                           uniqueSymbol
                               );
                           } else {
                               try {
                                   createJob(e);
                                   logger.info("Job: {} created successfully",
                                               uniqueSymbol
                                   );
                               }catch (CloudAppException exception){
                                   logger.error("Job: {} create failed",
                                               uniqueSymbol, exception
                                   );
                               }
                               
                           }
                       })
        ;
    }
    
    /**
     * Jobs marked as "auto-delete" will be deleted
     * from the server before the program is closed.
     */
    @Override
    public void destroy() throws Exception {
        distributedJobs.stream()
                       .filter(GlobalJobMetadata::isAutoDeleteJob)
                       .forEach(e -> {
                           String uniqueSymbol = getUniqueSymbol(e);
                           Long firstJobId = findFirstJobId(uniqueSymbol);
                           
                           if (Objects.nonNull(firstJobId)) {
                               if (deleteJob(firstJobId)) {
                                   logger.info("Job: {} deleted successfully",
                                               uniqueSymbol
                                   );
                               }
                           } else {
                               logger.info("Job: {} does not exist, " +
                                                   "unnecessary delete",
                                           uniqueSymbol
                               );
                           }
                       })
        ;
    }
    
    /**
     * Unique symbol, like job name or value name(handler name)
     *
     * @param obj job metadata
     * @return job name
     */
    public abstract String getUniqueSymbol(GlobalJobMetadata obj);
    
    /**
     * Create job group and set job group id to current env
     *
     * @param obj job group metadata
     * @throws CloudAppException exception
     */
    public abstract void createJobGroupAndSetIdToEnv(JobGroupMetadata obj)
            throws CloudAppException;
    
    /**
     * Create job
     *
     * @param obj job metadata
     * @throws CloudAppException exception
     */
    public abstract void createJob(GlobalJobMetadata obj)
            throws CloudAppException;
    
    /**
     * Find the first job id by job name
     *
     * @param uniqueSymbol unique symbol
     * @return job id, null if not found
     * @throws CloudAppException exception
     */
    public abstract Long findFirstJobId(String uniqueSymbol)
            throws CloudAppException;
    
    /**
     * Delete job by job id
     *
     * @param jobId job id
     * @return true if deleted successfully
     * @throws CloudAppException exception
     */
    public abstract boolean deleteJob(Long jobId) throws CloudAppException;
    
}
