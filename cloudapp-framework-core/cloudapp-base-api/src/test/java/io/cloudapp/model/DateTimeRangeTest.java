package io.cloudapp.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class DateTimeRangeTest {
    
    private DateTimeRange timeRange;
    
    @Before
    public void setUp() throws Exception {
        timeRange = new DateTimeRange();
    }
    
    @Test
    public void testStartTimeGetterAndSetter() {
        final Date startTime = new GregorianCalendar(2020, Calendar.JANUARY,
                                                     1
        ).getTime();
        timeRange.setStartTime(startTime);
        assertEquals(startTime, timeRange.getStartTime());
    }
    
    @Test
    public void testEndTimeGetterAndSetter() {
        final Date endTime = new GregorianCalendar(2020, Calendar.JANUARY,
                                                   1
        ).getTime();
        timeRange.setEndTime(endTime);
        assertEquals(endTime, timeRange.getEndTime());
    }
    
    @Test
    public void testBuilder() throws Exception {
        // Setup
        // Run the test
        final DateTimeRange.Builder result = DateTimeRange.builder();
        
        // Verify the results
    }
    
    @Test
    public void testFromString1() {
        // Run the test
        String startTime = "2020-01-01 00:00:00";
        String endTime = "2022-01-02 00:00:00";
        final DateTimeRange result = DateTimeRange.fromString(startTime, endTime);
        
        assertEquals(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                     result.getStartTime()
        );
        assertEquals(new GregorianCalendar(2022, Calendar.JANUARY, 2).getTime(),
                     result.getEndTime()
        );
        assertNotEquals("o", result);
        assertNotEquals(0, result.hashCode());
    }
    
    @Test
    public void testFromString2() {
        // Run the test
        final DateTimeRange result = DateTimeRange.fromString(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        assertEquals(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                     result.getStartTime()
        );
        assertEquals(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                     result.getEndTime()
        );
        assertFalse(result.equals("o"));
        assert result.hashCode() != 0;
    }
    
}
