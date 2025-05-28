package io.cloudapp.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PairsTest {
    
    private Pairs<String, String> pairsUnderTest;
    
    @Before
    public void setUp() throws Exception {
        pairsUnderTest = new Pairs<>();
    }
    
    @Test
    public void testOf() {
        // Setup
        // Run the test
        final Pairs<String, String> result = pairsUnderTest.of("key", "value");
        
        // Verify the results
    }
    
    @Test
    public void testToList() {
        final List<Pairs.Pair<String, ? extends String>> result = pairsUnderTest.toList();
    }
    
    @Test
    public void testToPairs() {
        // Run the test
        final Pairs<String, String> result = Pairs.toPairs("key", "value");
        assertEquals(Pairs.toPairs("key", "value"), result);
        assertEquals(Collections.singletonList(new Pairs.Pair<>("key", "value")),
                     result.toList()
        );
        
        assertNotEquals(Pairs.toPairs("key", "value1"), result);
        assertNotEquals(new Object(), result);
        
        assertNotEquals(Pairs.toPairs(new Object(), new Object()), result);
        
    }
    
}
