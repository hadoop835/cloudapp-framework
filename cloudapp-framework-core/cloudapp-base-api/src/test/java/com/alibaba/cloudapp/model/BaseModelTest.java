package com.alibaba.cloudapp.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseModelTest {
    
    private BaseModel baseModel;
    
    @Before
    public void setUp() throws Exception {
        baseModel = new BaseModel() {};
    }
    
    @Test
    public void testEquals() {
        assertNotEquals("o", baseModel);
    }
    
    @Test
    public void testHashCode() {
        assert baseModel.hashCode() != 0;
    }
    
}
