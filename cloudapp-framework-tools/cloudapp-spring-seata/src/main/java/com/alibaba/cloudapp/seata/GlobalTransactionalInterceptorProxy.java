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
import com.alibaba.cloudapp.model.TransactionalContext;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.seata.config.ConfigurationChangeEvent;
import io.seata.config.ConfigurationChangeListener;
import io.seata.spring.annotation.GlobalLock;
import io.seata.spring.annotation.GlobalTransactional;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GlobalTransactionalInterceptorProxy extends TransactionalInterceptorProxyImpl
        implements MethodInterceptor, ConfigurationChangeListener {
    
    private static final Map<Class<?>, MethodInterceptor> beanIntercepterMap
            = Collections.synchronizedMap(new HashMap<>());
    
    public GlobalTransactionalInterceptorProxy() { }
    
    @Override
    @WithSpan
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        MethodInterceptor delegate = beanIntercepterMap.get(
                methodInvocation.getMethod().getDeclaringClass());
        
        TransactionalContext context = getTransactionalContext(methodInvocation);
        try {
            
            for (TransactionalInterceptor interceptor : interceptors) {
                interceptor.beforeExec(context);
            }
            
            Object result = delegate.invoke(methodInvocation);
            
            context.setSuccess(true);
            context.setReturnValue(result);
            return result;
            
        } finally {
            for (TransactionalInterceptor interceptor : interceptors) {
                interceptor.afterExec(context);
            }
        }
    }
    
    @Override
    public void onChangeEvent(ConfigurationChangeEvent event) {
        beanIntercepterMap.values().forEach(delegate -> {
            if(delegate instanceof ConfigurationChangeListener
                    && event.getNewValue() != null) {
                ((ConfigurationChangeListener) delegate).onChangeEvent(event);
            }
        });
    }
    
    public void setDelegate(Class<?> beanName, MethodInterceptor delegate) {
        beanIntercepterMap.put(beanName, delegate);
    }
    
    private TransactionalContext getTransactionalContext(MethodInvocation methodInvocation) {
        Class<?> targetClass = methodInvocation.getThis() != null ?
                AopUtils.getTargetClass(methodInvocation.getThis()) : null;
        
        TransactionalContext context = new TransactionalContext();
        
        Method specificMethod = ClassUtils.getMostSpecificMethod(methodInvocation.getMethod(), targetClass);
        final Method method = BridgeMethodResolver.findBridgedMethod(specificMethod);
        
        context.setAnnotations(method.getAnnotations());
        
        final GlobalTransactional transactional =
                method.getAnnotation(GlobalTransactional.class);
        final GlobalLock lock = method.getAnnotation(GlobalLock.class);
        
        if(transactional == null && lock == null) {
            return context;
        }
        
        String serviceName = transactional != null ? transactional.name()
                : "globalLock" + methodInvocation.getMethod().getName();
        
        serviceName = StringUtils.hasText(serviceName) ? serviceName : "";
        
        context.setTransactionName(serviceName);
        context.setStartTime(System.currentTimeMillis());
        
        Object[] values = methodInvocation.getArguments();
        TypeVariable<Method>[] typeParameters = method.getTypeParameters();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < typeParameters.length; i++) {
            params.put(typeParameters[i].getName(), values[i]);
        }
        
        context.setParams(params);
        return context;
    }
    
}
