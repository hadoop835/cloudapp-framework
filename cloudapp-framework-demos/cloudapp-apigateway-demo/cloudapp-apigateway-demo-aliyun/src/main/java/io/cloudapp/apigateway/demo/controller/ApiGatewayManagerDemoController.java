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

package io.cloudapp.apigateway.demo.controller;

import io.cloudapp.apigateway.aliyun.properties.ApiKeyProperties;
import io.cloudapp.apigateway.aliyun.service.ApiGatewayManager;
import io.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class ApiGatewayManagerDemoController {

    private static final Logger logger = LoggerFactory.getLogger(
            ApiGatewayManagerDemoController.class);

    @Autowired
    ApiGatewayManager apiGatewayManager;

    private String gwInstanceId = "i-ac18395ee8b98329c00";
    private String name = "gxr-test2";

    @RequestMapping("/checkConsumerExists")
    public Boolean checkConsumerExists() {
        boolean exists = false;
        try {
            exists = apiGatewayManager.checkConsumerExists(name, gwInstanceId);
        } catch (CloudAppException e) {
            logger.error("Check consumer exists failed.", e);
        }
        System.out.println("The consumer exists is: " + exists + ", in CSB.");
        return exists;
    }

    @RequestMapping("/createConsumer")
    public void createConsumer() {
        try {
            ApiKeyProperties properties = new ApiKeyProperties();
            properties.setApiKey("key198727837444");
    
            apiGatewayManager.createApiKeyConsumer(name, gwInstanceId, null, properties);
            logger.info("The consumer is created in CSB.");
        } catch (CloudAppException e) {
            logger.error("Create consumer failed.", e);
        }
    }

}
