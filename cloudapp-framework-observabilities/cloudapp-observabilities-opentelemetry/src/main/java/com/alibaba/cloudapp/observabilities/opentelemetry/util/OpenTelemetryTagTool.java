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

package com.alibaba.cloudapp.observabilities.opentelemetry.util;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.baggage.BaggageBuilder;
import io.opentelemetry.api.baggage.BaggageEntry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * OpenTelemetryTagTool, provide some useful tools for OpenTelemetry.
 */
public class OpenTelemetryTagTool {
    
    public static String getTraceId() {
        return Span.current().getSpanContext().getTraceId();
    }
    
    public static Map<String, String> baggageItems() {
        Map<String, String> result = new HashMap<>();
        Map<String, BaggageEntry> bis = Baggage.current().asMap();
        for (Map.Entry<String, BaggageEntry> entry : bis.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getValue());
        }

        return result;
    }
    
    public static BaggageBuilder putBaggageItems(Map<String, String> pairs) {
        Baggage originBaggage = Baggage.current();
        Map<String, BaggageEntry> originBaggageMap = originBaggage.asMap();

        BaggageBuilder baggageBuilder = Baggage.builder();
        Set<String> keys = pairs.keySet();

        originBaggageMap.forEach((s, baggageEntry) -> {
            if (keys.contains(s)) {
                baggageBuilder.put(s, pairs.get(s));
            } else {
                baggageBuilder.put(s, baggageEntry.getValue());
            }
        });

        Set<String> originKeys = originBaggageMap.keySet();
        pairs.forEach((key, value) -> {
            if (!originKeys.contains(key)) {
                baggageBuilder.put(key, value);
            }
        });

        return baggageBuilder;
    }
    
    public static Scope buildBaggageScope(BaggageBuilder baggageBuilder) {
        return baggageBuilder.build().makeCurrent();
    }
}
