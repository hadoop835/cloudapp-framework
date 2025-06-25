package com.alibaba.cloudapp.exeption;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CloudAppInvalidRequestExceptionTest {
    
    private CloudAppInvalidRequestException cloudAppInvalidRequestExceptionUnderTest;
    
    @Before
    public void setUp() throws Exception {
        cloudAppInvalidRequestExceptionUnderTest = new CloudAppInvalidRequestException(
                "msg", "code", new Exception("message"));
    }
    
    @Test
    public void testGetCode() {
        assertEquals("code",
                     cloudAppInvalidRequestExceptionUnderTest.getCode()
        );
    }
    
}
