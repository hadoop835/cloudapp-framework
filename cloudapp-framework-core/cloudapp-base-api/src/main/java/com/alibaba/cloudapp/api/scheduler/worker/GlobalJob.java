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

package com.alibaba.cloudapp.api.scheduler.worker;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalJob {
    
    /**
     * Configurable generated jobName, beanName.
     * If not specified, the name of the method will be used.
     */
    String value() default "";
    
    /**
     * Configurable operations to be performed before the job execution.
     */
    String preProcess() default "";
    
    /**
     * Configurable operations to be performed after the job execution.
     */
    String postProcess() default "";
    
    /**
     * Cron expression for scheduling the job.
     */
    String cron() default "";
    
    /**
     * Fixed delay between job executions.
     * in second.
     */
    long fixedDelay() default 0L;
    
    /**
     * Fixed rate at which the job should be executed.
     * The unit is seconds, but it needs to be greater than 60
     */
    long fixedRate() default 0L;
    
    /**
     * Execution mode of the job, supports standalone, broadcast, and sharding.
     */
    String executeMode() default "standalone";
    
    /**
     * Sharding parameters for sharding jobs.
     * Format:[shard number]=shard parameter, multiple shard
     * separated by comma or line break, such as "0=a,1=b,2=c".
     */
    String shardingParams() default "";
    
    /**
     * Description of the job.
     */
    String description() default "Created from CloudApp framework";
    
    /**
     * Whether to automatically create the job and submit it to the server on startup.
     */
    boolean autoCreateJob() default true;
    
    /**
     * Whether to automatically delete the job from the server on shutdown.
     */
    boolean autoDeleteJob() default false;
    
}
