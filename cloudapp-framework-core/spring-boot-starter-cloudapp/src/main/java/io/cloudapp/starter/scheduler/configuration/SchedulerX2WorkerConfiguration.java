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
package io.cloudapp.starter.scheduler.configuration;

import com.alibaba.schedulerx.worker.SchedulerxWorker;
import com.aliyun.schedulerx220190430.Client;
import io.cloudapp.scheduler.schedulerx2.GlobalJobBeanRegistrar;
import io.cloudapp.scheduler.schedulerx2.GlobalJobScanner;
import io.cloudapp.scheduler.schedulerx2.GlobalJobSyncManager;
import io.cloudapp.starter.scheduler.properties.SchedulerX2WorkerProperties;
import io.cloudapp.starter.scheduler.refresh.SchedulerX2ClientRefreshComponent;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@AutoConfiguration
@ConditionalOnClass({com.aliyun.schedulerx220190430.Client.class, SchedulerxWorker.class})
@EnableConfigurationProperties({SchedulerX2WorkerProperties.class})
@ConditionalOnProperty(
        prefix = "io.cloudapp.scheduler.schedulerx2-worker",
        value = "enabled",
        havingValue = "true")
public class SchedulerX2WorkerConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public SchedulerX2ClientRefreshComponent schedulerX2ClientRefreshComponent(
            SchedulerX2WorkerProperties properties
    ) {
        return new SchedulerX2ClientRefreshComponent(properties);
    }
    @Bean("schedulerX2Client")
    @ConditionalOnMissingBean
    public Client schedulerx2Client(SchedulerX2ClientRefreshComponent comp) {
        return comp.getBean();
    }
    
    
    @Bean("globalJobScanner")
    @ConditionalOnMissingBean
    @Order(1)
    public GlobalJobScanner globalJobScanner() {
        return new GlobalJobScanner();
    }
    
    @Bean("globalJobBeanRegistrar")
    @ConditionalOnMissingBean
    @Order(2)
    public GlobalJobBeanRegistrar globalJobBeanRegistrar(
            SchedulerX2ClientRefreshComponent comp
    ) {
        return comp.getRegistrar();
    }
    
    @Bean("globalJobSyncManager")
    @ConditionalOnMissingBean
    @Order(3)
    public GlobalJobSyncManager globalJobSyncManager(
            SchedulerX2ClientRefreshComponent comp
    ) {
        return comp.getJobSyncManager();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public SchedulerxWorker schedulerxWorker(SchedulerX2ClientRefreshComponent comp) {
        return comp.getWorker();
    }
}
