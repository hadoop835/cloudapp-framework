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

package com.alibaba.cloudapp.starter.messaging.properties;

import com.alibaba.cloudapp.messaging.kafka.properties.KafkaConsumerProperties;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaProducerProperties;
import com.alibaba.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(CloudAppKafkaProperties.PREFIX)
public class CloudAppKafkaProperties extends RefreshableProperties {
    
    public static final String PREFIX = "io.cloudapp.messaging.kafka";

    private boolean enabled;

    private String servers;

    private KafkaProperties.Ssl ssl = new KafkaProperties.Ssl();

    private String username;

    private String password;

    private String mechanism;

    private String securityProtocol;

    private String identificationAlgorithm;

    private List<KafkaConsumerProperties> inputs;

    private List<KafkaProducerProperties> outputs;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getServers() {
        return servers;
    }

    public String getMechanism() {
		return mechanism;
	}

    public void setMechanism(String mechanism) {
		this.mechanism = mechanism;
	}

    public void setServers(String servers) {
        this.servers = servers;
    }

    public KafkaProperties.Ssl getSsl() {
        return ssl;
    }

    public void setSsl(KafkaProperties.Ssl ssl) {
        this.ssl = ssl;
    }

    public List<KafkaConsumerProperties> getInputs() {
        return inputs;
    }

    public void setInputs(List<KafkaConsumerProperties> inputs) {
        this.inputs = inputs;
    }

    public List<KafkaProducerProperties> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<KafkaProducerProperties> outputs) {
        this.outputs = outputs;
    }

    public String getUsername() {
		return username;
	}

    public void setUsername(String username) {
		this.username = username;
	}

    public String getPassword() {
		return password;
	}

    public void setPassword(String password) {
		this.password = password;
	}

    public String getSecurityProtocol() {
		return securityProtocol;
	}

    public void setSecurityProtocol(String securityProtocol) {
		this.securityProtocol = securityProtocol;
	}

    public String getIdentificationAlgorithm() {
		return identificationAlgorithm;
	}

    public void setIdentificationAlgorithm(String identificationAlgorithm) {
		this.identificationAlgorithm = identificationAlgorithm;
	}
}
