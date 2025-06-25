package com.alibaba.cloudapp.starter.sequence.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SnowflakePropertiesTest {
    
    private SnowflakeProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new SnowflakeProperties();
    }
    
    @Test
    void testWorkerIdGetterAndSetter() {
        final long workerId = 0L;
        properties.setWorkerId(workerId);
        assertEquals(workerId, properties.getWorkerId());
    }
    
    @Test
    void testWorkerIdBitsGetterAndSetter() {
        final long workerIdBits = 0L;
        properties.setWorkerIdBits(workerIdBits);
        assertEquals(workerIdBits,
                     properties.getWorkerIdBits()
        );
    }
    
    @Test
    void testSequenceBitsGetterAndSetter() {
        final long sequenceBits = 0L;
        properties.setSequenceBits(sequenceBits);
        assertEquals(sequenceBits,
                     properties.getSequenceBits()
        );
    }
    
    @Test
    void testEnabledGetterAndSetter() {
        final boolean enabled = false;
        properties.setEnabled(enabled);
        assertFalse(properties.isEnabled());
    }
    
}
