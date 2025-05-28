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

import freemarker.core.UndefinedOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import io.cloudapp.exeption.CloudAppException;
import io.cloudapp.model.MailUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FreemarkerTemplateEmailServiceTest {
    
    @Mock
    private JavaMailSender mockJavaMailSender;
    @Mock
    private FreeMarkerConfigurationFactory mockFactory;
    
    private FreemarkerTemplateEmailService emailService;
    @Mock
    private Configuration configuration;
    @Mock
    private Template template;
    
    @Before
    public void setUp() throws Exception {
        emailService = new FreemarkerTemplateEmailService(
                mockJavaMailSender, mockFactory);
        
        when(mockFactory.createConfiguration()).thenReturn(configuration);
        
        emailService.afterPropertiesSet();
        
        
        when(configuration.getTemplate(any(String.class))).thenReturn(template);
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
                "templateName", "subject", "from",
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
        
        MailException mailException = new MailSendException("message");
        doThrow(mailException).when(mockJavaMailSender).send(
                any(MimeMessage.class));
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> emailService.sendEmail(
                             "templateName", "subject", "from",
                             Collections.singletonList("value"), data
                     )
        );
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
        
        File file = new File(path.toString());
        
        if(!file.exists() && !file.createNewFile()) {
            throw new IOException("File not created");
        }
        
        // Run the test
        emailService.sendEmail(
                "templateName", "subject", "from",
                Collections.singletonList("value"), data, path
        );
        
        // Verify the results
        verify(mockJavaMailSender).send(any(MimeMessage.class));
        
        file.deleteOnExit();
    }
    
    @Test
    public void testSendEmail2_JavaMailSenderSendThrowsMailException() {
        // Setup
        final Map<String, Object> data = new HashMap<>();
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        MailException mailException = new MailSendException("message");
        doThrow(mailException).when(mockJavaMailSender).send(
                any(MimeMessage.class));
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> emailService.sendEmail(
                             "templateName", "subject", "from",
                             Collections.singletonList("value"), data,
                             Paths.get("filename.txt")
                     )
        );
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
        
        Path path = Paths.get("filename.txt");
        
        File file = new File(path.toString());
        
        if(!file.exists() && !file.createNewFile()) {
            throw new IOException("File not created");
        }
        
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        // Run the test
        emailService.sendEmail(
                mailUser, "subject", "templateName", data,
                path
        );
        
        // Verify the results
        verify(mockJavaMailSender).send(any(MimeMessage.class));
        
        file.deleteOnExit();
    }
    
    @Test
    public void testSendEmail3_JavaMailSenderSendThrowsMailException() {
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
        
        MailException mailException = new MailSendException("message");
        doThrow(mailException).when(mockJavaMailSender).send(
                any(MimeMessage.class));
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> emailService.sendEmail(
                             mailUser, "subject", "templateName", data,
                             Paths.get("filename.txt")
                     )
        );
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
        emailService.sendEmail(mailUser, "subject",
                               "templateName", data
        );
        
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
        
        MailException mailException = new MailSendException("message");
        doThrow(mailException).when(mockJavaMailSender).send(
                any(MimeMessage.class));
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> emailService.sendEmail(
                             mailUser, "subject", "templateName", data)
        );
    }
    
    @Test
    public void testGetTemplateContent() throws TemplateException, IOException {
        // Setup
        final Map<String, Object> data = new HashMap<>();
        
        Answer<Object> answer = invocation -> {
            Object[] objects = invocation.getArguments();
            StringWriter writer = (StringWriter) objects[1];
            writer.write("result");
            return objects[0];
        };
        
        doAnswer(answer).when(template).process(
                anyMap(), any(StringWriter.class));
        
        // Run the test
        final String result = emailService.getTemplateContent(
                "templateName", data);
        
        // Verify the results
        assertEquals("result", result);
    }
    
    @Test
    public void testAfterPropertiesSet() throws Exception {
        // Setup
        int major = 2;
        int minor = 3;
        int micro = 4;
        String extraInfo = "extraInfo";
        boolean gaeCompatible = false;
        GregorianCalendar gc = new GregorianCalendar(
                2020, Calendar.JANUARY, 1
        );
        Version version = new Version(major, minor, micro, extraInfo,
                                      gaeCompatible, gc.getTime());
        
        // Configure FreeMarkerConfigurationFactory.createConfiguration(...).
        final Configuration configuration = new Configuration(version);
        configuration.setIncompatibleImprovements(version);
        configuration.setWhitespaceStripping(false);
        configuration.setAutoEscapingPolicy(
                Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY
        );
        configuration.setOutputFormat(UndefinedOutputFormat.INSTANCE);
        configuration.setRegisteredCustomOutputFormats(Collections.emptyList());
        when(mockFactory.createConfiguration()).thenReturn(configuration);
        
        // Run the test
        emailService.afterPropertiesSet();
        
        // Verify the results
    }
    
    @Test
    public void testAfterPropertiesSet_FreeMarkerConfigurationFactoryThrowsIOException()
            throws Exception {
        // Setup
        when(mockFactory.createConfiguration()).thenThrow(IOException.class);
        
        // Run the test
        assertThrows(IOException.class,
                     () -> emailService.afterPropertiesSet()
        );
    }
    
    @Test
    public void testAfterPropertiesSet_FreeMarkerConfigurationFactoryThrowsTemplateException()
            throws Exception {
        // Setup
        when(mockFactory.createConfiguration())
                .thenThrow(TemplateException.class);
        
        // Run the test
        assertThrows(TemplateException.class,
                     () -> emailService.afterPropertiesSet()
        );
    }
    
}
