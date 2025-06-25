package com.alibaba.cloudapp.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MailUserTest {
    
    private MailUser mailUser;
    
    @Before
    public void setUp() throws Exception {
        mailUser = new MailUser();
    }
    
    @Test
    public void testFromGetterAndSetter() {
        final String from = "from";
        mailUser.setFrom(from);
        assertEquals(from, mailUser.getFrom());
    }
    
    @Test
    public void testReplyToGetterAndSetter() {
        final String replyTo = "replyTo";
        mailUser.setReplyTo(replyTo);
        assertEquals(replyTo, mailUser.getReplyTo());
    }
    
    @Test
    public void testSetTo1() {
        // Setup
        // Run the test
        final MailUser result = mailUser.setTo("to");
        
        // Verify the results
    }
    
    @Test
    public void testToGetterAndSetter() {
        final String to = "to";
        mailUser.setTo(to);
        
        assert Arrays.asList(mailUser.getTo()).contains(to);
    }
    
    @Test
    public void testSetCc1() {
        // Setup
        // Run the test
        final MailUser result = mailUser.setCc("cc");
        
        // Verify the results
    }
    
    @Test
    public void testCcGetterAndSetter() {
        final String cc = "cc";
        mailUser.setCc(cc);
        
        assert Arrays.asList(mailUser.getCc()).contains(cc);
    }
    
    @Test
    public void testSetBcc1() {
        // Setup
        // Run the test
        final MailUser result = mailUser.setBcc("bcc");
        
        // Verify the results
    }
    
    @Test
    public void testBccGetterAndSetter() {
        final String bcc = "bcc";
        mailUser.setBcc(bcc);
        
        assert Arrays.asList(mailUser.getBcc()).contains(bcc);
    }
    
}
