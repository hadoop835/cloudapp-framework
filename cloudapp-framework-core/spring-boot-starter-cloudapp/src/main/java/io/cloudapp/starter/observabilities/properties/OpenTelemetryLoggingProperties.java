package io.cloudapp.starter.observabilities.properties;

import io.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(OpenTelemetryLoggingProperties.PREFIX)
public class OpenTelemetryLoggingProperties extends RefreshableProperties {
    
    public static final String PREFIX = "io.cloudapp.observabilities.ot.logging";
    
    private boolean enableTraceId;
    
    public boolean isEnableTraceId() {
        return enableTraceId;
    }
    public void setEnableTraceId(boolean enableTraceId) {
        this.enableTraceId = enableTraceId;
    }
}
