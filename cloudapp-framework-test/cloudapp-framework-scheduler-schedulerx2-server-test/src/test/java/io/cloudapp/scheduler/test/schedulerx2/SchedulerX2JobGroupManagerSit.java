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
package io.cloudapp.scheduler.test.schedulerx2;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.schedulerx220190430.Client;
import com.aliyun.schedulerx220190430.models.ListGroupsRequest;
import com.aliyun.schedulerx220190430.models.ListGroupsResponse;
import io.cloudapp.api.scheduler.model.JobGroup;
import io.cloudapp.api.scheduler.server.JobGroupManager;
import io.cloudapp.util.RandomStringGenerator;
import org.junit.Assert;
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
public class SchedulerX2JobGroupManagerSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            SchedulerX2JobGroupManagerSit.class);
    
    @Autowired
    JobGroupManager jobGroupManager;
    
    private String namespace = "983693cc-fb92-4b83-b197-1245440ff429";
    
    private String groupId = "test-group";
    
    private String appName = "test-app";
    
    
    @Test
    public void getDelegatingClient() {
        Client delegatingClient = (Client) jobGroupManager.getDelegatingClient();
        
        ListGroupsRequest request = new ListGroupsRequest();
//        namespaceId and regionId is required in ListGroupsRequest
        request.setNamespace(namespace);
        request.setRegionId("public");
        try {
            ListGroupsResponse groups = delegatingClient.listGroups(
                    request);
            if (groups != null & !groups.getBody().getData().getAppGroups().isEmpty()) {
            groups.getBody().getData().getAppGroups().stream().forEach(
                    item -> logger.info("AppGroup is: {}",
                                        JSONObject.toJSONString(item)
                    ));
                
            } else {
                logger.info("List AppGroup result is empty, please create an " +
                                    "AppGroup in the schedulerx console " +
                                    "first .");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void list() {
        List list = jobGroupManager.list(namespace, "");
        list.stream().forEach(item -> logger.info("list jobGroup result: {}",
                                                  JSON.toJSONString(item)
        ));
    }
    
    @Test
    public void create() {
        JobGroup obj = new JobGroup();
        obj.setNamespace(namespace);
        obj.setGroupId("test-group" + RandomStringGenerator.generate(3));
        obj.setAppName("test-app" + RandomStringGenerator.generate(3));
        Long job = jobGroupManager.create(obj);
        
        logger.info("create jobGroup is: {}", JSON.toJSONString(obj));
        logger.info("create jobGroup result: {}", JSON.toJSONString(job));
        
        jobGroupManager.delete(namespace, obj.getGroupId());
    }
    
    @Test
    public void get() {
        JobGroup jobGroup = jobGroupManager.get(namespace, groupId);
        logger.info("get jobGroup result: {}", JSON.toJSONString(jobGroup));
    }
    
    
    @Test
    public void update() {
        JobGroup obj = new JobGroup();
        obj.setNamespace(namespace);
        obj.setGroupId(groupId);
        obj.setAppName(appName);
        
        JobGroup oldJobGroup = jobGroupManager.get(namespace, groupId);
        if (oldJobGroup == null) {
            jobGroupManager.create(obj);
        }
        
        obj.setDescription("update desc...");
        boolean updateResult = jobGroupManager.update(obj);
        logger.info("update jobGroup result: {}", updateResult);
        
        JobGroup newJobGroup = jobGroupManager.get(namespace, groupId);
        Assert.assertEquals(obj.getDescription(), newJobGroup.getDescription());
        logger.info("update jobGroup is: {}", JSON.toJSONString(newJobGroup));
        
    }
    
    
    @Test
    public void delete() {
        String group_tmp = "test_group_tmp";
        JobGroup obj = new JobGroup();
        obj.setNamespace(namespace);
        obj.setGroupId(group_tmp);
        obj.setAppName("test-app" + RandomStringGenerator.generate(3));
        Long job = jobGroupManager.create(obj);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        boolean delete = jobGroupManager.delete(namespace, group_tmp);
        logger.info("delete jobGroup is: {}", JSON.toJSONString(obj));
        logger.info("delete jobGroup result: {}", delete);
    }
    
}
