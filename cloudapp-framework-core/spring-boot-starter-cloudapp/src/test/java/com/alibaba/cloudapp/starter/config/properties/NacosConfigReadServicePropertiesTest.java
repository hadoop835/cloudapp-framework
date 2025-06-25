package com.alibaba.cloudapp.starter.config.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NacosConfigReadServicePropertiesTest {
    
    private NacosConfigReadServiceProperties serviceProperties;
    
    @BeforeEach
    void setUp() {
        serviceProperties = new NacosConfigReadServiceProperties();
    }
    
    @Test
    void testTimeoutGetterAndSetter() {
        final int timeout = 0;
        serviceProperties.setTimeout(timeout);
        assertEquals(timeout, serviceProperties.getTimeout());
    }
    
    @Test
    void testGroupGetterAndSetter() {
        final String group = "group";
        serviceProperties.setGroup(group);
        assertEquals(group, serviceProperties.getGroup());
    }
    
    @Test
    void testToString() {
        String result = String.format(
                "NacosConfigReadServiceProperties{group='%s', timeout=%d}",
                serviceProperties.getGroup(),
                serviceProperties.getTimeout()
        );
        assertEquals(result, serviceProperties.toString());
    }
    
}
