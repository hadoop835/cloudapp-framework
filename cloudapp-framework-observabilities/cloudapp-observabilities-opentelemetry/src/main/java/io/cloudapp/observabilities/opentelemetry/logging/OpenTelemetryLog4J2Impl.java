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
package io.cloudapp.observabilities.opentelemetry.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.PatternParser;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * This class is used to replace the log level flag with trace_id and span_id.
 * This is used to support the log4j2 log level injection.
 */
public class OpenTelemetryLog4J2Impl {

    private final boolean isEnableTraceId;
    
    private static final String MDC_TRACE_PATTERN = "%X{trace_id}";
    private static final String MDC_SPAN_PATTERN = "%X{span_id}";
    
    public OpenTelemetryLog4J2Impl(boolean isEnableTraceId) {
        this.isEnableTraceId = isEnableTraceId;
    }
    
    public void updateLogger() {
        // if not enable trace id injection, return directly
        if (!isEnableTraceId) {
            return;
        }
        processContext(getContext());
    }

    void processContext(LoggerContext context) {
        mutateContextPattern(context);
        context.updateLoggers();
    }
    
    LoggerContext getContext() {
        return (LoggerContext) LogManager.getContext(false);
    }
    
    /**
     * When the traceId is enabled for log4j2, this method will get the global
     * LoggerContext and change it configuration. Replace log level flag with
     * trace_id and span_id and rebuild serialization.
     */
    public void mutateContextPattern(LoggerContext context) {
        Configuration config = context.getConfiguration();
        
        // iterate all appenders to change layout pattern and set formatter
        for (Map.Entry<String, Appender> entry : config.getAppenders().entrySet()) {
            Appender appender = entry.getValue();
            if (appender.getLayout() instanceof PatternLayout) {
                PatternLayout layout = (PatternLayout) appender.getLayout();
                String currentPattern = layout.getConversionPattern();
                
                String newPattern = parseLevelPattern(currentPattern);
                PatternParser parser = PatternLayout.createPatternParser(config);
                // parse new pattern to load additional formatter
                parser.parse(newPattern);
                
                // rebuild serializer
                AbstractStringLayout.Serializer eventSerializer =
                        PatternLayout.newSerializerBuilder()
                                     .setConfiguration(config)
                                     .setPattern(newPattern).setDefaultPattern("%m%n").build();
                
                Class<?> patternLayoutClass = layout.getClass();
                Field patternField;
                Field serializerField;
                try {
                    patternField = patternLayoutClass.getDeclaredField(
                            "conversionPattern");
                    serializerField = patternLayoutClass.getDeclaredField(
                            "eventSerializer");
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
                // set layout attributes
                patternField.setAccessible(true);
                serializerField.setAccessible(true);
                try {
                    patternField.set(appender.getLayout(), newPattern);
                    serializerField.set(appender.getLayout(),
                                        eventSerializer);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Parse layout pattern, replace log level flag to include trace_id and
     * span_id
     * @param currentPattern origin layout pattern
     * @return pattern after replace level flag
     */
    private static String parseLevelPattern(String currentPattern) {
        return levelFormatter(levelFormatter(currentPattern, "(%p)"),
                              "(%-?\\d*level)");
    }
    
    private static String levelFormatter(String pattern, String levelKey) {
        String format = String.format("%s %s", MDC_TRACE_PATTERN, MDC_SPAN_PATTERN);
        return pattern.replaceAll(levelKey, format + " $1");
    }
}
