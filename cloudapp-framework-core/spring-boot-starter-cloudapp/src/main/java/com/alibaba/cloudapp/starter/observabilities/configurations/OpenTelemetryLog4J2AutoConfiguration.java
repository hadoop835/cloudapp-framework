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

import com.alibaba.cloudapp.observabilities.opentelemetry.logging.OpenTelemetryLog4J2Impl;
import com.alibaba.cloudapp.starter.observabilities.properties.OpenTelemetryLoggingProperties;
import com.alibaba.cloudapp.starter.observabilities.properties.OpenTelemetryProperties;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(Log4jContextFactory.class)
@ConditionalOnProperty(
        prefix = OpenTelemetryProperties.PREFIX,
        value = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(OpenTelemetryLoggingProperties.class)
public class OpenTelemetryLog4J2AutoConfiguration {
    @Bean("openTelemetryLog4J2Impl")
    @ConditionalOnMissingBean
    public OpenTelemetryLog4J2Impl openTelemetryLoggingConfiguration(
            OpenTelemetryLoggingProperties properties) {
        OpenTelemetryLog4J2Impl otLog4j2 = new OpenTelemetryLog4J2Impl(properties.isEnableTraceId());
        otLog4j2.updateLogger();
        return otLog4j2;
    }
}
