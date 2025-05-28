package io.cloudapp.api.scheduler.worker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractGlobalJobScannerTest {
    
    @Mock
    private ApplicationContext mockContext;
    
    private AbstractGlobalJobScanner jobScanner;
    
    @Before
    public void setUp() throws Exception {
        jobScanner = new AbstractGlobalJobScanner() {
        
        };
        
        Field field = jobScanner.getClass().getSuperclass()
                                .getDeclaredField("context");
        field.setAccessible(true);
        field.set(jobScanner, mockContext);
    }
    
    @Test
    public void testGetGlobalJobs() {
        // Setup
        
        // Run the test
        final List<GlobalJobMetadata> result = AbstractGlobalJobScanner.getGlobalJobs();
        
        // Verify the results
    }
    
    @Test
    public void testAfterSingletonsInstantiated() {
        // Setup
        when(mockContext.getBeanNamesForType(
                Object.class, false, true
        )).thenReturn(new String[]{"beanName"});
        
        when(mockContext.findAnnotationOnBean(
                "beanName", Lazy.class
        )).thenReturn(null);
        
        when(mockContext.getBean("beanName")).thenReturn(new Object());
        
        // Run the test
        jobScanner.afterSingletonsInstantiated();
        
        // Verify the results
    }
    
    @Test
    public void testAfterSingletonsInstantiated_NoItems() {
        // Setup
        when(mockContext.getBeanNamesForType(
                Object.class, false,true
        )).thenReturn(new String[]{"beanName"});
        
        when(mockContext.getBean(anyString())).thenReturn(new Object());
        
        // Run the test
        jobScanner.afterSingletonsInstantiated();
        
        // Verify the results
    }
    
    @Test
    public void testAfterSingletonsInstantiated_BeanReturnsNull() {
        // Setup
        when(mockContext.getBeanNamesForType(
                Object.class, false,true
        )).thenReturn(new String[]{"beanName"});
        
        when(mockContext.findAnnotationOnBean(
                "beanName", Lazy.class
        )).thenReturn(null);
        when(mockContext.getBean("beanName")).thenReturn("result");
        
        // Run the test
        jobScanner.afterSingletonsInstantiated();
        
        // Verify the results
    }
    
    @Test
    public void testAfterSingletonsInstantiated_Exception() {
        // Setup
        when(mockContext.getBeanNamesForType(
                Object.class, false, true
        )).thenReturn(new String[]{"beanName"});
        
        when(mockContext.findAnnotationOnBean(
                "beanName", Lazy.class
        )).thenThrow(NoSuchBeanDefinitionException.class);
        
        // Run the test
        assertThrows(NoSuchBeanDefinitionException.class,
                     () -> jobScanner.afterSingletonsInstantiated()
        );
    }
    
    @Test
    public void testAfterSingletonsInstantiated_BeansException() {
        // Setup
        when(mockContext.getBeanNamesForType(
                Object.class, false,true
        )).thenReturn(new String[]{"beanName"});
        
        when(mockContext.findAnnotationOnBean(
                "beanName",Lazy.class
        )).thenReturn(null);
        
        when(mockContext.getBean("beanName"))
                .thenThrow(new BeanCreationException(""));
        
        // Run the test
        assertThrows(BeansException.class,
                     () -> jobScanner.afterSingletonsInstantiated()
        );
    }
    
}
