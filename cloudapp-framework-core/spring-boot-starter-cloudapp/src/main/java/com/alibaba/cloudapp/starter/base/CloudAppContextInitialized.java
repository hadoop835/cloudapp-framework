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

package com.alibaba.cloudapp.starter.base;

import com.alibaba.cloudapp.starter.refresh.RefreshProxyBeanAutowirePostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.GenericApplicationContext;

import static org.springframework.context.annotation.AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME;

public class CloudAppContextInitialized implements ApplicationListener<ApplicationContextInitializedEvent> {

    public void registerBeanPostProcessor(GenericApplicationContext applicationContext,
                                                  String beanName, Class<?> beanPostProcessorClass) {

        if (applicationContext.containsBean(beanName)) {
            applicationContext.removeBeanDefinition(beanName);
        }

        applicationContext.registerBeanDefinition(beanName, BeanDefinitionBuilder
                .rootBeanDefinition(beanPostProcessorClass).setRole(BeanDefinition.ROLE_INFRASTRUCTURE)
                .getBeanDefinition());


    }




    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        registerBeanPostProcessor((GenericApplicationContext) event.getApplicationContext(),
                AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME,
                RefreshProxyBeanAutowirePostProcessor.class);

    }
}
