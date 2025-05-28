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

package io.cloudapp.api.scheduler.model;

import java.util.Map;

/**
 * Job
 */
public class Job {
    /**
     * Job ID
     */
    private Long jobId;

    /**
     * Identifier for the group to which the job belongs.
     */
    private String groupId;

    /**
     * The name of the job.
     */
    private String name;

    /**
     * Namespace of the job.
     */
    private String namespace;

    /**
     * The type of job.
     */
    private JobType jobType;

    /**
     * The type of time expression used for scheduling.
     */
    private TimeType timeType;

    /**
     * The time expression used for scheduling.
     */
    private String timeExpression;

    /**
     * The fully qualified class name of the job
     * When the job type is Java, it is required.
     */
    private String className;

    /**
     * A brief description of the job.
     */
    private String description;

    /**
     * The mode in which the job should be executed.
     */
    private String executeMode;

    /**
     * The content or payload that the job will process.
     * When the task type is Python/Shell/Go/K8s, the script code content is required.
     */
    private String content;

    /**
     * Custom parameters for the job.
     */
    private String parameters;

    /**
     * The maximum number of attempts to execute the job before failing.
     */
    private Integer maxAttempt;

    /**
     * The interval (in seconds) between each attempt.
     */
    private Integer attemptInterval;

    /**
     * Information about the individuals to notify upon job completion or failure.
     */
    private Map<String, Object> contacts;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExecuteMode() {
        return executeMode;
    }

    public void setExecuteMode(String executeMode) {
        this.executeMode = executeMode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Integer getMaxAttempt() {
        return maxAttempt;
    }

    public void setMaxAttempt(Integer maxAttempt) {
        this.maxAttempt = maxAttempt;
    }

    public Integer getAttemptInterval() {
        return attemptInterval;
    }

    public void setAttemptInterval(Integer attemptInterval) {
        this.attemptInterval = attemptInterval;
    }

    public Map<String, Object> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, Object> contacts) {
        this.contacts = contacts;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }

    public String getTimeExpression() {
        return timeExpression;
    }

    public void setTimeExpression(String timeExpression) {
        this.timeExpression = timeExpression;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
