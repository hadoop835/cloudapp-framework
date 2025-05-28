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
package io.cloudapp.starter.base;

import io.cloudapp.api.common.ComponentLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RefreshableComponent<Prop, Bean> implements ComponentLifeCycle,
        ApplicationContextAware, InitializingBean, DisposableBean {
    /**
     * Logger
     */
    private final static Logger logger = LoggerFactory.getLogger(
            RefreshableComponent.class);
    
    private static final int CORE_POOL_SIZE = 2;
    
    private static final int MAXIMUM_POOL_SIZE = 8;
    
    private static final int KEEP_ALIVE_TIME_IN_SECONDS = 1;
    
    private static final int QUEUE_CAPACITY = 16;
    
    @Autowired
    private RefreshManager refreshManager;

    /**
     * Refresh count
     */
    private final AtomicInteger refreshAccount = new AtomicInteger(0);

    /**
     * Spring application context
     */
    protected ApplicationContext applicationContext;


    protected Prop properties;

    protected Bean bean;

    private static ThreadPoolExecutor executor;

    @Override
    public abstract void postStart() ;

    @Override
    public abstract void preStop() ;

    public abstract String bindKey();

    static {
        initRefreshingThread();
    }

    public RefreshableComponent(Prop properties) {
        this.properties = properties;
        bean = createBean(properties);
    }
    
    public RefreshableComponent(Prop properties, Bean bean) {
        this.properties = properties;
        this.bean = bean;
    }

    public Bean getBean() {
        return bean;
    }


    public void onKeysChanged() {
        refresh(properties);
    }

    /**
     * Component name
     * 
     * @return Component name
     */
    public abstract String getName();

    public void refresh(Prop properties) {
        logger.info("Refreshing {}, start to submit the task", getName());


        executor.execute(() -> {
            logger.info("Refreshing {}, stopping", getName());
            preStop();

            logger.info("Refreshing {}, starting", getName());
            postStart();

            logger.info("Refreshing {}, started", getName());
            refreshAccount.incrementAndGet();
        });

    }


    protected abstract Bean createBean(Prop properties);


    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("Starting component: {}, submitting the task", getName());

        if(refreshManager == null) {
            refreshManager = applicationContext.getBean(RefreshManager.class);
        }
        
        refreshManager.register(this);


        executor.execute(() -> {
            logger.info("Starting component: {}, before starting", getName());
            postStart();
            logger.info("Component started: {}", getName());
        });
    }

    private static void initRefreshingThread() {
        if (executor != null) {
            return;
        }

        executor = createThreadPool();
    }

    private static synchronized ThreadPoolExecutor createThreadPool() {
        if (executor != null) {
            return executor;
        }

        logger.info("Creating refresh thread pool");

        AtomicInteger threadIndex = new AtomicInteger(0);

        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME_IN_SECONDS,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                r -> new Thread(r, "CloudApp-Refresh-Thread-" +
                                threadIndex.incrementAndGet())
        );
    }

    @Override
    public void destroy() {
        logger.info("Before stopping component: {}", getName());
        refreshManager.unregister(this);

        preStop();
        logger.info("Component stopped: {}", getName());
    }

}
