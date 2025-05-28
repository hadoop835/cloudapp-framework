package io.cloudapp.observabilities.opentelemetry.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FieldMethodArgsWrapperTest {
    
    @Test
    public void test() {
        FieldMethodArgsWrapper argsWrapper = new FieldMethodArgsWrapper(
                "test", new Object[]{"test"});
        Assert.assertEquals("test", argsWrapper.getName());
        Assert.assertArrayEquals(new Object[]{"test"}, argsWrapper.getArgs());
        argsWrapper.setName("test2");
        Assert.assertEquals("test2", argsWrapper.getName());
        argsWrapper.setArgs(new Object[]{"test2"});
        Assert.assertArrayEquals(new Object[]{"test2"}, argsWrapper.getArgs());
        
    }
}
