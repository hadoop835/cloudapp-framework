package io.cloudapp.api.scheduler.worker;

import io.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractGlobalJobSyncManagerTest {
    
    @Mock
    private JobGroupMetadata mockJobGroup;
    
    private AbstractGlobalJobSyncManager<String> jobSyncManager;
    public static final String CLIENT = "client";
    public static final String REGION_ID = "regionId";
    public static final String NAMESPACE = "namespace";
    public static final String GROUP_ID = "groupId";
    
    @Before
    public void setUp() throws Exception {
        jobSyncManager = new AbstractGlobalJobSyncManager<String>(
                CLIENT, mockJobGroup, REGION_ID, NAMESPACE, GROUP_ID
        ) {
            
            @Override
            public String getUniqueSymbol(GlobalJobMetadata obj) {
                return "uniqueSymbol";
            }
            
            @Override
            public void createJobGroupAndSetIdToEnv(JobGroupMetadata obj)
                    throws CloudAppException {
                
            }
            
            @Override
            public void createJob(GlobalJobMetadata obj)
                    throws CloudAppException {
                
            }
            
            @Override
            public Long findFirstJobId(String uniqueSymbol)
                    throws CloudAppException {
                return 1L;
            }
            
            @Override
            public boolean deleteJob(Long jobId) throws CloudAppException {
                return false;
            }
        };
    }
    
    @Test
    public void testAfterSingletonsInstantiated() {
        // Setup
        // Run the test
        jobSyncManager.afterSingletonsInstantiated();
        
        // Verify the results
    }
    
    @Test
    public void testDestroy() throws Exception {
        // Setup
        // Run the test
        jobSyncManager.destroy();
        
        // Verify the results
    }
    
}
