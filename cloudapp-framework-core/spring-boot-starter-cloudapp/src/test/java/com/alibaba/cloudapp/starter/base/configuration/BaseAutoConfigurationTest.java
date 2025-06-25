package com.alibaba.cloudapp.starter.base.configuration;

import com.alibaba.cloudapp.starter.base.RefreshManager;
import com.alibaba.cloudapp.starter.refresh.aspect.RefreshAspect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BaseAutoConfigurationTest {
    
    private BaseAutoConfiguration baseAutoConfigurationUnderTest;
    
    @BeforeEach
    void setUp() {
        baseAutoConfigurationUnderTest = new BaseAutoConfiguration();
    }
    
    @Test
    void testRefreshManager() {
        // Setup
        // Run the test
        final RefreshManager result = baseAutoConfigurationUnderTest.refreshManager();
        
        // Verify the results
    }
    
    @Test
    void testRefreshAspect() {
        // Setup
        // Run the test
        final RefreshAspect result = baseAutoConfigurationUnderTest.refreshAspect();
        
        // Verify the results
    }
    
}
