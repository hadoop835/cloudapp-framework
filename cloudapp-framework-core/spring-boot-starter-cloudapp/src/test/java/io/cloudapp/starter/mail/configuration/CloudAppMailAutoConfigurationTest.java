package io.cloudapp.starter.mail.configuration;

import io.cloudapp.mail.BaseEmailService;
import io.cloudapp.starter.mail.properties.CloudAppMailProperties;
import io.cloudapp.starter.mail.refresh.MailSenderRefreshableComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;

import static org.mockito.Mockito.mock;

class CloudAppMailAutoConfigurationTest {
    
    private CloudAppMailAutoConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new CloudAppMailAutoConfiguration();
    }
    
    @Test
    void testRefreshComponent() {
        // Setup
        final CloudAppMailProperties properties = new CloudAppMailProperties();
        properties.setHost("host");
        properties.setPort(0);
        properties.setUsername("username");
        properties.setPassword("password");
        properties.setProtocol("protocol");
        
        // Run the test
        final MailSenderRefreshableComponent result = configuration.refreshComponent(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testJavaMailSender() {
        // Setup
        final CloudAppMailProperties cloudAppMailProperties = new CloudAppMailProperties();
        cloudAppMailProperties.setHost("host");
        cloudAppMailProperties.setPort(0);
        cloudAppMailProperties.setUsername("username");
        cloudAppMailProperties.setPassword("password");
        cloudAppMailProperties.setProtocol("protocol");
        final MailSenderRefreshableComponent comp = new MailSenderRefreshableComponent(
                cloudAppMailProperties);
        
        // Run the test
        final JavaMailSender result = configuration.javaMailSender(
                comp);
        
        // Verify the results
    }
    
    @Test
    void testSession() {
        // Setup
        final CloudAppMailProperties cloudAppMailProperties = new CloudAppMailProperties();
        cloudAppMailProperties.setHost("host");
        cloudAppMailProperties.setPort(0);
        cloudAppMailProperties.setUsername("username");
        cloudAppMailProperties.setPassword("password");
        cloudAppMailProperties.setProtocol("protocol");
        final MailSenderRefreshableComponent component = new MailSenderRefreshableComponent(
                cloudAppMailProperties);
        
        // Run the test
        final Session result = configuration.session(
                component);
        
        // Verify the results
    }
    
    @Test
    void testEmailService() {
        // Setup
        final JavaMailSender javaMailSender = mock(JavaMailSender.class);
        
        // Run the test
        final BaseEmailService result = configuration.emailService(javaMailSender);
        
        // Verify the results
    }
    
}
