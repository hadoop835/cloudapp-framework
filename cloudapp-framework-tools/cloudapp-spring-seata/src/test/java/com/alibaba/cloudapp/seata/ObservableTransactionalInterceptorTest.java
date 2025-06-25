/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.alibaba.cloudapp.seata;

import com.alibaba.cloudapp.model.TransactionalContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ObservableTransactionalInterceptorTest {
    
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private GlobalTransactionHolder mockHolder;
    
    private ObservableTransactionalInterceptor interceptor;
    private TransactionalContext context;
    
    @Before
    public void setUp() {
        interceptor = new ObservableTransactionalInterceptor(mockHolder);
        context = new TransactionalContext();
        context.setXid("xid");
        context.setBranchId("branchId");
        context.setTransactionName("transactionName");
        context.setTenantId("tenantId");
        context.setBusinessKey("businessKey");
        context.setParams(null);
        context.setStartTime(System.currentTimeMillis());
    }
    
    @Test
    public void testBeforeExec() {
        // Setup
        
        when(mockHolder.getCount()).thenReturn(0L);
        when(mockHolder.getRunningGauge()).thenReturn(0L);
        
        // Run the test
        interceptor.beforeExec(context);
        
        // Verify the results
        verify(mockHolder).setCount(1L);
        verify(mockHolder).setRunningGauge(1L);
    }
    
    @Test
    public void testAfterExec() {
        // Setup
        when(mockHolder.getMaxRunningTime()).thenReturn(0L);
        when(mockHolder.getMinRunningTime()).thenReturn(0L);
        
        // Run the test
        context.setSuccess(false);
        context.setReturnValue("");
        
        interceptor.afterExec(context);
        
        int noCall = 0;
        // Verify the results
        verify(mockHolder).setRunningGauge(anyLong());
        verify(mockHolder, times(noCall)).setSucceedCount(anyLong());
        verify(mockHolder).setFailedCount(anyLong());
        verify(mockHolder, times(noCall)).setMinRunningTime(anyLong());
    }
    
}
