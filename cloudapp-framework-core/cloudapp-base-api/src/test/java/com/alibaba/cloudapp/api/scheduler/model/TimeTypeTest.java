package com.alibaba.cloudapp.api.scheduler.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeTypeTest {
    
    @Test
    public void testGetValue() {
        assertEquals(-1, TimeType.NONE.getValue());
        assertEquals(1, TimeType.CRON.getValue());
        assertEquals(2, TimeType.FIXED_DELAY.getValue());
        assertEquals(3, TimeType.FIXED_RATE.getValue());
        
        final int value = 0;
        
        // Run the test
        TimeType.NONE.setValue(value);
        TimeType.CRON.setValue(value);
        TimeType.FIXED_DELAY.setValue(value);
        TimeType.FIXED_RATE.setValue(value);
        
        assertEquals(0, TimeType.FIXED_RATE.getValue());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("none", TimeType.NONE.getDescription());
        assertEquals("cron", TimeType.CRON.getDescription());
        assertEquals("fixed_delay", TimeType.FIXED_DELAY.getDescription());
        assertEquals("fixed_rate", TimeType.FIXED_RATE.getDescription());
        
        final String description = "description";
        
        // Run the test
        TimeType.NONE.setDescription(description);
        TimeType.CRON.setDescription(description);
        TimeType.FIXED_DELAY.setDescription(description);
        TimeType.FIXED_RATE.setDescription(description);
        
        assertEquals("description", TimeType.FIXED_RATE.getDescription());
    }
    
}
