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

package com.alibaba.cloudapp.starter.mail.configuration;

import com.alibaba.cloudapp.api.email.EmailService;
import com.alibaba.cloudapp.api.email.TemplateEmailService;
import com.alibaba.cloudapp.mail.BaseEmailService;
import com.alibaba.cloudapp.starter.mail.properties.CloudAppMailProperties;
import com.alibaba.cloudapp.starter.mail.refresh.MailSenderRefreshableComponent;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;

@AutoConfiguration
@ConditionalOnClass({BaseEmailService.class, JavaMailSender.class})
@ConditionalOnProperty(prefix = CloudAppMailProperties.PREFIX, name = "enabled",
        havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(CloudAppMailProperties.class)
@Import({ThymeleafMailAutoConfiguration.class,
        FreemarkerMailAutoConfiguration.class})
public class CloudAppMailAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public MailSenderRefreshableComponent refreshComponent(
            CloudAppMailProperties properties
    ) {
        return new MailSenderRefreshableComponent(properties);
    }
    
    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public JavaMailSender javaMailSender(MailSenderRefreshableComponent comp) {
        return comp.getBean();
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CloudAppMailProperties.PREFIX, name = "jndi-name")
    public Session session(MailSenderRefreshableComponent component) {
        return component.getSession();
    }
    
    @Bean
    @ConditionalOnMissingBean({
            EmailService.class,
            TemplateEmailService.class
    })
    public BaseEmailService emailService(JavaMailSender javaMailSender) {
        return new BaseEmailService(javaMailSender);
    }
    
}
