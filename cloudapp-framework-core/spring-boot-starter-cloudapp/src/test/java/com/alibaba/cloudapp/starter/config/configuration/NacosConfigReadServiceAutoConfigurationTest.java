package com.alibaba.cloudapp.starter.config.configuration;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloudapp.config.aliyun.NacosConfigReadService;
import com.alibaba.cloudapp.starter.config.properties.NacosConfigReadServiceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NacosConfigReadServiceAutoConfigurationTest {
    
    private NacosConfigReadServiceAutoConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new NacosConfigReadServiceAutoConfiguration();
    }
    
    @Test
    void testNacosConfigReadService() {
        // Setup
        final NacosConfigReadServiceProperties properties = new NacosConfigReadServiceProperties();
        properties.setTimeout(0);
        properties.setGroup("defaultGroup");
        
        final NacosConfigProperties nacosConfigProperties = new NacosConfigProperties();
        nacosConfigProperties.setServerAddr("serverAddr");
        nacosConfigProperties.setUsername("username");
        nacosConfigProperties.setPassword("password");
        nacosConfigProperties.setPrefix("prefix");
        nacosConfigProperties.setFileExtension("fileExtension");
        final NacosConfigManager nacosConfigManager = new NacosConfigManager(
                nacosConfigProperties);
        
        // Run the test
        final NacosConfigReadService result = configuration.nacosConfigReadService(
                properties, nacosConfigManager);
        
        // Verify the results
        
        assert result != null;
    }
    
}
