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
package io.cloudapp.api.observabilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate fields that represent metrics in a POJO class annotated
 * by MetricCollection.
 * This annotation can be applied to fields to be automatically collected by
 *  metric collection.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Metric {
    /**
     * The name of the metric. Used to uniquely identify the metric.
     * @return the name of the metric.
     */
    String name();

    /**
     * The type of the metric.
     * Specifies the type of metric, such as "COUNTER", "GAUGE", etc.
     * Default is {@link MetricType#COUNTER}.
     * @return The type of the metric
     */
    MetricType type() default MetricType.COUNTER;
    
    /**
     * The description of the metric.
     * Provides a brief description of the metric.
     * Default is "defined by cloud-app framework".
     * @return The description of the metric
     */
    String description() default "defined by cloud-app framework";

    /**
     * The unit of the metric.
     * Used to indicate the unit of measurement for the metric value, such as
     * "s", "K", etc.
     * Default is an empty string.
     * @return The unit of the metric
     */
    String unit() default "";

    /**
     * Whether the metric is asynchronous.
     * Indicates if the metric should be collected asynchronously.
     * If set to "true", this metric will be collected immediately as
     * long as this value is modified by "setter" method.
     * Default is false.
     * @return Whether the metric is asynchronous
     */
    boolean async() default false;
    
    /**
     * Set the bucket boundaries for histogram metric. Default bucket
     * boundaries [0.0, 10.0, 100.0, 1000.0, 10000.0, 250.0, 2500.0, 5.0, 50.0,
     * 500.0, 5000.0, 75.0, 750.0, 7500.0] will be effective when metric type
     * is "HISTOGRAM" and the buckets not set.
     * Only valid for histogram metric.
     * @return The bucket boundaries for histogram metric
     */
    double[] buckets() default {};
    
    /**
     * Additional attributes to be added to the metric.
     * An attribute is consist with key-pair.
     * Multiple attributes are separated by semicolon.
     * Default is an empty string.
     * @return additional attributes to be added to the counter metric.
     */
    String attributes() default "";
    
}
