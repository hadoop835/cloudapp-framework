package io.cloudapp.starter.mail.configuration;

import io.cloudapp.mail.ThymeleafTemplateEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.ICacheManager;
import org.thymeleaf.context.IEngineContextFactory;
import org.thymeleaf.templateparser.markup.decoupled.IDecoupledTemplateLogicResolver;

import java.util.HashSet;

import static org.mockito.Mockito.mock;

class ThymeleafMailAutoConfigurationTest {
    
    private ThymeleafMailAutoConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new ThymeleafMailAutoConfiguration();
    }
    
    @Test
    void testEmailService() {
        // Setup
        final JavaMailSender javaMailSender = null;
        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolvers(new HashSet<>());
        templateEngine.setCacheManager(mock(ICacheManager.class));
        templateEngine.setEngineContextFactory(mock(IEngineContextFactory.class));
        templateEngine.setDecoupledTemplateLogicResolver(mock(
                IDecoupledTemplateLogicResolver.class));
        templateEngine.setMessageResolvers(new HashSet<>());
        
        // Run the test
        final ThymeleafTemplateEmailService result = configuration.emailService(
                javaMailSender, templateEngine);
        
        // Verify the results
    }
    
}
