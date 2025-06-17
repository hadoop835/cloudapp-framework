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

import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import io.cloudapp.api.scheduler.worker.GlobalJobContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * GlobalJobMethodProcessor that invoke method of spring bean.
 */
public class GlobalJobMethodProcessor extends JavaProcessor {
    /**
     * invoke method of the target object
     */
    private final Object target;
    /**
     * process method to invoke
     */
    private final Method method;
    /**
     * method to invoke before process
     */
    private final Method preMethod;
    /**
     * method to invoke after process
     */
    private final Method postMethod;
    
    public GlobalJobMethodProcessor(Object target,
                                    Method method,
                                    Method preMethod,
                                    Method postMethod) {
        this.target = target;
        this.method = method;
        this.preMethod = preMethod;
        this.postMethod = postMethod;
    }
    
    /**
     * set GlobalJobContext to adapt schedulerx2 JobContext
     * After calling it, you need to call GlobalJobContext.clear() in finally
     */
    private void setGlobalJobContext(JobContext context) {
        GlobalJobContext ctx = new GlobalJobContext(
                context.getJobId(),
                context.getJobParameters(),
                context.getShardingId() == null ? 0 :context.getShardingId().intValue(),
                context.getShardingNum(),
                context.getShardingParameter()
        );
        
        GlobalJobContext.setContext(ctx);
    }
    
    
    @Override
    public void preProcess(JobContext context) {
        try {
            setGlobalJobContext(context);
            
            if (preMethod != null) {
                preMethod.invoke(target);
            }
            
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
            
        } finally {
            GlobalJobContext.clear();
        }
    }
    
    @Override
    public ProcessResult postProcess(JobContext context) {
        try {
            setGlobalJobContext(context);
            
            if (postMethod != null) {
                postMethod.invoke(target);
            }
            
            return new ProcessResult(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            GlobalJobContext.clear();
        }
    }
    
    @Override
    public ProcessResult process(JobContext context) throws Exception {
        try {
            setGlobalJobContext(context);
            
            
            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length > 0) {
                // method-param can not be primitive-types
                method.invoke(target, new Object[paramTypes.length]);
            } else {
                method.invoke(target);
            }
            return new ProcessResult(true);
        } finally {
            GlobalJobContext.clear();
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + "[" + target.getClass() + "#" + method.getName() + "]";
    }
    
}
