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

import io.cloudapp.api.seata.TransactionalInterceptor;
import io.cloudapp.model.TransactionalContext;
import io.seata.saga.engine.store.StateLogStore;
import io.seata.saga.proctrl.ProcessContext;
import io.seata.saga.statelang.domain.ExecutionStatus;
import io.seata.saga.statelang.domain.StateInstance;
import io.seata.saga.statelang.domain.StateMachineInstance;

import java.util.List;

public class StateLogStoreProxy implements StateLogStore {
    
    private final StateLogStore delegate;
    private final List<TransactionalInterceptor> interceptors;
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#recordStateMachineStarted(
     * io.seata.saga.statelang.domain.StateMachineInstance,
     * io.seata.saga.proctrl.ProcessContext)
     */
    public StateLogStoreProxy(
            StateLogStore delegate,
            List<TransactionalInterceptor> interceptors
    ) {
        this.delegate = delegate;
        this.interceptors = interceptors;
    }
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#recordStateMachineStarted(
     *     io.seata.saga.statelang.domain.StateMachineInstance,
     *     io.seata.saga.proctrl.ProcessContext
     * )
     */
    @Override
    public void recordStateMachineStarted(
            StateMachineInstance instance, ProcessContext context
    ) {
        TransactionalContext transCtx = createTransactionContext(instance);
        
        interceptors.forEach(interceptor -> interceptor.beforeExec(transCtx));
        delegate.recordStateMachineStarted(instance, context);
    }
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#recordStateMachineFinished(
     *     io.seata.saga.statelang.domain.StateMachineInstance,
     *     io.seata.saga.proctrl.ProcessContext
     * )
     */
    @Override
    public void recordStateMachineFinished(
            StateMachineInstance instance, ProcessContext context
    ) {
        TransactionalContext transCtx = createTransactionContext(instance);
        transCtx.setSuccess(ExecutionStatus.SU.equals(
                instance.getStatus()
        ));
        transCtx.setReturnValue(instance.getEndParams());
        
        interceptors.forEach(interceptor -> interceptor.afterExec(transCtx));
        delegate.recordStateMachineFinished(instance, context);
    }
    
    /**
     * get transactionContext from state machine instance
     */
    private static TransactionalContext createTransactionContext(
            StateMachineInstance instance) {
        TransactionalContext transactionalContext = new TransactionalContext();
        transactionalContext.setTransactionName(
                instance.getStateMachine() != null ?
                        instance.getStateMachine().getName()
                        : instance.getMachineId()
        );
        transactionalContext.setTenantId(instance.getTenantId());
        transactionalContext.setXid(instance.getId());
        transactionalContext.setBusinessKey(instance.getBusinessKey());
        transactionalContext.setParams(instance.getStartParams());
        transactionalContext.setStartTime(instance.getGmtStarted().getTime());
        return transactionalContext;
    }
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#recordStateMachineRestarted(
     *     io.seata.saga.statelang.domain.StateMachineInstance,
     *     io.seata.saga.proctrl.ProcessContext
     * )
     */
    @Override
    public void recordStateMachineRestarted(
            StateMachineInstance machineInstance, ProcessContext context
    ) {
        delegate.recordStateMachineRestarted(machineInstance, context);
    }
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#recordStateStarted(
     *     io.seata.saga.statelang.domain.StateInstance,
     *     io.seata.saga.proctrl.ProcessContext
     * )
     */
    @Override
    public void recordStateStarted(
            StateInstance stateInstance, ProcessContext context
    ) {
        delegate.recordStateStarted(stateInstance, context);
    }
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#recordStateFinished(
     *     io.seata.saga.statelang.domain.StateInstance,
     *     io.seata.saga.proctrl.ProcessContext
     * )
     */
    @Override
    public void recordStateFinished(
            StateInstance stateInstance, ProcessContext context
    ) {
        delegate.recordStateFinished(stateInstance, context);
    }
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#getStateMachineInstance(
     *     java.lang.String
     * )
     */
    @Override
    public StateMachineInstance getStateMachineInstance(
            String stateMachineInstanceId
    ) {
        return delegate.getStateMachineInstance(stateMachineInstanceId);
    }
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#getStateMachineInstanceByBusinessKey(
     *     java.lang.String, java.lang.String
     * )
     */
    @Override
    public StateMachineInstance getStateMachineInstanceByBusinessKey(
            String businessKey, String tenantId
    ) {
        return delegate.getStateMachineInstanceByBusinessKey(
                businessKey, tenantId
        );
    }
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#queryStateMachineInstanceByParentId(java.lang.String)
     */
    @Override
    public List<StateMachineInstance> queryStateMachineInstanceByParentId(String parentId) {
        return delegate.queryStateMachineInstanceByParentId(parentId);
    }
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#getStateInstance(
     *     java.lang.String, java.lang.String
     * )
     */
    @Override
    public StateInstance getStateInstance(
            String stateInstanceId, String machineInstId
    ) {
        return delegate.getStateInstance(stateInstanceId, machineInstId);
    }
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#queryStateInstanceListByMachineInstanceId(
     *      java.lang.String
     * )
     */
    @Override
    public List<StateInstance> queryStateInstanceListByMachineInstanceId(
            String stateMachineInstanceId
    ) {
        return delegate.queryStateInstanceListByMachineInstanceId(
                stateMachineInstanceId);
    }
    
    /**
     * @see io.seata.saga.engine.store.StateLogStore#clearUp()
     */
    @Override
    public void clearUp() {
        delegate.clearUp();
    }
    
}
