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

package com.alibaba.cloudapp.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import com.alibaba.cloudapp.api.email.TemplateEmailService;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.model.MailUser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FreemarkerTemplateEmailService extends BaseEmailService
        implements TemplateEmailService, InitializingBean {
    
    private final FreeMarkerConfigurationFactory factory;
    private Configuration configuration;
    
    public FreemarkerTemplateEmailService(JavaMailSender javaMailSender,
                                          FreeMarkerConfigurationFactory factory) {
        super(javaMailSender);
        this.factory = factory;
    }
    
    /**
     * Send email with template
     * @param templateName The name of the template
     * @param subject The subject of email
     * @param from The account of sender
     * @param recipients The account of recipients
     * @param data template data
     * @throws CloudAppException if send email failed
     */
    @Override
    public void sendEmail(String templateName,
                          String subject,
                          String from,
                          List<String> recipients,
                          Map<String, Object> data) throws CloudAppException {
        String content = getTemplateContent(templateName, data);
        this.sendEmail(subject, content, from, recipients, true);
    }
    
    /**
     * Send email with template and attachment
     * @param templateName The name of the template
     * @param subject The subject of email
     * @param from The account of sender
     * @param recipients The account of recipients
     * @param data template data
     * @param attachment The path of attachment file
     * @throws CloudAppException if send email failed
     */
    @Override
    public void sendEmail(String templateName,
                          String subject,
                          String from,
                          List<String> recipients,
                          Map<String, Object> data,
                          Path attachment) throws CloudAppException {
        String content = getTemplateContent(templateName, data);
        this.sendEmail(subject, content, from, recipients, attachment, true);
    }
    
    /**
     * Send email with template and attachment
     * @param mailUser    The basic of email
     * @param templateName The name of the template
     * @param data         template data
     * @param attachment   The path of attachment file
     * @throws CloudAppException if send email failed
     */
    @Override
    public void sendEmail(MailUser mailUser,
                          String subject,
                          String templateName,
                          Map<String, Object> data,
                          Path attachment) throws CloudAppException {
        String content = getTemplateContent(templateName, data);
        this.sendEmail(mailUser, subject, content, attachment, true);
    }
    
    /**
     * Send email with template
     * @param mailUser    The user of email
     * @param subject     The subject of email
     * @param templateName The name of the template
     * @param data         template data
     * @throws CloudAppException if send email failed
     */
    @Override
    public void sendEmail(MailUser mailUser,
                          String subject,
                          String templateName,
                          Map<String, Object> data) throws CloudAppException {
        String content = getTemplateContent(templateName, data);
        this.sendEmail(mailUser, subject, content, true);
    }
    
    public String getTemplateContent(String templateName,
                                      Map<String, Object> data) {
        try {
            Template template = configuration.getTemplate(templateName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(
                    template, data);
        } catch (Exception e) {
            throw new CloudAppException("Failed to process template", e);
        }
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        configuration = factory.createConfiguration();
    }
    
}
