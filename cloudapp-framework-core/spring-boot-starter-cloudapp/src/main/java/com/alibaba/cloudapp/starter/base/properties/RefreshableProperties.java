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
package com.alibaba.cloudapp.starter.base.properties;

import com.alibaba.cloudapp.starter.refresh.PropKeyRefreshedEvent;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.PostConstruct;

public abstract class RefreshableProperties implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    
    /**
     * refreshable not set ===> refresh disabled.
     * refreshable = false ===> refresh disabled.
     * refreshable = true  ===> refresh enabled.
     */
    private Boolean refreshable;

    // default enabled.
    private boolean refreshEnabled = true;

    public void setRefreshable(String refreshable) {
        boolean ref = Boolean.parseBoolean(refreshable);

        Object old = this.refreshable;
        this.refreshable = ref;

        // first time set value.
        if (old == null) {
            refreshEnabled = ref;
        }
    }
    
    public Boolean getRefreshable() {
        return refreshable;
    }
    
    @PostConstruct
    public void afterConstruct()
    {
        if (! refreshEnabled) {
            // disabled.
            return;
        }

        ConfigurationProperties prop = AnnotationUtils.getAnnotation(
                this.getClass(), ConfigurationProperties.class);
        
        assert prop != null;
        String prefix = prop.prefix();
        this.publishEvent(prefix, this);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    public void publishEvent(String prefix, RefreshableProperties properties) {
        applicationContext.publishEvent(new PropKeyRefreshedEvent(prefix, properties));
    }
    
}
