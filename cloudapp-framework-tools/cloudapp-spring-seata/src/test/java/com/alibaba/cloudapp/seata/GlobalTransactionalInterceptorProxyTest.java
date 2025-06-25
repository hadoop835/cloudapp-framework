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

import com.alibaba.cloudapp.api.seata.TransactionalInterceptor;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.model.TransactionalContext;
import io.seata.config.ConfigurationChangeEvent;
import io.seata.config.ConfigurationChangeType;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.spring.annotation.GlobalTransactionalInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GlobalTransactionalInterceptorProxyTest {
    
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private GlobalTransactionalInterceptorProxy interceptorProxy;
    
    @Mock
    private GlobalTransactionalInterceptor proxy;
    
    @Mock
    private TransactionalInterceptor interceptor;
    @Mock
    private MethodInvocation invocation;
    
    @Before
    public void setUp() throws NoSuchMethodException {
        Method method = this.getClass().getMethod("testMethod");
        GlobalTransactional gtx = method.getAnnotation(GlobalTransactional.class);
        
        when(invocation.getMethod()).thenReturn(method);
        
        interceptorProxy.setDelegate(this.getClass(), proxy);
        
        TypeVariable<Method>[] typeVariables = new TypeVariable[0];
        doNothing().when(interceptor).beforeExec(any(TransactionalContext.class));
    }
    
    @GlobalTransactional
    public void testMethod() {
    
    }
    
    @Test
    public void testInvoke() throws Throwable {
        
        // Run the test
        interceptorProxy.addInterceptor(interceptor);
        final Object result = interceptorProxy.invoke(invocation);
        
        // Verify the results
        verify(interceptor).beforeExec(any(TransactionalContext.class));
        verify(interceptor).afterExec(any(TransactionalContext.class));
    }
    
    @Test
    public void testInvoke_ThrowsThrowable() throws Throwable {
        // Setup
        doThrow(new CloudAppException("error")).when(proxy).invoke(invocation);
        
        interceptorProxy.addInterceptor(interceptor);
        
        // Run the test
        assertThrows(Throwable.class, () -> interceptorProxy.invoke(invocation));
    }
    
    @Test
    public void testOnChangeEvent() {
        // Setup
        String dataId = "dataId";
        String namespace = "namespace";
        String oldValue = "oldValue";
        String newValue = "newValue";
        
        final ConfigurationChangeEvent event = new ConfigurationChangeEvent(
                dataId, namespace, oldValue, newValue,
                ConfigurationChangeType.ADD
        );
        
        // Run the test
        interceptorProxy.setDelegate(String.class, proxy);
        interceptorProxy.onChangeEvent(event);
        
        // Verify the results
        verify(interceptorProxy).onChangeEvent(event);
    }
    
    @Test
    public void testSetDelegate() {
        
        // Verify the results
        verify(interceptorProxy).setDelegate(this.getClass(), proxy);
    }
    
}
