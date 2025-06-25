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
package com.alibaba.cloudapp.observabilities.opentelemetry.metric;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import com.alibaba.cloudapp.api.observabilities.MetricType;
import io.opentelemetry.api.metrics.DoubleGauge;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongCounter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Register templates handler for metrics, which can be used as grafana
 * dashboard model json file.
 */
@Slf4j
public class MetricsTemplateHandler implements HttpHandler {

    private final Configuration cfg = new Configuration(
            Configuration.VERSION_2_3_30);

    public MetricsTemplateHandler() {
        cfg.setClassForTemplateLoading(
                MetricsTemplateHandler.class, "/templates");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 设置响应内容类型
        exchange.getResponseHeaders().set("Content-Type", "text/plain; " +
                "charset=UTF-8");
        
        String content = generateMetricTemplates();
        
        long responseLength = -1;
        if (content != null) {
            responseLength = content.getBytes(StandardCharsets.UTF_8).length;
        }
        exchange.sendResponseHeaders(200, responseLength);
        OutputStream response = exchange.getResponseBody();
        if (content != null && !content.isEmpty()) {
            response.write(content.getBytes(StandardCharsets.UTF_8));
        }
        response.close();
    }
    
    /**
     * Iterate all registered metrics and generate templates.
     * @return grafana template content
     */
    private String generateMetricTemplates() {
        Map<String, Object> registeredMetrics = MetricCollectionAspect.Metrics;
        StringBuilder panelBuilder = new StringBuilder();
        // build panel templates id indexer
        AtomicInteger panelIdx = new AtomicInteger(1);
        AtomicInteger metricIdx = new AtomicInteger(1);
        registeredMetrics.forEach((name, metric) -> {
            // replace dot to underscore for metric
            name = name.replace(".", "_");
            appendTemplate(panelBuilder,
                           renderRowTemplate(name, metricIdx, panelIdx));
            if (metric instanceof LongCounter) {
                appendTemplate(
                        panelBuilder,
                        renderTemplate(name, metricIdx, panelIdx, MetricType.COUNTER));
            } else if (metric instanceof DoubleGauge) {
                appendTemplate(
                        panelBuilder,
                        renderTemplate(name, metricIdx, panelIdx, MetricType.GAUGE));
            } else if (metric instanceof DoubleHistogram) {
                appendTemplate(
                        panelBuilder,
                        renderTemplate(name, metricIdx, panelIdx, MetricType.HISTOGRAM));
            }
            metricIdx.getAndIncrement();
        });
        
        // Inject panel templates to model
        return renderModelTemplate(panelBuilder.toString());
    }
    
    private void appendTemplate(StringBuilder stringBuilder,
                                String toAppendTemplate) {
        if (toAppendTemplate == null || toAppendTemplate.isEmpty()) {
            return;
        }
        if (stringBuilder.length() > 0 && !stringBuilder.toString().endsWith(",")) {
            stringBuilder.append(",").append(toAppendTemplate);
        } else {
            stringBuilder.append(toAppendTemplate);
        }
    }
    
    /**
     * Render model template with specified template context
     * @param panelTemplates template content consist of multiple templates
     * @return model template content
     */
    private String renderModelTemplate(String panelTemplates) {
        Map<String, Object> modelContext = new HashMap<>();
        modelContext.put("panel_templates", panelTemplates);
        try {
            return getTemplateContent(modelContext, PanelTemplateConstants.getModelTemplate());
        } catch (Exception e) {
            log.error("render model template failed", e);
            return null;
        }
    }
    
    /**
     * Render row template with specified template context
     * @param name metric type
     * @param metricIdx metric indexer
     * @param panelIdx panel indexer
     * @return row template content
     */
    private String renderRowTemplate(String name, AtomicInteger metricIdx,
                                     AtomicInteger panelIdx) {
        Map<String, Object> rowTemplateContext = new HashMap<>();
        rowTemplateContext.put("metric_idx", metricIdx.get());
        rowTemplateContext.put("idx", panelIdx.getAndIncrement());
        rowTemplateContext.put("metric_title", name);
        String templateName = PanelTemplateConstants.getRowTemplate();
        try {
            return getTemplateContent(rowTemplateContext, templateName);
        } catch (Exception e) {
            log.error("render row template failed", e);
            return null;
        }
    }
    
    /**
     * Render and return template content by metric type with specified
     * template context, and increase the index of panel.
     * @param context template parameter context
     * @param metricType metric type
     * @param panelIdx panel indexer
     * @return template content
     * @throws TemplateException
     * @throws IOException
     */
    private String getTemplateContent(Map<String, Object> context,
                                      MetricType metricType,
                                      AtomicInteger panelIdx)
            throws TemplateException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (String templateName :
                PanelTemplateConstants.getTemplatesByMetricType(metricType)) {
            // if templates more than one, get and increase the index
            if (stringBuilder.length() != 0) {
                stringBuilder.append(",");
                context.put("idx", panelIdx.getAndIncrement());
            }
            stringBuilder.append(getTemplateContent(context, templateName));
        }
        return stringBuilder.toString();
    }
    
    /**
     * Render and return template content by a template name with specified
     * template context
     * @param context template parameter context
     * @param templateName template name
     * @return template content with given context
     * @throws TemplateException
     * @throws IOException
     */
    private String getTemplateContent(Map<String, Object> context,
                                      String templateName)
            throws TemplateException, IOException {
        Template tpl = cfg.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        tpl.process(context, writer);
        return writer.toString();
    }
    
    /**
     * Render template, build template context by metric name and row id
     * and panel. A specified metric type may consist of multiple panels
     * with same row id.
     * @param name metric name
     * @param metricIdx metric id which will be considered as a row id
     * @param panelIdx panel id
     * @param metricType metric type
     * @return template content
     */
    private String renderTemplate(String name, AtomicInteger metricIdx,
                                  AtomicInteger panelIdx, MetricType metricType) {
        Map<String, Object> counterContext = new HashMap<>();
        counterContext.put("metric_name", name);
        counterContext.put("metric_idx", metricIdx.get());
        counterContext.put("idx", panelIdx.getAndIncrement());
        try {
            return getTemplateContent(counterContext, metricType, panelIdx);
        } catch (Exception e) {
            log.error("failed to render grafana json for metric: {}, type: {}",
                      name, metricType);
        }
        return null;
    }
}
