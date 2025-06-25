package com.alibaba.cloudapp.exeption;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CloudAppPermissionExceptionTest {
    
    private CloudAppPermissionException cloudAppPermissionExceptionUnderTest;
    
    @Before
    public void setUp() throws Exception {
        cloudAppPermissionExceptionUnderTest = new CloudAppPermissionException(
                "msg", "code", new Exception("message"));
    }
    
    @Test
    public void testGetCode() {
        assertEquals("code", cloudAppPermissionExceptionUnderTest.getCode());
    }
    
}
