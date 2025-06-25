package com.alibaba.cloudapp.starter.sequence.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RedisSequencePropertiesTest {
    
    private RedisSequenceProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new RedisSequenceProperties();
    }
    
    @Test
    void testEnabledGetterAndSetter() {
        final boolean enabled = false;
        properties.setEnabled(enabled);
        assertFalse(properties.isEnabled());
    }
    
    @Test
    void testSequenceNameGetterAndSetter() {
        final String sequenceName = "sequenceName";
        properties.setSequenceName(sequenceName);
        assertEquals(sequenceName,
                     properties.getSequenceName()
        );
    }
    
    @Test
    void testStepGetterAndSetter() {
        final Long step = 0L;
        properties.setStep(step);
        assertEquals(step, properties.getStep());
    }
    
}
