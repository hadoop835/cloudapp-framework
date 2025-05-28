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
package io.cloudapp.starter.filestore.configuration;

import io.cloudapp.filestore.minio.ClientProvider;
import io.cloudapp.starter.base.RefreshableComponent;
import io.cloudapp.starter.filestore.properties.MinioEndpointProperties;
import io.minio.MinioClient;

public class MinioFileStoreComponent extends
        RefreshableComponent<MinioEndpointProperties, ClientProvider> {
    
    public static final String BINDING_PROP_KEY = "io.cloudapp.filestore.minio";
    
    
    public MinioFileStoreComponent(MinioEndpointProperties properties) {
        super(properties);
    }
    
    @Override
    public void postStart() {
        MinioClient client = MinioClient
                .builder()
                .region(properties.getRegion())
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(),
                             properties.getSecretKey()
                )
                .build();
        this.bean.setClient(client);
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
        return "filestore.minio";
    }
    
    @Override
    protected ClientProvider createBean(MinioEndpointProperties properties) {
        
        MinioClient client = MinioClient
                .builder()
                .region(properties.getRegion())
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(),
                             properties.getSecretKey()
                )
                .build();
        
        return new ClientProvider(client);
    }
    
    
}
