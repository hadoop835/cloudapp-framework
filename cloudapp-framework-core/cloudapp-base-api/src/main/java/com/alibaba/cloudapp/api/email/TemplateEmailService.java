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

package com.alibaba.cloudapp.api.email;

import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.model.MailUser;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Defines the interface for sending mail by template
 */
public interface TemplateEmailService extends EmailService {
    
    /**
     * Template emails are sent via SMTP or other mail gateways.
     * If the sending fails or the mailbox service connection fails, a
     * MessagingException will occur.
     *
     * @param templateName The name of the template
     * @param subject      The subject of email
     * @param from         The account of sender
     * @param recipients   The account of recipients
     * @param data         template data
     * @throws CloudAppException if sending fails
     */
    void sendEmail(String templateName,
                   String subject,
                   String from,
                   List<String> recipients,
                   Map<String, Object> data) throws CloudAppException;
    
    
    /**
     * Template emails are sent via SMTP or other mail gateways.
     * If the sending fails or the mailbox service connection fails, a
     * MessagingException will occur.
     *
     * @param templateName The name of the template
     * @param subject      The subject of email
     * @param from         The account of sender
     * @param recipients   The account of recipients
     * @param data         template data
     * @param attachment   The path of attachment file
     * @throws CloudAppException if sending fails
     */
    void sendEmail(String templateName,
                   String subject,
                   String from,
                   List<String> recipients,
                   Map<String, Object> data,
                   Path attachment
    ) throws CloudAppException;
    
    
    /**
     * Template emails are sent via SMTP or other mail gateways.
     * If the sending fails or the mailbox service connection fails, a
     * MessagingException will occur.
     *
     * @param mailUser    The user of email
     * @param subject     The subject of email
     * @param templateName The name of the template
     * @param data         template data
     * @param attachment   The path of attachment file
     * @throws CloudAppException if sending fails
     */
    void sendEmail(MailUser mailUser,
                   String subject,
                   String templateName,
                   Map<String, Object> data,
                   Path attachment
    ) throws CloudAppException;
    
    /**
     * Template emails are sent via SMTP or other mail gateways.
     * If the sending fails or the mailbox service connection fails, a
     * MessagingException will occur.
     *
     * @param mailUser    The user of email
     * @param subject     The subject of email
     * @param templateName The name of the template
     * @param data         template data
     * @throws CloudAppException if sending fails
     */
    void sendEmail(MailUser mailUser,
                   String subject,
                   String templateName,
                   Map<String, Object> data
    ) throws CloudAppException;
    
    /**
     * Get template content by template name and params
     *
     * @param templateName template name
     * @param params       template params
     * @return template content
     */
    String getTemplateContent(String templateName, Map<String, Object> params);
    
}
