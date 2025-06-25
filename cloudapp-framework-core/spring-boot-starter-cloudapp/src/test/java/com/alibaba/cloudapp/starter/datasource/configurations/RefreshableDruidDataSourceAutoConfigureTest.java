package com.alibaba.cloudapp.starter.datasource.configurations;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import com.alibaba.cloudapp.starter.datasource.properties.RefreshableDruidProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RefreshableDruidDataSourceAutoConfigureTest {
    
    private RefreshableDruidDataSourceAutoConfigure sourceAutoConfigure;
    
    @BeforeEach
    void setUp() {
        sourceAutoConfigure = new RefreshableDruidDataSourceAutoConfigure();
    }
    
    @Test
    void testRefreshableDataSource() {
        // Setup
        final RefreshableDruidProperties properties = new RefreshableDruidProperties();
        properties.setManageVersion("manageVersion");
        properties.setMaxRefreshWaitSeconds(0);
        
        // Run the test
        final DruidDataSourceWrapper result = sourceAutoConfigure.refreshableDataSource(
                properties);
        
        // Verify the results
    }
    
}
