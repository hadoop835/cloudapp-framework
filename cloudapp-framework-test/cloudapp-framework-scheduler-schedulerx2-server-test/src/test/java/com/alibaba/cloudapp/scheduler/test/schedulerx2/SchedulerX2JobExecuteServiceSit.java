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
package com.alibaba.cloudapp.scheduler.test.schedulerx2;

import com.alibaba.fastjson2.JSON;
import com.alibaba.cloudapp.api.scheduler.model.JobInstance;
import com.alibaba.cloudapp.api.scheduler.server.JobExecuteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SchedulerX2JobExecuteServiceSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            SchedulerX2JobExecuteServiceSit.class);
    
    @Autowired
    private JobExecuteService jobExecuteService;
    
    private String namespace = "838d9c47-843e-40b8-bbdc-53808274b857";
    
    private String groupId = "test-group";
    
    private Long jobId = 29287470L;
    
    private Long instanceId = 4114082292L;
    
    @Test
    public void getDelegatingClient() {
        Object delegatingClient = jobExecuteService.getDelegatingClient();
        logger.info("getDelegatingClient result: {}",
                    JSON.toJSONString(delegatingClient)
        );
    }
    
    @Test
    public void get() {
        JobInstance jobInstance = jobExecuteService.get(namespace,
                                                        groupId,
                                                        jobId,
                                                        instanceId
        );
        logger.info("get jobInstance result: {}",
                    JSON.toJSONString(jobInstance)
        );
    }
    
    @Test
    public void list() {
        List list = jobExecuteService.list(namespace, groupId, jobId);
        list.stream().forEach(
                e -> logger.info("list job detail: {}", JSON.toJSONString(e)));
    }
    
    @Test
    public void setSuccess() {
        boolean setSuccess = jobExecuteService.setSuccess(namespace, groupId,
                                                          jobId,
                                                          instanceId
        );
        logger.info("setSuccess result: {}", setSuccess);
    }
    
    @Test
    public void stop() {
        boolean stop = jobExecuteService.stop(namespace, groupId, jobId,
                                              instanceId
        );
        logger.info("stop result: {}", stop);
    }
    
    @Test
    public void retry() {
        boolean retry = jobExecuteService.retry(namespace, groupId, jobId,
                                                instanceId
        );
        logger.info("retry result: {}", retry);
    }
    
}
