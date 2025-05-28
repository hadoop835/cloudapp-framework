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

public class GlobalJobContext {
    private static final ThreadLocal<GlobalJobContext> contextHolder = new ThreadLocal<>();
    
    public GlobalJobContext(long jobId, String jobParam, int shardIndex,
                            int shardTotal, String shardParameter) {
        this.jobId = jobId;
        this.jobParam = jobParam;
        this.shardIndex = shardIndex;
        this.shardTotal = shardTotal;
        this.shardParameter = shardParameter;
    }
    
    // ---------------------- base info ----------------------
    
    /**
     * job id
     */
    private final long jobId;
    
    /**
     * job param
     */
    private final String jobParam;
    
    // ---------------------- for shard ----------------------
    
    /**
     * shard index
     */
    private final int shardIndex;
    
    /**
     * shard total
     */
    private final int shardTotal;
    
    private final String shardParameter;
    
    public long getJobId() {
        return jobId;
    }
    
    public String getJobParam() {
        return jobParam;
    }
    
    
    public int getShardIndex() {
        return shardIndex;
    }
    
    public int getShardTotal() {
        return shardTotal;
    }
    
    public String getShardParameter() {
        return shardParameter;
    }
    
    public static void setContext(GlobalJobContext ctx){
        contextHolder.set(ctx);
    }
    
    public static GlobalJobContext getContext(){
        return contextHolder.get();
    }
    
    public static void clear() {
        contextHolder.remove();
    }
}
