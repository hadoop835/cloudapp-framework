package com.alibaba.cloudapp.exeption;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CloudAppTimeoutExceptionTest {
    
    private CloudAppTimeoutException cloudAppTimeoutExceptionUnderTest;
    
    @Before
    public void setUp() throws Exception {
        cloudAppTimeoutExceptionUnderTest = new CloudAppTimeoutException(
                "msg", "code", new Exception("message")
        );
    }
    
    @Test
    public void testGetCode() {
        assertEquals("code", cloudAppTimeoutExceptionUnderTest.getCode());
    }
    
}
