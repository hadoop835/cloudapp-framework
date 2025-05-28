package io.cloudapp.starter.seata.configuration;

import io.seata.saga.engine.StateMachineConfig;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.spring.boot.autoconfigure.properties.SagaAsyncThreadPoolProperties;
import io.seata.spring.boot.autoconfigure.properties.SeataProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;

class CloudAppSagaAutoConfigurationTest {
    
    private CloudAppSagaAutoConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new CloudAppSagaAutoConfiguration();
    }
    
    @Test
    void testDbStateMachineConfig() {
        // Setup
        final DataSource dataSource = null;
        final DataSource sagaDataSource = null;
        final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1,
                2,
                1000L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(4)
        );
        final SeataProperties properties = new SeataProperties();
        properties.setEnabled(false);
        properties.setApplicationId("applicationId");
        properties.setTxServiceGroup("txServiceGroup");
        properties.setEnableAutoDataSourceProxy(false);
        properties.setDataSourceProxyMode("dataSourceProxyMode");
        
        // Run the test
        final StateMachineConfig result = configuration.dbStateMachineConfig(
                dataSource, sagaDataSource, threadPoolExecutor, properties);
        
        // Verify the results
    }
    
    @Test
    void testSagaAsyncThreadPoolProperties() {
        // Setup
        // Run the test
        final SagaAsyncThreadPoolProperties result = configuration.sagaAsyncThreadPoolProperties();
        
        // Verify the results
    }
    
    @Test
    void testStateMachineEngine() {
        // Setup
        final StateMachineConfig config = null;
        
        // Run the test
        final StateMachineEngine result = configuration.stateMachineEngine(
                config);
        
        // Verify the results
    }
    
    @Test
    void testSagaRejectedExecutionHandler() {
        // Setup
        // Run the test
        final RejectedExecutionHandler result = configuration.sagaRejectedExecutionHandler();
        
        // Verify the results
    }
    
    @Test
    void testSagaAsyncThreadPoolExecutor() {
        // Setup
        final SagaAsyncThreadPoolProperties properties = new SagaAsyncThreadPoolProperties();
        properties.setCorePoolSize(1);
        properties.setMaxPoolSize(2);
        properties.setKeepAliveTime(1000);
        
        final RejectedExecutionHandler rejectedExecutionHandler = mock(RejectedExecutionHandler.class);
        
        // Run the test
        final ThreadPoolExecutor result = configuration.sagaAsyncThreadPoolExecutor(
                properties, rejectedExecutionHandler);
        
        // Verify the results
    }
    
}
