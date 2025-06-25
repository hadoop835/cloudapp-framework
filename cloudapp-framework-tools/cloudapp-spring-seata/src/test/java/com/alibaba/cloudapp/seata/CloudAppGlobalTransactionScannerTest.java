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

import io.seata.tm.api.FailureHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.aop.TargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CloudAppGlobalTransactionScannerTest {
    
    private static final String APPLICATION_ID = "applicationId";
    private static final String TX_SERVICE_GROUP = "txServiceGroup";
    private static final Integer DEFAULT_MODE = 3;
    private static final String BEAN_NAME = "beanName";
    private static final TargetSource TARGET_SOURCE = mock(TargetSource.class);
    
    @Mock
    private FailureHandler mockFailureHandlerHook;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private CloudAppGlobalTransactionScanner scanner;
    @Mock
    private GlobalTransactionalInterceptorProxy proxy;
    @Mock
    private ApplicationContext applicationContext;
    
    
    @Before
    public void setUp() {
        
        when(applicationContext.getBean(eq(GlobalTransactionalInterceptorProxy.class)))
                .thenReturn(proxy);
        
        scanner.setApplicationContext(applicationContext);
    }
    
    @Test
    public void testGetAdvicesAndAdvisorsForBean() {
        
        assertArrayEquals(new Object[]{proxy},
                          scanner.getAdvicesAndAdvisorsForBean(
                                  Object.class, BEAN_NAME, TARGET_SOURCE)
        );
    }
    
    @Test
    public void testWrapIfNecessary() {
        // Setup
        Object bean = new Object();
        // Run the test
        final Object result = scanner.wrapIfNecessary(
                bean, BEAN_NAME, "cacheKey");
        
        // Verify the results
        assertEquals(bean, result);
    }
    
    @Test
    public void testSetApplicationContext() {
        // Setup
        
        // Run the test
        scanner.setApplicationContext(applicationContext);
        
        // Verify the results
        Object[] result = scanner.getAdvicesAndAdvisorsForBean(
                Object.class, BEAN_NAME, TARGET_SOURCE
        );
        assertNotNull(result);
        assertEquals(proxy, result[0]);
    }
    
    @Test
    public void testSetApplicationContext_ThrowsApplicationContextException() {
        // Setup
        when(applicationContext.getBean(GlobalTransactionalInterceptorProxy.class))
                .thenThrow(new ApplicationContextException("message"));
        // Run the test
        assertThrows(ApplicationContextException.class,
                     () -> scanner.setApplicationContext(
                             applicationContext)
        );
    }
    
    @Test
    public void testSetApplicationContext_ThrowsBeansException() {
        // Setup
        when(applicationContext.getBean(GlobalTransactionalInterceptorProxy.class))
                .thenThrow(new NoSuchBeanDefinitionException("message"));
        
        // Run the test
        assertThrows(BeansException.class,
                     () -> scanner.setApplicationContext(
                             applicationContext)
        );
    }
    
}
