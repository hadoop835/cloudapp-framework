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
package com.alibaba.cloudapp.starter.observabilities.configurations;

import com.alibaba.cloudapp.api.observabilities.TraceService;
import com.alibaba.cloudapp.observabilities.opentelemetry.trace.OpenTelemetryTraceService;
import com.alibaba.cloudapp.starter.observabilities.properties.OpenTelemetryProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass({TraceService.class, OpenTelemetryTraceService.class})
@ConditionalOnProperty(
        prefix = OpenTelemetryProperties.PREFIX,
        value = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class OpenTelemetryTraceConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public static TraceService otTraceService() {
        return new OpenTelemetryTraceService();
    }
}
