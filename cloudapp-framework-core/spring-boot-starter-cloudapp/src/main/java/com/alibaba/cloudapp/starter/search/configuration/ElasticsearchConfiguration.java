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
package com.alibaba.cloudapp.starter.search.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.cloudapp.exeption.CloudAppInvalidAccessException;
import com.alibaba.cloudapp.search.elasticsearch.CloudAppElasticsearchService;
import com.alibaba.cloudapp.starter.properties.EnableModuleProperties;
import com.alibaba.cloudapp.starter.refresh.RefreshableProxyFactory;
import com.alibaba.cloudapp.starter.refresh.aspect.RefreshableBinding;
import com.alibaba.cloudapp.starter.search.properties.ElasticsearchProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

@AutoConfiguration
@ConditionalOnClass(ElasticsearchClient.class)
@EnableModuleProperties(ElasticsearchProperties.class)
@ConditionalOnProperty(prefix = "io.cloudapp.search.elasticsearch",
        value = "enabled",
        havingValue = "true")
public class ElasticsearchConfiguration {
    
    @Bean("elasticsearchClient")
    @ConditionalOnMissingBean
    @RefreshableBinding(ElasticsearchProperties.PREFIX)
    public ElasticsearchClient elasticsearchClient(ElasticsearchProperties properties)
            throws CloudAppInvalidAccessException {
        return RefreshableProxyFactory.create(
                this::createElasticSearchClient, properties);
    }
    
    private ElasticsearchClient createElasticSearchClient(
            ElasticsearchProperties properties
    ) {
        Assert.hasText(properties.getHost(),
                       "Elasticsearch host must be provided"
        );
        Assert.hasText(properties.getUsername(),
                       "Elasticsearch username must be provided"
        );
        Assert.hasText(properties.getPassword(),
                       "Elasticsearch password must be provided"
        );
        
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                properties.getUsername(), properties.getPassword());
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        
        String scheme = properties.isUseHttps() ? "https" : "http";
        HttpHost host = new HttpHost(
                properties.getHost(), properties.getPort(), scheme);
        
        RestClient restClient = RestClient
                .builder(host)
                .setHttpClientConfigCallback(
                        c -> c.setDefaultCredentialsProvider(credentialsProvider))
                .build();
        
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper()
        );
        
        return new ElasticsearchClient(transport);
    }
    
    @Bean("cloudAppElasticsearchService")
    @ConditionalOnMissingBean
    public CloudAppElasticsearchService cloudAppElasticsearchService(
            ElasticsearchClient client) {
        return new CloudAppElasticsearchService(client);
    }
    
}
