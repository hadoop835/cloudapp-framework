package io.cloudapp.api.scheduler.worker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GlobalJobHelperTest {
    
    @Mock
    private GlobalJobContext context;
    
    private MockedStatic<GlobalJobContext> mockedStatic;
    
    @Before
    public void setUp() {
        mockedStatic = mockStatic(GlobalJobContext.class);
        mockedStatic.when(GlobalJobContext::getContext).thenReturn(context);
    }
    
    @After
    public void tearDown() {
        mockedStatic.close();
    }
    
    @Test
    public void testGetJobId() {
        assertEquals(0L, GlobalJobHelper.getJobId());
        
        mockedStatic.when(GlobalJobContext::getContext).thenReturn(null);
        assertEquals(-1L, GlobalJobHelper.getJobId());
    }
    
    @Test
    public void testGetJobParam() {
        when(context.getJobParam()).thenReturn("jobParam");
        assertEquals("jobParam", GlobalJobHelper.getJobParam());
        
        mockedStatic.when(GlobalJobContext::getContext).thenReturn(null);
        assert GlobalJobHelper.getJobParam() == null;
    }
    
    @Test
    public void testGetShardIndex() {
        when(context.getShardIndex()).thenReturn(0);
        assertEquals(0, GlobalJobHelper.getShardIndex());
        
        mockedStatic.when(GlobalJobContext::getContext).thenReturn(null);
        assert GlobalJobHelper.getShardIndex() == -1;
    }
    
    @Test
    public void testGetShardTotal() {
        when(context.getShardTotal()).thenReturn(0);
        assertEquals(0, GlobalJobHelper.getShardTotal());
        
        mockedStatic.when(GlobalJobContext::getContext).thenReturn(null);
        assert GlobalJobHelper.getShardTotal() == -1;
    }
    
    @Test
    public void testGetShardParameter() {
        when(context.getShardParameter()).thenReturn("shardParameter");
        assertEquals("shardParameter", GlobalJobHelper.getShardParameter());
        
        mockedStatic.when(GlobalJobContext::getContext).thenReturn(null);
        assert GlobalJobHelper.getShardParameter().isEmpty();
    }
    
}
