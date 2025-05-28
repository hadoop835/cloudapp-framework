package io.cloudapp.api.scheduler.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JobInstanceTest {
    
    private JobInstance jobInstanceUnderTest;
    
    @Before
    public void setUp() throws Exception {
        jobInstanceUnderTest = new JobInstance();
    }
    
    @Test
    public void testExecutorGetterAndSetter() {
        final String executor = "executor";
        jobInstanceUnderTest.setExecutor(executor);
        assertEquals(executor, jobInstanceUnderTest.getExecutor());
    }
    
    @Test
    public void testParametersGetterAndSetter() {
        final String parameters = "parameters";
        jobInstanceUnderTest.setParameters(parameters);
        assertEquals(parameters, jobInstanceUnderTest.getParameters());
    }
    
    @Test
    public void testInstanceIdGetterAndSetter() {
        final Long instanceId = 0L;
        jobInstanceUnderTest.setInstanceId(instanceId);
        assertEquals(instanceId, jobInstanceUnderTest.getInstanceId());
    }
    
    @Test
    public void testJobIdGetterAndSetter() {
        final Long jobId = 0L;
        jobInstanceUnderTest.setJobId(jobId);
        assertEquals(jobId, jobInstanceUnderTest.getJobId());
    }
    
    @Test
    public void testProgressGetterAndSetter() {
        final String progress = "progress";
        jobInstanceUnderTest.setProgress(progress);
        assertEquals(progress, jobInstanceUnderTest.getProgress());
    }
    
    @Test
    public void testScheduleTimeGetterAndSetter() {
        final String scheduleTime = "scheduleTime";
        jobInstanceUnderTest.setScheduleTime(scheduleTime);
        assertEquals(scheduleTime, jobInstanceUnderTest.getScheduleTime());
    }
    
    @Test
    public void testStartTimeGetterAndSetter() {
        final String startTime = "startTime";
        jobInstanceUnderTest.setStartTime(startTime);
        assertEquals(startTime, jobInstanceUnderTest.getStartTime());
    }
    
    @Test
    public void testEndTimeGetterAndSetter() {
        final String endTime = "endTime";
        jobInstanceUnderTest.setEndTime(endTime);
        assertEquals(endTime, jobInstanceUnderTest.getEndTime());
    }
    
    @Test
    public void testDataTimeGetterAndSetter() {
        final String dataTime = "dataTime";
        jobInstanceUnderTest.setDataTime(dataTime);
        assertEquals(dataTime, jobInstanceUnderTest.getDataTime());
    }
    
    @Test
    public void testStatusGetterAndSetter() {
        final Integer status = 0;
        jobInstanceUnderTest.setStatus(status);
        assertEquals(status, jobInstanceUnderTest.getStatus());
    }
    
    @Test
    public void testResultGetterAndSetter() {
        final String result1 = "result";
        jobInstanceUnderTest.setResult(result1);
        assertEquals(result1, jobInstanceUnderTest.getResult());
    }
    
    @Test
    public void testTriggerTypeGetterAndSetter() {
        final Integer triggerType = 0;
        jobInstanceUnderTest.setTriggerType(triggerType);
        assertEquals(triggerType, jobInstanceUnderTest.getTriggerType());
    }
    
    @Test
    public void testWorkAddrGetterAndSetter() {
        final String workAddr = "workAddr";
        jobInstanceUnderTest.setWorkAddr(workAddr);
        assertEquals(workAddr, jobInstanceUnderTest.getWorkAddr());
    }
    
}
