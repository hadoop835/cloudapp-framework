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

package com.alibaba.cloudapp.mail.test;

import com.alibaba.cloudapp.api.email.TemplateEmailService;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.model.MailUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerTemplateEmailServiceSit {

    private static final Logger logger = LoggerFactory.getLogger(
            FreemarkerTemplateEmailServiceSit.class);
    private static final String TEMPLATE_NAME = "test.ftl";
    @Autowired
    @Qualifier("freemarkerTemplateEmailService")
    TemplateEmailService templateEmailService;
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Test
    public void sendEmail_template_subject_from_recipients_data()
            throws CloudAppException {
        String subject = "test subject";
        String from = "test@gmail.com";
        List<String> recipients = new ArrayList<>();
        recipients.add("test@gmail.com");
        Map<String, Object> data = new HashMap<>();
        data.put("name", recipients.get(0));
        data.put("showData", Collections.singletonMap("data", "test " +
                "data. current time : " + new Date().toString()));
        
        try {
            templateEmailService.sendEmail(TEMPLATE_NAME, subject, from,
                                           recipients, data
            );
            logger.info("send mail success.");
        } catch (CloudAppException e) {
            logger.error("send mail fail.", e);
        }
    }
    
    @Test
    public void sendEmail_template_subject_from_recipients_data_attachment()
            throws CloudAppException {
        String subject = "test subject";
        String from = "test@gmail.com";
        List<String> recipients = new ArrayList<>();
        recipients.add("test@gmail.com");
        Map<String, Object> data = new HashMap<>();
        data.put("name", recipients.get(0));
        data.put("showData", Collections.singletonMap("data", "test " +
                "data. current time : " + new Date().toString()));
        Path attachment = null;
        try {
            attachment = readFileFromResources("README.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            templateEmailService.sendEmail(TEMPLATE_NAME, subject, from,
                                           recipients, data, attachment
            );
            logger.info("send mail success.");
        } catch (CloudAppException e) {
            logger.error("send mail fail.", e);
        }
    }
    
    @Test
    public void sendEmail_MailUser_subject_templateName_data_attachment()
            throws CloudAppException {
        MailUser mailUser = new MailUser();
        mailUser.setTo("test@gmail.com");
        mailUser.setFrom("test@gmail.com");
        String subject = "test subject";
        Map<String, Object> data = new HashMap<>();
        data.put("name", mailUser.getTo()[0]);
        data.put("showData", Collections.singletonMap("data", "test " +
                "data. current time : " + new Date().toString()));
        Path attachment = null;
        try {
            attachment = readFileFromResources("README.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            
            templateEmailService.sendEmail(mailUser, subject, TEMPLATE_NAME,
                                           data, attachment
            );
            logger.info("send mail success.");
        } catch (CloudAppException e) {
            logger.error("send mail fail.", e);
        }
    }
    
    @Test
    public void sendEmail_MailUser_subject_templateName_data()
            throws CloudAppException {
        MailUser mailUser = new MailUser();
        mailUser.setTo("test@gmail.com");
        mailUser.setFrom("test@gmail.com");
        String subject = "test subject";
        Map<String, Object> data = new HashMap<>();
        data.put("name", mailUser.getTo()[0]);
        data.put("showData", Collections.singletonMap("data", "test " +
                "data. current time : " + new Date().toString()));
        try {
            templateEmailService.sendEmail(mailUser, subject, TEMPLATE_NAME,
                                           data
            );
            logger.info("send mail success.");
        } catch (CloudAppException e) {
            logger.error("send mail fail.", e);
        }
    }
    
    public Path readFileFromResources(String fileName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + fileName);
        return resource.getFile().toPath();
    }
    
}
