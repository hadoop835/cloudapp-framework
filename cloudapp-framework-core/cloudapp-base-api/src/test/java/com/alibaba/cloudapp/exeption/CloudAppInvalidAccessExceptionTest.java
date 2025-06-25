package com.alibaba.cloudapp.exeption;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CloudAppInvalidAccessExceptionTest {
    
    private CloudAppInvalidAccessException exception;
    
    @Before
    public void setUp() throws Exception {
        exception = new CloudAppInvalidAccessException(
                "msg", "code", new Exception("message"));
    }
    
    @Test
    public void testGetCode() {
        assertEquals("code", exception.getCode());
    }
    
}
