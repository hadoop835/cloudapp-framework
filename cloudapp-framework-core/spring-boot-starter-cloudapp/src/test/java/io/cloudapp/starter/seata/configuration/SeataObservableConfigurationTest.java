package io.cloudapp.starter.seata.configuration;

import io.cloudapp.seata.GlobalTransactionHolder;
import io.cloudapp.seata.ObservableTransactionalInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeataObservableConfigurationTest {
    
    private SeataObservableConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new SeataObservableConfiguration();
    }
    
    @Test
    void testGlobalTransactionHolder() {
        // Setup
        // Run the test
        final GlobalTransactionHolder result = configuration.globalTransactionHolder();
        
        // Verify the results
    }
    
    @Test
    void testObservableTransactionalInterceptor() {
        // Setup
        final GlobalTransactionHolder holder = new GlobalTransactionHolder();
        holder.setCount(0L);
        holder.setSucceedCount(0L);
        holder.setFailedCount(0L);
        holder.setRunningGauge(0L);
        holder.setMaxRunningTime(0L);
        
        // Run the test
        final ObservableTransactionalInterceptor result = configuration.observableTransactionalInterceptor(
                holder);
        
        // Verify the results
    }
    
}
