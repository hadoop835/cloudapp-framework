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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * GlobalJobScanner
 * <p>
 * Scan all the beans in the application context and find all the methods
 * annotated with GlobalJob.
 */
public abstract class AbstractGlobalJobScanner implements SmartInitializingSingleton {
    private static final Logger logger = LoggerFactory.getLogger(
            AbstractGlobalJobSyncManager.class);
    
    private static final List<GlobalJobMetadata> distributedJobs =
            new ArrayList<>();
    
    public static List<GlobalJobMetadata> getGlobalJobs() {
        return distributedJobs;
    }

    @Autowired
    private ApplicationContext context;
    
    @Override
    public void afterSingletonsInstantiated() {
        String[] beanNames = context.getBeanNamesForType(Object.class,
                                                         false, true
        );
        for (String beanName : beanNames) {
            logger.debug("GlobalJob annotation scan beanName:{}",
                         beanName
            );
            
            Object bean = null;
            Lazy onBean = context.findAnnotationOnBean(beanName,
                                                       Lazy.class
            );
            if (onBean != null) {
                logger.debug("GlobalJob annotation scan, skip @Lazy Bean:{}",
                             beanName
                );
                continue;
            } else {
                bean = context.getBean(beanName);
            }
            
            Class<?> beanClass = bean.getClass();
            scanForGlobalJobs(beanClass, beanName);
        }
    }
    
    private void scanForGlobalJobs(Class<?> clazz, String beanName) {
        Method[] methods = clazz.getDeclaredMethods();
        
        for (Method method : methods) {
            if (method.isAnnotationPresent(GlobalJob.class)) {
                GlobalJob globalJob = method.getAnnotation(
                        GlobalJob.class);
                GlobalJobMetadata metadata = new GlobalJobMetadata()
                        .setBeanName(beanName)
                        .setProcessMethod(method)
                        .setFixedDelay(globalJob.fixedDelay())
                        .setFixedRate(globalJob.fixedRate())
                        .setExecuteMode(globalJob.executeMode())
                        .setShardingParams(globalJob.shardingParams())
                        .setCron(globalJob.cron())
                        .setDescription(globalJob.description())
                        .setAutoCreateJob(globalJob.autoCreateJob())
                        .setAutoDeleteJob(globalJob.autoDeleteJob())
                        ;
                method.setAccessible(true);
                
                if (!globalJob.preProcess().isEmpty()){
                    try {
                        Method preMethod = clazz.getDeclaredMethod(
                                globalJob.preProcess());
                        preMethod.setAccessible(true);
                        metadata.setPreProcessMethod(preMethod);
                    }catch (NoSuchMethodException e){
                        logger.error("preProcess method not found:{}",
                                globalJob.preProcess());
                        throw new RuntimeException("initMethod invalid, for[" + clazz + "#" + method.getName() + "] .");
                    }
                }
                if (!globalJob.postProcess().isEmpty()){
                    try {
                        Method postProcess = clazz.getDeclaredMethod(
                                globalJob.postProcess());
                        postProcess.setAccessible(true);
                        metadata.setPostProcessMethod(postProcess);
                    }catch (NoSuchMethodException e){
                        logger.error("postProcess method not found:{}",
                                     globalJob.postProcess());
                        throw new RuntimeException("initMethod invalid, for[" + clazz + "#" + method.getName() + "] .");
                    }
                }
                
                // set value if not empty, otherwise use method name
                if (!globalJob.value().isEmpty()) {
                    metadata.setValueName(globalJob.value());
                } else {
                    metadata.setValueName(method.getName());
                }
                
                distributedJobs.add(metadata);
            }
        }
    }
    
}
