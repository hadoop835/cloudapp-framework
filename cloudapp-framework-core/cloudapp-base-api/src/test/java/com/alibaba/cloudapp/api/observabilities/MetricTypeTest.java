package com.alibaba.cloudapp.api.observabilities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MetricTypeTest {
    
    @Test
    public void testGetType() {
        assertEquals("counter", MetricType.COUNTER.getType());
        assertEquals("gauge", MetricType.GAUGE.getType());
        assertEquals("histogram", MetricType.HISTOGRAM.getType());
    }
    
    @Test
    public void testGetInstance() {
        assertEquals(MetricType.COUNTER, MetricType.getInstance("counter"));
    }
    
}
