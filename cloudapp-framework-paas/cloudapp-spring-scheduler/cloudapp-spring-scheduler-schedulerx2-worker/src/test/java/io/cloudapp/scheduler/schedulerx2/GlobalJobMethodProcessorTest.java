package io.cloudapp.scheduler.schedulerx2;

import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GlobalJobMethodProcessorTest {
    
    private GlobalJobMethodProcessor processor;
    
    private TestClass target;
    private Method preMethod;
    private Method postMethod;
    private Method paramMethod;
    
    @Before
    public void setUp() throws NoSuchMethodException {
        
        target = new TestClass();
        
        Method method = TestClass.class.getMethod("testMethod");
        
        paramMethod = TestClass.class.getMethod("testMethod", String.class);
        preMethod = TestClass.class.getMethod("preMethod");
        postMethod = TestClass.class.getMethod("postMethod");
        
        processor = new GlobalJobMethodProcessor(
                target, method, preMethod, postMethod
        );
    }
    
    @Test
    public void testPreProcess() {
        // Setup
        final JobContext context = JobContext.newBuilder()
                .setShardingId(1L).build();
        
        // Run the test
        processor.preProcess(context);
        
        // Verify the results
    }
    
    @Test
    public void testPreProcess_param() {
        // Setup
        final JobContext context = JobContext.newBuilder()
                .setShardingId(1L).build();
        
        processor = new GlobalJobMethodProcessor(
                target, paramMethod, preMethod, postMethod
        );
        
        // Run the test
        processor.preProcess(context);
        
        // Verify the results
    }
    
    @Test
    public void testPostProcess() {
        // Setup
        final JobContext context = JobContext
                .newBuilder()
                .setShardingId(0L)
                .build();
        
        // Run the test
        final ProcessResult result = processor.postProcess(context);
        
        // Verify the results
    }
    
    @Test
    public void testProcess() throws Exception {
        // Setup
        final JobContext context = JobContext.newBuilder()
                .setShardingId(1L).build();
        
        // Run the test
        final ProcessResult result = processor.process(context);
        
        // Verify the results
    }
    
    @Test
    public void testProcess_ThrowsException() {
        // Setup
        final JobContext context = JobContext.newBuilder().build();
        
        // Run the test
        assertThrows(Exception.class, () -> processor.process(context));
    }
    
    @Test
    public void testToString() {
        // Setup
        // Run the test
        final String result = processor.toString();
        
        // Verify the results
        assertTrue(result.contains(TestClass.class.getName()));
        assertTrue(result.contains(processor.getClass().getName()));
    }
    
    public static class TestClass {
        public void testMethod() {
            // Do nothing
        }
        
        public void testMethod(String param) {
            // Do nothing
        }
        
        public void preMethod() {
        }
        
        public void postMethod() {
        }
        
    }
    
}
