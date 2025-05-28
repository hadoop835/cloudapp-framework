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
package io.cloudapp.starter.observabilities.configurations;

import io.cloudapp.observabilities.opentelemetry.metric.GlobalOpenTelemetry;
import io.cloudapp.observabilities.opentelemetry.metric.MetricCollectionAspect;
import io.cloudapp.observabilities.opentelemetry.metric.MetricHelperServer;
import io.cloudapp.starter.observabilities.properties.OpenTelemetryProperties;
import io.cloudapp.starter.observabilities.refresh.OpenTelemetryRefreshComponent;
import io.cloudapp.starter.refresh.aspect.RefreshableBinding;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(MetricCollectionAspect.class)
@EnableConfigurationProperties({OpenTelemetryProperties.class})
@ConditionalOnProperty(
        prefix = OpenTelemetryProperties.PREFIX,
        value = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class OpenTelemetryMetricConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public OpenTelemetryRefreshComponent otRefreshComponent(
            OpenTelemetryProperties properties) {
        return new OpenTelemetryRefreshComponent(properties);
    }
    
    @Bean(name="globalOpenTelemetryInstance")
    @ConditionalOnProperty(
            prefix = OpenTelemetryProperties.PREFIX,
            value = "enabled",
            havingValue = "true"
    )
    @ConditionalOnMissingBean
    @RefreshableBinding(OpenTelemetryProperties.PREFIX)
    public static GlobalOpenTelemetry globalOpenTelemetry(
            OpenTelemetryRefreshComponent component
    ) {
        return component.getBean();
    }
    
    
    @Bean(name="otMetricCollectionAspect")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = OpenTelemetryProperties.PREFIX,
            value = "enabled",
            havingValue = "true"
    )
    @RefreshableBinding(OpenTelemetryProperties.PREFIX)
    public static MetricCollectionAspect otMetricCollectionAspect(
            OpenTelemetryRefreshComponent component
    ) {
        return new MetricCollectionAspect();
    }
    
    @Bean(name="metricHelperServer")
    @ConditionalOnProperty(
            prefix = OpenTelemetryProperties.PREFIX,
            value = "enableHelperServer",
            havingValue = "true",
            matchIfMissing = true
    )
    @ConditionalOnMissingBean
    @RefreshableBinding(OpenTelemetryProperties.PREFIX)
    public static MetricHelperServer metricHelperServer(
            OpenTelemetryRefreshComponent component
    ) {
        return component.getServer();
    }
}
