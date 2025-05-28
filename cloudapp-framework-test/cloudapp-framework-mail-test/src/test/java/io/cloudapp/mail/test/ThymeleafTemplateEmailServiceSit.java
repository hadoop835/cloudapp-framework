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

package io.cloudapp.mail.test;

import io.cloudapp.api.email.TemplateEmailService;
import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.model.MailUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ThymeleafTemplateEmailServiceSit {

    private static final Logger logger = LoggerFactory.getLogger(
            ThymeleafTemplateEmailServiceSit.class);
    private static final String TEMPLATE_NAME = "test.html";
    @Autowired
    @Qualifier("thymeleafTemplateEmailService")
    TemplateEmailService templateEmailService;
    
    @Test
    public void sendEmail_template_subject_from_recipients_data()
            throws CloudAppException {
        String subject = "test subject";
        String from = "test@gmail.com";
        List<String> recipients = new ArrayList<>();
        recipients.add("test@gmail.com");
        Map<String, Object> data = new HashMap<>();
        data.put("name", recipients.get(0));
        try {
            templateEmailService.sendEmail(TEMPLATE_NAME, subject, from,
                                           recipients, data
            );
        } catch (CloudAppException e) {
            logger.error("send mail fail.", e);
        }
    }
    
    @Test
    public void sendEmail_template_subject_from_recipients_data_attachment()
            throws CloudAppException {
        String templateName = "test";
        String subject = "test subject";
        String from = "test@gmail.com";
        List<String> recipients = new ArrayList<>();
        recipients.add("test@gmail.com");
        Map<String, Object> data = new HashMap<>();
        Path attachment = null;
        
        try {
            templateEmailService.sendEmail(templateName, subject, from,
                                           recipients, data
            );
        } catch (CloudAppException e) {
            logger.error("send mail fail.", e);
        }
    }
    
    @Test
    public void sendEmail_MailUser_subject_templateName_data_attachment()
            throws CloudAppException {
        MailUser mailUser = new MailUser();
        mailUser.setTo("test@gmail.com");
        String subject = "test subject";
        String templateName = "test";
        Map<String, Object> data = new HashMap<>();
        data.put("name", "test");
        Path attachment = null;
        
        try {
            templateEmailService.sendEmail(mailUser, subject, templateName,
                                           data, attachment
            );
        } catch (CloudAppException e) {
            logger.error("send mail fail.", e);
        }
        
    }
    
    @Test
    public void sendEmail_MailUser_subject_templateName_data()
            throws CloudAppException {
        MailUser mailUser = new MailUser();
        mailUser.setTo("test@gmail.com");
        String subject = "test subject";
        String templateName = "test";
        Map<String, Object> data = new HashMap<>();
        data.put("name", "test");
        try {
            templateEmailService.sendEmail(mailUser, subject, templateName,
                                           data
            );
        } catch (CloudAppException e) {
            logger.error("send mail fail.", e);
        }
    }
    
}
