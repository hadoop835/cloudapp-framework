package com.alibaba.cloudapp.starter.base.configuration;

import com.alibaba.cloudapp.starter.base.properties.ThreadPoolProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.Assert.assertThrows;

class MonitoringThreadPoolConfigurationTest {
    
    private MonitoringThreadPoolConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new MonitoringThreadPoolConfiguration();
    }
    
    @Test
    void testMonitoringThreadPool() {
        // Setup
        final ThreadPoolProperties props = new ThreadPoolProperties();
        props.setCorePoolSize(1);
        props.setMaximumPoolSize(2);
        props.setKeepAliveSeconds(10);
        props.setQueueCapacity(1);
        props.setThreadNamePrefix("threadNamePrefix");
        props.setAwaitTerminationSeconds(0L);
        props.setAllowCoreThreadTimeOut(false);
        
        // Run the test
        final ThreadPoolExecutor result = configuration.monitoringThreadPool(
                props);
        
        // Verify the results
    }
    
    @Test
    void testMonitoringThreadPool_error() {
        // Setup
        final ThreadPoolProperties props = new ThreadPoolProperties();
        props.setCorePoolSize(0);
        props.setMaximumPoolSize(0);
        props.setKeepAliveSeconds(0);
        props.setQueueCapacity(0);
        props.setThreadNamePrefix("threadNamePrefix");
        props.setAwaitTerminationSeconds(0L);
        props.setAllowCoreThreadTimeOut(false);
        
        // Run the test
        assertThrows(IllegalArgumentException.class, () ->
                configuration.monitoringThreadPool(props));
        
        // Verify the results
    }
    
}
