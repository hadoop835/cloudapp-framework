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

public class JobInstance {
    /**
     * executor
     */
    private String executor;
    
    /**
     * instance id
     */
    private Long instanceId;
    
    /**
     * job id
     */
    private Long jobId;
    
    /**
     * job instance parameters
     */
    public String parameters;
    
    /**
     * progress
     */
    private String progress;

    /**
     * schedule time
     */
    private String scheduleTime;
    
    /**
     * execution start time
     */
    private String startTime;
    
    /**
     * execution end time
     */
    private String endTime;
    
    /**
     * data time
     */
    private String dataTime;
    
    /**
     * execution status
     */
    private Integer status;
    
    /**
     * execution result
     */
    private String result;
    
    /**
     * trigger type, manual or schedule
     */
    private Integer triggerType;
    
    /**
     * work address
     */
    private String workAddr;
    
    public String getExecutor() {
        return executor;
    }
    
    public void setExecutor(String executor) {
        this.executor = executor;
    }
    
    public String getParameters() {
        return parameters;
    }
    
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
    
    public Long getInstanceId() {
        return instanceId;
    }
    
    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }
    
    public Long getJobId() {
        return jobId;
    }
    
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
    
    public String getProgress() {
        return progress;
    }
    
    public void setProgress(String progress) {
        this.progress = progress;
    }
    
    public String getScheduleTime() {
        return scheduleTime;
    }
    
    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }
    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public String getDataTime() {
        return dataTime;
    }
    
    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public Integer getTriggerType() {
        return triggerType;
    }
    
    public void setTriggerType(Integer triggerType) {
        this.triggerType = triggerType;
    }
    
    public String getWorkAddr() {
        return workAddr;
    }
    
    public void setWorkAddr(String workAddr) {
        this.workAddr = workAddr;
    }
    
}
