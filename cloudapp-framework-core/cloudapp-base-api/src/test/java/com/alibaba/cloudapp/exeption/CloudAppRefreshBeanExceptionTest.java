package com.alibaba.cloudapp.exeption;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CloudAppRefreshBeanExceptionTest {
    
    private CloudAppRefreshBeanException beanException;
    
    @Before
    public void setUp() throws Exception {
        beanException = new CloudAppRefreshBeanException(
                new Exception("message"));
    }
    
    @Test
    public void testGetCode() {
        assertEquals("CloudApp.RefreshBeanError", beanException.getCode());
    }
    
}
