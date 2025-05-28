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
package io.cloudapp.observabilities.opentelemetry.metric;

import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter;
import io.opentelemetry.exporter.prometheus.PrometheusHttpServer;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.MetricReader;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;

import java.time.Duration;
import java.util.function.Predicate;

public class MetricReaderExporterConfig {
    
    static final Predicate<String> ALLOW_ALL = attributeKey -> true;
    
    public static MetricExporter otlpHttpMetricExporter(String endpoint,
                                                        Integer timeout) {
        return OtlpHttpMetricExporter.builder()
                .setEndpoint(endpoint)
                .setTimeout(Duration.ofSeconds(timeout))
                .build();
    }
    
    public static MetricReader periodicMetricReader(String metricEndpoint,
                                                    Integer timeout,
                                                    Integer interval) {
        return PeriodicMetricReader
                .builder(otlpHttpMetricExporter(metricEndpoint, timeout))
                .setInterval(Duration.ofSeconds((interval)))
                .build();
    }

    /**
     * Build prometheus http server as metric reader
     * @return MetricReader
     */
    public static MetricReader prometheusMetricReader(String address,
                                                      Integer prometheusPort) {
        return PrometheusHttpServer
                .builder()
                .setHost(address)
                .setPort(prometheusPort)
                .setOtelScopeEnabled(true)
                .setAllowedResourceAttributesFilter(ALLOW_ALL)
                .build();
    }
}
