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

package io.cloudapp.mail.demo.controller;

import io.cloudapp.mail.demo.service.MailDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    
    private static final String CONTENT = "Hello, this is a test mail!";
    
    @Autowired
    private MailDemoService mailService;
    
    @RequestMapping("/send")
    public void sendMail(
            String to,
            String subject
    ) {
        mailService.sendMail(to, subject, CONTENT);
    }
    
    @RequestMapping("/sendFile")
    public void sendMailFile(
            String to,
            String subject
    ) {
        mailService.sendMailFile(to, subject, CONTENT);
    }
}
