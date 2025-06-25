package com.alibaba.cloudapp.starter.messaging.properties;

import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketConsumerProperties;
import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketProducerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class CloudAppRocketPropertiesTest {
    
    private CloudAppRocketProperties properties;
    
    @Mock
    private ApplicationContext applicationContext;
    
    @BeforeEach
    void setUp() {
        properties = new CloudAppRocketProperties();
        properties.setApplicationContext(applicationContext);
    }
    
    @Test
    void testEnabledGetterAndSetter() {
        final boolean enabled = false;
        properties.setEnabled(enabled);
        assertFalse(properties.isEnabled());
    }
    
    @Test
    void testNameServerGetterAndSetter() {
        final String nameServer = "nameServer";
        properties.setNameServer(nameServer);
        assertEquals(nameServer,
                     properties.getNameServer()
        );
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
    void testUseTLSGetterAndSetter() {
        final boolean useTLS = false;
        properties.setUseTLS(useTLS);
        assertFalse(properties.isUseTLS());
    }
    
    @Test
    void testEnableMsgTraceGetterAndSetter() {
        final boolean enableMsgTrace = false;
        properties.setEnableMsgTrace(enableMsgTrace);
        assertFalse(properties.isEnableMsgTrace());
    }
    
    @Test
    void testTraceTopicGetterAndSetter() {
        final String traceTopic = "traceTopic";
        properties.setTraceTopic(traceTopic);
        assertEquals(traceTopic,
                     properties.getTraceTopic()
        );
    }
    
    @Test
    void testInputsGetterAndSetter() {
        final List<RocketConsumerProperties> inputs = Arrays.asList(
                new RocketConsumerProperties());
        properties.setInputs(inputs);
        assertEquals(inputs, properties.getInputs());
    }
    
    @Test
    void testOutputsGetterAndSetter() {
        final List<RocketProducerProperties> outputs = Arrays.asList(
                new RocketProducerProperties());
        properties.setOutputs(outputs);
        assertEquals(outputs, properties.getOutputs());
    }
    
    @Test
    void testAccessChannelGetterAndSetter() {
        final String accessChannel = "accessChannel";
        properties.setAccessChannel(accessChannel);
        assertEquals(accessChannel,
                     properties.getAccessChannel()
        );
    }
    
}
