package io.cloudapp.scheduler.schedulerx2;

import com.aliyun.schedulerx220190430.Client;
import com.aliyun.schedulerx220190430.models.*;
import io.cloudapp.api.scheduler.model.Job;
import io.cloudapp.api.scheduler.model.JobType;
import io.cloudapp.api.scheduler.model.TimeType;
import io.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SchedulerX2JobManagerTest {
    
    @Mock
    private Client client;
    
    @InjectMocks
    private SchedulerX2JobManager manager;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testCreate() throws Exception {
        Job job = new Job();
        job.setGroupId("test-group");
        job.setNamespace("test-namespace");
        job.setName("test-job");
        job.setExecuteMode("test-mode");
        job.setJobType(JobType.SHELL);
        job.setParameters("test-params");
        job.setTimeType(TimeType.NONE);
        
        CreateJobResponse createJobResponse = new CreateJobResponse();
        CreateJobResponseBody body = new CreateJobResponseBody();
        body.setData(
                new CreateJobResponseBody.CreateJobResponseBodyData().setJobId(
                        123L));
        createJobResponse.setBody(body);
        
        when(client.createJob(any(CreateJobRequest.class))).thenReturn(
                createJobResponse);
        
        Long jobId = manager.create(job);
        
        assertNotNull(jobId);
        assertEquals(123L, jobId.longValue());
    }
    
    @Test
    public void testUpdate() throws Exception {
        Job job = new Job();
        job.setGroupId("test-group");
        job.setNamespace("test-namespace");
        job.setJobId(123L);
        job.setName("test-job");
        job.setExecuteMode("test-mode");
        job.setTimeType(TimeType.CRON);
        
        UpdateJobResponse updateJobResponse = new UpdateJobResponse();
        UpdateJobResponseBody body = new UpdateJobResponseBody();
        body.setSuccess(true);
        updateJobResponse.setBody(body);
        
        when(client.updateJob(any(UpdateJobRequest.class))).thenReturn(
                updateJobResponse);
        
        assertTrue(manager.update(job));
        
        body.setSuccess(null);
        body.setMessage("message");
        when(client.updateJob(any(UpdateJobRequest.class))).thenReturn(
                updateJobResponse);
        assertFalse(manager.update(job));
    }
    
    @Test
    public void testDelete() throws Exception {
        DeleteJobResponse deleteJobResponse = new DeleteJobResponse();
        DeleteJobResponseBody body = new DeleteJobResponseBody();
        body.setSuccess(true);
        deleteJobResponse.setBody(body);
        
        when(client.deleteJob(any(DeleteJobRequest.class))).thenReturn(
                deleteJobResponse);
        
        boolean result = manager.delete(
                "test-namespace", "test-group", 123L
        );
        
        assertTrue(result);
        
        when(client.deleteJob(any(DeleteJobRequest.class))).thenThrow(
                new RuntimeException());
        assertThrows(CloudAppException.class, () -> manager.delete(
                "test-namespace", "test-group", 123L
        ));
    }
    
    @Test
    public void testGet() throws Exception {
        GetJobInfoResponse getJobInfoResponse = new GetJobInfoResponse();
        GetJobInfoResponseBody body = new GetJobInfoResponseBody();
        
        GetJobInfoResponseBody.GetJobInfoResponseBodyData data =
                new GetJobInfoResponseBody.GetJobInfoResponseBodyData();
        GetJobInfoResponseBody.GetJobInfoResponseBodyDataJobConfigInfo jobConfigInfo =
                new GetJobInfoResponseBody.GetJobInfoResponseBodyDataJobConfigInfo();
        
        jobConfigInfo.setJobId(123L);
        jobConfigInfo.setName("test-job");
        jobConfigInfo.setExecuteMode("test-mode");
        jobConfigInfo.setJobType(JobType.JAVA.getKey());
        data.setJobConfigInfo(jobConfigInfo);
        body.setData(data);
        getJobInfoResponse.setBody(body);
        
        when(client.getJobInfo(any(GetJobInfoRequest.class))).thenReturn(
                getJobInfoResponse);
        
        Job job = manager.get(
                "test-namespace", "test-group",123L
        );
        
        assertNotNull(job);
        assertEquals(123L, job.getJobId().longValue());
        assertEquals("test-job", job.getName());
        assertEquals("test-mode", job.getExecuteMode());
        
        body.setSuccess(null);
        body.setMessage("message");
        body.setData(null);
        when(client.getJobInfo(any(GetJobInfoRequest.class))).thenReturn(
                getJobInfoResponse);
        assertThrows(CloudAppException.class, () -> manager.get(
                "test-namespace", "test-group",123L
        ));
    }
    
    @Test
    public void testList() throws Exception {
        ListJobsResponse listJobsResponse = new ListJobsResponse();
        ListJobsResponseBody body = new ListJobsResponseBody();
        ListJobsResponseBody.ListJobsResponseBodyData data = new ListJobsResponseBody.ListJobsResponseBodyData();
        List<ListJobsResponseBody.ListJobsResponseBodyDataJobs> jobs = new ArrayList<>();
        ListJobsResponseBody.ListJobsResponseBodyDataJobs job = new ListJobsResponseBody.ListJobsResponseBodyDataJobs();
        job.setJobId(123L);
        job.setName("test-job");
        job.setExecuteMode("test-mode");
        job.setJobType(JobType.JAVA.getKey());
        jobs.add(job);
        data.setJobs(jobs);
        body.setData(data);
        listJobsResponse.setBody(body);
        
        when(client.listJobs(any(ListJobsRequest.class))).thenReturn(
                listJobsResponse);
        
        List<Job> jobList = manager.list(
                "test-namespace","test-group",
                "test-job","active"
        );
        
        assertNotNull(jobList);
        assertEquals(1, jobList.size());
        assertEquals(123L, jobList.get(0).getJobId().longValue());
        assertEquals("test-job", jobList.get(0).getName());
        assertEquals("test-mode", jobList.get(0).getExecuteMode());
        
        body.setSuccess(null);
        body.setMessage("message");
        body.setData(null);
        when(client.listJobs(any(ListJobsRequest.class))).thenReturn(
                listJobsResponse);
        assertThrows(CloudAppException.class, () -> manager.list(
                "test-namespace","test-group",
                "test-job","active"
        ));
    }
    
    @Test
    public void testDisable() throws Exception {
        DisableJobResponse disableJobResponse = new DisableJobResponse();
        DisableJobResponseBody body = new DisableJobResponseBody();
        body.setSuccess(true);
        disableJobResponse.setBody(body);
        
        when(client.disableJob(any(DisableJobRequest.class))).thenReturn(
                disableJobResponse);
        
        boolean result = manager.disable("test-namespace",
                                         "test-group", 123L
        );
        
        assertTrue(result);
    }
    
    @Test
    public void testEnable() throws Exception {
        EnableJobResponse enableJobResponse = new EnableJobResponse();
        EnableJobResponseBody body = new EnableJobResponseBody();
        body.setSuccess(true);
        enableJobResponse.setBody(body);
        
        when(client.enableJob(any(EnableJobRequest.class))).thenReturn(
                enableJobResponse);
        
        boolean result = manager.enable(
                "test-namespace","test-group", 123L
        );
        
        assertTrue(result);
        
        when(client.enableJob(any(EnableJobRequest.class))).thenThrow(
                new RuntimeException());
        assertThrows(CloudAppException.class, () -> manager.enable(
                "test-namespace","test-group", 123L
        ));
    }
    
    @Test
    public void testExecute() throws Exception {
        ExecuteJobResponse executeJobResponse = new ExecuteJobResponse();
        ExecuteJobResponseBody body = new ExecuteJobResponseBody();
        body.setData(
                new ExecuteJobResponseBody.ExecuteJobResponseBodyData().setJobInstanceId(
                        456L));
        executeJobResponse.setBody(body);
        
        when(client.executeJob(any(ExecuteJobRequest.class))).thenReturn(
                executeJobResponse);
        
        Long jobInstanceId = manager.execute(
                "test-namespace","test-group",
                123L,"test-params"
        );
        
        assertNotNull(jobInstanceId);
        assertEquals(456L, jobInstanceId.longValue());
        
        body.setSuccess(null);
        body.setMessage("message");
        body.setData(null);
        when(client.executeJob(any(ExecuteJobRequest.class))).thenReturn(
                executeJobResponse);
        assertThrows(CloudAppException.class, () -> manager.execute(
                "test-namespace","test-group",
                123L,"test-params"
        ));
    }
    
    @Test
    public void testGetDelegatingClient() {
        Client delegatingClient = manager.getDelegatingClient();
        
        assertNotNull(delegatingClient);
        assertEquals(client, delegatingClient);
    }
    
}