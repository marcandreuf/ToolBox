package org.mandfer.tools.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author marcandreuf
 */
public class StringUtilities {


    /**
     * Checks if given byte array is valid UTF-8 encoded.
     *
     * @param bytes
     * @return true when valid UTF8 encoded
     */
    public boolean isValidUTF8(final byte[] bytes) {
        try {
            Charset.availableCharsets().get("UTF-8").newDecoder().decode(ByteBuffer.wrap(bytes));
        } catch (CharacterCodingException e) {
            return false;
        }

        return true;
    }

    public boolean isContentEquals(String content, String matching) {
        String trimmedContent = trimEndLineChars(content);
        String timmedMatching = trimEndLineChars(matching);

        trimmedContent = removeSpaces(trimmedContent);
        timmedMatching = removeSpaces(timmedMatching);

        return trimmedContent.equals(timmedMatching);
    }

    public String trimEndLineChars(String text) {
        return text.replaceAll(System.getProperty("line.separator") + "|\\r|\\n|\\t", "");
    }

    public Point parseCoordinates(String strCoordinates) {
        String[] arCoordinates = strCoordinates.split(",");
        int x = Integer.valueOf(arCoordinates[0].trim());
        int y = Integer.valueOf(arCoordinates[1].trim());
        return new Point(x, y);
    }

    public String removeSpaces(String content) {
        return content.replaceAll(" ", "").replaceAll("\\t", "");
    }


    public float getAmountValue(String text) {
        boolean isNegative = checkIfNegative(text);
        Matcher m = extractNumber(text);
        if (m.find()) {
            return convertToFloat(isNegative, m);
        } else {
            throw new NumberFormatException(
                    "Not found any number into the string " + text);
        }
    }

    private boolean checkIfNegative(String text) {
        Pattern pisNeg = Pattern.compile("^-{1}");
        Matcher misNeg = pisNeg.matcher(text);
        if (misNeg.find()) {
            return true;
        }
        return false;
    }

    private Matcher extractNumber(String text) {
        Pattern p = Pattern.compile("\\d+\\.?\\d+");
        Matcher m = p.matcher(text.replaceAll(",", ""));
        return m;
    }

    private Float convertToFloat(boolean isNegative, Matcher m) {
        return Float.valueOf((isNegative ? "-" : "") + m.group());
    }

    public String subStringTrimedFromChar(char c, String text) {
        int posLastChar = text.lastIndexOf(c);
        return trimEndLineChars(text.substring(posLastChar + 1));
    }

    public String getDiffs(String origin, String matching) throws IOException {
        DiffTool diffTool = new DiffTool();
        LinkedList<DiffTool.Diff> Difference =
                diffTool.diff_main(origin, matching, false);
        return Difference.toString();
    }

    public String loadSample(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
    }

    public InputStream getInputStream(String content) {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }


    public JsonObject getJsonObject(String jsonString) {
        return new JsonParser().parse(jsonString).getAsJsonObject();
    }

    public String shuffle(String string) {
        if (string == null || string.isEmpty()) return string;
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        return StringUtils.join(letters, "");
    }
}
