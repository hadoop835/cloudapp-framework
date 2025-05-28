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
import io.cloudapp.api.scheduler.model.JobInstance;
import io.cloudapp.exeption.CloudAppException;

import java.util.List;

/**
 * Job Execute Service
 * Defines the interface for job execution operations, providing functionalities
 * such as job success marking, retry, stop, and job execute record.
 *
 * @param <Client> The type of the client used for communication with the scheduler service.
 */
public interface JobExecuteService<Client> extends DelegatingClientAware<Client> {
    
    /**
     * Marks a job instance as successful.
     *
     * @param namespace The namespace of the job, used to identify the namespace.
     *                  for schedulerX2, it is a must
     * @param groupId The group ID of the job, used to identify the job group.
     * @param jobId The ID of the job, used to identify the specific job.
     * @param instanceId The instance ID of the job, used to identify a specific execution instance of the job.
     *
     * @return Returns true if the operation is successful, otherwise false.
     * @throws CloudAppException exception Throws this exception when an error occurs during the operation.
     */
    boolean setSuccess(String namespace,
                       String groupId,
                       Long jobId,
                       Long instanceId)
            throws CloudAppException;

    /**
     * Retries a job instance.
     *
     * @param namespace The namespace of the job, used to identify the
     *                  namespace.
     * @param groupId The group ID of the job, used to identify the job group.
     * @param jobId The ID of the job, used to identify the specific job.
     * @param instanceId The instance ID of the job, used to identify a specific execution instance of the job.
     *
     * @return Returns true if the retry operation is successful, otherwise false.
     * @throws CloudAppException Throws this exception when an error occurs during the retry.
     */
    boolean retry(String namespace, String groupId, Long jobId, Long instanceId)
            throws CloudAppException;

    /**
     * Stops a running job instance.
     *
     * @param namespace The namespace of the job, used to identify the namespace.
     * @param groupId The group ID of the job, used to identify the job group.
     * @param jobId The ID of the job, used to identify the specific job.
     * @param instanceId The instance ID of the job, used to identify a specific execution instance of the job.
     *
     * @return Returns true if the stop operation is successful, otherwise false.
     * @throws CloudAppException Throws this exception when an error occurs during the stop operation.
     */
    boolean stop(String namespace, String groupId, Long jobId, Long instanceId)
            throws CloudAppException;

    /**
     * Lists all job instances of a job.
     *
     * @param namespace The namespace of the job, used to identify the namespace.
     *                  for schedulerX2, it is a must
     * @param groupId The group ID of the job, used to identify the job group.
     * @param jobId The ID of the job, used to identify the specific job.
     *
     * @return Returns a list of job instances.
     * @throws CloudAppException Throws this exception when an error occurs during the listing operation.
     */
    List<JobInstance> list(String namespace, String groupId, Long jobId)
            throws CloudAppException;

    /**
     * Gets a specific job instance.
     *
     * @param namespace The namespace of the job, used to identify the namespace.
     *                  for schedulerX2, it is a must
     * @param groupId The group ID of the job, used to identify the job group.
     * @param jobId The ID of the job, used to identify the specific job.
     * @param instanceId The instance ID of the job, used to identify a specific execution instance of the job.
     *
     * @return Returns the queried job instance.
     * @throws CloudAppException Throws this exception when an error occurs during the query operation.
     */
    JobInstance get(String namespace, String groupId, Long jobId,
                    Long instanceId)
            throws CloudAppException;
}

