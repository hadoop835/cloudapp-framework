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

package io.cloudapp.microservice.aliyun.demo;

import io.cloudapp.api.microservice.TrafficService;
import io.opentelemetry.context.Scope;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version = "1.0.0", group = "DUBBO")
public class DemoServiceImpl implements DemoService {

    private static final Logger logger = LoggerFactory.getLogger(io.cloudapp.microservice.aliyun.demo.DemoApplication.class);

    @Autowired
    private TrafficService trafficService;

    @Override
    public String tag(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        logger.info("traffic label before inject: {}", trafficService.getCurrentTrafficLabel());
        try (Scope ignored = trafficService.withTrafficLabel(name)){
            logger.info("traffic label after inject: {}", trafficService.getCurrentTrafficLabel());
        }
        return name;
    }
}
