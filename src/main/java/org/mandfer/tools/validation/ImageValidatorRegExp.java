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
public class ImageValidatorRegExp implements ImageValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String IMAGE_PATTERN =
            "([^\\s]+(\\.(?i)(jpg|png|gif|bmp|mp4|mov|jpeg|webp|psd|ico|pcx|nef|cr2|orf|arw|rw2|rwl|srw))$)";

    public ImageValidatorRegExp(){
        pattern = Pattern.compile(IMAGE_PATTERN);
    }

    /**
     * Validate image with regular expression
     * @param image image for validation
     * @return true valid image, false invalid image
     */
    public boolean validate(final String image){

        matcher = pattern.matcher(image);
        return matcher.matches();

    }
}
