package io.cloudapp.starter.scheduler.configuration;

import com.aliyun.schedulerx220190430.Client;
import com.aliyun.teaopenapi.models.Config;
import io.cloudapp.scheduler.schedulerx2.SchedulerX2JobExecuteService;
import io.cloudapp.scheduler.schedulerx2.SchedulerX2JobGroupManager;
import io.cloudapp.scheduler.schedulerx2.SchedulerX2JobManager;
import io.cloudapp.starter.scheduler.properties.SchedulerX2ServerProperties;
import io.cloudapp.starter.scheduler.refresh.SchedulerX2ServerRefreshComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulerX2ServerConfigurationTest {
    
    private SchedulerX2ServerConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new SchedulerX2ServerConfiguration();
    }
    
    @Test
    void testSchedulerX2ServerRefreshComponent() {
        // Setup
        final SchedulerX2ServerProperties properties = new SchedulerX2ServerProperties();
        properties.setEnabled(false);
        properties.setAccessKeyId("accessKeyId");
        properties.setAccessKeySecret("accessKeySecret");
        properties.setEndpoint("endpoint");
        properties.setRegionId("regionId");
        
        // Run the test
        final SchedulerX2ServerRefreshComponent result = configuration.schedulerX2ServerRefreshComponent(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testSchedulerx2Client() {
        // Setup
        final SchedulerX2ServerProperties schedulerX2ServerProperties = new SchedulerX2ServerProperties();
        schedulerX2ServerProperties.setEnabled(false);
        schedulerX2ServerProperties.setAccessKeyId("accessKeyId");
        schedulerX2ServerProperties.setAccessKeySecret("accessKeySecret");
        schedulerX2ServerProperties.setEndpoint("endpoint");
        schedulerX2ServerProperties.setRegionId("regionId");
        final SchedulerX2ServerRefreshComponent comp = new SchedulerX2ServerRefreshComponent(
                schedulerX2ServerProperties);
        
        // Run the test
        final Client result = configuration.schedulerx2Client(
                comp);
        
        // Verify the results
    }
    
    @Test
    void testJobGroupManger() throws Exception {
        // Setup
        final Config config = new Config();
        config.setAccessKeyId("accessKeyId");
        config.setAccessKeySecret("accessKeySecret");
        config.setSecurityToken("securityToken");
        config.setBearerToken("bearerToken");
        config.setProtocol("protocol");
        config.setRegionId("regionId");
        final Client client = new Client(config);
        
        // Run the test
        final SchedulerX2JobGroupManager result = configuration.jobGroupManger(
                client);
        
        // Verify the results
    }
    
    @Test
    void testJobManger() throws Exception {
        // Setup
        final Config config = new Config();
        config.setAccessKeyId("accessKeyId");
        config.setAccessKeySecret("accessKeySecret");
        config.setSecurityToken("securityToken");
        config.setBearerToken("bearerToken");
        config.setProtocol("protocol");
        config.setRegionId("regionId");
        final Client client = new Client(config);
        
        // Run the test
        final SchedulerX2JobManager result = configuration.jobManger(
                client);
        
        // Verify the results
    }
    
    @Test
    void testJobExecuteService() throws Exception {
        // Setup
        final Config config = new Config();
        config.setAccessKeyId("accessKeyId");
        config.setAccessKeySecret("accessKeySecret");
        config.setSecurityToken("securityToken");
        config.setBearerToken("bearerToken");
        config.setProtocol("protocol");
        config.setRegionId("regionId");
        final Client client = new Client(config);
        
        // Run the test
        final SchedulerX2JobExecuteService result = configuration.jobExecuteService(
                client);
        
        // Verify the results
    }
    
}
