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
package io.cloudapp.api.config;

import io.cloudapp.api.common.DelegatingClientAware;
import io.cloudapp.exeption.CloudAppException;

import java.util.function.Consumer;

public interface ConfigReadService<Client> extends DelegatingClientAware<Client> {
    String DATA_ID_SEPARATOR_CHAR = "/";
    /**
     * Get the config value as a string
     *
     * @param configName the config name, for Nacos this could be like for format
     *                   /group/dataId.
     * @return the config value as a string
     */
    String  getConfig(String configName) throws CloudAppException;

    /**
     * Get the config value as a required object.
     *
     * @param configName the config name, for Nacos this could be like for format
     *                   /group/dataId.
     * @param cls        the class type of the required object.
     * @return the config value as a request object.
     * @throws CloudAppException if the config cannot be found or converted.
     */
    <T> T  getConfig(String configName, Class<T> cls) throws CloudAppException;

    /**
     * Get the config value as a required object, and listen for the updates.
     *
     * @param configName the config name, for Nacos this could be like for format
     *                   /group/dataId.
     * @param listener    the listener to the config value when it's updated.
     * @return the config value as a string.
     */
    String getAndListen(String configName, Consumer<String> listener)
            throws CloudAppException;
}
