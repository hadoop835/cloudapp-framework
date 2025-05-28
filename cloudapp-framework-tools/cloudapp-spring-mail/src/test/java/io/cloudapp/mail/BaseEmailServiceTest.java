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

import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.model.MailUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseEmailServiceTest {
    
    public static final String SUBJECT = "subject";
    public static final String CONTENT = "content";
    public static final String FROM = "from";
    public static final String FILE_NAME = "fileName.txt";
    
    @Mock
    private JavaMailSender mockJavaMailSender;
    
    private BaseEmailService baseEmailServiceUnderTest;
    
    @Before
    public void setUp() {
        baseEmailServiceUnderTest = new BaseEmailService(mockJavaMailSender);
    }
    
    @After
    public void tearDown() {
        // Add additional tear down code here
    }
    
    @Test
    public void testSendEmail1() {
        // Setup
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        // Run the test
        baseEmailServiceUnderTest.sendEmail(
                SUBJECT, CONTENT, FROM,
                Collections.singletonList("value"), false
        );
        
        // Verify the results
        verify(mockJavaMailSender).send(any(MimeMessage.class));
    }
    
    @Test
    public void testSendEmail1_JavaMailSenderSendThrowsMailException() {
        // Setup
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        MailException exception = new MailSendException("send exception");
        doThrow(exception).when(mockJavaMailSender).send(any(MimeMessage.class));
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> baseEmailServiceUnderTest.sendEmail(
                             SUBJECT, CONTENT, FROM,
                             Collections.singletonList("value"), false
                     )
        );
    }
    
    @Test
    public void testSendEmail2() throws IOException {
        // Setup
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        Path path = Paths.get(FILE_NAME);
        File file = path.toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("File not found");
        }
        
        // Run the test
        baseEmailServiceUnderTest.sendEmail(
                SUBJECT, CONTENT, FROM, Collections.singletonList("value"),
                path, false
        );
        
        // Verify the results
        verify(mockJavaMailSender).send(any(MimeMessage.class));
        
        file.deleteOnExit();
    }
    
    @Test
    public void testSendEmail2_JavaMailSenderSendThrowsMailException()
            throws IOException {
        // Setup
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        MailException exception = new MailSendException("send exception");
        doThrow(exception).when(mockJavaMailSender).send(any(MimeMessage.class));
        
        Path path = Paths.get(FILE_NAME);
        File file = path.toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("File not found");
        }
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> baseEmailServiceUnderTest.sendEmail(
                             SUBJECT, CONTENT, FROM,
                             Collections.singletonList("value"), path, false
                     )
        );
        
        file.deleteOnExit();
    }
    
    @Test
    public void testSendEmail3() {
        // Setup
        final MailUser mailUser = new MailUser();
        mailUser.setFrom(FROM);
        mailUser.setReplyTo("replyTo");
        mailUser.setTo("to");
        mailUser.setCc("cc");
        mailUser.setBcc("bcc");
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        // Run the test
        baseEmailServiceUnderTest.sendEmail(
                mailUser, SUBJECT, CONTENT, false
        );
        
        // Verify the results
        verify(mockJavaMailSender).send(any(MimeMessage.class));
    }
    
    @Test
    public void testSendEmail3_JavaMailSenderSendThrowsMailException() {
        // Setup
        final MailUser mailUser = new MailUser();
        mailUser.setFrom(FROM);
        mailUser.setReplyTo("replyTo");
        mailUser.setTo("to");
        mailUser.setCc("cc");
        mailUser.setBcc("bcc");
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        MailException exception = new MailSendException("send exception");
        doThrow(exception).when(mockJavaMailSender).send(any(MimeMessage.class));
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> baseEmailServiceUnderTest.sendEmail(
                             mailUser, SUBJECT, CONTENT, false
                     )
        );
    }
    
    @Test
    public void testSendEmail4() throws IOException {
        // Setup
        final MailUser mailUser = new MailUser();
        mailUser.setFrom(FROM);
        mailUser.setReplyTo("replyTo");
        mailUser.setTo("to");
        mailUser.setCc("cc");
        mailUser.setBcc("bcc");
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        Path path = Paths.get(FILE_NAME);
        File file = path.toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("File not found");
        }
        
        // Run the test
        baseEmailServiceUnderTest.sendEmail(
                mailUser, SUBJECT, CONTENT, path, false
        );
        
        // Verify the results
        verify(mockJavaMailSender).send(any(MimeMessage.class));
        
        file.deleteOnExit();
    }
    
    @Test
    public void testSendEmail4_JavaMailSenderSendThrowsMailException()
            throws IOException {
        // Setup
        final MailUser mailUser = new MailUser();
        mailUser.setFrom(FROM);
        mailUser.setReplyTo("replyTo");
        mailUser.setTo("to");
        mailUser.setCc("cc");
        mailUser.setBcc("bcc");
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        MailException exception = new MailSendException("send exception");
        doThrow(exception).when(mockJavaMailSender).send(any(MimeMessage.class));
        
        Path path = Paths.get(FILE_NAME);
        File file = path.toFile();
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("File not found");
        }
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> baseEmailServiceUnderTest.sendEmail(
                             mailUser, SUBJECT, CONTENT, path, false
                     )
        );
    }
    
}
