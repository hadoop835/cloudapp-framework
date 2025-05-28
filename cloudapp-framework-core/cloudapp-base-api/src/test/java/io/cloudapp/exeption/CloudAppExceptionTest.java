package io.cloudapp.exeption;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CloudAppExceptionTest {
    
    private CloudAppException exception;
    
    @Before
    public void setUp() throws Exception {
        exception = new CloudAppException("msg", "code", new Exception(
                "message"));
    }
    
    @Test
    public void testGetCode() {
        assertEquals("code", exception.getCode());
    }
    
}
