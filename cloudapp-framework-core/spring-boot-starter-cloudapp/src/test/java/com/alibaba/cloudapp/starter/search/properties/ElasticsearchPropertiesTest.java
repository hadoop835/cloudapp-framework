package com.alibaba.cloudapp.starter.search.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ElasticsearchPropertiesTest {
    
    private ElasticsearchProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new ElasticsearchProperties();
    }
    
    @Test
    void testEnabledGetterAndSetter() {
        final boolean enabled = false;
        properties.setEnabled(enabled);
        assertFalse(properties.isEnabled());
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
    
}
