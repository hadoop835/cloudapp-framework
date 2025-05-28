package io.cloudapp.api.messaging.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConsumerTypeTest {
    
    @Test
    public void testToString() {
        assertEquals("PUSH", ConsumerType.PUSH.toString());
        assertEquals("PULL", ConsumerType.PULL.toString());
    }
    
    @Test
    public void testFromString() {
        assertEquals(ConsumerType.PUSH, ConsumerType.fromString("push"));
    }
    
}
