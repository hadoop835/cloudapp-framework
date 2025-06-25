package com.alibaba.cloudapp.api.scheduler.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExecuteModeTest {
    
    @Test
    public void testGetKey() {
        assertEquals("standalone", ExecuteMode.STANDALONE.getKey());
        assertEquals("broadcast", ExecuteMode.BROADCAST.getKey());
        assertEquals("sharding", ExecuteMode.SHARDING.getKey());
    }
    
    @Test
    public void testSetKey() {
        // Setup
        final String key = "key";
        
        // Run the test
        ExecuteMode.STANDALONE.setKey(key);
        ExecuteMode.BROADCAST.setKey(key);
        ExecuteMode.SHARDING.setKey(key);
        
        // Verify the results
    }
    
    @Test
    public void testGetDesc() {
        assertEquals("单机运行", ExecuteMode.STANDALONE.getDesc());
        assertEquals("广播运行", ExecuteMode.BROADCAST.getDesc());
        assertEquals("分片运行", ExecuteMode.SHARDING.getDesc());
    }
    
    @Test
    public void testSetDesc() {
        // Setup
        final String desc = "desc";
        
        // Run the test
        ExecuteMode.STANDALONE.setDesc(desc);
        ExecuteMode.BROADCAST.setDesc(desc);
        ExecuteMode.SHARDING.setDesc(desc);
        
        // Verify the results
    }
    
}
