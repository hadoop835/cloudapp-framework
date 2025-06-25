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

import io.seata.saga.engine.AsyncCallback;
import io.seata.saga.engine.StateMachineConfig;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.engine.exception.EngineExecutionException;
import io.seata.saga.engine.exception.ForwardInvalidException;
import io.seata.saga.statelang.domain.StateMachineInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StateMachineEngineProxyTest {
    
    private static final String NAME = "machineName";
    private static final String TENANT = "tenantId";
    private static final String BUSINESS_KEY = "businessKey";
    private final String stateMachineInstId = "stateMachineInstId";
    
    @Mock
    private StateMachineEngine mockDelegate;
    @Mock
    private StateMachineInstance instance;
    
    private StateMachineEngineProxy machineEngineProxy;
    
    @Before
    public void setUp() {
        machineEngineProxy = new StateMachineEngineProxy(mockDelegate);
    }
    
    @Test
    public void testStart() {
        // Setup
        final Map<String, Object> startParams = new HashMap<>();
        when(mockDelegate.start(NAME, TENANT, new HashMap<>()))
                .thenReturn(instance);
        
        // Run the test
        final StateMachineInstance result = machineEngineProxy
                .start(NAME, TENANT, startParams);
        
        // Verify the results
        assertEquals(instance, result);
    }
    
    @Test
    public void testStart_StateMachineEngineThrowsEngineExecutionException() {
        // Setup
        final Map<String, Object> startParams = new HashMap<>();
        when(mockDelegate.start(NAME, TENANT, new HashMap<>()))
                .thenThrow(EngineExecutionException.class);
        
        // Run the test
        assertThrows(EngineExecutionException.class,
                     () -> machineEngineProxy.start(
                             NAME, TENANT, startParams)
        );
    }
    
    @Test
    public void testStartWithBusinessKey() {
        // Setup
        final Map<String, Object> startParams = new HashMap<>();
        when(mockDelegate.startWithBusinessKey(
                NAME, TENANT, BUSINESS_KEY, new HashMap<>()
        )).thenReturn(null);
        
        when(mockDelegate.startWithBusinessKey(
                NAME, TENANT, BUSINESS_KEY, startParams
        )).thenReturn(instance);
        
        // Run the test
        final StateMachineInstance result = machineEngineProxy.startWithBusinessKey(
                NAME, TENANT, BUSINESS_KEY, startParams);
        
        // Verify the results
        assertEquals(instance, result);
    }
    
    @Test
    public void testStartWithBusinessKey_StateMachineEngineThrowsEngineExecutionException() {
        // Setup
        final Map<String, Object> startParams = new HashMap<>();
        when(mockDelegate.startWithBusinessKey(
                NAME, TENANT, BUSINESS_KEY, new HashMap<>()
        )).thenThrow(EngineExecutionException.class);
        
        // Run the test
        assertThrows(EngineExecutionException.class,
                     () -> machineEngineProxy.startWithBusinessKey(
                             NAME, TENANT, BUSINESS_KEY, startParams
                     )
        );
    }
    
    @Test
    public void testStartAsync() {
        // Setup
        final Map<String, Object> startParams = new HashMap<>();
        final AsyncCallback mockCallback = mock(AsyncCallback.class);
        when(mockDelegate.startAsync(
                eq(NAME), eq(TENANT), eq(new HashMap<>()),
                any(AsyncCallback.class)
        )).thenReturn(null);
        
        when(mockDelegate.startAsync(
                NAME, TENANT, startParams, mockCallback
        )).thenReturn(instance);
        
        
        // Run the test
        final StateMachineInstance result = machineEngineProxy.startAsync(
                NAME, TENANT, startParams, mockCallback);
        
        // Verify the results
        assertEquals(instance, result);
    }
    
    @Test
    public void testStartAsync_StateMachineEngineThrowsEngineExecutionException() {
        // Setup
        final Map<String, Object> startParams = new HashMap<>();
        final AsyncCallback mockCallback = mock(AsyncCallback.class);
        when(mockDelegate.startAsync(
                eq(NAME), eq(TENANT), eq(new HashMap<>()),
                any(AsyncCallback.class)
        )).thenThrow(EngineExecutionException.class);
        
        // Run the test
        assertThrows(EngineExecutionException.class,
                     () -> machineEngineProxy.startAsync(
                             NAME, TENANT, startParams, mockCallback
                     )
        );
    }
    
    @Test
    public void testStartWithBusinessKeyAsync() {
        // Setup
        final Map<String, Object> startParams = new HashMap<>();
        final AsyncCallback mockCallback = mock(AsyncCallback.class);
        when(mockDelegate.startWithBusinessKeyAsync(
                eq(NAME), eq(TENANT), eq(BUSINESS_KEY),
                eq(new HashMap<>()), any(AsyncCallback.class)
        )).thenReturn(instance);
        
        // Run the test
        final StateMachineInstance result = machineEngineProxy.startWithBusinessKeyAsync(
                NAME, TENANT, BUSINESS_KEY, startParams, mockCallback
        );
        
        // Verify the results
        assertEquals(instance, result);
    }
    
    @Test
    public void testStartWithBusinessKeyAsync_StateMachineEngineThrowsEngineExecutionException() {
        // Setup
        final Map<String, Object> startParams = new HashMap<>();
        final AsyncCallback mockCallback = mock(AsyncCallback.class);
        when(mockDelegate.startWithBusinessKeyAsync(
                eq(NAME), eq(TENANT), eq(BUSINESS_KEY),
                eq(new HashMap<>()), any(AsyncCallback.class)
        )).thenThrow(EngineExecutionException.class);
        
        // Run the test
        assertThrows(EngineExecutionException.class,
                     () -> machineEngineProxy.startWithBusinessKeyAsync(
                             NAME, TENANT, BUSINESS_KEY, startParams,
                             mockCallback
                     )
        );
    }
    
    @Test
    public void testForward() {
        // Setup
        final Map<String, Object> replaceParams = new HashMap<>();
        when(mockDelegate.forward(stateMachineInstId,
                                  new HashMap<>()
        )).thenReturn(instance);
        
        // Run the test
        final StateMachineInstance result = machineEngineProxy.forward(
                stateMachineInstId, replaceParams);
        
        // Verify the results
        assertEquals(instance, result);
    }
    
    @Test
    public void testForward_StateMachineEngineThrowsForwardInvalidException() {
        // Setup
        final Map<String, Object> replaceParams = new HashMap<>();
        when(mockDelegate.forward(stateMachineInstId, new HashMap<>()))
                .thenThrow(ForwardInvalidException.class);
        
        // Run the test
        assertThrows(ForwardInvalidException.class,
                     () -> machineEngineProxy.forward(
                             stateMachineInstId, replaceParams)
        );
    }
    
    @Test
    public void testForwardAsync() {
        // Setup
        final Map<String, Object> replaceParams = new HashMap<>();
        final AsyncCallback mockCallback = mock(AsyncCallback.class);
        when(mockDelegate.forwardAsync(eq(stateMachineInstId),
                                       eq(new HashMap<>()),
                                       any(AsyncCallback.class)
        )).thenReturn(instance);
        
        // Run the test
        final StateMachineInstance result = machineEngineProxy.forwardAsync(
                stateMachineInstId, replaceParams, mockCallback);
        
        // Verify the results
        assertEquals(instance, result);
    }
    
    @Test
    public void testForwardAsync_StateMachineEngineThrowsForwardInvalidException() {
        // Setup
        final Map<String, Object> replaceParams = new HashMap<>();
        final AsyncCallback mockCallback = mock(AsyncCallback.class);
        when(mockDelegate.forwardAsync(eq(stateMachineInstId),
                                       eq(new HashMap<>()),
                                       any(AsyncCallback.class)
        )).thenThrow(ForwardInvalidException.class);
        
        // Run the test
        assertThrows(ForwardInvalidException.class,
                     () -> machineEngineProxy.forwardAsync(
                             stateMachineInstId, replaceParams, mockCallback)
        );
    }
    
    @Test
    public void testCompensate() {
        // Setup
        final Map<String, Object> replaceParams = new HashMap<>();
        when(mockDelegate.compensate(stateMachineInstId, new HashMap<>()))
                .thenReturn(instance);
        
        // Run the test
        final StateMachineInstance result = machineEngineProxy.compensate(
                stateMachineInstId, replaceParams);
        
        // Verify the results
        assertEquals(instance, result);
    }
    
    @Test
    public void testCompensate_StateMachineEngineThrowsEngineExecutionException() {
        // Setup
        final Map<String, Object> replaceParams = new HashMap<>();
        when(mockDelegate.compensate(stateMachineInstId, new HashMap<>()))
                .thenThrow(EngineExecutionException.class);
        
        // Run the test
        assertThrows(EngineExecutionException.class,
                     () -> machineEngineProxy.compensate(
                             stateMachineInstId, replaceParams)
        );
    }
    
    @Test
    public void testCompensateAsync() {
        // Setup
        final Map<String, Object> replaceParams = new HashMap<>();
        final AsyncCallback mockCallback = mock(AsyncCallback.class);
        when(mockDelegate.compensateAsync(
                eq(stateMachineInstId), eq(new HashMap<>()),
                any(AsyncCallback.class)
        )).thenReturn(instance);
        
        // Run the test
        final StateMachineInstance result = machineEngineProxy.compensateAsync(
                stateMachineInstId, replaceParams, mockCallback);
        
        // Verify the results
        assertEquals(instance, result);
    }
    
    @Test
    public void testCompensateAsync_StateMachineEngineThrowsEngineExecutionException() {
        // Setup
        final Map<String, Object> replaceParams = new HashMap<>();
        final AsyncCallback mockCallback = mock(AsyncCallback.class);
        when(mockDelegate.compensateAsync(eq(stateMachineInstId),
                                          eq(new HashMap<>()),
                                          any(AsyncCallback.class)
        )).thenThrow(EngineExecutionException.class);
        
        // Run the test
        assertThrows(EngineExecutionException.class,
                     () -> machineEngineProxy.compensateAsync(
                             stateMachineInstId, replaceParams, mockCallback)
        );
    }
    
    @Test
    public void testSkipAndForward() {
        // Setup
        final Map<String, Object> replaceParams = new HashMap<>();
        when(mockDelegate.skipAndForward(stateMachineInstId,
                                         new HashMap<>()
        )).thenReturn(instance);
        
        // Run the test
        final StateMachineInstance result = machineEngineProxy.skipAndForward(
                stateMachineInstId, replaceParams);
        
        // Verify the results
        assertEquals(instance, result);
    }
    
    @Test
    public void testSkipAndForward_StateMachineEngineThrowsEngineExecutionException() {
        // Setup
        final Map<String, Object> replaceParams = new HashMap<>();
        when(mockDelegate.skipAndForward(stateMachineInstId,
                                         new HashMap<>()
        )).thenThrow(EngineExecutionException.class);
        
        // Run the test
        assertThrows(EngineExecutionException.class,
                     () -> machineEngineProxy.skipAndForward(
                             stateMachineInstId, replaceParams)
        );
    }
    
    @Test
    public void testSkipAndForwardAsync() {
        // Setup
        final AsyncCallback mockCallback = mock(AsyncCallback.class);
        when(mockDelegate.skipAndForwardAsync(eq(stateMachineInstId),
                                              any(AsyncCallback.class)
        )).thenReturn(instance);
        
        // Run the test
        final StateMachineInstance result = machineEngineProxy.skipAndForwardAsync(
                stateMachineInstId, mockCallback);
        
        // Verify the results
        assertEquals(instance, result);
    }
    
    @Test
    public void testSkipAndForwardAsync_StateMachineEngineThrowsEngineExecutionException() {
        // Setup
        final AsyncCallback mockCallback = mock(AsyncCallback.class);
        when(mockDelegate.skipAndForwardAsync(eq(stateMachineInstId),
                                              any(AsyncCallback.class)
        )).thenThrow(EngineExecutionException.class);
        
        // Run the test
        assertThrows(EngineExecutionException.class,
                     () -> machineEngineProxy.skipAndForwardAsync(
                             stateMachineInstId, mockCallback)
        );
    }
    
    @Test
    public void testGetStateMachineConfig() {
        // Setup
        when(mockDelegate.getStateMachineConfig()).thenReturn(null);
        
        // Run the test
        final StateMachineConfig result = machineEngineProxy.getStateMachineConfig();
        
        // Verify the results
        assertNull(result);
    }
    
    @Test
    public void testReloadStateMachineInstance() {
        // Setup
        when(mockDelegate.reloadStateMachineInstance("instId"))
                .thenReturn(instance);
        
        // Run the test
        final StateMachineInstance result = machineEngineProxy.reloadStateMachineInstance(
                "instId");
        
        // Verify the results
        assertEquals(instance, result);
    }
    
}
