package io.cloudapp.starter.base.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RefreshablePropertiesTest {
    
    @Mock
    private ApplicationContext context;
    
    private RefreshableProperties refreshableProperties;
    
    @BeforeEach
    void setUp() {
        refreshableProperties = new ThreadPoolProperties();
        
        refreshableProperties.setApplicationContext(context);
    }
    
    @Test
    void testSetRefreshable() {
        // Setup
        // Run the test
        refreshableProperties.setRefreshable("refreshable");
        
        // Verify the results
    }
    
    @Test
    void testGetRefreshable() {
        refreshableProperties.setRefreshable("false");
        assertFalse(refreshableProperties.getRefreshable());
    }
    
    @Test
    void testAfterConstruct() {
        // Setup
        refreshableProperties.setRefreshable("true");
        // Run the test
        refreshableProperties.afterConstruct();
        
        // Verify the results
        verify(context).publishEvent(any(ApplicationEvent.class));
    }
    
    @Test
    void testPublishEvent() {
        // Setup
        final RefreshableProperties properties = null;
        
        // Run the test
        refreshableProperties.publishEvent("prefix", properties);
        
        // Verify the results
        verify(context).publishEvent(
                any(ApplicationEvent.class));
    }
    
}
