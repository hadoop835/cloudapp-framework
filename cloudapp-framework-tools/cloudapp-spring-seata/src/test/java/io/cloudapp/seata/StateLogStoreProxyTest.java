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

package io.cloudapp.seata;

import io.seata.saga.engine.store.StateLogStore;
import io.seata.saga.proctrl.ProcessContext;
import io.seata.saga.statelang.domain.StateInstance;
import io.seata.saga.statelang.domain.StateMachineInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StateLogStoreProxyTest {
    
    private static final String INSTANCE_ID = "stateMachineInstanceId";
    private static final String STATE_INSTANCE_ID = "stateInstanceId";
    private static final String MACHINE_ID = "machineInstId";
    private static final String TENANT_ID = "TENANT_ID";
    private static final String BUSINESS_KEY = "businessKey";
    @Mock
    private StateLogStore mockDelegate;
    
    private StateLogStoreProxy stateLogStoreProxyUnderTest;
    
    @Before
    public void setUp() throws Exception {
        stateLogStoreProxyUnderTest = new StateLogStoreProxy(
                mockDelegate, Collections.emptyList()
        );
    }
    
    @Test
    public void testRecordStateMachineStarted() {
        // Setup
        final StateMachineInstance instance = mock(StateMachineInstance.class);
        final ProcessContext context = mock(ProcessContext.class);
        
        when(instance.getGmtStarted()).thenReturn(new Date());
        
        // Run the test
        stateLogStoreProxyUnderTest.recordStateMachineStarted(
                instance, context
        );
        
        // Verify the results
        verify(mockDelegate).recordStateMachineStarted(
                any(StateMachineInstance.class),
                any(ProcessContext.class)
        );
    }
    
    @Test
    public void testRecordStateMachineFinished() {
        // Setup
        final StateMachineInstance instance = mock(StateMachineInstance.class);
        final ProcessContext context = mock(ProcessContext.class);
        
        when(instance.getGmtStarted()).thenReturn(new Date());
        
        // Run the test
        stateLogStoreProxyUnderTest.recordStateMachineFinished(
                instance, context
        );
        
        // Verify the results
        verify(mockDelegate).recordStateMachineFinished(
                any(StateMachineInstance.class),
                any(ProcessContext.class)
        );
    }
    
    @Test
    public void testRecordStateMachineRestarted() {
        // Setup
        final StateMachineInstance instance = mock(StateMachineInstance.class);
        final ProcessContext context = mock(ProcessContext.class);
        
        // Run the test
        stateLogStoreProxyUnderTest.recordStateMachineRestarted(
                instance, context
        );
        
        // Verify the results
        verify(mockDelegate).recordStateMachineRestarted(
                any(StateMachineInstance.class),
                any(ProcessContext.class)
        );
    }
    
    @Test
    public void testRecordStateStarted() {
        // Setup
        final StateInstance instance = mock(StateInstance.class);
        final ProcessContext context = mock(ProcessContext.class);
        
        // Run the test
        stateLogStoreProxyUnderTest.recordStateStarted(instance, context);
        
        // Verify the results
        verify(mockDelegate).recordStateStarted(
                any(StateInstance.class),
                any(ProcessContext.class)
        );
    }
    
    @Test
    public void testRecordStateFinished() {
        // Setup
        final StateInstance stateInstance = mock(StateInstance.class);
        final ProcessContext context = mock(ProcessContext.class);
        
        // Run the test
        stateLogStoreProxyUnderTest.recordStateFinished(stateInstance, context);
        
        // Verify the results
        verify(mockDelegate).recordStateFinished(
                any(StateInstance.class),
                any(ProcessContext.class)
        );
    }
    
    @Test
    public void testGetStateMachineInstance() {
        // Setup
        when(mockDelegate.getStateMachineInstance(INSTANCE_ID))
                .thenReturn(mock(StateMachineInstance.class));
        
        // Run the test
        final StateMachineInstance result = stateLogStoreProxyUnderTest.getStateMachineInstance(
                INSTANCE_ID);
        
        // Verify the results
        assertNotNull(result);
    }
    
    @Test
    public void testGetStateMachineInstanceByBusinessKey() {
        // Setup
        when(mockDelegate.getStateMachineInstanceByBusinessKey(
                BUSINESS_KEY, TENANT_ID
        )).thenReturn(mock(StateMachineInstance.class));
        
        // Run the test
        final StateMachineInstance result = stateLogStoreProxyUnderTest
                .getStateMachineInstanceByBusinessKey(BUSINESS_KEY, TENANT_ID);
        
        // Verify the results
        assertNotNull(result);
    }
    
    @Test
    public void testQueryStateMachineInstanceByParentId() {
        // Setup
        when(mockDelegate.queryStateMachineInstanceByParentId("parentId"))
                .thenReturn(Collections.emptyList());
        
        // Run the test
        final List<StateMachineInstance> result = stateLogStoreProxyUnderTest
                .queryStateMachineInstanceByParentId("parentId");
        
        // Verify the results
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testQueryStateMachineInstanceByParentId_StateLogStoreReturnsNoItems() {
        // Setup
        when(mockDelegate.queryStateMachineInstanceByParentId(
                "parentId")).thenReturn(Collections.emptyList());
        
        // Run the test
        final List<StateMachineInstance> result = stateLogStoreProxyUnderTest.queryStateMachineInstanceByParentId(
                "parentId");
        
        // Verify the results
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testGetStateInstance() {
        // Setup
        when(mockDelegate.getStateInstance(STATE_INSTANCE_ID, MACHINE_ID))
                .thenReturn(null);
        
        // Run the test
        final StateInstance result = stateLogStoreProxyUnderTest.getStateInstance(
                STATE_INSTANCE_ID, MACHINE_ID);
        
        // Verify the results
    }
    
    @Test
    public void testQueryStateInstanceListByMachineInstanceId() {
        // Setup
        when(mockDelegate.queryStateInstanceListByMachineInstanceId(
                INSTANCE_ID
        )).thenReturn(Collections.emptyList());
        
        // Run the test
        final List<StateInstance> result = stateLogStoreProxyUnderTest
                .queryStateInstanceListByMachineInstanceId(INSTANCE_ID);
        
        // Verify the results
    }
    
    @Test
    public void testQueryStateInstanceListByMachineInstanceId_StateLogStoreReturnsNoItems() {
        // Setup
        when(mockDelegate.queryStateInstanceListByMachineInstanceId(
                INSTANCE_ID)).thenReturn(Collections.emptyList());
        
        // Run the test
        final List<StateInstance> result = stateLogStoreProxyUnderTest
                .queryStateInstanceListByMachineInstanceId(INSTANCE_ID);
        
        // Verify the results
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testClearUp() {
        // Setup
        // Run the test
        stateLogStoreProxyUnderTest.clearUp();
        
        // Verify the results
        verify(mockDelegate).clearUp();
    }
    
}
