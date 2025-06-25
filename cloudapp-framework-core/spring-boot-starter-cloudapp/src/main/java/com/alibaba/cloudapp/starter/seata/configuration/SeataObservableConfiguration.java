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

package com.alibaba.cloudapp.starter.seata.configuration;

import com.alibaba.cloudapp.observabilities.opentelemetry.metric.MetricCollectionAspect;
import com.alibaba.cloudapp.seata.GlobalTransactionHolder;
import com.alibaba.cloudapp.seata.ObservableTransactionalInterceptor;
import io.seata.spring.boot.autoconfigure.SeataDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({MetricCollectionAspect.class,
        GlobalTransactionHolder.class})
@AutoConfigureAfter(SeataDataSourceAutoConfiguration.class)
public class SeataObservableConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public GlobalTransactionHolder globalTransactionHolder() {
        return new GlobalTransactionHolder();
    }
    
    @Bean("observableTransactionalInterceptor")
    @ConditionalOnMissingBean
    public ObservableTransactionalInterceptor observableTransactionalInterceptor(
            GlobalTransactionHolder holder
    ) {
        return new ObservableTransactionalInterceptor(holder);
    }

}
