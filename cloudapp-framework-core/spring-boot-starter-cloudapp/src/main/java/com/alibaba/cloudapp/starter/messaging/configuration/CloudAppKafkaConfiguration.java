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

package com.alibaba.cloudapp.starter.messaging.configuration;

import com.alibaba.cloudapp.messaging.kafka.CloudAppKafkaProducer;
import com.alibaba.cloudapp.starter.base.configuration.BaseAutoConfiguration;
import com.alibaba.cloudapp.starter.messaging.factory.CloudAppKafkaBeanFactory;
import com.alibaba.cloudapp.starter.messaging.properties.CloudAppKafkaProperties;
import com.alibaba.cloudapp.starter.messaging.refresh.KafkaRefreshComponent;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@AutoConfiguration(after = {BaseAutoConfiguration.class})
@ConditionalOnClass({KafkaTemplate.class, CloudAppKafkaProducer.class})
@EnableConfigurationProperties({CloudAppKafkaProperties.class})
@ConditionalOnProperty(
        prefix = "io.cloudapp.messaging.kafka",
        value = "enabled",
        havingValue = "true",
        matchIfMissing = true)
public class CloudAppKafkaConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public KafkaRefreshComponent kafkaRefreshComponent(
            CloudAppKafkaProperties properties,
            CloudAppKafkaBeanFactory beanFactory
    ) {
        return new KafkaRefreshComponent(properties, beanFactory);
    }

    @Bean
    public static CloudAppKafkaBeanFactory cloudAppKafkaBeanFactory() {
        return new CloudAppKafkaBeanFactory();
    }
}
