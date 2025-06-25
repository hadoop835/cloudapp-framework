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

package com.alibaba.cloudapp.base.threadpool.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
public class ThreadDemoController {
    
    private static final Logger logger = LoggerFactory.getLogger(
            ThreadDemoController.class);
    
    @Autowired
    private ThreadPoolExecutor monitoringThreadPool;
    
    @GetMapping("/thread/getCorePoolSize")
    public int getCorePoolSize() {
        int corePoolSize = monitoringThreadPool.getCorePoolSize();
        logger.info("corePoolSize: {}", corePoolSize);
        return corePoolSize;
    }
    
    @GetMapping("/thread/getMaximumPoolSize")
    public int getMaximumPoolSize() {
        int maximumPoolSize = monitoringThreadPool.getMaximumPoolSize();
        logger.info("maximumPoolSize: {}", maximumPoolSize);
        return maximumPoolSize;
    }
    
    @GetMapping("/thread/getLargestPoolSize")
    public int getLargestPoolSize() {
        int largestPoolSize = monitoringThreadPool.getLargestPoolSize();
        logger.info("largestPoolSize: {}", largestPoolSize);
        return largestPoolSize;
    }
    
    @GetMapping("/thread/getPoolSize")
    public int getPoolSize() {
        int poolSize = monitoringThreadPool.getPoolSize();
        logger.info("poolSize: {}", poolSize);
        return poolSize;
    }
    
    @GetMapping("/thread/info")
    public String getInfo() {
        return monitoringThreadPool.toString() + ": " + monitoringThreadPool.getCompletedTaskCount();
    }
    
    
    @GetMapping("/thread/execute")
    public String execute(@RequestParam("p") String p) {
        monitoringThreadPool.execute(() -> {
            logger.warn("Executing with params: {}", p);
        });
        
        return "OK";
    }
    
    @GetMapping("/thread/getKeepAliveSeconds")
    public int getKeepAliveSeconds() {
        int keepAliveTime = (int) monitoringThreadPool.getKeepAliveTime(
                TimeUnit.SECONDS);
        return keepAliveTime;
    }
    
}
