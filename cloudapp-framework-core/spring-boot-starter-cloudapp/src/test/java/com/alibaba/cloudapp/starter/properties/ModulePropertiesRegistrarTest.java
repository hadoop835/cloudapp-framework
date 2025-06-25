package com.alibaba.cloudapp.starter.properties;

import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.mock.env.MockEnvironment;

class ModulePropertiesRegistrarTest {
    
    private ModulePropertiesRegistrar propertiesRegistrar;
    
    @BeforeEach
    void setUp() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("test.name", "test");
        
        propertiesRegistrar = new ModulePropertiesRegistrar();
        propertiesRegistrar.setEnvironment(env);
    }
    
    @Test
    void testRegisterBeanDefinitions() {
        // Setup
        final AnnotationMetadata metadata = AnnotationMetadata.introspect(
                TestClass.class);
        final BeanDefinitionRegistry registry = new GenericApplicationContext();
        
        // Run the test
        propertiesRegistrar.registerBeanDefinitions(metadata, registry);
        
        // Verify the results
        BeanDefinition result = registry.getBeanDefinition(TestClass.class.getName());
    }
    
    @Data
    @EnableModuleProperties(TestClass.class)
    @ConfigurationProperties(prefix = "test")
    public static class TestClass {
        
        private String name;
        
    }
    
}
