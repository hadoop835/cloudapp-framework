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

import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.model.MailUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ThymeleafTemplateEmailServiceTest {
    
    public static final String TEMPLATE_NAME = "templateName";
    public static final String SUBJECT = "subject";
    
    @Mock
    private JavaMailSender mockJavaMailSender;
    
    private ThymeleafTemplateEmailService emailService;
    
    @Before
    public void setUp() {
        TemplateEngine engine = new TemplateEngine();
        engine.setDialect(new SpringStandardDialect());
        
        emailService = new ThymeleafTemplateEmailService(
                mockJavaMailSender, engine
        );
        
    }
    
    
    @Test
    public void testSendEmail1() {
        // Setup
        final Map<String, Object> data = new HashMap<>();
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        // Run the test
        emailService.sendEmail(
                TEMPLATE_NAME, SUBJECT, "from",
                Collections.singletonList("value"), data
        );
        
        // Verify the results
        verify(mockJavaMailSender).send(any(MimeMessage.class));
    }
    
    @Test
    public void testSendEmail1_JavaMailSenderSendThrowsMailException() {
        // Setup
        final Map<String, Object> data = new HashMap<>();
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        MailException ex = new MailSendException("message");
        doThrow(ex).when(mockJavaMailSender).send(any(MimeMessage.class));
        
        // Run the test
        assertThrows(CloudAppException.class, () -> emailService.sendEmail(
                TEMPLATE_NAME, SUBJECT, "from",
                Collections.singletonList("value"), data
        ));
    }
    
    @Test
    public void testSendEmail2() throws IOException {
        // Setup
        final Map<String, Object> data = new HashMap<>();
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        Path path = Paths.get("filename.txt");
        File file = path.toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("File not found");
        }
        
        // Run the test
        emailService.sendEmail(
                TEMPLATE_NAME, SUBJECT, "from",
                Collections.singletonList("value"), data, path
        );
        
        // Verify the results
        verify(mockJavaMailSender).send(any(MimeMessage.class));
        
        file.deleteOnExit();
    }
    
    @Test
    public void testSendEmail2_JavaMailSenderSendThrowsMailException()
            throws IOException {
        // Setup
        final Map<String, Object> data = new HashMap<>();
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        MailException ex = new MailSendException("message");
        doThrow(ex).when(mockJavaMailSender).send(
                any(MimeMessage.class));
        
        Path path = Paths.get("filename.txt");
        File file = path.toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("File not found");
        }
        
        // Run the test
        assertThrows(CloudAppException.class, () -> emailService.sendEmail(
                TEMPLATE_NAME, SUBJECT, "from",
                Collections.singletonList("value"), data, path
        ));
        
        file.deleteOnExit();
    }
    
    @Test
    public void testSendEmail3() throws IOException {
        // Setup
        final MailUser mailUser = new MailUser();
        mailUser.setFrom("from");
        mailUser.setReplyTo("replyTo");
        mailUser.setTo("to");
        mailUser.setCc("cc");
        mailUser.setBcc("bcc");
        
        final Map<String, Object> data = new HashMap<>();
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        Path path = Paths.get("filename.txt");
        File file = path.toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("File not found");
        }
        
        // Run the test
        emailService.sendEmail(
                mailUser, SUBJECT, TEMPLATE_NAME, data, path
        );
        
        // Verify the results
        verify(mockJavaMailSender).send(any(MimeMessage.class));
        
        file.deleteOnExit();
    }
    
    @Test
    public void testSendEmail3_JavaMailSenderSendThrowsMailException()
            throws IOException {
        // Setup
        final MailUser mailUser = new MailUser();
        mailUser.setFrom("from");
        mailUser.setReplyTo("replyTo");
        mailUser.setTo("to");
        mailUser.setCc("cc");
        mailUser.setBcc("bcc");
        
        final Map<String, Object> data = new HashMap<>();
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        MailException ex = new MailSendException("message");
        doThrow(ex).when(mockJavaMailSender).send(
                any(MimeMessage.class));
        
        Path path = Paths.get("filename.txt");
        File file = path.toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("File not found");
        }
        
        // Run the test
        assertThrows(CloudAppException.class, () -> emailService.sendEmail(
                mailUser, SUBJECT, TEMPLATE_NAME, data, path
        ));
        
        file.deleteOnExit();
    }
    
    @Test
    public void testSendEmail4() {
        // Setup
        final MailUser mailUser = new MailUser();
        mailUser.setFrom("from");
        mailUser.setReplyTo("replyTo");
        mailUser.setTo("to");
        mailUser.setCc("cc");
        mailUser.setBcc("bcc");
        
        final Map<String, Object> data = new HashMap<>();
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        // Run the test
        emailService.sendEmail(mailUser, SUBJECT, TEMPLATE_NAME, data);
        
        // Verify the results
        verify(mockJavaMailSender).send(any(MimeMessage.class));
    }
    
    @Test
    public void testSendEmail4_JavaMailSenderSendThrowsMailException() {
        // Setup
        final MailUser mailUser = new MailUser();
        mailUser.setFrom("from");
        mailUser.setReplyTo("replyTo");
        mailUser.setTo("to");
        mailUser.setCc("cc");
        mailUser.setBcc("bcc");
        
        final Map<String, Object> data = new HashMap<>();
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        MailException ex = new MailSendException("message");
        doThrow(ex).when(mockJavaMailSender).send(
                any(MimeMessage.class));
        
        // Run the test
        assertThrows(CloudAppException.class, () -> emailService.sendEmail(
                mailUser, SUBJECT, TEMPLATE_NAME, data
        ));
    }
    
    @Test
    public void testGetTemplateContent() {
        // Setup
        final Map<String, Object> data = new HashMap<>();
        
        // Run the test
        final String result = emailService.getTemplateContent(
                TEMPLATE_NAME, data
        );
        
        // Verify the results
        assertEquals(TEMPLATE_NAME, result);
    }
    
}
