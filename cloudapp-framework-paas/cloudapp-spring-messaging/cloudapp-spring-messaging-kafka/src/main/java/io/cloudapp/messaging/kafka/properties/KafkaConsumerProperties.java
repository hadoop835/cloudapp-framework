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

package io.cloudapp.messaging.kafka.properties;

import io.cloudapp.util.CloudAppStringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.IsolationLevel;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KafkaConsumerProperties {

    private String group;

    private String name;

    private String topic;

    private String type;
    
    private String listenerBean;

    private String bootstrapServers;

    private String autoOffsetReset;

    private int maxFetchBytes;

    private int sessionTimeout;

    private Class<?> keyDeserializer = StringDeserializer.class;

    private Class<?> valueDeserializer = StringDeserializer.class;

    private IsolationLevel isolationLevel = IsolationLevel.READ_UNCOMMITTED;

    private Map<String, Object> properties;


    public String getListenerBean() {
        return this.listenerBean;
    }

    public void setListenerBean(String listenerBean) {
        this.listenerBean = listenerBean == null ? null : CloudAppStringUtils.trimSpaceChar(listenerBean);
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group == null ? null : CloudAppStringUtils.trimSpaceChar(group);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : CloudAppStringUtils.trimSpaceChar(name);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic == null ? null : CloudAppStringUtils.trimSpaceChar(topic);
    }
    
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers == null ? null
                : CloudAppStringUtils.trimSpaceChar(bootstrapServers);
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset == null ? null
                : CloudAppStringUtils.trimSpaceChar(autoOffsetReset);
    }

    public Class<?> getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(Class<?> keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public Class<?> getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(Class<?> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    public IsolationLevel getIsolationLevel() {
        return isolationLevel;
    }

    public void setIsolationLevel(IsolationLevel isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new HashMap<>();
        if (properties != null) {
            properties.forEach((k, v) ->
                    this.properties.put(
                            CloudAppStringUtils.trimSpaceChar(k),
                            CloudAppStringUtils.trimSpaceChar(v)
                    )
            );
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : CloudAppStringUtils.trimSpaceChar(type);
    }

    public int getMaxFetchBytes() {
        return maxFetchBytes;
    }

    public void setMaxFetchBytes(int maxFetchBytes) {
        this.maxFetchBytes = maxFetchBytes;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    /**
     * build consumer properties map
     * @return config map
     */
    public Map<String, Object> buildConsumerProperties() {
        Map<String, Object> config = new HashMap<>();
        //Config auto offset reset
        if (StringUtils.hasText(this.autoOffsetReset)) {
            config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.autoOffsetReset);
        }
        //Config session timeout
        if (this.sessionTimeout > 0) {
            config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, this.sessionTimeout);
        }
        //Config max fetch bytes
        if (this.maxFetchBytes > 0) {
            config.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, this.maxFetchBytes);
            config.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, this.maxFetchBytes);
        }

        //client
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, this.name);
        if (StringUtils.hasText(this.group)) {
            config.put(ConsumerConfig.GROUP_ID_CONFIG, this.group);
        }

        config.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, this.isolationLevel.name().toLowerCase(Locale.ROOT));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, this.keyDeserializer);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.valueDeserializer);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);

        if (properties != null) {
            config.putAll(properties);
        }
        return config;
    }
}
