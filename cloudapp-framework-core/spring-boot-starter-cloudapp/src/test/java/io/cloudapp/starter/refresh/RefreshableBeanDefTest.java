package io.cloudapp.starter.refresh;

import io.cloudapp.starter.base.properties.RefreshableProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RefreshableBeanDefTest {
    
    @Mock
    private TargetRefreshable mockPoint;
    
    private RefreshableBeanDef beanDef;
    
    @BeforeEach
    void setUp() {
        beanDef = new RefreshableBeanDef(mockPoint);
    }
    
    @Test
    void testRefreshBean() {
        // Setup
        final RefreshableProperties properties = mock(RefreshableProperties.class);
        
        // Run the test
        beanDef.refreshBean(properties);
        
        // Verify the results
        verify(mockPoint).refreshTarget(any(RefreshableProperties.class));
    }
    
}
