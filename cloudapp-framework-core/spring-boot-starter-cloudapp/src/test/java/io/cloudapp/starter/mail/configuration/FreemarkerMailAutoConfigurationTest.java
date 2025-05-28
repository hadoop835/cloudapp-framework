package io.cloudapp.starter.mail.configuration;

import io.cloudapp.mail.FreemarkerTemplateEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

class FreemarkerMailAutoConfigurationTest {
    
    private FreemarkerMailAutoConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new FreemarkerMailAutoConfiguration();
    }
    
    @Test
    void testEmailService() {
        // Setup
        final JavaMailSender javaMailSender = null;
        final FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
        
        // Run the test
        final FreemarkerTemplateEmailService result = configuration.emailService(
                javaMailSender, factory);
        
        // Verify the results
    }
    
}
