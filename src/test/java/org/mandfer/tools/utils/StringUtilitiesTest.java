package org.mandfer.tools.utils;


import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author marcandreuf
 */
public class StringUtilitiesTest {

    public static final String resources_path = 
      "src"+File.separator+
      "test"+File.separator+
      "resources"+File.separator;
      
    public static final String SAMPLE_ORIGINAL_HTML = 
      resources_path+"sampleOriginal.html";
    public static final String SAMPLE_MODIFIED_HTML = 
      resources_path+"sampleModified.html";
    
    private StringUtilities stringUtilities;
    private String content = "sample text more lines\t";
    private String expected = "sample text more lines";

    private Logger logger = 
      LoggerFactory.getLogger(StringUtilitiesTest.class);

    @Before
    public void setUp() {
        stringUtilities = new StringUtilities();
    }


    @Test
    public void testIsValidUTF8String() throws UnsupportedEncodingException {
        String nonUtf8Content = new String("A" + "\u00ea" + "\u00f1" + "\u00fc" + "Ç");
        String utfContent = "hello";

        assertTrue(!stringUtilities.isValidUTF8(nonUtf8Content.getBytes("ISO-8859-1")));
        assertTrue(stringUtilities.isValidUTF8(utfContent.getBytes("UTF-8")));
    }

    @Test
    public void testCompareEqualsContent() throws IOException {
        String content = "hello world";
        String matching = "hello world";

        assertTrue(stringUtilities.isContentEquals(content, matching));
    }

    @Test
    public void testCompareNonEqualsContent() throws IOException {
        String content = "hello world";
        String matching = "world";

        assertTrue(!stringUtilities.isContentEquals(content, matching));
    }

    @Test
    public void testRemoveNewLinesChars() {
        String rawText = stringUtilities.trimEndLineChars(content);

        assertTrue(rawText.equals(expected));
    }

    @Test
    public void testRemoveAllSpaces() {
        String noSpacesText = stringUtilities.removeSpaces(content);

        assertTrue(!noSpacesText.contains(" "));
        assertTrue(!noSpacesText.contains("\\t"));
    }

    @Test
    public void testGetCoordinates() {
        Point p = stringUtilities.parseCoordinates("10, 20");

        assertTrue("Point values are properly created.", (p.x == 10) && (p.y == 20));
    }

    @Test
    public void testReadAmountText() {
        String positiveAmountText = "£86.67 available";
        float positiveAmount = stringUtilities.getAmountValue(positiveAmountText);
        assertTrue(positiveAmount == 86.67f);


        String negativeAmountText = "-£86.67";
        float negativeAmount = stringUtilities.getAmountValue(negativeAmountText);
        assertTrue(negativeAmount == -86.67f);
    }

    @Test
    public void testBigNumbersText() {
        String bigNumber = "£36,506.45";
        float positiveAmount = stringUtilities.getAmountValue(bigNumber);
        System.out.println(positiveAmount);
        assertTrue(positiveAmount == 36506.45f);

        bigNumber = "£1,256,436,506.45";
        positiveAmount = stringUtilities.getAmountValue(bigNumber);
        System.out.println(positiveAmount);
        assertTrue(positiveAmount == 1256436506.45f);
    }

    @Test(expected = NumberFormatException.class)
    public void testFailGetAmountValue() throws NumberFormatException {
        stringUtilities.getAmountValue("nonumber");
    }


    @Test
    public void testExtractOnlyCharactersFromAGivenCharToTheEnd() {
        String sample = "Business nickname::TESTSAMPLE003\n\r";
        String result = stringUtilities.subStringTrimedFromChar(':', sample);
        assertTrue(result.equals("TESTSAMPLE003"));
    }


    @Test
    public void testGetStringDiffs() throws IOException {
        String originContent = stringUtilities.loadSample(SAMPLE_ORIGINAL_HTML);
        String matchingContent = stringUtilities.loadSample(SAMPLE_MODIFIED_HTML);

        String differences = stringUtilities.getDiffs(originContent, matchingContent);

        assertTrue(differences.contains("Diff(DELETE"));
        assertTrue(differences.contains("Diff(INSERT"));
    }

    @Test
    public void testGetStringNoDiffs() throws IOException {
        String originContent = stringUtilities.loadSample(SAMPLE_ORIGINAL_HTML);
        String matchingContent = stringUtilities.loadSample(SAMPLE_ORIGINAL_HTML);

        String differences = stringUtilities.getDiffs(originContent, matchingContent);

        assertTrue(differences.contains("Diff(EQUAL"));
        assertTrue(!differences.contains("Diff(DELETE"));
        assertTrue(!differences.contains("Diff(INSERT"));
    }

    @Test
    public void getInputStreamFromString() throws IOException {
        InputStream sampleIS = stringUtilities.getInputStream(content);
        String myString = IOUtils.toString(sampleIS, "UTF-8");
        assertTrue(stringUtilities.isContentEquals(content, myString));
    }


    @Test
    public void testGetJsonObjectFromStringInlineDefinitionFormat() throws Exception {

        String strParam1 = "{'sampleKey':'sampleValue'}";
        String strParam2 = "{'sampleKey2':2}";

        JsonObject jsonParam1 = stringUtilities.getJsonObject(strParam1);
        JsonObject jsonParam2 = stringUtilities.getJsonObject(strParam2);

        String value = jsonParam1.get("sampleKey").getAsString();
        assertTrue(value != null && value.equals("sampleValue"));

        int value2 = jsonParam2.get("sampleKey2").getAsInt();
        assertTrue(value2 == 2);
    }

    @Test
    public void testShuffleString() {
        String result = stringUtilities.shuffle(content);

        assertFalse(result.equals(content));
    }

}
