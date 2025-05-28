package io.cloudapp.api.messaging.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationTest {
    
    private Location locationUnderTest;
    
    @Before
    public void setUp() throws Exception {
        locationUnderTest = new Location();
    }
    
    @Test
    public void testHostGetterAndSetter() {
        final String host = "host";
        locationUnderTest.setHost(host);
        assertEquals(host, locationUnderTest.getHost());
    }
    
    @Test
    public void testPidGetterAndSetter() {
        final int pid = 0;
        locationUnderTest.setPid(pid);
        assertEquals(pid, locationUnderTest.getPid());
    }
    
    @Test
    public void testThreadNameGetterAndSetter() {
        final String threadName = "threadName";
        locationUnderTest.setThreadName(threadName);
        assertEquals(threadName, locationUnderTest.getThreadName());
    }
    
    @Test
    public void testThreadIdGetterAndSetter() {
        final long threadId = 0L;
        locationUnderTest.setThreadId(threadId);
        assertEquals(threadId, locationUnderTest.getThreadId());
    }
    
    @Test
    public void testTraceIdGetterAndSetter() {
        final String traceId = "traceId";
        locationUnderTest.setTraceId(traceId);
        assertEquals(traceId, locationUnderTest.getTraceId());
    }
    
    @Test
    public void testSpanIdGetterAndSetter() {
        final String spanId = "spanId";
        locationUnderTest.setSpanId(spanId);
        assertEquals(spanId, locationUnderTest.getSpanId());
    }
    
}
