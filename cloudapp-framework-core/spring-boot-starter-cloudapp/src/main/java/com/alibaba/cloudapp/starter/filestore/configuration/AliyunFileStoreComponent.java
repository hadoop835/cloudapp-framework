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
package com.alibaba.cloudapp.starter.filestore.configuration;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.auth.DefaultCredentials;
import com.aliyun.oss.internal.OSSConstants;
import com.alibaba.cloudapp.starter.base.RefreshableComponent;
import com.alibaba.cloudapp.starter.filestore.properties.OSSEndpointProperties;
import org.springframework.util.StringUtils;

public class AliyunFileStoreComponent extends
        RefreshableComponent<OSSEndpointProperties, OSSClient> {

    public static final String BINDING_PROP_KEY = "io.cloudapp.filestore.aliyun";

    public AliyunFileStoreComponent(OSSEndpointProperties properties) {
        super(properties);
    }

    @Override
    public void postStart() {
        Credentials creds = new DefaultCredentials(properties.getAccessKey(),
                properties.getSecretKey(), properties.getStsToken());

        if(!StringUtils.hasText(properties.getEndpoint())) {
            bean.setEndpoint(OSSConstants.DEFAULT_OSS_ENDPOINT);
        } else {
            bean.setEndpoint(properties.getEndpoint());
        }

        if(StringUtils.hasText(properties.getRegion())) {
            bean.setRegion(properties.getRegion());
        }

        bean.switchCredentials(creds);
    }

    @Override
    public void preStop() {

    }

    @Override
    public String bindKey() {
        return BINDING_PROP_KEY;
    }

    @Override
    public String getName() {
        return "filestore.aliyun";
    }

    @Override
    protected OSSClient createBean(OSSEndpointProperties properties) {
        OSSClientBuilder builder = new OSSClientBuilder();

        return (OSSClient) builder.build(properties.getEndpoint(),
                properties.getAccessKey(),
                properties.getSecretKey(),
                properties.getStsToken());
    }
}
