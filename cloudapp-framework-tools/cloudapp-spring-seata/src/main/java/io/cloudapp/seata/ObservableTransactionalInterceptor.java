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
import io.seata.spring.annotation.GlobalTransactional;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class ObservableTransactionalInterceptor implements
        TransactionalInterceptor {
    
    private final AtomicLong counter = new AtomicLong(0L);
    private final AtomicLong succeeded = new AtomicLong(0L);
    private final AtomicLong failed = new AtomicLong(0L);
    private final AtomicLong running = new AtomicLong(0L);
    
    private final GlobalTransactionHolder holder;
    
    public ObservableTransactionalInterceptor(GlobalTransactionHolder holder) {
        this.holder = holder;
    }
    
    @Override
    public void beforeExec(TransactionalContext context) {
        if (noHandle(context)) {
            return;
        }
        
        holder.setCount(holder.getCount() + 1);
        holder.setRunningGauge(holder.getRunningGauge() + 1);
    }
    
    @Override
    public void afterExec(TransactionalContext context) {
        if (noHandle(context)) {
            return;
        }
        holder.setRunningGauge(counter.decrementAndGet());
        if (context.isSuccess()) {
            holder.setSucceedCount(succeeded.incrementAndGet());
        } else {
            holder.setFailedCount(failed.incrementAndGet());
        }
        long endTime = System.currentTimeMillis();
        long cost = endTime - context.getStartTime();
        if (cost > holder.getMaxRunningTime()) {
            holder.setMaxRunningTime(cost);
        }
        if (cost < holder.getMinRunningTime()) {
            holder.setMinRunningTime(cost);
        }
    }
    
    private boolean noHandle(TransactionalContext context) {
        if (context == null || context.getTransactionName() == null) {
            return true;
        }
        return context.getAnnotations() != null
                && Arrays.stream(context.getAnnotations()).noneMatch(
                    a -> a.annotationType().equals(GlobalTransactional.class
                )
        );
    }
    
}
