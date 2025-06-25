package com.alibaba.cloudapp.starter.base;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.support.GenericApplicationContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.context.annotation.AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME;

@RunWith(MockitoJUnitRunner.class)
public class CloudAppContextInitializedTest {
    
    private CloudAppContextInitialized cloudAppContextInitializedUnderTest;
    
    @Before
    public void setUp() {
        cloudAppContextInitializedUnderTest = new CloudAppContextInitialized();
    }
    
    @Test
    public void testRegisterBeanPostProcessor() {
        // Setup
        final GenericApplicationContext applicationContext = new GenericApplicationContext();
        
        // Run the test
        cloudAppContextInitializedUnderTest.registerBeanPostProcessor(
                applicationContext, "beanName", String.class);
        
        // Verify the results
    }
    
    @Test
    public void testOnApplicationEvent() {
        final GenericApplicationContext applicationContext = new GenericApplicationContext();
        // Setup
        final ApplicationContextInitializedEvent event =
                mock(ApplicationContextInitializedEvent.class);
        
        when(event.getApplicationContext()).thenReturn(applicationContext);
        
        cloudAppContextInitializedUnderTest.registerBeanPostProcessor(
                applicationContext, AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME, String.class);
        
        // Run the test
        cloudAppContextInitializedUnderTest.onApplicationEvent(event);
        
        // Verify the results
    }
    
}
