package com.alibaba.cloudapp.api.scheduler.worker;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JobGroupMetadataTest {
    
    private JobGroupMetadata groupMetadata;
    
    @Before
    public void setUp() throws Exception {
        groupMetadata = new JobGroupMetadata();
    }
    
    @Test
    public void testIdGetterAndSetter() {
        final Integer id = 0;
        groupMetadata.setId(id);
        assertEquals(id, groupMetadata.getId());
    }
    
    @Test
    public void testAppNameGetterAndSetter() {
        final String appName = "appName";
        groupMetadata.setAppName(appName);
        assertEquals(appName, groupMetadata.getAppName());
    }
    
    @Test
    public void testTitleGetterAndSetter() {
        final String title = "title";
        groupMetadata.setTitle(title);
        assertEquals(title, groupMetadata.getTitle());
    }
    
    @Test
    public void testAuthorGetterAndSetter() {
        final String author = "author";
        groupMetadata.setAuthor(author);
        assertEquals(author, groupMetadata.getAuthor());
    }
    
    @Test
    public void testAlarmEmailGetterAndSetter() {
        final String alarmEmail = "alarmEmail";
        groupMetadata.setAlarmEmail(alarmEmail);
        assertEquals(alarmEmail, groupMetadata.getAlarmEmail());
    }
    
}
