package com.alibaba.cloudapp.starter.base;

import com.alibaba.cloudapp.starter.base.properties.ThreadPoolProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshableComponentTest {
    
    @Mock
    private RefreshManager mockRefreshManager;
    @Mock
    private ApplicationContext context;
    private ThreadPoolProperties properties;
    
    private RefreshableComponent<ThreadPoolProperties, String> component;
    
    @BeforeEach
    void setUp() {
        ThreadPoolProperties properties = new ThreadPoolProperties();
        component = new RefreshableComponent<ThreadPoolProperties, String>(
                properties, "beanName"
        ) {
            
            @Override
            public void postStart() {
            
            }
            
            @Override
            public void preStop() {
            
            }
            
            @Override
            public String bindKey() {
                return null;
            }
            
            @Override
            public String getName() {
                return "test";
            }
            
            @Override
            protected String createBean(ThreadPoolProperties properties) {
                return "beanName";
            }
            
        };
        component.setApplicationContext(context);
        ReflectionTestUtils.setField(
                component, "refreshManager", mockRefreshManager
        );
    }
    
    @Test
    void testGetBean() {
        assertEquals("beanName", component.getBean());
    }
    
    @Test
    void testOnKeysChanged() {
        // Setup
        // Run the test
        component.onKeysChanged();
        
        // Verify the results
    }
    
    @Test
    void testRefresh() {
        // Setup
        // Run the test
        component.refresh(properties);
        
        // Verify the results
    }
    
    @Test
    void testAfterPropertiesSet() {
        // Setup
        // Run the test
        component.afterPropertiesSet();
        
        // Verify the results
        verify(mockRefreshManager).register(any(RefreshableComponent.class));
    }
    
    @Test
    void testAfterPropertiesSet_ApplicationContextThrowsBeansException()
            throws Exception {
        // Setup
        when(context.getBean(RefreshManager.class))
                .thenThrow(new BeanCreationException(""));
        
        Field field = component.getClass().getSuperclass().getDeclaredField(
                "refreshManager");
        field.setAccessible(true);
        field.set(component, null);
        // Run the test
        assertThrows(BeansException.class,
                     () -> component.afterPropertiesSet()
        );
    }
    
    @Test
    void testDestroy() {
        // Setup
        // Run the test
        component.destroy();
        
        // Verify the results
        verify(mockRefreshManager).unregister(any(RefreshableComponent.class));
    }
    
}
