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

import io.seata.saga.engine.AsyncCallback;
import io.seata.saga.engine.StateMachineConfig;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.engine.exception.EngineExecutionException;
import io.seata.saga.engine.exception.ForwardInvalidException;
import io.seata.saga.engine.impl.DefaultStateMachineConfig;
import io.seata.saga.engine.store.StateLogStore;
import io.seata.saga.statelang.domain.StateMachineInstance;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class StateMachineEngineProxy extends TransactionalInterceptorProxyImpl
        implements StateMachineEngine, InitializingBean {
    
    private final StateMachineEngine delegate;
    private ApplicationContext applicationContext;
    
    
    public StateMachineEngineProxy(StateMachineEngine delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public StateMachineInstance start(
            String stateMachineName,
            String tenantId,
            Map<String, Object> startParams
    ) throws EngineExecutionException {
        
        return delegate.start(
                stateMachineName, tenantId, startParams
        );
    }
    
    @Override
    public StateMachineInstance startWithBusinessKey(
            String stateMachineName,
            String tenantId,
            String businessKey,
            Map<String, Object> startParams
    ) throws EngineExecutionException {
        
        return delegate.startWithBusinessKey(
                stateMachineName, tenantId, businessKey, startParams
        );
        
    }
    
    @Override
    public StateMachineInstance startAsync(
            String stateMachineName,
            String tenantId,
            Map<String, Object> startParams,
            AsyncCallback callback
    ) throws EngineExecutionException {
        
        return delegate.startAsync(
                stateMachineName, tenantId, startParams, callback
        );
    }
    
    @Override
    public StateMachineInstance startWithBusinessKeyAsync(
            String stateMachineName,
            String tenantId,
            String businessKey,
            Map<String, Object> startParams,
            AsyncCallback callback
    ) throws EngineExecutionException {
        
        return delegate.startWithBusinessKeyAsync(
                stateMachineName, tenantId, businessKey, startParams, callback
        );
        
    }
    
    @Override
    public StateMachineInstance forward(
            String stateMachineInstId, Map<String, Object> replaceParams
    ) throws ForwardInvalidException {
        
        return delegate.forward(stateMachineInstId, replaceParams);
    }
    
    @Override
    public StateMachineInstance forwardAsync(
            String stateMachineInstId,
            Map<String, Object> replaceParams,
            AsyncCallback callback
    ) throws ForwardInvalidException {
        return delegate.forwardAsync(
                stateMachineInstId, replaceParams, callback
        );
    }
    
    @Override
    public StateMachineInstance compensate(
            String stateMachineInstId, Map<String, Object> replaceParams
    ) throws EngineExecutionException {
        
        return delegate.compensate(
                stateMachineInstId, replaceParams
        );
    }
    
    @Override
    public StateMachineInstance compensateAsync(
            String stateMachineInstId,
            Map<String, Object> replaceParams,
            AsyncCallback callback
    ) throws EngineExecutionException {
        
        return delegate.compensateAsync(
                stateMachineInstId, replaceParams, callback
        );
    }
    
    @Override
    public StateMachineInstance skipAndForward(
            String stateMachineInstId, Map<String, Object> replaceParams
    ) throws EngineExecutionException {
        
        return delegate.skipAndForward(
                stateMachineInstId, replaceParams
        );
    }
    
    @Override
    public StateMachineInstance skipAndForwardAsync(
            String stateMachineInstId, AsyncCallback callback
    ) throws EngineExecutionException {
        
        return delegate.skipAndForwardAsync(
                stateMachineInstId, callback
        );
    }
    
    @Override
    public StateMachineConfig getStateMachineConfig() {
        return delegate.getStateMachineConfig();
    }
    
    @Override
    public StateMachineInstance reloadStateMachineInstance(String instId) {
        return delegate.reloadStateMachineInstance(instId);
    }
    
    @Override
    public void afterPropertiesSet() {
        StateLogStore logStore = delegate.getStateMachineConfig()
                                         .getStateLogStore();
        
        if (!(logStore instanceof StateLogStoreProxy)) {
            StateLogStoreProxy proxy = new StateLogStoreProxy(
                    logStore, interceptors);
            
            ((DefaultStateMachineConfig) delegate.getStateMachineConfig())
                    .setStateLogStore(proxy);
        }
    }
    
}
