package com.alibaba.cloudapp.api.scheduler.worker;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GlobalJobContextTest {
    
    private GlobalJobContext globalJobContextUnderTest;
    
    @Before
    public void setUp() throws Exception {
        globalJobContextUnderTest = new GlobalJobContext(
                0L, "jobParam", 0, 0,
                "shardParameter"
        );
    }
    
    @Test
    public void testGetJobId() {
        assertEquals(0L, globalJobContextUnderTest.getJobId());
    }
    
    @Test
    public void testGetJobParam() {
        assertEquals("jobParam", globalJobContextUnderTest.getJobParam());
    }
    
    @Test
    public void testGetShardIndex() {
        assertEquals(0, globalJobContextUnderTest.getShardIndex());
    }
    
    @Test
    public void testGetShardTotal() {
        assertEquals(0, globalJobContextUnderTest.getShardTotal());
    }
    
    @Test
    public void testGetShardParameter() {
        assertEquals("shardParameter",
                     globalJobContextUnderTest.getShardParameter()
        );
    }
    
    @Test
    public void testSetContext() {
        // Setup
        final GlobalJobContext ctx = new GlobalJobContext(
                0L, "jobParam", 0, 0,
                "shardParameter"
        );
        
        // Run the test
        GlobalJobContext.setContext(ctx);
        
        // Verify the results
    }
    
    @Test
    public void testGetContext() {
        // Run the test
        final GlobalJobContext result = GlobalJobContext.getContext();
        assertEquals(0L, result.getJobId());
        assertEquals("jobParam", result.getJobParam());
        assertEquals(0, result.getShardIndex());
        assertEquals(0, result.getShardTotal());
        assertEquals("shardParameter", result.getShardParameter());
    }
    
    @Test
    public void testClear() {
        // Setup
        // Run the test
        GlobalJobContext.clear();
        
        // Verify the results
    }
    
}
