package io.cloudapp.api.scheduler.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JobTest {
    
    private Job jobUnderTest;
    
    @Before
    public void setUp() throws Exception {
        jobUnderTest = new Job();
    }
    
    @Test
    public void testJobIdGetterAndSetter() {
        final Long jobId = 0L;
        jobUnderTest.setJobId(jobId);
        assertEquals(jobId, jobUnderTest.getJobId());
    }
    
    @Test
    public void testGroupIdGetterAndSetter() {
        final String groupId = "groupId";
        jobUnderTest.setGroupId(groupId);
        assertEquals(groupId, jobUnderTest.getGroupId());
    }
    
    @Test
    public void testNameGetterAndSetter() {
        final String name = "name";
        jobUnderTest.setName(name);
        assertEquals(name, jobUnderTest.getName());
    }
    
    @Test
    public void testDescriptionGetterAndSetter() {
        final String description = "description";
        jobUnderTest.setDescription(description);
        assertEquals(description, jobUnderTest.getDescription());
    }
    
    @Test
    public void testExecuteModeGetterAndSetter() {
        final String executeMode = "executeMode";
        jobUnderTest.setExecuteMode(executeMode);
        assertEquals(executeMode, jobUnderTest.getExecuteMode());
    }
    
    @Test
    public void testContentGetterAndSetter() {
        final String content = "content";
        jobUnderTest.setContent(content);
        assertEquals(content, jobUnderTest.getContent());
    }
    
    @Test
    public void testParametersGetterAndSetter() {
        final String parameters = "parameters";
        jobUnderTest.setParameters(parameters);
        assertEquals(parameters, jobUnderTest.getParameters());
    }
    
    @Test
    public void testMaxAttemptGetterAndSetter() {
        final Integer maxAttempt = 0;
        jobUnderTest.setMaxAttempt(maxAttempt);
        assertEquals(maxAttempt, jobUnderTest.getMaxAttempt());
    }
    
    @Test
    public void testAttemptIntervalGetterAndSetter() {
        final Integer attemptInterval = 0;
        jobUnderTest.setAttemptInterval(attemptInterval);
        assertEquals(attemptInterval, jobUnderTest.getAttemptInterval());
    }
    
    @Test
    public void testContactsGetterAndSetter() {
        final Map<String, Object> contacts = new HashMap<>();
        jobUnderTest.setContacts(contacts);
        assertEquals(contacts, jobUnderTest.getContacts());
    }
    
    @Test
    public void testNamespaceGetterAndSetter() {
        final String namespace = "namespace";
        jobUnderTest.setNamespace(namespace);
        assertEquals(namespace, jobUnderTest.getNamespace());
    }
    
    @Test
    public void testJobTypeGetterAndSetter() {
        final JobType jobType = JobType.JAVA;
        jobUnderTest.setJobType(jobType);
        assertEquals(jobType, jobUnderTest.getJobType());
    }
    
    @Test
    public void testTimeTypeGetterAndSetter() {
        final TimeType timeType = TimeType.NONE;
        jobUnderTest.setTimeType(timeType);
        assertEquals(timeType, jobUnderTest.getTimeType());
    }
    
    @Test
    public void testTimeExpressionGetterAndSetter() {
        final String timeExpression = "timeExpression";
        jobUnderTest.setTimeExpression(timeExpression);
        assertEquals(timeExpression, jobUnderTest.getTimeExpression());
    }
    
    @Test
    public void testClassNameGetterAndSetter() {
        final String className = "className";
        jobUnderTest.setClassName(className);
        assertEquals(className, jobUnderTest.getClassName());
    }
    
}
