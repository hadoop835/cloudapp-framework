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
package io.cloudapp.microservice.aliyun;

import com.alibaba.fastjson2.JSON;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.baggage.BaggageBuilder;
import io.opentelemetry.api.baggage.BaggageEntry;
import io.opentelemetry.context.Scope;
import io.opentelemetry.api.trace.Span;
import java.util.*;

public class OpenTelemetryTagTool {

    public static final String LANE_TAG = "__microservice_tag__";
    // CANARY_TAG is immutable, only can be read
    public static final String CANARY_TAG = "__microservice_match_result__";

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

    static class Tag {
        private String name;

        private String tag;

        private int priority;

        public Tag(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
    }

    public static BaggageBuilder withCanaryTag(String tag) {
        List<OpenTelemetryTagTool.Tag> tagList = constructTag(tag);
        String mergeTagStr = JSON.toJSONString(tagList);
        return putBaggageItem(LANE_TAG, mergeTagStr);
    }

    public static BaggageBuilder putBaggageItem(String key, String value) {
        Map<String, String> maps = new HashMap<>();
        maps.put(key, value);
        return putBaggageItems(maps);
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

    public static String originalTrafficTag(String labelKey) {
        String tags = Baggage.current().getEntryValue(labelKey);
        List<Tag> tagObjs = JSON.parseArray(tags, Tag.class);
        if (tagObjs == null || tagObjs.isEmpty()) {
            return null;
        } else {
            return tagObjs.get(0).getName();
        }
    }

    private static List<OpenTelemetryTagTool.Tag> constructTag(String tag) {
        List<OpenTelemetryTagTool.Tag> tagList = new ArrayList<>();
        OpenTelemetryTagTool.Tag t = new OpenTelemetryTagTool.Tag(tag);
        t.setPriority(100);
        tagList.add(t);
        return tagList;
    }
}
