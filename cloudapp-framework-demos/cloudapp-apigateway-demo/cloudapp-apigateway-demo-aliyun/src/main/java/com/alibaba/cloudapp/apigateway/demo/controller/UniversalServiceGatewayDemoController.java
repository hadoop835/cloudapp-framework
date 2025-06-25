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

package com.alibaba.cloudapp.apigateway.demo.controller;

import com.alibaba.cloudapp.api.gateway.GatewayService;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class UniversalServiceGatewayDemoController {

    @Autowired
    @Qualifier("gatewayService")
    GatewayService gatewayService;

    @Value("${brokerAddress}")
    private String brokerUrl;

    @Autowired
    Environment env;

    @RequestMapping("/get")
    public ResponseEntity<String> get() {
        RestTemplate restTemplate = gatewayService.getRestTemplate();
        RequestEntity<Void> request = RequestEntity.get(brokerUrl + "/get")
                .header("platform", "CSB2").build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        System.out.println("The simple get request response is: " + "\n" + response);
        return response;
    }

    @RequestMapping("/asyncGet")
    public Future<String> asyncGet() {
        Future<String> future = gatewayService.asyncGet(brokerUrl + "/get", String.class);
        if (future != null) {
            while (!future.isDone()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                System.out.println("The simple async get request response is: " + "\n" + future.get());
                return future;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return future;
    }


    @RequestMapping("/post")
    public String post() {
        String response = null;
        try {
            response = gatewayService.post(brokerUrl + "/post", "", String.class);
        } catch (CloudAppException e) {
            e.printStackTrace();
        }
        System.out.println("The simple post request response is: " + "\n" + response);
        return response;
    }

    @RequestMapping("/request")
    public ResponseEntity<String> request() {
        RequestEntity request = RequestEntity.post(brokerUrl + "/post").header("appName", env.getProperty("spring.application.name")).build();
        ResponseEntity<String> response = null;
        try {
            response = gatewayService.request(request, String.class);
            System.out.println("The complicated get request response is: " + "\n" + response);
        } catch (CloudAppException e) {
            e.printStackTrace();
        }
        return response;
    }



}
