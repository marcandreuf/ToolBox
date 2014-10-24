package org.mandfer.tools.utils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * @author marcandreuf
 */
public class DateUtils {

    public static String DEFAULT_SHORT_DATE_FORMAT = "dd MMM yyyy";
    public static String DEFAULT_LONG_DATE_FORMAT = "MMMMM dd yyyy";

    public boolean isDateInPeriodFromDate(DateTime checkDate, Period period, DateTime fromDate) {
        DateTime startDate = fromDate.minus(period);
        return isDateInGivenPeriod(startDate, fromDate, checkDate);
    }

    public boolean isDateInGivenPeriod(DateTime startDate, DateTime endDate, DateTime checkDate) {
        Interval interval = new Interval(startDate, endDate);
        return interval.contains(checkDate);
    }

    public DateTime getDateTimeFromString(String testDate, String formatPattern) {
        DateTimeFormatter monthAndYear =
                new DateTimeFormatterBuilder().appendPattern(formatPattern).toFormatter();
        return DateTime.parse(testDate, monthAndYear);
    }

    public String printFormatted(DateTime datetime, String pattern) {
        return datetime.toString(pattern);
    }

}
