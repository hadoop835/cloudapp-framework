package com.alibaba.cloudapp.starter.mail.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CloudAppMailPropertiesTest {
    
    private CloudAppMailProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new CloudAppMailProperties();
    }
    
    @Test
    void testHostGetterAndSetter() {
        final String host = "host";
        properties.setHost(host);
        assertEquals(host, properties.getHost());
    }
    
    @Test
    void testPortGetterAndSetter() {
        final Integer port = 0;
        properties.setPort(port);
        assertEquals(port, properties.getPort());
    }
    
    @Test
    void testUsernameGetterAndSetter() {
        final String username = "username";
        properties.setUsername(username);
        assertEquals(username, properties.getUsername());
    }
    
    @Test
    void testPasswordGetterAndSetter() {
        final String password = "password";
        properties.setPassword(password);
        assertEquals(password, properties.getPassword());
    }
    
    @Test
    void testProtocolGetterAndSetter() {
        final String protocol = "protocol";
        properties.setProtocol(protocol);
        assertEquals(protocol, properties.getProtocol());
    }
    
    @Test
    void testGetDefaultEncoding() {
        assertEquals(StandardCharsets.UTF_8,
                     properties.getDefaultEncoding()
        );
    }
    
    @Test
    void testSetDefaultEncoding() {
        // Setup
        // Run the test
        properties.setDefaultEncoding(
                StandardCharsets.UTF_8);
        
        // Verify the results
    }
    
    @Test
    void testGetProperties() {
        assertEquals(new HashMap<>(),
                     properties.getProperties()
        );
    }
    
    @Test
    void testSetProperties() {
        // Setup
        final Map<String, String> properties = new HashMap<>();
        
        // Run the test
        this.properties.setProperties(properties);
        
        // Verify the results
    }
    
    @Test
    void testJndiNameGetterAndSetter() {
        final String jndiName = "jndiName";
        properties.setJndiName(jndiName);
        assertEquals(jndiName, properties.getJndiName());
    }
    
}
