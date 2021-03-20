package org.mandfer.tools.utils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Locale;


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

    public DateTime getDateTimeFromString(String strDate, String formatPattern, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(formatPattern).withLocale(locale);
        return formatter.parseDateTime(strDate);
    }

    public String printFormatted(DateTime datetime, String pattern, Locale locale) {
        return datetime.toString(pattern, locale);
    }

    public String getShortMonth(DateTime sampleDate, Locale locale) {
        return sampleDate.monthOfYear().getAsShortText(locale);
    }

    public DateTime createJodaDateTime(Date date) {
        return new DateTime(date);
    }

}
