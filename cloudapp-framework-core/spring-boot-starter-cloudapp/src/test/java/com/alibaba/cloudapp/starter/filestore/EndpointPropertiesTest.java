package com.alibaba.cloudapp.starter.filestore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class EndpointPropertiesTest {
    
    private EndpointProperties properties;
    
    @Mock
    private ApplicationContext applicationContext;
    
    @BeforeEach
    void setUp() {
        properties = new EndpointProperties();
        properties.setApplicationContext(applicationContext);
    }
    
    @Test
    void testAccessKeyGetterAndSetter() {
        final String accessKey = "accessKey";
        properties.setAccessKey(accessKey);
        assertEquals(accessKey, properties.getAccessKey());
    }
    
    @Test
    void testSecretKeyGetterAndSetter() {
        final String secretKey = "secretKey";
        properties.setSecretKey(secretKey);
        assertEquals(secretKey, properties.getSecretKey());
    }
    
    @Test
    void testRegionGetterAndSetter() {
        final String region = "region";
        properties.setRegion(region);
        assertEquals(region, properties.getRegion());
    }
    
    @Test
    void testStsTokenGetterAndSetter() {
        final String stsToken = "stsToken";
        properties.setStsToken(stsToken);
        assertEquals(stsToken, properties.getStsToken());
    }
    
    @Test
    void testEndpointGetterAndSetter() {
        final String endpoint = "endpoint";
        properties.setEndpoint(endpoint);
        assertEquals(endpoint, properties.getEndpoint());
    }
    
    @Test
    void testEnabledGetterAndSetter() {
        final boolean enabled = false;
        properties.setEnabled(enabled);
        assertFalse(properties.isEnabled());
    }
    
}
