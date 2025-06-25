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

package com.alibaba.cloudapp.seata;

import com.alibaba.cloudapp.api.observabilities.Metric;
import com.alibaba.cloudapp.api.observabilities.MetricCollection;
import com.alibaba.cloudapp.api.observabilities.MetricType;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@MetricCollection
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GlobalTransactionHolder {
    @Metric(name="seata.transactional.counter")
    private long count;
    
    @Metric(name="seata.transactional.succeed.counter")
    private long succeedCount;
    
    @Metric(name="seata.transactional.failed.counter")
    private long failedCount;
    
    @Metric(name="seata.transactional.running.gauge", type = MetricType.GAUGE)
    private long runningGauge;
    
    @Metric(name="seata.transactional.max.running.time",
            type = MetricType.GAUGE, unit = "ms")
    private long maxRunningTime;
    
    @Metric(name="seata.transactional.min.running.time",
            type = MetricType.GAUGE, unit = "ms")
    private long minRunningTime;
    
    public long getCount() {
        return count;
    }
    
    public void setCount(long count) {
        this.count = count;
    }
    
    public long getSucceedCount() {
        return succeedCount;
    }
    
    public void setSucceedCount(long succeedCount) {
        this.succeedCount = succeedCount;
    }
    
    public long getFailedCount() {
        return failedCount;
    }
    
    public void setFailedCount(long failedCount) {
        this.failedCount = failedCount;
    }
    
    public long getRunningGauge() {
        return runningGauge;
    }
    
    public void setRunningGauge(long runningGauge) {
        this.runningGauge = runningGauge;
    }
    
    public long getMaxRunningTime() {
        return maxRunningTime;
    }
    
    public void setMaxRunningTime(long maxRunningTime) {
        this.maxRunningTime = maxRunningTime;
    }
    
    public long getMinRunningTime() {
        return minRunningTime;
    }
    
    public void setMinRunningTime(long minRunningTime) {
        this.minRunningTime = minRunningTime;
    }
    
}
