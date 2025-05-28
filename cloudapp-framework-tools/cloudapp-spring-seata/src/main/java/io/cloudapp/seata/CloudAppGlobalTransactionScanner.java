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

import io.cloudapp.exeption.CloudAppException;
import io.seata.spring.annotation.GlobalTransactionScanner;
import io.seata.tm.api.DefaultFailureHandlerImpl;
import io.seata.tm.api.FailureHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CloudAppGlobalTransactionScanner extends GlobalTransactionScanner
        implements ApplicationContextAware {
    
    private static final Logger logger =
            LoggerFactory.getLogger(CloudAppGlobalTransactionScanner.class);
    
    private GlobalTransactionalInterceptorProxy interceptor;
    
    private final static int DEFAULT_MODE = 3;
    private static final FailureHandler DEFAULT_FAIL_HANDLER = new DefaultFailureHandlerImpl();
    
    public CloudAppGlobalTransactionScanner(String txServiceGroup) {
        this(txServiceGroup, txServiceGroup, DEFAULT_MODE);
    }
    
    public CloudAppGlobalTransactionScanner(String txServiceGroup, int mode) {
        this(txServiceGroup, txServiceGroup, mode);
    }
    
    public CloudAppGlobalTransactionScanner(
            String applicationId, String txServiceGroup) {
        this(applicationId, txServiceGroup, DEFAULT_MODE);
    }
    
    public CloudAppGlobalTransactionScanner(
            String applicationId, String txServiceGroup, int mode) {
        this(applicationId, txServiceGroup, mode, DEFAULT_FAIL_HANDLER);
    }
    
    public CloudAppGlobalTransactionScanner(
            String applicationId,
            String txServiceGroup,
            FailureHandler failureHandlerHook) {
        this(applicationId, txServiceGroup, DEFAULT_MODE, failureHandlerHook);
    }
    
    public CloudAppGlobalTransactionScanner(
            String applicationId,
            String txServiceGroup,
            int mode,
            FailureHandler failureHandlerHook) {
        super(applicationId, txServiceGroup, mode, failureHandlerHook);
    }
    
    
    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(
            Class beanClass, String beanName, TargetSource customTargetSource
    ) throws BeansException {
        return new Object[]{interceptor};
    }
    
    @Override
    protected Object wrapIfNecessary(Object bean, String beanName,
                                     Object cacheKey) {
        Object retBean = super.wrapIfNecessary(bean, beanName, cacheKey);
        if (interceptor == null) {
            throw new IllegalStateException(
                    "Bean TransactionalInterceptorProxy is defined");
        }
        
        Object[] interceptors = super.getAdvicesAndAdvisorsForBean(
                MethodInterceptor.class, beanName, null);
        
        if (interceptors == null || interceptors.length == 0) {
            throw new CloudAppException("No TransactionalInterceptor found");
        }
        
        if (interceptors[0] != null) {
            Class<?> clazz = AopProxyUtils.ultimateTargetClass(retBean);
            interceptor.setDelegate(
                    clazz, (MethodInterceptor) interceptors[0]);
        }
        
        return retBean;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.interceptor = applicationContext.getBean(
                GlobalTransactionalInterceptorProxy.class);
    }
    
}
