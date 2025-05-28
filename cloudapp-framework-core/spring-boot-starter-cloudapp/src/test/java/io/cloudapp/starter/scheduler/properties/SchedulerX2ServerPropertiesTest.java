package io.cloudapp.starter.scheduler.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SchedulerX2ServerPropertiesTest {
    
    private SchedulerX2ServerProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new SchedulerX2ServerProperties();
    }
    
    @Test
    void testEnabledGetterAndSetter() {
        final boolean enabled = false;
        properties.setEnabled(enabled);
        assertFalse(properties.isEnabled());
    }
    
    @Test
    void testAccessKeyIdGetterAndSetter() {
        final String accessKeyId = "accessKeyId";
        properties.setAccessKeyId(accessKeyId);
        assertEquals(accessKeyId,
                     properties.getAccessKeyId()
        );
    }
    
    @Test
    void testAccessKeySecretGetterAndSetter() {
        final String accessKeySecret = "accessKeySecret";
        properties.setAccessKeySecret(
                accessKeySecret);
        assertEquals(accessKeySecret,
                     properties.getAccessKeySecret()
        );
    }
    
    @Test
    void testEndpointGetterAndSetter() {
        final String endpoint = "endpoint";
        properties.setEndpoint(endpoint);
        assertEquals(endpoint,
                     properties.getEndpoint()
        );
    }
    
    @Test
    void testRegionIdGetterAndSetter() {
        final String regionId = "regionId";
        properties.setRegionId(regionId);
        assertEquals(regionId,
                     properties.getRegionId()
        );
    }
    
}
