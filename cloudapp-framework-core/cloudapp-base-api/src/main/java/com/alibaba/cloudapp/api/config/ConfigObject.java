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
package com.alibaba.cloudapp.api.config;

import com.alibaba.cloudapp.model.BaseModel;

public class ConfigObject<T> extends BaseModel {
    /**
     * The name of the config, for Nacos , it could be like: /group/dataId
     */
    private String configName;

    /**
     * The content of the config
     */
    private T content;

    /**
     * The underlying reference object of the config
     */
    private Object refObject;

    public static class Builder<T> extends
            BaseModel.Builder<ConfigObject.Builder<T>, ConfigObject<T>> {


        public Builder<T> configName(String configName) {
            operations.add(config -> config.setConfigName(configName));
            return this;
        }

        public Builder<T> content(T content) {
            operations.add(config -> config.setContent(content));
            return this;
        }

        public Builder<T> refObject(Object refObject) {
            operations.add(config -> config.setRefObject(refObject));
            return this;
        }

        @Override
        protected void validate(ConfigObject<T> args) {

        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Object getRefObject() {
        return refObject;
    }

    public void setRefObject(Object refObject) {
        this.refObject = refObject;
    }
}
