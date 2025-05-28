package io.cloudapp.scheduler.schedulerx2;

import com.aliyun.schedulerx220190430.Client;
import com.aliyun.schedulerx220190430.models.*;
import com.aliyun.teautil.models.RuntimeOptions;
import io.cloudapp.api.scheduler.model.JobInstance;
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
import static org.mockito.Mockito.*;

public class SchedulerX2JobExecuteServiceTest {
    
    @Mock
    private Client client;
    
    @InjectMocks
    private SchedulerX2JobExecuteService executeService;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testSetSuccess() throws Exception {
        SetJobInstanceSuccessResponse successResponse = new SetJobInstanceSuccessResponse();
        SetJobInstanceSuccessResponseBody body = new SetJobInstanceSuccessResponseBody();
        body.setSuccess(true);
        successResponse.setBody(body);
        
        when(client.setJobInstanceSuccessWithOptions(any(SetJobInstanceSuccessRequest.class), any(RuntimeOptions.class)))
                .thenReturn(successResponse);
        
        boolean result = executeService.setSuccess("test-namespace", "test-group", 123L, 456L);
        
        assertTrue(result);
    }
    
    @Test
    public void testRetry() throws Exception {
        RetryJobInstanceResponse retryResponse = new RetryJobInstanceResponse();
        RetryJobInstanceResponseBody body = new RetryJobInstanceResponseBody();
        body.setSuccess(true);
        retryResponse.setBody(body);
        
        when(client.retryJobInstanceWithOptions(any(RetryJobInstanceRequest.class), any(RuntimeOptions.class)))
                .thenReturn(retryResponse);
        
        boolean result = executeService.retry("test-namespace", "test-group", 123L, 456L);
        
        assertTrue(result);
        
        body.setSuccess(null);
        body.setMessage("message");
        when(client.retryJobInstanceWithOptions(any(RetryJobInstanceRequest.class), any(RuntimeOptions.class)))
                .thenReturn(retryResponse);
        
        assertThrows(CloudAppException.class, () -> executeService.retry(
                "test-namespace", "test-group", 123L, 456L));
        
        body.setMessage(null);
        when(client.retryJobInstanceWithOptions(any(RetryJobInstanceRequest.class), any(RuntimeOptions.class)))
                .thenReturn(retryResponse);
        assertThrows(CloudAppException.class, () -> executeService.retry(
                "test-namespace", "test-group", 123L, 456L));

    }
    
    @Test
    public void testStop() throws Exception {
        StopInstanceResponse stopResponse = new StopInstanceResponse();
        StopInstanceResponseBody body = new StopInstanceResponseBody();
        body.setSuccess(true);
        stopResponse.setBody(body);
        
        when(client.stopInstanceWithOptions(any(StopInstanceRequest.class), any(RuntimeOptions.class)))
                .thenReturn(stopResponse);
        
        boolean result = executeService.stop("test-namespace", "test-group", 123L, 456L);
        
        assertTrue(result);
        
        body.setSuccess(null);
        body.setMessage("message");
        when(client.stopInstanceWithOptions(any(StopInstanceRequest.class), any(RuntimeOptions.class)))
                .thenReturn(stopResponse);
        
        assertThrows(CloudAppException.class, () -> executeService.stop(
                "test-namespace", "test-group", 123L, 456L));
        
        body.setMessage(null);
        when(client.stopInstanceWithOptions(any(StopInstanceRequest.class), any(RuntimeOptions.class)))
                .thenReturn(stopResponse);
        assertThrows(CloudAppException.class, () -> executeService.stop(
                "test-namespace", "test-group", 123L, 456L));
        
    }
    
    @Test
    public void testList() throws Exception {
        GetJobInstanceListResponse listResponse = new GetJobInstanceListResponse();
        GetJobInstanceListResponseBody body = new GetJobInstanceListResponseBody();
        GetJobInstanceListResponseBody.GetJobInstanceListResponseBodyData data = new GetJobInstanceListResponseBody.GetJobInstanceListResponseBodyData();
        List<GetJobInstanceListResponseBody.GetJobInstanceListResponseBodyDataJobInstanceDetails> instances = new ArrayList<>();
        GetJobInstanceListResponseBody.GetJobInstanceListResponseBodyDataJobInstanceDetails instance = new GetJobInstanceListResponseBody.GetJobInstanceListResponseBodyDataJobInstanceDetails();
        instance.setInstanceId(456L);
        instance.setJobId(123L);
        instance.setStatus(4);
        instances.add(instance);
        data.setJobInstanceDetails(instances);
        body.setData(data);
        listResponse.setBody(body);
        
        when(client.getJobInstanceList(any(GetJobInstanceListRequest.class))).thenReturn(listResponse);
        
        List<JobInstance> jobInstances = executeService.list("test-namespace", "test-group", 123L);
        
        assertNotNull(jobInstances);
        assertEquals(1, jobInstances.size());
        assertEquals(456L, jobInstances.get(0).getInstanceId().longValue());
        assertEquals(123L, jobInstances.get(0).getJobId().longValue());
        assertEquals(Integer.valueOf(4), jobInstances.get(0).getStatus());
        
        body.setSuccess(null);
        body.setMessage("message");
        body.setData(null);
        when(client.getJobInstanceList(any(GetJobInstanceListRequest.class)))
                .thenReturn(listResponse);
        
        assertThrows(CloudAppException.class, () -> executeService.list(
                "test-namespace", "test-group", 123L
        ));
        
    }
    
    @Test
    public void testGet() throws Exception {
        GetJobInstanceResponse getResponse = new GetJobInstanceResponse();
        GetJobInstanceResponseBody body = new GetJobInstanceResponseBody();
        GetJobInstanceResponseBody.GetJobInstanceResponseBodyData data = new GetJobInstanceResponseBody.GetJobInstanceResponseBodyData();
        GetJobInstanceResponseBody.GetJobInstanceResponseBodyDataJobInstanceDetail instance = new GetJobInstanceResponseBody.GetJobInstanceResponseBodyDataJobInstanceDetail();
        instance.setInstanceId(456L);
        instance.setJobId(123L);
        instance.setStatus(4);
        data.setJobInstanceDetail(instance);
        body.setData(data);
        getResponse.setBody(body);
        
        when(client.getJobInstance(any(GetJobInstanceRequest.class))).thenReturn(getResponse);
        
        JobInstance jobInstance = executeService.get("test" +
                                                                           "-namespace", "test-group", 123L, 456L);
        
        assertNotNull(jobInstance);
        assertEquals(456L, jobInstance.getInstanceId().longValue());
        assertEquals(123L, jobInstance.getJobId().longValue());
        assertEquals(Integer.valueOf(4), jobInstance.getStatus());
    }
    
    @Test
    public void testGetDelegatingClient() {
        Client delegatingClient = executeService.getDelegatingClient();
        
        assertNotNull(delegatingClient);
        assertEquals(client, delegatingClient);
    }
}