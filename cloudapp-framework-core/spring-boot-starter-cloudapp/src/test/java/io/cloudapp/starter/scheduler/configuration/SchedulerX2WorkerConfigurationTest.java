package io.cloudapp.starter.scheduler.configuration;

import com.alibaba.schedulerx.worker.SchedulerxWorker;
import com.aliyun.schedulerx220190430.Client;
import io.cloudapp.scheduler.schedulerx2.GlobalJobBeanRegistrar;
import io.cloudapp.scheduler.schedulerx2.GlobalJobScanner;
import io.cloudapp.scheduler.schedulerx2.GlobalJobSyncManager;
import io.cloudapp.starter.scheduler.properties.SchedulerX2WorkerProperties;
import io.cloudapp.starter.scheduler.refresh.SchedulerX2ClientRefreshComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulerX2WorkerConfigurationTest {
    
    private SchedulerX2WorkerConfiguration configuration;
    
    private SchedulerX2WorkerProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new SchedulerX2WorkerProperties();
        properties.setNamespace("namespace");
        properties.setGroupId("groupId");
        properties.setAppKey("appKey");
        properties.setEnabled(false);
        properties.setAccessKeyId("accessKeyId");
        properties.setEndpoint("endpoint");
        properties.setRegionId("regionId");
        properties.setAccessKeySecret("accessKeySecret");
        properties.setOpenAPIEndpoint("openAPIEndpoint");
        
        configuration = new SchedulerX2WorkerConfiguration();
    }
    
    @Test
    void testSchedulerX2ClientRefreshComponent() {
        // Setup
        
        // Run the test
        final SchedulerX2ClientRefreshComponent result =
                configuration.schedulerX2ClientRefreshComponent(properties);
        
        // Verify the results
    }
    
    @Test
    void testSchedulerx2Client() {
        // Setup
        final SchedulerX2ClientRefreshComponent comp = new SchedulerX2ClientRefreshComponent(
                properties);
        
        // Run the test
        final Client result = configuration.schedulerx2Client(
                comp);
        
        // Verify the results
    }
    
    @Test
    void testGlobalJobScanner() {
        // Setup
        // Run the test
        final GlobalJobScanner result = configuration.globalJobScanner();
        
        // Verify the results
    }
    
    @Test
    void testGlobalJobBeanRegistrar() {
        // Setup
        final SchedulerX2ClientRefreshComponent comp = new SchedulerX2ClientRefreshComponent(
                properties);
        
        // Run the test
        final GlobalJobBeanRegistrar result = configuration.globalJobBeanRegistrar(
                comp);
        
        // Verify the results
    }
    
    @Test
    void testGlobalJobSyncManager() {
        // Setup
        final SchedulerX2ClientRefreshComponent comp = new SchedulerX2ClientRefreshComponent(
                properties);
        
        // Run the test
        final GlobalJobSyncManager result = configuration.globalJobSyncManager(
                comp);
        
        // Verify the results
    }
    
    @Test
    void testSchedulerxWorker() {
        // Setup
        final SchedulerX2ClientRefreshComponent comp = new SchedulerX2ClientRefreshComponent(
                properties);
        
        // Run the test
        final SchedulerxWorker result = configuration.schedulerxWorker(
                comp);
        
        // Verify the results
    }
    
}
