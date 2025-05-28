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
package io.cloudapp.starter.observabilities.properties;

import io.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(OpenTelemetryProperties.PREFIX)
public class OpenTelemetryProperties extends RefreshableProperties {
    
    public static final String PREFIX = "io.cloudapp.observabilities.ot";
    
    private boolean enabled;
    
    private String metricEndpoint;
    
    private Integer timeout = 10;
    
    private Integer interval = 30;
    
    private String defaultServiceName = "default";
    /**
     *
     */
    private Boolean enabledHelperServer = false;
    private Integer helperServerPort = 56199;
    private String helperServerBindAddress = "127.0.0.1";
    
    private Boolean prometheusExporter = true;
    private Integer prometheusPort = 9464;
    
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getMetricEndpoint() {
        return metricEndpoint;
    }
    
    public void setMetricEndpoint(String metricEndpoint) {
        this.metricEndpoint = metricEndpoint;
    }
    
    public String getDefaultServiceName() {
        return defaultServiceName;
    }
    
    public void setDefaultServiceName(String defaultServiceName) {
        this.defaultServiceName = defaultServiceName;
    }
    
    public Integer getTimeout() {
        return timeout;
    }
    
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
    
    public Integer getInterval() {
        return interval;
    }
    
    public void setInterval(Integer interval) {
        this.interval = interval;
    }
    
    public Boolean isEnabledHelperServer() {
        return enabledHelperServer;
    }
    
    public void setEnabledHelperServer(Boolean enabled) {
        this.enabledHelperServer = enabled;
    }
    
    public Integer getHelperServerPort() {
        return helperServerPort;
    }
    
    public void setHelperServerPort(Integer serverPort) {
        this.helperServerPort = serverPort;
    }
    
    public String getHelperServerBindAddress() {
        return helperServerBindAddress;
    }
    
    public void setHelperServerBindAddress(String helperServerBindAddress) {
        this.helperServerBindAddress = helperServerBindAddress;
    }
    
    public Integer getPrometheusPort() {
        return prometheusPort;
    }
    
    public void setPrometheusPort(Integer prometheusPort) {
        this.prometheusPort = prometheusPort;
    }
    
    public Boolean isPrometheusExporter() {
        return prometheusExporter;
    }
    
    public void setPrometheusExporter(Boolean prometheusExporter) {
        this.prometheusExporter = prometheusExporter;
    }
}
