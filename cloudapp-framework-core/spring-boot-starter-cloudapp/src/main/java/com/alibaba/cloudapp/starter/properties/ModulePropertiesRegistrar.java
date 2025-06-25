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

package com.alibaba.cloudapp.starter.properties;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * In order to solve the problem of properties class inheritance configuration coverage
 */
public class ModulePropertiesRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        metadata.getAnnotations()
                .stream(EnableModuleProperties.class)
                .flatMap((annotation) -> Arrays.stream(annotation.getClassArray(MergedAnnotation.VALUE)))
                .filter((type) -> void.class != type)
                .forEach(type -> {

                    // check the super class of current class, and must not be Object.class
                    boolean createSuperBean = !type.getSuperclass().equals(Object.class)
                            && !registry.containsBeanDefinition(type.getSuperclass().getName());

                    Optional<String> superConfigRoot = getClassConfigurationRoot(type.getSuperclass());

                    // check super class is a configuration class, and environment contain that's properties
                    createSuperBean = createSuperBean && superConfigRoot.isPresent() && !superConfigRoot.get().isEmpty();
//                            && containsProperty(type.getSuperclass(), superConfigRoot.get());

                    RootBeanDefinition beanDefinition;

                    if(createSuperBean && !registry.containsBeanDefinition(type.getSuperclass().getName())) {
                        // create superclass bean, To prevent overwriting parent class configuration
                        beanDefinition = new RootBeanDefinition(type.getSuperclass());
                        beanDefinition.setParentName(null);
                        beanDefinition.setDecoratedDefinition(null);
                        beanDefinition.setTargetType(type.getSuperclass());
                        beanDefinition.setPrimary(true); // must be primary
                        registry.registerBeanDefinition(type.getSuperclass().getName(), beanDefinition);
                    }

                    // create current properties bean
                    beanDefinition = new RootBeanDefinition(type);
                    beanDefinition.setParentName(null);
                    beanDefinition.setDecoratedDefinition(null);
                    beanDefinition.setTargetType(type);
                    registry.registerBeanDefinition(type.getName(), beanDefinition);
                });
    }

    private Optional<String> getClassConfigurationRoot(Class<?> clazz) {
        ConfigurationProperties properties = clazz.getAnnotation(ConfigurationProperties.class);
        if(Objects.isNull(properties)) {
            return Optional.empty();
        }
        if(properties.prefix().isEmpty()) {
            return Optional.of(properties.value());
        }
        return Optional.of(properties.prefix());
    }

    private Boolean containsProperty(Class<?> clazz, String prefix) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            String name = field.getName();
            String key = prefix + "." + name;

            if (environment.containsProperty(key)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
