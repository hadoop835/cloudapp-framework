package com.alibaba.cloudapp.api.common;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PaginationTest {
    
    private Pagination<String> paginationUnderTest;
    
    @Before
    public void setUp() throws Exception {
        paginationUnderTest = new Pagination.Builder<String>()
                .dataList(Collections.emptyList())
                .hasNext(false)
                .maxResults(10)
                .nextToken("nextToken")
                .build();
    }
    
    @Test
    public void testBuilder() throws Exception {
        // Setup
        // Run the test
        final Pagination.Builder<String> result = paginationUnderTest.builder();
        
        // Verify the results
    }
    
    @Test
    public void testDataListGetterAndSetter() {
        final List<String> dataList = Collections.singletonList("value");
        paginationUnderTest.setDataList(dataList);
        assertEquals(dataList, paginationUnderTest.getDataList());
    }
    
    @Test
    public void testMaxResultsGetterAndSetter() {
        final Integer maxResults = 0;
        paginationUnderTest.setMaxResults(maxResults);
        assertEquals(maxResults, paginationUnderTest.getMaxResults());
    }
    
    @Test
    public void testHasNextGetterAndSetter() {
        final Boolean hasNext = false;
        paginationUnderTest.setHasNext(hasNext);
        assertFalse(paginationUnderTest.getHasNext());
    }
    
    @Test
    public void testNextTokenGetterAndSetter() {
        final String nextToken = "nextToken";
        paginationUnderTest.setNextToken(nextToken);
        assertEquals(nextToken, paginationUnderTest.getNextToken());
    }
    
}
