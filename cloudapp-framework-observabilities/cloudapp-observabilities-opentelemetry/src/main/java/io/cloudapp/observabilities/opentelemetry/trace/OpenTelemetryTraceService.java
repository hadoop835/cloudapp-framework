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

package io.cloudapp.observabilities.opentelemetry.trace;

import io.cloudapp.api.common.ComponentLifeCycle;
import io.cloudapp.api.observabilities.TraceService;
import io.cloudapp.observabilities.opentelemetry.util.OpenTelemetryTagTool;
import io.opentelemetry.api.baggage.BaggageBuilder;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class OpenTelemetryTraceService implements TraceService,
        ComponentLifeCycle {
    
    private static final Logger logger = LoggerFactory.getLogger(
            OpenTelemetryTraceService.class);
    
    @Override
    public Map<String, String> getBaggageUserData() {
        return OpenTelemetryTagTool.baggageItems();
    }
    
    @Override
    public String getBaggageUserDataValue(String key) {
        Map<String, String> baggageItems = OpenTelemetryTagTool.baggageItems();
        
        if (baggageItems.containsKey(key)) {
            return baggageItems.get(key);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("The baggage item with key {} is not found in the current " +
                            "span", key);
        }
        
        return null;
    }
    
    @Override
    public Scope withBaggageUserData(Map<String, String> pairs) {
        BaggageBuilder builder = OpenTelemetryTagTool.putBaggageItems(pairs);
        return OpenTelemetryTagTool.buildBaggageScope(builder);
    }
    
    @Override
    public void postStart() {
        logger.info("Enabled OpenTelemetry Trace Service.");
    }
    
    @Override
    public void preStop() {
        logger.info("Stopping OpenTelemetry Trace Service.");
    }
}
