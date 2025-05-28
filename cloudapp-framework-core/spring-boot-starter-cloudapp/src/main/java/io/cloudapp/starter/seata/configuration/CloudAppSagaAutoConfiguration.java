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

package io.cloudapp.starter.seata.configuration;

import io.cloudapp.seata.StateMachineEngineProxy;
import io.seata.saga.engine.StateMachineConfig;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.engine.config.DbStateMachineConfig;
import io.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import io.seata.saga.rm.StateMachineEngineHolder;
import io.seata.spring.boot.autoconfigure.SeataSagaAutoConfiguration;
import io.seata.spring.boot.autoconfigure.properties.SagaAsyncThreadPoolProperties;
import io.seata.spring.boot.autoconfigure.properties.SeataProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import javax.sql.DataSource;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
       value ={ "io.cloudapp.seata.enabled",
        "io.cloudapp.seata.saga.enabled"}, havingValue = "true"
)
@AutoConfigureAfter({
        DataSourceAutoConfiguration.class,
        CloudAppSeataAutoConfiguration.class
})
public class CloudAppSagaAutoConfiguration {
    
    /**
     * Create state machine config bean.
     */
    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "io.cloudapp.seata.saga.state-machine")
    public StateMachineConfig dbStateMachineConfig(
            DataSource dataSource,
            @Qualifier(SeataSagaAutoConfiguration.SAGA_DATA_SOURCE_BEAN_NAME)
            @Autowired(required = false) DataSource sagaDataSource,
            @Qualifier(SeataSagaAutoConfiguration.SAGA_ASYNC_THREAD_POOL_EXECUTOR_BEAN_NAME)
            @Autowired(required = false) ThreadPoolExecutor threadPoolExecutor,
            SeataProperties properties) {
        DbStateMachineConfig config = new DbStateMachineConfig();
        config.setDataSource(sagaDataSource != null ? sagaDataSource : dataSource);
        
        config.setApplicationId(properties.getApplicationId());
        config.setTxServiceGroup(properties.getTxServiceGroup());
        
        if (threadPoolExecutor != null) {
            config.setThreadPoolExecutor(threadPoolExecutor);
        }
        
        return config;
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty("io.cloudapp.seata.saga.async-thread-pool")
    @ConfigurationProperties("io.cloudapp.seata.saga.async-thread-pool")
    public SagaAsyncThreadPoolProperties sagaAsyncThreadPoolProperties() {
        return new SagaAsyncThreadPoolProperties();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public StateMachineEngine stateMachineEngine(StateMachineConfig config) {
        ProcessCtrlStateMachineEngine engine = new ProcessCtrlStateMachineEngine();
        engine.setStateMachineConfig(config);
        
        StateMachineEngineProxy proxy = new StateMachineEngineProxy(engine);
        new StateMachineEngineHolder().setStateMachineEngine(proxy);
        return proxy;
    }
    
    @Bean(SeataSagaAutoConfiguration.SAGA_REJECTED_EXECUTION_HANDLER_BEAN_NAME)
    @ConditionalOnMissingBean
    public RejectedExecutionHandler sagaRejectedExecutionHandler() {
        return new ThreadPoolExecutor.CallerRunsPolicy();
    }
    
    /**
     * Create state machine async thread pool executor bean.
     */
    @Bean(SeataSagaAutoConfiguration.SAGA_ASYNC_THREAD_POOL_EXECUTOR_BEAN_NAME)
    @ConditionalOnMissingBean
    public ThreadPoolExecutor sagaAsyncThreadPoolExecutor(
            SagaAsyncThreadPoolProperties properties,
            @Qualifier(SeataSagaAutoConfiguration.SAGA_REJECTED_EXECUTION_HANDLER_BEAN_NAME)
            RejectedExecutionHandler rejectedExecutionHandler) {
        ThreadPoolExecutorFactoryBean threadFactory = new ThreadPoolExecutorFactoryBean();
        threadFactory.setBeanName("sagaStateMachineThreadPoolExecutorFactory");
        threadFactory.setThreadNamePrefix("sagaAsyncExecute-");
        threadFactory.setCorePoolSize(properties.getCorePoolSize());
        threadFactory.setMaxPoolSize(properties.getMaxPoolSize());
        threadFactory.setKeepAliveSeconds(properties.getKeepAliveTime());
        
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory,
                rejectedExecutionHandler
        );
    }
    
}
