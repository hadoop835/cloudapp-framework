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
 * Used to mark method invocation count statistics.
 * Method invoke counts can be collected as a counter metric.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InvokeCount {
    /**
     * The name of the counter metric.
     * @return the name of the counter metric.
     */
    String name();
    
    /**
     * Additional attributes to be added to the counter metric.
     * An attribute is consist with key-pair.
     * Multiple attributes are separated by semicolon.
     * Default is an empty string.
     * @return additional attributes to be added to the counter metric.
     */
    String attributes() default "";
    
    /**
     * Unit of the metric.
     * Default is an empty string.
     * @return the unit of the metric.
     */
    String unit() default "";
    
    /**
     * Description of the metric.
     * Default is an empty string.
     * @return description of the metric.
     */
    String description() default "";
}
