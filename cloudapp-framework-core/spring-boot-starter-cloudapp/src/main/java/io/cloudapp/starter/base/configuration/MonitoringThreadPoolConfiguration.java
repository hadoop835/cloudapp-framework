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
package io.cloudapp.starter.base.configuration;

import io.cloudapp.starter.base.properties.ThreadPoolProperties;
import io.cloudapp.starter.properties.EnableModuleProperties;
import io.cloudapp.starter.refresh.RefreshableProxyFactory;
import io.cloudapp.starter.refresh.aspect.RefreshableBinding;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Configuration
@EnableModuleProperties(ThreadPoolProperties.class)
@ConditionalOnProperty(prefix = ThreadPoolProperties.BINDING_PROP_KEY,
        value = "enabled",
        havingValue = "true")
public class MonitoringThreadPoolConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MonitoringThreadPoolConfiguration.class);

    @Bean("monitoringThreadPool")
    @RefreshableBinding(ThreadPoolProperties.BINDING_PROP_KEY)
    public ThreadPoolExecutor monitoringThreadPool(ThreadPoolProperties props) {

        return RefreshableProxyFactory.create(this::createThreadPoolExecutor,
                props,
                this::stopThreadPool);
    }

    @NotNull
    private ThreadPoolExecutor createThreadPoolExecutor(ThreadPoolProperties props) {
        AtomicInteger threadIndex = new AtomicInteger(0);

        log.info("Creating a new thread pool executor: {}", props);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                props.getCorePoolSize(),
                props.getMaximumPoolSize(),
                props.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(props.getQueueCapacity()),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NotNull Runnable r) {
                        String threadName = props.getThreadNamePrefix() + "-" +
                                threadIndex.incrementAndGet() ;
                        return new Thread(r, threadName);
                    }
                }
        );

        threadPoolExecutor.allowCoreThreadTimeOut(props.isAllowCoreThreadTimeOut());

        try {
            threadPoolExecutor.awaitTermination(
                    props.getAwaitTerminationSeconds(),
                    TimeUnit.SECONDS
            );
        } catch (InterruptedException e) {
            throw new RuntimeException("threadPoolExecutor Interrupted", e);
        }

        return threadPoolExecutor;
    }

    private void stopThreadPool(ThreadPoolExecutor threadPoolExecutor) {
        try {
            log.info("Stopping the previous thread pool executor.");
            threadPoolExecutor.shutdown();
        } catch (Exception e) {
            log.error("Stop thread pool exception", e);
        }
    }
    
}
