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
package com.alibaba.cloudapp.scheduler.schedulerx2;

import com.alibaba.cloudapp.api.scheduler.worker.GlobalJobMetadata;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * GlobalJobBeanRegistrar registers all GlobalJob annotated data to Spring beans
 */
public class GlobalJobBeanRegistrar implements
        SmartInitializingSingleton {
    private static final Logger logger = LoggerFactory.getLogger(
            GlobalJobBeanRegistrar.class);
    
    @Autowired
    private ApplicationContext context;
    
    private final List<GlobalJobMetadata> globalJobs =
            GlobalJobScanner.getGlobalJobs();
    
    @Override
    public void afterSingletonsInstantiated() {
        registerGlobalJobBeans();
    }
    
    /**
     * Register all GlobalJob annotated data to Spring beans
     */
    private void registerGlobalJobBeans() {
        for (GlobalJobMetadata metadata : globalJobs) {
            GlobalJobMethodProcessor methodProcessor =
                    new GlobalJobMethodProcessor(
                            context.getBean(metadata.getBeanName()),
                            metadata.getProcessMethod(),
                            metadata.getPreProcessMethod(),
                            metadata.getPostProcessMethod()
                    );
            
            registerBean(
                    CommonConstants.getRegisterBeanNameFunc.apply(metadata),
                    methodProcessor
            );
        }
    }
    
    /**
     * Register bean to Spring
     *
     * @param beanName name of the bean to be registered
     * @param obj      bean object to be registered
     */
    private void registerBean(String beanName, Object obj) {
        try {
            SingletonBeanRegistry registry =
                    (SingletonBeanRegistry) context.getAutowireCapableBeanFactory();
            
            registry.registerSingleton(beanName, obj);
            logger.info("Registered GlobalJob beanName: {}", beanName);
            
        } catch (Exception e) {
            String msg = String.format("Failed to load class %s", beanName);
            throw new CloudAppException(msg, e);
        }
    }
    
}
