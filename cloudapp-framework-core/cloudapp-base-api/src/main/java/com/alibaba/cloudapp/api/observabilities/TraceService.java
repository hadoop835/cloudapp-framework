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
package com.alibaba.cloudapp.api.observabilities;

import io.opentelemetry.context.Scope;

import java.util.Map;

public interface TraceService {
    /**
     * Get the current user requesting values, e.g: the user-data in an trace
     * context request
     *
     * @return a map of key-value pairs
     */
    Map<String, String> getBaggageUserData();
    
    /**
     * Get the current user requesting value in context baggage, e.g: the
     * user-data value
     * in an
     * eagleeye context
     *
     * @param key The key of the parameter
     * @return The value of the parameter
     */
    String getBaggageUserDataValue(String key);
    
    /**
     * Build opentelemetry scope object to specified where user-data will be brought.
     * @param pairs key-value pairs of user-data that brought by baggage
     * @return return the opentelemetry scope object, used by try...final .
     */
    Scope withBaggageUserData(Map<String, String> pairs);
    
}
