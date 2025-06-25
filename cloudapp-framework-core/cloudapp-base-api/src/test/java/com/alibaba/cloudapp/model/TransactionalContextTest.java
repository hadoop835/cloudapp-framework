package com.alibaba.cloudapp.model;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TransactionalContextTest {
    
    private TransactionalContext context;
    
    @Before
    public void setUp() throws Exception {
        context = new TransactionalContext();
    }
    
    @Test
    public void testSuccessGetterAndSetter() {
        final boolean success = false;
        context.setSuccess(success);
        assertFalse(context.isSuccess());
    }
    
    @Test
    public void testXidGetterAndSetter() {
        final String xid = "xid";
        context.setXid(xid);
        assertEquals(xid, context.getXid());
    }
    
    @Test
    public void testBranchIdGetterAndSetter() {
        final String branchId = "branchId";
        context.setBranchId(branchId);
        assertEquals(branchId, context.getBranchId());
    }
    
    @Test
    public void testTransactionNameGetterAndSetter() {
        final String transactionName = "transactionName";
        context.setTransactionName(transactionName);
        assertEquals(transactionName,
                     context.getTransactionName()
        );
    }
    
    @Test
    public void testTenantIdGetterAndSetter() {
        final String tenantId = "tenantId";
        context.setTenantId(tenantId);
        assertEquals(tenantId, context.getTenantId());
    }
    
    @Test
    public void testBusinessKeyGetterAndSetter() {
        final String businessKey = "businessKey";
        context.setBusinessKey(businessKey);
        assertEquals(businessKey,
                     context.getBusinessKey()
        );
    }
    
    @Test
    public void testParamsGetterAndSetter() {
        final Map<String, Object> params = new HashMap<>();
        context.setParams(params);
        assertEquals(params, context.getParams());
    }
    
    @Test
    public void testAnnotationsGetterAndSetter() {
        final Annotation[] annotations = new Annotation[]{};
        context.setAnnotations(annotations);
        assertArrayEquals(annotations,
                          context.getAnnotations()
        );
    }
    
    @Test
    public void testReturnValueGetterAndSetter() {
        final Object returnValue = "returnValue";
        context.setReturnValue(returnValue);
        assertEquals(returnValue,
                     context.getReturnValue()
        );
    }
    
    @Test
    public void testStartTimeGetterAndSetter() {
        final long startTime = 0L;
        context.setStartTime(startTime);
        assertEquals(startTime, context.getStartTime());
    }
    
}
