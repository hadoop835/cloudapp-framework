/**
 * Copyright (c) 2022-present Alibaba Group
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.alibaba.cloudapp.microservice.aliyun.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class DemoController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EchoService echoService;
    
    @RequestMapping(value = "/address")
    public String address() {
        return "Consumer -> " + echoService.address();
    }
    
}
