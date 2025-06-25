package com.alibaba.cloudapp.api.scheduler.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JobTypeTest {
    
    @Test
    public void testGetByKey() {
        assertEquals(JobType.JAVA, JobType.getByKey("java"));
    }
    
    @Test
    public void testGetKey() {
        assertEquals("java", JobType.JAVA.getKey());
        assertEquals("python", JobType.PYTHON.getKey());
        assertEquals("shell", JobType.SHELL.getKey());
        assertEquals("http", JobType.HTTP.getKey());
        
        final String key = "key";
        
        // Run the test
        JobType.JAVA.setKey(key);
        JobType.PYTHON.setKey(key);
        JobType.SHELL.setKey(key);
        JobType.HTTP.setKey(key);
        
        assertEquals("key", JobType.HTTP.getKey());
    }
    
    @Test
    public void testSetKey() {
        // Setup
       
        
        // Verify the results
    }
    
}
