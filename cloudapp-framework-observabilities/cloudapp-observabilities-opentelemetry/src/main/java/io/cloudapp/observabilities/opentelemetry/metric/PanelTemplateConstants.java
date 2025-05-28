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

import io.cloudapp.api.observabilities.MetricType;

import java.util.ArrayList;
import java.util.List;

public class PanelTemplateConstants {
    
    public static String getRowTemplate() {
        return "row.ftl";
    }
  
    public static String getModelTemplate() {
        return "model.ftl";
    }
    
    public static List<String> getTemplatesByMetricType(MetricType metricType) {
        List<String> templateNames = new ArrayList<>();
        switch (metricType) {
            case COUNTER:
                templateNames.add("gauge.ftl");
                templateNames.add("timeseries.ftl");
                return templateNames;
            case GAUGE:
                templateNames.add("avg_gauge.ftl");
                templateNames.add("max_gauge.ftl");
                return templateNames;

            case HISTOGRAM:
                templateNames.add("histogram.ftl");
                templateNames.add("heatmap.ftl");
                return templateNames;
                
            default:
                return templateNames;
        }
    }
}
