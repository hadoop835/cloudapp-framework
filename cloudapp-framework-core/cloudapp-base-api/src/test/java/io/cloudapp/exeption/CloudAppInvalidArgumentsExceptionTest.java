package io.cloudapp.exeption;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CloudAppInvalidArgumentsExceptionTest {
    
    private CloudAppInvalidArgumentsException exception;
    
    @Before
    public void setUp() throws Exception {
        exception = new CloudAppInvalidArgumentsException(
                "msg", "code", new Exception("message"));
    }
    
    @Test
    public void testGetCode() {
        assertEquals("code", exception.getCode()
        );
    }
    
}
