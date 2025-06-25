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

package com.alibaba.cloudapp.seata.demo;

import io.seata.saga.engine.AsyncCallback;
import io.seata.saga.proctrl.ProcessContext;
import io.seata.saga.statelang.domain.StateMachineInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoAsyncCallback implements AsyncCallback {
    
    public static final Logger logger = LoggerFactory.getLogger(DemoAsyncCallback.class);
    
    public static final Object object = new Object();
    
    @Override
    public void onFinished(ProcessContext context,
                           StateMachineInstance stateMachineInstance) {
        synchronized(object) {
            object.notify();
        }
    }
    
    @Override
    public void onError(ProcessContext context,
                        StateMachineInstance stateMachineInstance,
                        Exception exp) {
        logger.error("stateMachineInstance:[{}] execute error",
                stateMachineInstance.getId(), exp);
        synchronized(object) {
            object.notifyAll();
        }
    }
    
    public void waitingForFinish(StateMachineInstance inst) {
        synchronized (object) {
            try {
                object.wait();
            } catch (InterruptedException e) {
                logger.error("waiting stateMachineInstance:[{}] finish error",
                        inst.getId(), e);
            }
        }
    }
    
}
