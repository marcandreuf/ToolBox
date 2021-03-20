package org.mandfer.tools.utils;


import org.joda.time.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mandfer.tools.utils.DateUtils.DEFAULT_LONG_DATE_FORMAT;
import static org.mandfer.tools.utils.DateUtils.DEFAULT_SHORT_DATE_FORMAT;


/**
 * @author marcandreuf
 */
public class DateUtilsTest {

    private Logger logger = LoggerFactory.getLogger(DateUtilsTest.class);
    private DateUtils dateUtils;
    private DateTime endDate;

    @Before
    public void setUp() {
        dateUtils = new DateUtils();
        endDate = new DateTime(2013, 11, 25, 12, 0, 0, 0);
    }

    @Test
    public void testDateIntoLastWeek() {
        DateTime checkDate = new DateTime(2013, 11, 20, 12, 0, 0, 0);
        Period oneWeek = new Period(Weeks.ONE);

        boolean isIn = dateUtils.isDateInPeriodFromDate(checkDate, oneWeek, endDate);
        assertTrue(isIn);
    }

    @Test
    public void testDateIntoLastTwoWeek() {
        DateTime checkDate = new DateTime(2013, 11, 14, 12, 0, 0, 0);
        Period oneWeek = new Period(Weeks.TWO);

        boolean isIn = dateUtils.isDateInPeriodFromDate(checkDate, oneWeek, endDate);
        assertTrue(isIn);
    }


    @Test
    public void testDateIntoLastMonth() {
        DateTime checkDate = new DateTime(2013, 10, 26, 12, 0, 0, 0);
        Period oneWeek = new Period(Months.ONE);

        boolean isIn = dateUtils.isDateInPeriodFromDate(checkDate, oneWeek, endDate);
        assertTrue(isIn);
    }

    @Test
    public void testDateIntoLastTwoMonths() {
        DateTime checkDate = new DateTime(2013, 9, 26, 12, 0, 0, 0);
        Period oneWeek = new Period(Months.TWO);

        boolean isIn = dateUtils.isDateInPeriodFromDate(checkDate, oneWeek, endDate);
        assertTrue(isIn);
    }

    @Test
    public void testDateIntoLastThreeMonths() {
        DateTime checkDate = new DateTime(2013, 8, 26, 12, 0, 0, 0);
        Period oneWeek = new Period(Months.THREE);

        boolean isIn = dateUtils.isDateInPeriodFromDate(checkDate, oneWeek, endDate);
        assertTrue(isIn);
    }

    @Test
    public void testDateIntoLastFourMonths() {
        DateTime checkDate = new DateTime(2013, 7, 26, 12, 0, 0, 0);
        Period oneWeek = new Period(Months.FOUR);

        boolean isIn = dateUtils.isDateInPeriodFromDate(checkDate, oneWeek, endDate);
        assertTrue(isIn);
    }


    @Test
    public void testDateIntoSelectedDates() {
        DateTime startDate = new DateTime(2013, 7, 26, 12, 0, 0, 0);
        DateTime checkDate = new DateTime(2013, 7, 26, 12, 0, 0, 0);

        boolean isIn = dateUtils.isDateInGivenPeriod(startDate, endDate, checkDate);
        assertTrue(isIn);
    }

    @Test
    public void testGetDateTimeFromStringShortDate() {
        String year = "2013";
        String dayMonth = "17 Sep";
        String testDate = dayMonth + " " + year;

        DateTime parsedDate = dateUtils.getDateTimeFromString(
                testDate,
                DEFAULT_SHORT_DATE_FORMAT,
                Locale.ENGLISH );

        assertTrue(parsedDate.dayOfMonth().get() == 17);
        assertTrue(parsedDate.monthOfYear().get() == 9);
        assertTrue(parsedDate.year().get() == 2013);
    }

    @Test
    public void testGetDateTimeFromStringLongDate() {
        DateTime parsedDate = dateUtils.getDateTimeFromString(
                "September 17 2013",
                DEFAULT_LONG_DATE_FORMAT,
                Locale.ENGLISH );

        assertTrue(parsedDate.dayOfMonth().get() == 17);
        assertTrue(parsedDate.monthOfYear().get() == 9);
        assertTrue(parsedDate.year().get() == 2013);
    }

    @Test
    public void testPrintFormattedDateTime() {
        DateTime sampleDate = new DateTime(2013, 8, 26, 0, 0, 0);

        String printedDate = dateUtils.printFormatted(sampleDate, DEFAULT_SHORT_DATE_FORMAT, Locale.ENGLISH);

        assertTrue("Expected: 26 Aug 2013 of actual value " + printedDate, printedDate.equalsIgnoreCase("26 Aug 2013"));
    }


    @Test
    public void testGetMonthFromDateTime() {
        DateTime sampleDate = new DateTime(2013, 8, 26, 0, 0, 0);
        String month = dateUtils.getShortMonth(sampleDate, Locale.ENGLISH);
        assertEquals("Aug", month);
        month = dateUtils.getShortMonth(sampleDate, new Locale("es", "ES"));
        assertEquals("ago", month);
    }

    @Test
    public void testTransformJavaDateToJodaTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.FEBRUARY, 20);
        Date date = calendar.getTime();

        DateTime dateTime = dateUtils.createJodaDateTime(date);

        assertEquals(2, dateTime.getMonthOfYear());
    }


    @Test
    @Ignore
    public void testJodaTimeLearningTest() {
        LocalDate start = new LocalDate(2010, 5, 20);
        LocalDate end = new LocalDate(2010, 5, 25);
        Interval interval = new Interval(start.toDateTimeAtCurrentTime(),
                end.toDateTimeAtCurrentTime());
        LocalDate test = new LocalDate(2010, 5, 19);
        logger.debug("Is 19: " + interval.contains(test.toDateTimeAtCurrentTime()));
        test = new LocalDate(2010, 5, 20);
        logger.debug("Is 20: " + interval.contains(test.toDateTimeAtCurrentTime()));
        test = new LocalDate(2010, 5, 21);
        logger.debug("Is 21: " + interval.contains(test.toDateTimeAtCurrentTime()));
        test = new LocalDate(2010, 5, 24);
        logger.debug("Is 24: " + interval.contains(test.toDateTimeAtCurrentTime()));
        test = new LocalDate(2010, 5, 25);
        logger.debug("Is 25: " + interval.contains(test.toDateTimeAtCurrentTime()));
        test = new LocalDate(2010, 5, 26);
        logger.debug("Is 26: " + interval.contains(test.toDateTimeAtCurrentTime()));
    }

}
