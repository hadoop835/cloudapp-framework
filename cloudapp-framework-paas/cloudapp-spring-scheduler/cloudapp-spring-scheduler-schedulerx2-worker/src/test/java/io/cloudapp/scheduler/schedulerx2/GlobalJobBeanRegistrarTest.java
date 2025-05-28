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

package io.cloudapp.scheduler.schedulerx2;


import io.cloudapp.api.scheduler.worker.GlobalJobMetadata;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GlobalJobBeanRegistrarTest {

    @Mock
    private ApplicationContext context;

    @Mock(extraInterfaces = SingletonBeanRegistry.class)
    private AutowireCapableBeanFactory beanFactory;

    @InjectMocks
    private GlobalJobBeanRegistrar globalJobBeanRegistrar;
    
    @Before
    public void setUp() {
        GlobalJobMetadata metadata = new GlobalJobMetadata();
        metadata.setBeanName("testBean");
        metadata.setValueName("testValue");
        GlobalJobScanner.getGlobalJobs().add(metadata);
        
        when(context.getAutowireCapableBeanFactory()).thenReturn(beanFactory);
    }
    
    private void registerGlobalJobBeansMethod()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method registerGlobalJobBeansMethod = GlobalJobBeanRegistrar.class.getDeclaredMethod(
                "registerGlobalJobBeans");
        registerGlobalJobBeansMethod.setAccessible(true);
        registerGlobalJobBeansMethod.invoke(globalJobBeanRegistrar);
    }
    
    @Test
    public void registerGlobalJobBeans_SuccessfulRegistration()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        GlobalJobMetadata metadata = mock(GlobalJobMetadata.class);
        metadata.setBeanName("testBean");
        
        registerGlobalJobBeansMethod();

    }

    @Test
    public void registerGlobalJobBeans_ExceptionDuringRegistration() {
        
        doThrow(new IllegalStateException("Registration failed"))
                .when(context).getAutowireCapableBeanFactory();

        assertThrows(InvocationTargetException.class,
                     this::registerGlobalJobBeansMethod
        );
    }
}
