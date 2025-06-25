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

package com.alibaba.cloudapp.messaging.rocketmq.properties;

import com.alibaba.cloudapp.messaging.rocketmq.model.RocketDestination;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;

import java.util.ArrayList;
import java.util.List;

public class RocketConsumerProperties {

    private String accessChannel;

    private String group;

    private MessageModel messageModel = MessageModel.CLUSTERING;

    private int pullBatchSize = 10;

    private String namespace;

    private String name;

    private String topic;

    private List<String> tags = new ArrayList<>();

    private String nameServer;

    private String username;

    private String password;

    private boolean useTLS = false;

    private Boolean enableMsgTrace;

    private String traceTopic;

    private String type;

    private boolean isDefault = false;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccessChannel() {
        return accessChannel;
    }

    public void setAccessChannel(String accessChannel) {
        this.accessChannel = accessChannel;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public int getPullBatchSize() {
        return pullBatchSize;
    }

    public void setPullBatchSize(int pullBatchSize) {
        this.pullBatchSize = pullBatchSize;
    }


    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
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

    public boolean isUseTLS() {
        return useTLS;
    }

    public void setUseTLS(boolean useTLS) {
        this.useTLS = useTLS;
    }

    public Boolean isEnableMsgTrace() {
        return enableMsgTrace;
    }

    public void setEnableMsgTrace(Boolean enableMsgTrace) {
        this.enableMsgTrace = enableMsgTrace;
    }

    public String getTraceTopic() {
        return traceTopic;
    }

    public void setTraceTopic(String traceTopic) {
        this.traceTopic = traceTopic;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public RocketMQProperties.PullConsumer toRocketMqProperty() {
        RocketMQProperties.PullConsumer consumer = new RocketMQProperties.PullConsumer();
        consumer.setAccessKey(username);
        consumer.setSecretKey(password);
        consumer.setGroup(group);
        consumer.setInstanceName(name);
        consumer.setCustomizedTraceTopic(traceTopic);
        consumer.setEnableMsgTrace(enableMsgTrace);
        consumer.setMessageModel(messageModel.name());
        consumer.setNamespace(namespace);
        consumer.setPullBatchSize(pullBatchSize);
        RocketDestination destination = new RocketDestination(topic, tags);
        consumer.setSelectorType(SelectorType.TAG.name());
        consumer.setSelectorExpression(destination.getTagsString());
        consumer.setTopic(topic);
        consumer.setTlsEnable(useTLS);
        return consumer;
    }

}
