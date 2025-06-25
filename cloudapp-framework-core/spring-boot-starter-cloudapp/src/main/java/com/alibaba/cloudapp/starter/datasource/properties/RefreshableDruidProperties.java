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
package com.alibaba.cloudapp.starter.datasource.properties;

import com.alibaba.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = RefreshableDruidProperties.BIND_KEY)
public class RefreshableDruidProperties extends RefreshableProperties {

    public static final String BIND_KEY = "io.cloudapp.datasource.druid";

    /*
     * manage version, used for trigger refresh.
     */
    private String manageVersion = "1.0";

    /*
     * max wait seconds for refresh
     */
    private int maxRefreshWaitSeconds = 30;

    public void setManageVersion(String manageVersion) {
        this.manageVersion = manageVersion;
    }

    public String getManageVersion() {
        return manageVersion;
    }

    public int getMaxRefreshWaitSeconds() {
        return maxRefreshWaitSeconds;
    }

    public void setMaxRefreshWaitSeconds(int maxRefreshWaitSeconds) {
        this.maxRefreshWaitSeconds = maxRefreshWaitSeconds;
    }
}
