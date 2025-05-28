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
package io.cloudapp.starter.search.properties;

import io.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = ElasticsearchProperties.PREFIX)
public class ElasticsearchProperties extends RefreshableProperties {
    
    public static final String PREFIX = "io.cloudapp.search.elasticsearch";
    
    /**
     * Enable ES, default false
     */
    private boolean enabled = false;
    /**
     * ES cluster address
     */
    private String host;
    /**
     * ES cluster port, default 9200
     */
    private Integer port = 9200;
    /**
     * Access username
     */
    private String username;
    /**
     * Access password
     */
    private String password;
    
    private boolean useHttps = false;
    
    public boolean isUseHttps() {
        return useHttps;
    }
    
    public void setUseHttps(boolean useHttps) {
        this.useHttps = useHttps;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
