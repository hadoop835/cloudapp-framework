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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate classes that represent a collection of metrics.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MetricCollection {
    /**
     * Define the name of the collection, which will be set as an additional
     * attribute on the metric.
     * Suggestion is that an application use the same service-name.
     * @return The name of the metric collection.
     */
    String serviceName() default "";
    
    /**
     * Defined the scope name of the metric collection is associated with.
     * The suggestion is to set the scope name to the package name or fully
     * qualified class name.
     * @return The scope name of the metric collection.
     */
    String scopeName() default "";
    
    /**
     * Defined the scope version of the metric collection is associated with.
     * The suggestion is to set the scope version to the library version.
     * @return The scope version of the metric collection.
     */
    String scopeVersion() default "";
}
