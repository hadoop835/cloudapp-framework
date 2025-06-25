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
package com.alibaba.cloudapp.scheduler.schedulerx2;

import com.alibaba.cloudapp.api.scheduler.worker.GlobalJobMetadata;

import java.util.function.Function;

public interface CommonConstants {
    
    /**
     * Determine the uniqueness of the job based on the
     * "process method name" under namespace and groupId.
     * The logic of configuring a job only once.
     * <p>
     * The name of the created job: "ProcessMethod Name"
     * The registered bean name: "ProcessMethod Name"
     * The full class path:
     * "GlobalJobMethodProcessor class Name : ProcessMethod Name"
     * <p>
     * The value name of this value can be set through annotations.
     * If the annotation is not set, the default is the process method name.
     */
    Function<GlobalJobMetadata, String> getRegisterBeanNameFunc =
            (GlobalJobMetadata obj) ->
                    String.format("%s",
                                  obj.getValueName()
                    );
    
    Function<GlobalJobMetadata, String> getClassNameFunc =
            (GlobalJobMetadata obj) ->
                    String.format("%s:%s",
                                  GlobalJobMethodProcessor.class.getName(),
                                  obj.getValueName()
                    );
    
}
