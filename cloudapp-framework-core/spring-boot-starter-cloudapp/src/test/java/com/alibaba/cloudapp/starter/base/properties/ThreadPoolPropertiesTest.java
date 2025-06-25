package com.alibaba.cloudapp.starter.base.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ThreadPoolPropertiesTest {
    
    private ThreadPoolProperties threadPoolPropertiesUnderTest;
    
    @BeforeEach
    void setUp() {
        threadPoolPropertiesUnderTest = new ThreadPoolProperties();
    }
    
    @Test
    void testCorePoolSizeGetterAndSetter() {
        final int corePoolSize = 0;
        threadPoolPropertiesUnderTest.setCorePoolSize(corePoolSize);
        assertEquals(corePoolSize,
                     threadPoolPropertiesUnderTest.getCorePoolSize()
        );
    }
    
    @Test
    void testMaximumPoolSizeGetterAndSetter() {
        final int maximumPoolSize = 0;
        threadPoolPropertiesUnderTest.setMaximumPoolSize(maximumPoolSize);
        assertEquals(maximumPoolSize,
                     threadPoolPropertiesUnderTest.getMaximumPoolSize()
        );
    }
    
    @Test
    void testKeepAliveSecondsGetterAndSetter() {
        final int keepAliveSeconds = 0;
        threadPoolPropertiesUnderTest.setKeepAliveSeconds(keepAliveSeconds);
        assertEquals(keepAliveSeconds,
                     threadPoolPropertiesUnderTest.getKeepAliveSeconds()
        );
    }
    
    @Test
    void testQueueCapacityGetterAndSetter() {
        final int queueCapacity = 0;
        threadPoolPropertiesUnderTest.setQueueCapacity(queueCapacity);
        assertEquals(queueCapacity,
                     threadPoolPropertiesUnderTest.getQueueCapacity()
        );
    }
    
    @Test
    void testThreadNamePrefixGetterAndSetter() {
        final String threadNamePrefix = "threadNamePrefix";
        threadPoolPropertiesUnderTest.setThreadNamePrefix(threadNamePrefix);
        assertEquals(threadNamePrefix,
                     threadPoolPropertiesUnderTest.getThreadNamePrefix()
        );
    }
    
    @Test
    void testAwaitTerminationSecondsGetterAndSetter() {
        final long awaitTerminationSeconds = 0L;
        threadPoolPropertiesUnderTest.setAwaitTerminationSeconds(
                awaitTerminationSeconds);
        assertEquals(awaitTerminationSeconds,
                     threadPoolPropertiesUnderTest.getAwaitTerminationSeconds()
        );
    }
    
    @Test
    void testAllowCoreThreadTimeOutGetterAndSetter() {
        final boolean allowCoreThreadTimeOut = false;
        threadPoolPropertiesUnderTest.setAllowCoreThreadTimeOut(
                allowCoreThreadTimeOut);
        assertFalse(threadPoolPropertiesUnderTest.isAllowCoreThreadTimeOut());
    }
    
}
