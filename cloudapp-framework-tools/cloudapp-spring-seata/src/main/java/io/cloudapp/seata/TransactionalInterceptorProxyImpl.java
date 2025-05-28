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
import io.cloudapp.api.seata.TransactionalInterceptorProxy;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionalInterceptorProxyImpl implements ApplicationRunner,
        TransactionalInterceptorProxy, ApplicationContextAware {
    
    protected static final List<TransactionalInterceptor> interceptors =
            new ArrayList<>();
    
    private ApplicationContext applicationContext;
    
    
    public TransactionalInterceptorProxyImpl() {
    }
    
    @Override
    public List<TransactionalInterceptor> getInterceptors() {
        return interceptors;
    }
    
    @Override
    public void addInterceptor(TransactionalInterceptor interceptor) {
        if(!interceptors.contains(interceptor)) {
            interceptors.add(interceptor);
        }
    }
    
    public void addInterceptors(List<TransactionalInterceptor> list) {
        interceptors.addAll(list);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void run(ApplicationArguments args) {
        if(applicationContext instanceof ConfigurableApplicationContext) {
            ConfigurableApplicationContext context =
                    (ConfigurableApplicationContext) applicationContext;
            
            Map<String, TransactionalInterceptor> beans =
                    context.getBeansOfType(TransactionalInterceptor.class);
            for (TransactionalInterceptor bean : beans.values()) {
                addInterceptor(bean);
            }
        }
    }
    
}
