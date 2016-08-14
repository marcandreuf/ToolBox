package org.mandfer.tools.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Based on post:
 * http://www.mkyong.com/regular-expressions/how-to-validate-image-file-extension-with-regular-expression/
 *
 *
 * Created by marc on 02/04/16.
 */
public class FileTypeValidatorRegExp implements FileTypeValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String IMAGE_PATTERN =
            "([^\\s]+(\\.(?i)(jpg|png|gif|bmp|mp4|mov|jpeg|webp|psd|ico|pcx|nef|cr2|orf|arw|rw2|rwl|srw))$)";

    public FileTypeValidatorRegExp(){
        pattern = Pattern.compile(IMAGE_PATTERN);
    }

    public boolean isMediaType(final String fileName){
        matcher = pattern.matcher(fileName);
        return matcher.matches();

    }
}
