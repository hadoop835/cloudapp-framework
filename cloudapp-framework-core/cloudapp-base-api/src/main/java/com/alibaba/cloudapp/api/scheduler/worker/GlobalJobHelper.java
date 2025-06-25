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

public class GlobalJobHelper {
    // ---------------------- base info ----------------------
    
    public static long getJobId() {
        GlobalJobContext ctx = GlobalJobContext.getContext();
        if (ctx == null) {
            return -1;
        }
        
        return ctx.getJobId();
    }

    public static String getJobParam() {
        GlobalJobContext ctx = GlobalJobContext.getContext();
        if (ctx == null) {
            return null;
        }
        
        return ctx.getJobParam();
    }
    
    // ---------------------- for shard ----------------------
    
    public static int getShardIndex() {
        GlobalJobContext ctx = GlobalJobContext.getContext();
        if (ctx == null) {
            return -1;
        }
        
        return ctx.getShardIndex();
    }

    public static int getShardTotal() {
        GlobalJobContext ctx = GlobalJobContext.getContext();
        if (ctx == null) {
            return -1;
        }
        
        return ctx.getShardTotal();
    }
    
    public static String getShardParameter() {
        GlobalJobContext ctx = GlobalJobContext.getContext();
        if (ctx == null) {
            return "";
        }
        
        return ctx.getShardParameter();
    }
    
}
