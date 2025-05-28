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

import com.aliyun.schedulerx220190430.Client;
import io.cloudapp.scheduler.schedulerx2.SchedulerX2JobExecuteService;
import io.cloudapp.scheduler.schedulerx2.SchedulerX2JobGroupManager;
import io.cloudapp.scheduler.schedulerx2.SchedulerX2JobManager;
import io.cloudapp.starter.scheduler.properties.SchedulerX2ServerProperties;
import io.cloudapp.starter.scheduler.refresh.SchedulerX2ServerRefreshComponent;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass({com.aliyun.schedulerx220190430.Client.class})
@EnableConfigurationProperties({SchedulerX2ServerProperties.class})
@ConditionalOnProperty(
        prefix = "io.cloudapp.scheduler.schedulerx2-server",
        value = "enabled",
        havingValue = "true")
public class SchedulerX2ServerConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public SchedulerX2ServerRefreshComponent schedulerX2ServerRefreshComponent(
            SchedulerX2ServerProperties properties
    ) {
        return new SchedulerX2ServerRefreshComponent(properties);
    }
    
    @Bean("schedulerX2Client")
    @ConditionalOnMissingBean
    public Client schedulerx2Client(SchedulerX2ServerRefreshComponent comp) {
        return comp.getBean();
    }

    @Bean("schedulerX2JobGroupManger")
    @ConditionalOnMissingBean
    public SchedulerX2JobGroupManager jobGroupManger(Client client) {
        return new SchedulerX2JobGroupManager(client);
    }
    
    @Bean("schedulerX2JobManger")
    @ConditionalOnMissingBean
    public SchedulerX2JobManager jobManger(Client client) {
        return new SchedulerX2JobManager(client);
    }
    
    @Bean("schedulerX2JobExecuteService")
    @ConditionalOnMissingBean
    public SchedulerX2JobExecuteService jobExecuteService(Client client) {
        return new SchedulerX2JobExecuteService(client);
    }
}
