package com.alibaba.cloudapp.api.gateway;

import com.auth0.jwt.algorithms.Algorithm;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class AlgorithmUtilTest {
    
    @Test
    public void testGetVerifyAlgorithm() throws Exception {
        // Setup
        // Run the test
        Algorithm result = AlgorithmUtil.getVerifyAlgorithm(
                "HS256", "content".getBytes());
        
        // Verify the results
    }
    
    @Test
    public void testGetVerifyAlgorithm_ThrowsException() {
        // Setup
        // Run the test
        assertThrows(Exception.class, () -> AlgorithmUtil.getVerifyAlgorithm(
                "algorithm", "content".getBytes())
        );
    }
    
    @Test
    public void testGetGenerateAlgorithm() throws Exception {
        // Setup
        // Run the test
        Algorithm result = AlgorithmUtil.getGenerateAlgorithm(
                "HS256", "content".getBytes());
        // Verify the results
    }
    
    @Test
    public void testGetGenerateAlgorithm_ThrowsException() {
        // Setup
        // Run the test
        assertThrows(IllegalArgumentException.class,
                     () -> AlgorithmUtil.getGenerateAlgorithm(
                             "AES", "content".getBytes())
        );
    }
    
}
