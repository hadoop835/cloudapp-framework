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

package io.cloudapp.api.scheduler.server;

import io.cloudapp.api.common.DelegatingClientAware;
import io.cloudapp.api.scheduler.model.Job;
import io.cloudapp.exeption.CloudAppException;

import java.util.List;
/**
 * Job Management API
 * This API defines methods for CRUD and disable, enable operations on jobs.
 *
 * @param <Client> The underlying client type
 */
public interface JobManager<Client> extends DelegatingClientAware<Client> {

    /**
     * Create a new job
     *
     * @param obj the job to be created
     *
     * @return job ID of the created job
     * @throws CloudAppException if an error occurs during job creation
     */
    Long create(Job obj) throws CloudAppException;

    /**
     * Update an existing job
     *
     * @param obj the job to be updated
     *
     * @return true if the update is successful, false otherwise
     * @throws CloudAppException if an error occurs during job update
     */
    boolean update(Job obj) throws CloudAppException;

    /**
     * Delete a job by ID
     *
     * @param namespace the namespace
     *                  for schedulerX2, it is a must
     * @param groupId the ID of the group containing the job
     * @param jobId the ID of the job to be deleted
     *
     * @return true if the deletion is successful, false otherwise
     * @throws CloudAppException if an error occurs during job deletion
     */
    boolean delete(String namespace,String groupId, Long jobId)
            throws CloudAppException;
    
    /**
     * Lists jobs based on group ID, job name, and status
     *
     * @param namespace the namespace
     *                  for schedulerX2, it is a must
     * @param groupId the ID of the group containing the jobs
     * @param jobId   the ID of the job
     *
     * @return a list of jobs that match the criteria
     * @throws CloudAppException if an error occurs during job listing
     */
    Job get(String namespace, String groupId, Long jobId)
            throws CloudAppException;
    
    
    /**
     * Lists jobs based on group ID, job name, and status
     *
     * @param namespace the namespace
     *                  for schedulerX2, it is a must
     * @param groupId the ID of the group containing the jobs
     * @param jobName the name of the job (optional)
     * @param status  the status of the job (optional)
     *
     * @return a list of jobs that match the criteria
     * @throws CloudAppException if an error occurs during job listing
     */
    List<Job> list(String namespace, String groupId, String jobName,
                   String status) throws CloudAppException;

    /**
     * Disable a job
     *
     * @param namespace the namespace
     * @param groupId the ID of the group containing the job
     * @param jobId   the ID of the job to be disabled
     *
     * @return true if the operation is successful, false otherwise
     * @throws CloudAppException if an error occurs during job disabling
     */
    boolean disable(String namespace, String groupId, Long jobId)
            throws CloudAppException;

    /**
     * Enable a job
     *
     * @param namespace the namespace
     * @param groupId the ID of the group containing the job
     * @param jobId   the ID of the job to be enabled
     *
     * @return true if the operation is successful, false otherwise
     * @throws CloudAppException if an error occurs during job enabling
     */
    boolean enable(String namespace, String groupId, Long jobId)
            throws CloudAppException;
    
    /**
     * Executes a job once.
     *
     * @param namespace The namespace of the job, used to identify the namespace.
     *                  for schedulerX2, it is a must
     * @param groupId The group ID of the job, used to identify the job group.
     * @param jobId The ID of the job, used to identify the specific job.
     * @param params The parameters for the job execution, used to pass in execution-related parameters.
     *
     * @return Returns the instance ID of the job execution.
     * @throws CloudAppException Throws this exception when an error occurs during execution.
     */
    Long execute(String namespace, String groupId, Long jobId, String params)
            throws CloudAppException;
    
}
