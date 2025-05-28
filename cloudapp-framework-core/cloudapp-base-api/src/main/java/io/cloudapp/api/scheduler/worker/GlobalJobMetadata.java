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

import java.lang.reflect.Method;

public class GlobalJobMetadata {
    /**
     * The target class bean name of the method invoke.
     */
    private String beanName;
    
    /**
     * The value name of the GlobalJob.
     * Configurable generated jobName, beanName.
     * If not specified, the name of the method will be used.
     */
    private String valueName;
    
    /**
     * The method to be executed.
     */
    private Method processMethod;
    /**
     * pre execution method.
     */
    private Method preProcessMethod;
    /**
     * post execution method.
     */
    private Method postProcessMethod;
    
    /**
     * 'cron' expression.
     */
    private String cron;
    
    /**
     * Fixed delay in seconds.
     */
    private Long fixedDelay;
    
    /**
     * Fixed rate in seconds.
     */
    private Long fixedRate;
    
    /**
     * Execution mode.
     */
    private String executeMode;
    
    /**
     * Sharding params.
     * Only used when executeMode is 'sharding'.
     */
    private String shardingParams;
    
    /**
     * job description.
     */
    private String description;
    
    /**
     * Whether to create the job automatically. default true.
     */
    private boolean autoCreateJob = true;
    
    /**
     * Whether to delete the job automatically. default false.
     */
    private boolean autoDeleteJob;
    
    
    public GlobalJobMetadata() {
    }
    
    public String getBeanName() {
        return beanName;
    }
    
    public GlobalJobMetadata setBeanName(String beanName) {
        this.beanName = beanName;
        return this;
    }
    
    public String getValueName() {
        return valueName;
    }
    
    public GlobalJobMetadata setValueName(String valueName) {
        this.valueName = valueName;
        return this;
    }
    
    public Method getProcessMethod() {
        return processMethod;
    }
    
    public GlobalJobMetadata setProcessMethod(Method processMethod) {
        this.processMethod = processMethod;
        return this;
    }
    
    public Method getPreProcessMethod() {
        return preProcessMethod;
    }
    
    public GlobalJobMetadata setPreProcessMethod(Method preProcessMethod) {
        this.preProcessMethod = preProcessMethod;
        return this;
    }
    
    public Method getPostProcessMethod() {
        return postProcessMethod;
    }
    
    public GlobalJobMetadata setPostProcessMethod(Method postProcessMethod) {
        this.postProcessMethod = postProcessMethod;
        return this;
    }
    
    public String getCron() {
        return cron;
    }
    
    public GlobalJobMetadata setCron(String cron) {
        this.cron = cron;
        return this;
    }
    
    public Long getFixedDelay() {
        return fixedDelay;
    }
    
    public GlobalJobMetadata setFixedDelay(Long fixedDelay) {
        this.fixedDelay = fixedDelay;
        return this;
    }
    
    public Long getFixedRate() {
        return fixedRate;
    }
    
    public GlobalJobMetadata setFixedRate(Long fixedRate) {
        this.fixedRate = fixedRate;
        return this;
    }
    
    public String getExecuteMode() {
        return executeMode;
    }
    
    public GlobalJobMetadata setExecuteMode(String executeMode) {
        this.executeMode = executeMode;
        return this;
    }
    
    public String getDescription() {
        return description;
    }
    
    public GlobalJobMetadata setDescription(String description) {
        this.description = description;
        return this;
    }
    
    public boolean isAutoCreateJob() {
        return autoCreateJob;
    }
    
    public GlobalJobMetadata setAutoCreateJob(boolean autoCreateJob) {
        this.autoCreateJob = autoCreateJob;
        return this;
    }
    
    public boolean isAutoDeleteJob() {
        return autoDeleteJob;
    }
    
    public GlobalJobMetadata setAutoDeleteJob(boolean autoDeleteJob) {
        this.autoDeleteJob = autoDeleteJob;
        return this;
    }
    
    public String getShardingParams() {
        return shardingParams;
    }
    
    public GlobalJobMetadata setShardingParams(String shardingParams) {
        this.shardingParams = shardingParams;
        return this;
    }
    
}
