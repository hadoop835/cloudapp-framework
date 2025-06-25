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

package com.alibaba.cloudapp.api.scheduler.server;

import java.util.List;

import com.alibaba.cloudapp.api.common.DelegatingClientAware;
import com.alibaba.cloudapp.api.scheduler.model.JobGroup;
import com.alibaba.cloudapp.exeption.CloudAppException;

/**
 * Job Group Management API
 * This API defines methods for CRUD operations on job group.
 *
 * @param <Client> The underlying client type
 */
public interface JobGroupManager<Client> extends DelegatingClientAware<Client> {

    /**
     * Create a new job group
     *
     * @param obj the object containing details to create the group
     *
     * @return job group ID of the newly created group
     * @throws CloudAppException if an error occurs during the operation
     */
    Long create(JobGroup obj) throws CloudAppException;

    /**
     * Retrieves details of a specific job group
     *
     * @param namespace the namespace of the group
     *                 for schedulerX2, it is a must
     * @param groupId the ID of the group
     *
     * @return the information of the group
     * @throws CloudAppException if an error occurs during the operation
     */
    JobGroup get(String namespace, String groupId) throws CloudAppException;

    /**
     * Delete a job group
     *
     * @param namespace the namespace of the group
     *                 for schedulerX2, it is a must
     * @param id the ID of the group to delete
     *
     * @return true if the deletion was successful, false otherwise
     * @throws CloudAppException if an error occurs during the operation
     */
    boolean delete(String namespace, String id) throws CloudAppException;

    /**
     * Update an existing job group
     *
     * @param obj the object containing updated details
     *
     * @return true if the update was successful, false otherwise
     * @throws CloudAppException if an error occurs during the operation
     */
    boolean update(JobGroup obj) throws CloudAppException;

    /**
     * Lists all job groups matching a given name
     *
     * @param namespace the namespace of the group
     *                 for schedulerX2, it is a must
     * @param name the name to search for
     *
     * @return a list of matching job groups
     * @throws CloudAppException if an error occurs during the operation
     */
    List<JobGroup> list(String namespace, String name) throws CloudAppException;
}

