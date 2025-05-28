package io.cloudapp.starter.filestore.properties;

import io.cloudapp.starter.base.properties.RefreshableProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class OSSEndpointPropertiesTest {
    
    private OSSEndpointProperties endpointProperties;
    @Mock
    private ApplicationContext applicationContext;
    
    @BeforeEach
    void setUp() {
        endpointProperties = new OSSEndpointProperties();
        endpointProperties.setApplicationContext(applicationContext);
    }
    
    @Test
    void testAccessKeyGetterAndSetter() {
        final String accessKey = "accessKey";
        endpointProperties.setAccessKey(accessKey);
        assertEquals(accessKey, endpointProperties.getAccessKey());
    }
    
    @Test
    void testSecretKeyGetterAndSetter() {
        final String secretKey = "secretKey";
        endpointProperties.setSecretKey(secretKey);
        assertEquals(secretKey, endpointProperties.getSecretKey());
    }
    
    @Test
    void testRegionGetterAndSetter() {
        final String region = "region";
        endpointProperties.setRegion(region);
        assertEquals(region, endpointProperties.getRegion());
    }
    
    @Test
    void testStsTokenGetterAndSetter() {
        final String stsToken = "stsToken";
        endpointProperties.setStsToken(stsToken);
        assertEquals(stsToken, endpointProperties.getStsToken());
    }
    
    @Test
    void testEndpointGetterAndSetter() {
        final String endpoint = "endpoint";
        endpointProperties.setEndpoint(endpoint);
        assertEquals(endpoint, endpointProperties.getEndpoint());
    }
    
    @Test
    void testEnabledGetterAndSetter() {
        final boolean enabled = false;
        endpointProperties.setEnabled(enabled);
        assertFalse(endpointProperties.isEnabled());
    }
    
    @Test
    void testSetRefreshable() {
        // Setup
        // Run the test
        endpointProperties.setRefreshable("refreshable");
        
        // Verify the results
    }
    
    @Test
    void testGetRefreshable() {
        endpointProperties.setRefreshable("false");
        assertFalse(endpointProperties.getRefreshable());
    }
    
    @Test
    void testAfterConstruct() {
        // Setup
        // Run the test
        endpointProperties.afterConstruct();
        
        // Verify the results
    }
    
    @Test
    void testSetApplicationContext() {
        endpointProperties.setApplicationContext( applicationContext);
    }
    
    @Test
    void testPublishEvent() {
        // Setup
        final RefreshableProperties properties = null;
        
        // Run the test
        endpointProperties.publishEvent("prefix", properties);
        
        // Verify the results
    }
    
}
