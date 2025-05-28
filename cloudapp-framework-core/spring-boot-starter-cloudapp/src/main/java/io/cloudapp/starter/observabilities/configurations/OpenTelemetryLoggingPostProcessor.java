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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class OpenTelemetryLoggingPostProcessor implements
        EnvironmentPostProcessor {
    
    private final String PROPERTY_OT_SOURCE = "CLOUD_APP_OT_PROPERTY";
    private final String LOG_ENABLE_TRACE_ID = "io.cloudapp.observabilities" +
            ".ot.logging.enable-traceid";
    private final String MUTATE_LOG_KEY = "logging.pattern.level";
    
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application)  {
        
        String traceEnabled = environment.getProperty(LOG_ENABLE_TRACE_ID, "false");
        boolean traceEnabledBool = Boolean.parseBoolean(traceEnabled);
        if (!traceEnabledBool) {
            return;
        }
        String level = environment.getProperty(MUTATE_LOG_KEY, "%5p");
        String pattern = "[%mdc{trace_id} %mdc{span_id}] " + level;
        
        Map<String, Object> properties = new HashMap<>();
        properties.put(MUTATE_LOG_KEY, pattern);
        environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_OT_SOURCE, properties));
    }
}
