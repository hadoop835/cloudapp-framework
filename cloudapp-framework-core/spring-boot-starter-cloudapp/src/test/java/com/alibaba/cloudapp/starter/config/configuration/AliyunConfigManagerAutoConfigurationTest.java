package com.alibaba.cloudapp.starter.config.configuration;

import com.alibaba.cloudapp.config.aliyun.AliyunConfigManager;
import com.alibaba.cloudapp.starter.config.properties.AliyunConfigManagerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AliyunConfigManagerAutoConfigurationTest {
    
    private AliyunConfigManagerAutoConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new AliyunConfigManagerAutoConfiguration();
    }
    
    @Test
    void testAliyunConfigManager() {
        // Setup
        final AliyunConfigManagerProperties properties = new AliyunConfigManagerProperties();
        properties.setTimeout(0);
        properties.setGroup("defaultGroup");
        properties.setDomain("domain");
        properties.setRegionId("regionId");
        properties.setProtocol("protocol");
        properties.setAccessKey("accessKey");
        properties.setSecretKey("secretKey");
        properties.setNamespaceId("namespaceId");
        
        // Run the test
        final AliyunConfigManager result = configuration.aliyunConfigManager(
                properties);
        
        // Verify the results
    }
    
}
