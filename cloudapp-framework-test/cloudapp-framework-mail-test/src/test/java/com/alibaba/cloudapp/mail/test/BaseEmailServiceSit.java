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

import com.alibaba.cloudapp.api.email.EmailService;
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
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseEmailServiceSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            BaseEmailServiceSit.class);
    private static final String TEMPLATE_NAME = "templates/test.ftl";
    
    @Autowired
    @Qualifier("freemarkerTemplateEmailService")
    EmailService emailService;
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Test
    public void sendEmail_subject_content_from_recipients_isHtml() {
        String subject = "test-subject-1";
        String content = "test content";
        String from = "test@gmail.com";
        List<String> recipients = new ArrayList<>();
        recipients.add("test@gmail.com");
        boolean isHtml = true;
        
        try {
            emailService.sendEmail(subject, content, from, recipients, isHtml);
        } catch (CloudAppException e) {
            logger.error("send mail fail.", e);
        }
        
    }
    
    public Path readFileFromResources(String fileName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + fileName);
        return resource.getFile().toPath();
    }
    
    @Test
    public void sendEmail_subject_content_from_recipients_attachment_isHtml()
            throws CloudAppException, IOException {
        String subject = "test-subject-2";
        String content = "test content";
        String from = "test@gmail.com";
        List<String> recipients = new ArrayList<>();
        recipients.add("test@gmail.com");
        Path attachment = readFileFromResources("README.txt");
        
        boolean isHtml = true;
        
        try {
            emailService.sendEmail(subject, content, from, recipients,
                                   attachment, isHtml
            );
        } catch (Exception e) {
            logger.error("send mail fail.", e);
        }
    }
    
    @Test
    public void sendEmail_MailUser_subject_content_isHtml()
            throws CloudAppException {
        MailUser mailUser = new MailUser();
        mailUser.setFrom("test@gmail.com");
        mailUser.setTo("test@gmail.com");
        String subject = "test-subject-3";
//        String content = "test content";
//        boolean isHtml = true;
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>My First Heading</h1>\n" +
                "<p>My first paragraph.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        boolean isHtml = true;
        try {
            emailService.sendEmail(mailUser, subject, content, isHtml);
        } catch (Exception e) {
            logger.error("send mail fail.", e);
        }
    }
    
    @Test
    public void sendEmail_MailUser_subject_content_attachment_isHtml()
            throws CloudAppException, IOException {
        MailUser mailUser = new MailUser();
        mailUser.setFrom("test@gmail.com");
        mailUser.setTo("test@gmail.com");
        String subject = "test-subject-4";
        String content = "test content";
        Path attachment = readFileFromResources("README.txt");
        boolean isHtml = true;
        try {
            emailService.sendEmail(mailUser, subject, content, attachment,
                                   isHtml
            );
        } catch (Exception e) {
            logger.error("send mail fail.", e);
        }
    }
    
}
