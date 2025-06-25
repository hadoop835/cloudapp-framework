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

import com.alibaba.cloudapp.api.common.DelegatingClientAware;
import com.alibaba.cloudapp.exeption.CloudAppException;

import java.util.Collection;
import java.util.List;

public interface ConfigManager<Client> extends DelegatingClientAware<Client> {

    /**
     * Delete a config
     *
     * @param configName String config value.
     *
     * @return true if the config was deleted.
     */
    boolean deleteConfig(String configName) throws CloudAppException;

    /**
     * Delete a config
     *
     * @param config ConfigObject config value.
     *
     * @return true if the config was deleted.
     */
    boolean deleteConfig(ConfigObject<?> config) throws CloudAppException;

    /**
     * Batch delete a collection of configs
     *
     * @param configs Collection of ConfigObject config value.
     *
     * @return true if the all the configs were deleted.
     */
    List<Boolean> deleteConfigs(Collection<ConfigObject<?>> configs) throws CloudAppException;

    /**
     * Publish a config
     *
     * @param configName String config value.
     * @param content String config value.
     *
     * @return true if the config was published.
     */
    boolean publish(String configName, String content) throws CloudAppException;

    /**
     * Publish a config
     *
     * @param object ConfigObject config value.
     *
     * @return true if the config was published.
     */
    <T> boolean publishConfig(ConfigObject<T> object) throws CloudAppException;

    /**
     * Batch publish a collection of configs
     *
     * @param configs Collection of ConfigObject config value.
     *
     * @return true if the all the configs were published.
     */
    List<Boolean> publishConfigs(Collection<ConfigObject<?>> configs) throws CloudAppException;
}
