package com.alibaba.cloudapp.api.scheduler.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JobGroupTest {
    
    private JobGroup jobGroupUnderTest;
    
    @Before
    public void setUp() throws Exception {
        jobGroupUnderTest = new JobGroup();
    }
    
    @Test
    public void testNameGetterAndSetter() {
        final String name = "name";
        jobGroupUnderTest.setName(name);
        assertEquals(name, jobGroupUnderTest.getName());
    }
    
    @Test
    public void testDescriptionGetterAndSetter() {
        final String description = "description";
        jobGroupUnderTest.setDescription(description);
        assertEquals(description, jobGroupUnderTest.getDescription());
    }
    
    @Test
    public void testAppNameGetterAndSetter() {
        final String appName = "appName";
        jobGroupUnderTest.setAppName(appName);
        assertEquals(appName, jobGroupUnderTest.getAppName());
    }
    
    @Test
    public void testNamespaceGetterAndSetter() {
        final String namespace = "namespace";
        jobGroupUnderTest.setNamespace(namespace);
        assertEquals(namespace, jobGroupUnderTest.getNamespace());
    }
    
    @Test
    public void testGroupIdGetterAndSetter() {
        final String groupId = "groupId";
        jobGroupUnderTest.setGroupId(groupId);
        assertEquals(groupId, jobGroupUnderTest.getGroupId());
    }
    
}
