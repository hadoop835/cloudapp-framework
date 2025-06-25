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

package com.alibaba.cloudapp.messaging.kafka.properties;

import com.alibaba.cloudapp.util.CloudAppStringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class KafkaProducerProperties {

    private String group;

    private String name;

    private String topic;

    private Integer partition;

    private String bootstrapServers;

    private int reconnectBackoff = 3000;

    /**
     * Serializer class for keys.
     */
    private Class<?> keySerializer = StringSerializer.class;

    /**
     * Serializer class for values.
     */
    private Class<?> valueSerializer = StringSerializer.class;

    /**
     * Additional producer-specific properties used to configure the client.
     */
    private Map<String, Object> properties = new HashMap<>();

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

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public Class<?> getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(Class<?> keySerializer) {
        this.keySerializer = keySerializer;
    }

    public Class<?> getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(Class<?> valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new HashMap<>();
        if(properties != null) {
            properties.forEach((k, v) ->
                    this.properties.put(
                            CloudAppStringUtils.trimSpaceChar(k),
                            CloudAppStringUtils.trimSpaceChar(v)
                    )
            );
        }
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers == null ?
                null : CloudAppStringUtils.trimSpaceChar(bootstrapServers);
    }

    public int getReconnectBackoff() {
        return reconnectBackoff;
    }

    public void setReconnectBackoff(int reconnectBackoff) {
        this.reconnectBackoff = reconnectBackoff;
    }

    public String getGroup() {
		return group;
	}

    public void setGroup(String group) {
		this.group = group;
	}

    /**
     * build producer config map
     * @return config map
     */
    public Map<String, Object> buildProducerProperties() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.CLIENT_ID_CONFIG, this.name);

        if (StringUtils.hasText(this.group)) {
            config.put(CommonClientConfigs.GROUP_ID_CONFIG, this.group);
        }
        //Config serializer
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, this.keySerializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, this.valueSerializer);
        //set server
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);

        config.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, this.reconnectBackoff);

        if (properties != null) {
            config.putAll(properties);
        }
        return config;
    }
}
