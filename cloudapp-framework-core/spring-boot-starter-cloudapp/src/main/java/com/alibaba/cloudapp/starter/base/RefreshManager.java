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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RefreshManager implements ApplicationContextAware {
    private final static Logger logger = LoggerFactory.getLogger(
            RefreshManager.class);

    private  Map<String, RefreshableComponent> components =
            new ConcurrentHashMap<>();

    private static ApplicationContext applicationContext;


    private  void tryRefreshComponent(String key) {
        RefreshableComponent component = components.get(key);
        if (component == null) {
            logger.info("Component not registered by key {}.",  key);
            return;
        }

        logger.info("Refreshing component {} as key {} detected changed.",
                component.getName(), key);
        component.onKeysChanged();
    }

    public void register(RefreshableComponent component) {
        components.put(component.bindKey(), component);
    }

    public void unregister(RefreshableComponent component) {
        components.remove(component.bindKey());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static void onPropertiesChanged(String key) {
        if (applicationContext == null) {
            logger.trace("Refresh manager is not initialized yet," +
                    " as application context not found.");
            return;
        }

        RefreshManager refreshManager = applicationContext.getBean(
                RefreshManager.class);

        if (refreshManager == null) {
            logger.trace("Refresh manager is not initialized yet.");
            return;
        }

        refreshManager.tryRefreshComponent(key);
    }
}
