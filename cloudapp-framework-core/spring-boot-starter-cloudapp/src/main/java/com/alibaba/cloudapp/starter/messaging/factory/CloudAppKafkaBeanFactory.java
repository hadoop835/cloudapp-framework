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

package com.alibaba.cloudapp.starter.messaging.factory;

import com.alibaba.cloudapp.api.messaging.model.ConsumerType;
import com.alibaba.cloudapp.messaging.kafka.CloudAppKafkaConsumerFactory;
import com.alibaba.cloudapp.messaging.kafka.CloudAppKafkaProducerFactory;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaConsumerProperties;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaProducerProperties;
import com.alibaba.cloudapp.starter.messaging.properties.CloudAppKafkaProperties;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CloudAppKafkaBeanFactory implements BeanFactoryPostProcessor,
        EnvironmentAware {
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppKafkaBeanFactory.class);
    
    private static final Map<String, CloudAppKafkaProducerFactory<?, ?>> producerCache =
            Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, CloudAppKafkaConsumerFactory<?, ?>> consumerCache =
            Collections.synchronizedMap(new HashMap<>());
    
    private CloudAppKafkaProperties properties;
    private Map<String, Object> authConfig;
    private ConfigurableListableBeanFactory beanFactory;
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        this.beanFactory = beanFactory;
        
        // init producer bean
        if (properties.getOutputs() != null) {
            properties.getOutputs().forEach(output -> {
                if (beanFactory.containsBean(output.getName())) {
                    String msg = String.format(
                            "create kafka producer error, there has exists a bean named %s",
                            output.getName()
                    );
                    throw new IllegalStateException(msg);
                }
                
                initProducer(output);
            });
        } else {
            logger.warn("kafka output properties is empty");
        }
        
        // init consumer bean
        if (properties.getInputs() != null) {
            properties.getInputs().forEach(input -> {
                if (beanFactory.containsBean(input.getName() + "Factory")) {
                    throw new IllegalStateException(
                            "Create kafka consumer error, exists a bean named "
                                    + input.getName());
                }
                
                if (ConsumerType.PUSH.toString().equals(input.getType())) {
                    
                    return;
                }
                
                initConsumer(input);
            });
        } else {
            logger.warn("kafka input properties is empty");
        }
        
    }
    
    private void initConsumer(KafkaConsumerProperties input) {
        initConsumerProperties(properties, input);
        String name = input.getName() + "Factory";
        String configName = input.getName() + "Config";
        
        CloudAppKafkaConsumerFactory<?, ?> consumerFactory;
        if (consumerCache.containsKey(input.getName())) {
            consumerFactory = consumerCache.get(input.getName());
            consumerFactory.refresh(input.buildConsumerProperties());
            
        } else {
            consumerFactory = new CloudAppKafkaConsumerFactory<>(
                    input.buildConsumerProperties());
            consumerCache.put(input.getName(), consumerFactory);
        }
        
        boolean exists = beanFactory.containsBean(name);
        if (!exists) {
            beanFactory.registerSingleton(configName, input);
            beanFactory.registerSingleton(name, consumerFactory);
            
        } else {
            KafkaConsumerProperties prop = (KafkaConsumerProperties) beanFactory.getBean(configName);
            BeanUtils.copyProperties(input, prop);
        }
    }
    
    private void initProducer(KafkaProducerProperties output) {
        
        initProducerProperties(properties, output);
        
        String name = output.getName() + "Factory";
        String templateName = output.getName() + "Template";
        String configName = output.getName() + "Config";
        
        CloudAppKafkaProducerFactory<?, ?> producerFactory;
        if (producerCache.containsKey(output.getName())) {
            producerFactory = producerCache.get(output.getName());
            producerFactory.refresh(output.buildProducerProperties());
            
        } else {
            
            producerFactory = new CloudAppKafkaProducerFactory<>(
                    output.buildProducerProperties());
            producerCache.put(output.getName(), producerFactory);
        }
        
        boolean exists = beanFactory.containsBean(name);
        if (!exists) {
            KafkaTemplate<?, ?> producerTemplate = new KafkaTemplate<>(
                    producerFactory);
            producerTemplate.setDefaultTopic(output.getTopic());
            
            //add to factory
            beanFactory.registerSingleton(configName, output);
            beanFactory.registerSingleton(name, producerFactory);
            beanFactory.registerSingleton(templateName, producerTemplate);
        } else {
            KafkaProducerProperties prop = (KafkaProducerProperties) beanFactory.getBean(configName);
            BeanUtils.copyProperties(output, prop);
        }
    }
    
    /**
     * Merge authorization configuration into producer
     *
     * @param properties authorization config
     * @param output     producer config
     */
    private void initProducerProperties(
            CloudAppKafkaProperties properties,
            KafkaProducerProperties output
    ) {
        if (!StringUtils.hasText(output.getBootstrapServers())) {
            output.setBootstrapServers(properties.getServers());
        }
        
        if (output.getProperties() != null) {
            output.getProperties().putAll(authConfig);
        } else {
            output.setProperties(authConfig);
        }
    }
    
    /**
     * Merge authorization configuration into consumer
     *
     * @param properties authorization config
     * @param input      consumer config
     */
    private void initConsumerProperties(
            CloudAppKafkaProperties properties,
            KafkaConsumerProperties input
    ) {
        if (!StringUtils.hasText(input.getBootstrapServers())) {
            input.setBootstrapServers(properties.getServers());
        }
        
        if (input.getProperties() != null) {
            input.getProperties().putAll(authConfig);
        } else {
            input.setProperties(authConfig);
        }
    }
    
    private void initSslProperties(CloudAppKafkaProperties properties) {
        if (properties.getSsl() != null) {
            authConfig = properties.getSsl().buildProperties();
        } else {
            authConfig = new HashMap<>();
        }
        //init security protocol
        if (StringUtils.hasText(properties.getSecurityProtocol())) {
            authConfig.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,
                           properties.getSecurityProtocol()
            );
        }
        
        //init sasl mechanism
        if (StringUtils.hasText(properties.getMechanism())) {
            authConfig.put(SaslConfigs.SASL_MECHANISM,
                           properties.getMechanism()
            );
        }
        
        // init bootstrap servers
        if (StringUtils.hasText(properties.getServers())) {
            authConfig.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
                           properties.getServers()
            );
        }
        
        // add username and password
        if (StringUtils.hasText(
                properties.getUsername()) && StringUtils.hasText(
                properties.getPassword())) {
            String prefix = "org.apache.kafka.common.security.scram.ScramLoginModule";
            if ("PLAIN".equalsIgnoreCase(properties.getMechanism())) {
                prefix = "org.apache.kafka.common.security.plain.PlainLoginModule";
            }
            
            String jaasConfig = String.format(
                    "%s required username=\"%s\" password=\"%s\";",
                    prefix, properties.getUsername(), properties.getPassword()
            );
            
            authConfig.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        }
        
        // Set client authentication algorithm
        authConfig.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG,
                       StringUtils.hasText(
                               properties.getIdentificationAlgorithm())
                               ? properties.getIdentificationAlgorithm() : ""
        );
    }
    
    public void refresh(CloudAppKafkaProperties properties) {
        if(properties == null || (properties.getServers() == null
                && properties.getInputs() == null && properties.getOutputs() == null)) {
            return;
        }
        
        this.properties = properties;
        initSslProperties(properties);
        
        if(properties.getInputs() != null) {
            properties.getInputs().forEach(this::initConsumer);
        }
        
        if(properties.getOutputs() != null) {
            properties.getOutputs().forEach(this::initProducer);
        }
    }
    
    @Override
    public void setEnvironment(Environment environment)
            throws BeansException {
        if (properties == null || (!StringUtils.hasText(properties.getServers())
            && properties.getInputs() == null && properties.getOutputs() == null)){
            
            BindResult<CloudAppKafkaProperties> result = Binder
                    .get(environment)
                    .bind(CloudAppKafkaProperties.PREFIX,
                          CloudAppKafkaProperties.class
                    );
            
            if(this.properties == null) {
                properties = new CloudAppKafkaProperties();
            }
            BeanUtils.copyProperties(result.get(), this.properties);
        }
        
        //init ssl config
        initSslProperties(properties);
    }
    
}
