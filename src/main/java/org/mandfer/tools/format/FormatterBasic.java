package org.mandfer.tools.format;

/**
 * Created by marc on 02/04/16.
 */
public class FormatterBasic implements StringFormatter {
    @Override
    public String formatNumber(int number, int digits) {
        return String.format("%0"+digits+"d", number);
    }
}
