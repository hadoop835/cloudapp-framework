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

package io.cloudapp.mail;

import io.cloudapp.api.email.EmailService;
import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.model.MailUser;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class BaseEmailService implements EmailService {
    
    private final JavaMailSender mailSender;
    
    public BaseEmailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }
    
    /**
     * Send email to users
     *
     * @param subject    The subject of email
     * @param content    The content of email
     * @param from       Account to send mail from
     * @param recipients List of user mailboxes that receive mail
     * @throws CloudAppException if send email failed
     */
    @Override
    public void sendEmail(String subject,
                          String content,
                          String from,
                          List<String> recipients,
                          boolean isHtml
    ) throws CloudAppException {
        try {
            MailUser mailUser = new MailUser()
                    .setTo(recipients.toArray(new String[0]))
                    .setFrom(from);
            
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            
            initMailBasic(helper, mailUser, subject, content, isHtml);
            
            mailSender.send(msg);
        } catch (Exception e) {
            String errMsg = String.format("Failed to send email to %s",
                                          recipients
            );
            throw new CloudAppException(errMsg, "CloudApp.EmailSendFailed", e);
        }
    }
    
    @Override
    public void sendEmail(String subject,
                          String content,
                          String from,
                          List<String> recipients,
                          Path attachment,
                          boolean isHtml) throws CloudAppException {
        if (attachment == null) {
            this.sendEmail(subject, content, from, recipients, isHtml);
            return;
        }
        try {
            MailUser mailUser = new MailUser()
                    .setTo(recipients.toArray(new String[0]))
                    .setFrom(from);
            
            MimeMessage msg = mailSender.createMimeMessage();
            
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            
            initMailBasic(helper, mailUser, subject, content, isHtml);
            
            addAttachment(attachment, helper);
            
            mailSender.send(msg);
        } catch (Exception e) {
            String errMsg = String.format(
                    "Failed to send email to %s",
                    StringUtils.arrayToCommaDelimitedString(
                            recipients.toArray())
            );
            throw new CloudAppException(errMsg, "CloudApp.EmailSendFailed", e);
        }
    }
    
    @Override
    public void sendEmail(MailUser mailUser, String subject, String content,
                          boolean isHtml) throws CloudAppException {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            
            initMailBasic(helper, mailUser, subject, content, isHtml);
            
            mailSender.send(msg);
        } catch (Exception e) {
            String errMsg = String.format(
                    "Failed to send email to %s",
                    StringUtils.arrayToCommaDelimitedString(mailUser.getTo())
            );
            throw new CloudAppException(errMsg, "CloudApp.EmailSendFailed", e);
        }
    }
    
    
    @Override
    public void sendEmail(MailUser mailUser,
                          String subject,
                          String content,
                          Path attachment,
                          boolean isHtml) throws CloudAppException {
        if (attachment == null) {
            this.sendEmail(mailUser, subject, content, isHtml);
            return;
        }
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            
            initMailBasic(helper, mailUser, subject, content, isHtml);
            
            addAttachment(attachment, helper);
            
            mailSender.send(msg);
        } catch (Exception e) {
            String errMsg = String.format(
                    "Failed to send email to %s",
                    StringUtils.arrayToCommaDelimitedString(mailUser.getTo())
            );
            throw new CloudAppException(errMsg, "CloudApp.EmailSendFailed", e);
        }
    }
    
    
    /**
     * add attachment to email
     *
     * @param attachment file path
     * @param helper     MimeMessageHelper
     * @throws MessagingException if add attachment failed
     * @throws IOException        read file failed
     */
    private static void addAttachment(Path attachment, MimeMessageHelper helper)
            throws MessagingException, IOException {
        if (!Files.exists(attachment)) {
            throw new CloudAppException("attachment is not exists");
        }
        if (Files.isDirectory(attachment)) {
            try (Stream<Path> stream = Files
                    .list(attachment)
                    .filter(Files::isRegularFile)
                    .filter(Files::exists)) {
                stream.forEach(path -> {
                    try {
                        helper.addAttachment(
                                path.getFileName().toString(),
                                path.toFile()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            
        } else if (Files.isRegularFile(attachment)) {
            helper.addAttachment(attachment.getFileName().toString(),
                                 attachment.toFile()
            );
        } else {
            throw new CloudAppException("attachment is not regular file");
        }
    }
    
    /**
     * add mail basic info to email
     *
     * @param helper   MimeMessageHelper
     * @param mailUser mail basic info
     * @param content  email content
     * @param isHtml   whether content is html
     * @throws MessagingException if add mail basic info failed
     */
    private void initMailBasic(MimeMessageHelper helper, MailUser mailUser,
                               String subject, String content, boolean isHtml)
            throws MessagingException {
        
        if (mailUser == null) {
            return;
        }
        helper.setFrom(mailUser.getFrom());
        helper.setTo(mailUser.getTo());
        
        if (mailUser.getCc() != null) {
            helper.setCc(mailUser.getCc());
        }
        if (mailUser.getBcc() != null) {
            helper.setBcc(mailUser.getBcc());
        }
        if (mailUser.getReplyTo() != null) {
            helper.setReplyTo(mailUser.getReplyTo());
        }
        helper.setSubject(subject);
        helper.setSentDate(new Date());
        helper.setText(content, isHtml);
    }
    
}
