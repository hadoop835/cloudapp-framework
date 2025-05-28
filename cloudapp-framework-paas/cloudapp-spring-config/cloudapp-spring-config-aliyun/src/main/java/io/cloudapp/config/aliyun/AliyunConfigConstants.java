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
package io.cloudapp.config.aliyun;

public interface AliyunConfigConstants {
    
    String CLOUD_APP_NACOS_GET_CONFIG_ERROR = "CloudApp.Nacos.GetConfigError";
    String CLOUD_APP_NACOS_WATCH_ERROR = "CloudApp.Nacos.WatchConfigError";
    String CLOUD_APP_NACOS_DELETE_ERROR = "CloudApp.Nacos.DeleteConfigError";
    String CLOUD_APP_NACOS_PUBLISH_ERROR = "CloudApp.Nacos.PublishConfigError";
    String DEFAULT_GROUP = "DEFAULT_GROUP";
    long DEFAULT_TIMEOUT = 3000;
}
