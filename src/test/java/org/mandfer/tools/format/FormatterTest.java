package org.mandfer.tools.format;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by marc on 02/04/16.
 */
public class FormatterTest {


    private final StringFormatter stringFormatter;

    public FormatterTest(){
        stringFormatter = new FormatterBasic();
    }

    @Test
    public void testFormatIntegerTo2Digits(){
        int sample = 2;
        int digits = 2;

        String formatted = stringFormatter.formatNumber(sample, digits);
        System.out.println("num: "+formatted);
        Assert.assertEquals("Formatted string "+formatted+" is not matching ", "02", formatted);
    }

    @Test
    public void testFormatIntegerTo3Digits(){
        int sample = 2;
        int digits = 3;

        String formatted = stringFormatter.formatNumber(sample, digits);
        System.out.println("num: "+formatted);
        Assert.assertEquals("Formatted string "+formatted+" is not matching ", "002", formatted);
    }

    @Test
    public void testFormatIntegerLongDigits(){
        int sample = 234;
        int digits = 5;

        String formatted = stringFormatter.formatNumber(sample, digits);
        System.out.println("num: "+formatted);
        Assert.assertEquals("Formatted string "+formatted+" is not matching ", "00234", formatted);
    }
}
