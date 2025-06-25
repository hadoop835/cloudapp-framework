package com.alibaba.cloudapp.starter.config.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AliyunConfigManagerPropertiesTest {
    
    private AliyunConfigManagerProperties managerProperties;
    
    @BeforeEach
    void setUp() {
        managerProperties = new AliyunConfigManagerProperties();
    }
    
    @Test
    void testTimeoutGetterAndSetter() {
        final int timeout = 0;
        managerProperties.setTimeout(timeout);
        assertEquals(timeout,
                     managerProperties.getTimeout()
        );
    }
    
    @Test
    void testGroupGetterAndSetter() {
        final String group = "group";
        managerProperties.setGroup(group);
        assertEquals(group, managerProperties.getGroup());
    }
    
    @Test
    void testDomainGetterAndSetter() {
        final String domain = "domain";
        managerProperties.setDomain(domain);
        assertEquals(domain,
                     managerProperties.getDomain()
        );
    }
    
    @Test
    void testRegionIdGetterAndSetter() {
        final String regionId = "regionId";
        managerProperties.setRegionId(regionId);
        assertEquals(regionId,
                     managerProperties.getRegionId()
        );
    }
    
    @Test
    void testProtocolGetterAndSetter() {
        final String protocol = "protocol";
        managerProperties.setProtocol(protocol);
        assertEquals(protocol,
                     managerProperties.getProtocol()
        );
    }
    
    @Test
    void testAccessKeyGetterAndSetter() {
        final String accessKey = "accessKey";
        managerProperties.setAccessKey(accessKey);
        assertEquals(accessKey,
                     managerProperties.getAccessKey()
        );
    }
    
    @Test
    void testSecretKeyGetterAndSetter() {
        final String secretKey = "secretKey";
        managerProperties.setSecretKey(secretKey);
        assertEquals(secretKey, managerProperties.getSecretKey());
    }
    
    @Test
    void testToString() {
        assert managerProperties.toString().startsWith(
                "AliyunConfigManagerProperties{");
    }
    
    @Test
    void testToProperties() {
        // Setup
        
        // Run the test
        final Properties result = managerProperties.toProperties();
        
        // Verify the results
        assert !result.isEmpty();
    }
    
    @Test
    void testValidate() {
        // Setup
        
        // Run the test
        assertThrows(IllegalArgumentException.class,
                     () -> managerProperties.validate());
        
        managerProperties.setDomain("domain");
        assertThrows(IllegalArgumentException.class,
                     () -> managerProperties.validate());
        
        managerProperties.setRegionId("regionId");
        assertThrows(IllegalArgumentException.class,
                     () -> managerProperties.validate());
        
        managerProperties.setAccessKey("accessKey");
        assertThrows(IllegalArgumentException.class,
                     () -> managerProperties.validate());
        
        managerProperties.setSecretKey("secretKey");
        managerProperties.validate();
        
        // Verify the results
    }
    
    @Test
    void testNamespaceIdGetterAndSetter() {
        final String namespaceId = "namespaceId";
        managerProperties.setNamespaceId(namespaceId);
        assertEquals(namespaceId, managerProperties.getNamespaceId());
    }
    
}
