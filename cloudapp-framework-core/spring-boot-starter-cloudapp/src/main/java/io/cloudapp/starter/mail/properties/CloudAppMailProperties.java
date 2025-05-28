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

package io.cloudapp.starter.mail.properties;

import io.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = CloudAppMailProperties.PREFIX)
public class CloudAppMailProperties extends RefreshableProperties {
    
    public static final String PREFIX = "io.cloudapp.mail";
    
    /**
     * SMTP server host. For instance, 'smtp.example.com'.
     */
    private String host;
    
    /**
     * SMTP server port.
     */
    private Integer port;
    
    /**
     * Login user of the SMTP server.
     */
    private String username;
    
    /**
     * Login password of the SMTP server.
     */
    private String password;
    
    /**
     * Protocol used by the SMTP server.
     */
    private String protocol = "smtp";
    
    /**
     * Default MimeMessage encoding.
     */
    private Charset defaultEncoding = StandardCharsets.UTF_8;
    
    /**
     * Additional JavaMail Session properties.
     */
    private final Map<String, String> properties = new HashMap<>();
    
    /**
     * Session JNDI name. When set, takes precedence over other Session settings.
     */
    private String jndiName;
    
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
    
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    public Charset getDefaultEncoding() {
        return defaultEncoding;
    }
    
    public void setDefaultEncoding(Charset defaultEncoding) {
        if(defaultEncoding == null) {
            this.defaultEncoding = StandardCharsets.UTF_8;
        } else {
            this.defaultEncoding = defaultEncoding;
        }
    }
    
    public Map<String, String> getProperties() {
        return properties;
    }
    
    public void setProperties(Map<String, String> properties) {
        this.properties.putAll(properties);
    }
    
    public String getJndiName() {
        return jndiName;
    }
    
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }
    
}
