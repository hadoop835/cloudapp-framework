package com.alibaba.cloudapp.starter.oauth2.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class OAuth2ClientPropertiesTest {
    
    private OAuth2ClientProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new OAuth2ClientProperties();
    }
    
    @Test
    void testEnabledGetterAndSetter() {
        final Boolean enabled = false;
        properties.setEnabled(enabled);
        assertFalse(properties.getEnabled());
    }
    
}
