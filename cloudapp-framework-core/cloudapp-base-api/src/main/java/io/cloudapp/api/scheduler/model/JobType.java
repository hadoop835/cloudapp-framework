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
package io.cloudapp.api.scheduler.model;

import java.util.stream.Stream;

/**
 * JobType enum
 */
public enum JobType {
    JAVA("java"),
    PYTHON("python"),
    SHELL("shell"),
    HTTP("http");
    
    private String key;
    
    public static JobType getByKey(String key) {
        return Stream.of(values())
                     .filter(e -> e.getKey().equals(key))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException(
                             "invalid key:" + key));
    }
    
    JobType(String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
}
