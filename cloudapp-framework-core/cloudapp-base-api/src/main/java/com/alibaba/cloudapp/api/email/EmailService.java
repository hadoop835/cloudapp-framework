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

/**
 * Defines the interface for sending mail
 */
public interface EmailService {

    /**
     * Emails are sent via SMTP or other mail gateways.
     * If the sending fails or the mailbox service connection fails,
     * a MessagingException will occur.
     *
     * @param subject The subject of email
     * @param content The content of email
     * @param from Account to send mail from
     * @param recipients List of user mailboxes that receive mail
     * @param isHtml Whether the content is html or not
     * @throws CloudAppException if sending fails
     */
    void sendEmail(String subject,
                   String content,
                   String from,
                   List<String> recipients,
                   boolean isHtml)
            throws CloudAppException;
    
    /**
     * Emails with attachment are sent via SMTP or other mail gateways.
     * If the sending fails or the mailbox service connection fails,
     * a MessagingException will occur.
     *
     * @param subject The subject of email
     * @param content The content of email
     * @param from Account to send mail from
     * @param recipients List of user mailboxes that receive mail
     * @param attachment The path of attachment file
     * @param isHtml Whether the content is html or not
     * @throws CloudAppException if sending fails
     */
    void sendEmail(String subject,
                   String content,
                   String from,
                   List<String> recipients,
                   Path attachment,
                   boolean isHtml)
            throws CloudAppException;
    
    
    /**
     * Emails are sent via SMTP or other mail gateways.
     * If the sending fails or the mailbox service connection fails,
     * a MessagingException will occur.
     *
     * @param mailUser The user of email
     * @param subject The subject of email
     * @param content The content of email
     * @param isHtml Whether the content is html or not
     * @throws CloudAppException if sending fails
     */
    void sendEmail(MailUser mailUser,
                   String subject,
                   String content,
                   boolean isHtml)
            throws CloudAppException;
    
    /**
     * Emails with attachment are sent via SMTP or other mail gateways.
     * If the sending fails or the mailbox service connection fails,
     * a MessagingException will occur.
     *
     * @param mailUser The users of email
     * @param subject The subject of email
     * @param content The content of email
     * @param attachment The path of attachment file
     * @param isHtml Whether the content is html or not
     * @throws CloudAppException if sending fails
     */
    void sendEmail(MailUser mailUser,
                   String subject,
                   String content,
                   Path attachment,
                   boolean isHtml)
            throws CloudAppException;
    
}
