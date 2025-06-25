package com.alibaba.cloudapp.scheduler.schedulerx2;

import com.aliyun.schedulerx220190430.Client;
import com.aliyun.schedulerx220190430.models.*;
import com.alibaba.cloudapp.api.scheduler.worker.GlobalJobMetadata;
import com.alibaba.cloudapp.api.scheduler.worker.JobGroupMetadata;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GlobalJobSyncManagerTest {
    
    @Mock
    private Client mockClient;
    
    private GlobalJobSyncManager jobSyncManager;
    public static final String REGION_ID = "regionId";
    public static final String NAMESPACE = "namespace";
    public static final String GROUP_ID = "groupId";
    
    @Before
    public void setUp() {
        jobSyncManager = new GlobalJobSyncManager(
                mockClient, REGION_ID, NAMESPACE, GROUP_ID
        );
    }
    
    @Test
    public void testGetUniqueSymbol() {
        // Setup
        final GlobalJobMetadata obj = new GlobalJobMetadata();
        obj.setCron("cron");
        obj.setFixedDelay(0L);
        obj.setFixedRate(0L);
        obj.setExecuteMode("executeMode");
        obj.setDescription("description");
        obj.setAutoCreateJob(false);
        obj.setAutoDeleteJob(false);
        obj.setValueName("valueName");
        obj.setShardingParams("shardingParams");
        
        // Run the test
        final String result = jobSyncManager.getUniqueSymbol(obj);
        
        // Verify the results
        assertEquals("valueName", result);
    }
    
    @Test
    public void testCreateJobGroupAndSetIdToEnv() {
        jobSyncManager.createJobGroupAndSetIdToEnv(
                new JobGroupMetadata());
    }
    
    @Test
    public void testCreateJob() throws Exception {
        // Setup
        final GlobalJobMetadata obj = new GlobalJobMetadata();
        obj.setCron("cron");
        obj.setFixedDelay(0L);
        obj.setFixedRate(0L);
        obj.setExecuteMode("executeMode");
        obj.setDescription("description");
        obj.setAutoCreateJob(false);
        obj.setAutoDeleteJob(false);
        obj.setShardingParams("shardingParams");
        
        // Configure Client.createJob(...).
        final CreateJobResponse createJobResponse = new CreateJobResponse();
        createJobResponse.setHeaders(new HashMap<>());
        final CreateJobResponseBody body = new CreateJobResponseBody();
        final CreateJobResponseBody.CreateJobResponseBodyData data = new CreateJobResponseBody.CreateJobResponseBodyData();
        data.setJobId(0L);
        body.setData(data);
        body.setMessage("message");
        createJobResponse.setBody(body);
        when(mockClient.createJob(any(CreateJobRequest.class)))
                .thenReturn(createJobResponse);
        
        // Run the test
        jobSyncManager.createJob(obj);
        
        // Verify the results
    }
    
    @Test
    public void testCreateJob_ClientReturnsNull() throws Exception {
        // Setup
        final GlobalJobMetadata obj = new GlobalJobMetadata();
        obj.setFixedDelay(0L);
        obj.setFixedRate(0L);
        obj.setExecuteMode("executeMode");
        obj.setDescription("description");
        obj.setAutoCreateJob(false);
        obj.setAutoDeleteJob(false);
        obj.setShardingParams("shardingParams");
        
        when(mockClient.createJob(any(CreateJobRequest.class)))
                .thenReturn(null);
        
        jobSyncManager.createJob(obj);
        // Run the test
        
        obj.setFixedRate(2L);
        jobSyncManager.createJob(obj);
        
        obj.setFixedRate(0L);
        obj.setFixedDelay(0L);
        jobSyncManager.createJob(obj);
        
        obj.setCron("0 0 0 * * * ?");
        jobSyncManager.createJob(obj);
    }
    
    @Test
    public void testCreateJob_ClientThrowsException() throws Exception {
        // Setup
        final GlobalJobMetadata obj = new GlobalJobMetadata();
        obj.setCron("cron");
        obj.setFixedDelay(0L);
        obj.setFixedRate(0L);
        obj.setExecuteMode("executeMode");
        obj.setDescription("description");
        obj.setAutoCreateJob(false);
        obj.setAutoDeleteJob(false);
        obj.setShardingParams("shardingParams");
        
        when(mockClient.createJob(any(CreateJobRequest.class)))
                .thenThrow(Exception.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> jobSyncManager.createJob(obj)
        );
    }
    
    @Test
    public void testFindFirstJobId() throws Exception {
        // Setup
        // Configure Client.listJobs(...).
        final ListJobsResponse listJobsResponse = new ListJobsResponse();
        final ListJobsResponseBody body = new ListJobsResponseBody();
        final ListJobsResponseBody.ListJobsResponseBodyData data = new ListJobsResponseBody.ListJobsResponseBodyData();
        final ListJobsResponseBody.ListJobsResponseBodyDataJobs listJobsResponseBodyDataJobs = new ListJobsResponseBody.ListJobsResponseBodyDataJobs();
        listJobsResponseBodyDataJobs.setJobId(0L);
        data.setJobs(Collections.singletonList(listJobsResponseBodyDataJobs));
        body.setData(data);
        body.setMessage("message");
        listJobsResponse.setBody(body);
        when(mockClient.listJobs(any(ListJobsRequest.class)))
                .thenReturn(listJobsResponse);
        
        // Run the test
        final Long result = jobSyncManager.findFirstJobId(
                "jobName");
        
        // Verify the results
        assertEquals(Long.valueOf(0L), result);
    }
    
    @Test
    public void testFindFirstJobId_ClientReturnsNull() throws Exception {
        // Setup
        when(mockClient.listJobs(any(ListJobsRequest.class))).thenReturn(null);
        
        // Run the test
        final Long result = jobSyncManager.findFirstJobId(
                "jobName");
        
        // Verify the results
        assertNull(result);
    }
    
    @Test
    public void testFindFirstJobId_ClientThrowsException() throws Exception {
        // Setup
        when(mockClient.listJobs(any(ListJobsRequest.class)))
                .thenThrow(Exception.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> jobSyncManager.findFirstJobId(
                             "jobName")
        );
    }
    
    @Test
    public void testDeleteJob() throws Exception {
        // Setup
        // Configure Client.deleteJob(...).
        final DeleteJobResponse deleteJobResponse = new DeleteJobResponse();
        deleteJobResponse.setHeaders(new HashMap<>());
        deleteJobResponse.setStatusCode(0);
        final DeleteJobResponseBody body = new DeleteJobResponseBody();
        body.setCode(0);
        body.setSuccess(false);
        deleteJobResponse.setBody(body);
        when(mockClient.deleteJob(any(DeleteJobRequest.class)))
                .thenReturn(deleteJobResponse);
        
        // Run the test
        final boolean result = jobSyncManager.deleteJob(0L);
        
        // Verify the results
        assertFalse(result);
    }
    
    @Test
    public void testDeleteJob_ClientReturnsNull() throws Exception {
        // Setup
        when(mockClient.deleteJob(any(DeleteJobRequest.class)))
                .thenReturn(null);
        
        // Run the test
        final boolean result = jobSyncManager.deleteJob(0L);
        
        // Verify the results
        assertFalse(result);
    }
    
    @Test
    public void testDeleteJob_ClientThrowsException() throws Exception {
        // Setup
        when(mockClient.deleteJob(any(DeleteJobRequest.class)))
                .thenThrow(Exception.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> jobSyncManager.deleteJob(0L)
        );
    }
    
}
