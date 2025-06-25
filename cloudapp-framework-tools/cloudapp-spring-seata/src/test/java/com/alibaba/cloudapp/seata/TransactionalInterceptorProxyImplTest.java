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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionalInterceptorProxyImplTest {
    
    @Mock
    private ConfigurableApplicationContext applicationContext;
    
    private TransactionalInterceptorProxyImpl interceptorProxy;
    
    @Before
    public void setUp() throws Exception {
        interceptorProxy = new TransactionalInterceptorProxyImpl();
        interceptorProxy.setApplicationContext(applicationContext);
    }
    
    @Test
    public void testGetInterceptors() {
        // Setup
        // Run the test
        final List<TransactionalInterceptor> result = interceptorProxy.getInterceptors();
        
        // Verify the results
        assertNotNull(result);
    }
    
    @Test
    public void testAddInterceptor() {
        // Setup
        final TransactionalInterceptor interceptor =
                mock(TransactionalInterceptor.class);
        
        // Run the test
        interceptorProxy.addInterceptor(interceptor);
        
        // Verify the results
//        assertNotNull(interceptorProxy.getInterceptors());
    }
    
    @Test
    public void testAddInterceptors() {
        // Setup
        final List<TransactionalInterceptor> list = Collections.singletonList(
                mock(TransactionalInterceptor.class)
        );
        
        // Run the test
        interceptorProxy.addInterceptors(list);
        
        // Verify the results
        assertFalse(interceptorProxy.getInterceptors().isEmpty());
    }
    
    @Test
    public void testRun() {
        Map<String, TransactionalInterceptor> map = new HashMap<>();
        // Setup
        doReturn(map).when(applicationContext)
                     .getBeansOfType(TransactionalInterceptor.class);
        // Run the test
        interceptorProxy.run(null);
        
        // Verify the results
    }
    
    @Test
    public void testRun_ThrowsException() {
        // Setup
        doThrow(new CloudAppException("error"))
                .when(applicationContext)
                .getBeansOfType(TransactionalInterceptor.class);
        // Run the test
        assertThrows(Exception.class,
                     () -> interceptorProxy.run(null)
        );
    }
    
}
