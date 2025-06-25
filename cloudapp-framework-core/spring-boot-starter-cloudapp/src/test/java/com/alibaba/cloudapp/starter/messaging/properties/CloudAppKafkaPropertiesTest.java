package com.alibaba.cloudapp.starter.messaging.properties;

import com.alibaba.cloudapp.messaging.kafka.properties.KafkaConsumerProperties;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaProducerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class CloudAppKafkaPropertiesTest {
    
    private CloudAppKafkaProperties properties;
    @Mock
    private ApplicationContext context;
    
    @BeforeEach
    void setUp() {
        properties = new CloudAppKafkaProperties();
        properties.setApplicationContext(context);
    }
    
    @Test
    void testEnabledGetterAndSetter() {
        final boolean enabled = false;
        properties.setEnabled(enabled);
        assertFalse(properties.isEnabled());
    }
    
    @Test
    void testServersGetterAndSetter() {
        final String servers = "servers";
        properties.setServers(servers);
        assertEquals(servers, properties.getServers());
    }
    
    @Test
    void testMechanismGetterAndSetter() {
        final String mechanism = "mechanism";
        properties.setMechanism(mechanism);
        assertEquals(mechanism,
                     properties.getMechanism()
        );
    }
    
    @Test
    void testSslGetterAndSetter() {
        final KafkaProperties.Ssl ssl = new KafkaProperties.Ssl();
        properties.setSsl(ssl);
        assertEquals(ssl, properties.getSsl());
    }
    
    @Test
    void testInputsGetterAndSetter() {
        final List<KafkaConsumerProperties> inputs = Arrays.asList(
                new KafkaConsumerProperties());
        properties.setInputs(inputs);
        assertEquals(inputs, properties.getInputs());
    }
    
    @Test
    void testOutputsGetterAndSetter() {
        final List<KafkaProducerProperties> outputs = Arrays.asList(
                new KafkaProducerProperties());
        properties.setOutputs(outputs);
        assertEquals(outputs, properties.getOutputs());
    }
    
    @Test
    void testUsernameGetterAndSetter() {
        final String username = "username";
        properties.setUsername(username);
        assertEquals(username, properties.getUsername());
    }
    
    @Test
    void testPasswordGetterAndSetter() {
        final String password = "password";
        properties.setPassword(password);
        assertEquals(password, properties.getPassword());
    }
    
    @Test
    void testSecurityProtocolGetterAndSetter() {
        final String securityProtocol = "securityProtocol";
        properties.setSecurityProtocol(securityProtocol);
        assertEquals(securityProtocol,
                     properties.getSecurityProtocol()
        );
    }
    
    @Test
    void testIdentificationAlgorithmGetterAndSetter() {
        final String identificationAlgorithm = "identificationAlgorithm";
        properties.setIdentificationAlgorithm(
                identificationAlgorithm);
        assertEquals(identificationAlgorithm,
                     properties.getIdentificationAlgorithm()
        );
    }
    
}
