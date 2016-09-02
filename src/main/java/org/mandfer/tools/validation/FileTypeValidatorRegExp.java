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

    private Pattern exifCompatiblePattern, videoPattern;
    private Matcher matcher;

    private static final String EXIF_COMPATIBLE_PATTERN =
            "([^\\s]+(\\.(?i)(jpg|png|gif|bmp|mp4|mov|jpeg|webp|psd|ico|pcx|nef|cr2|orf|arw|rw2|rwl|srw))$)";

    private static final String VIDEO_PATTERN =
            "([^\\s]+(\\.(?i)(avi|mpg|wav|bup|ifo|vob|mp3|3gp))$)";

    public FileTypeValidatorRegExp(){
        exifCompatiblePattern = Pattern.compile(EXIF_COMPATIBLE_PATTERN);
        videoPattern = Pattern.compile(VIDEO_PATTERN);
    }

    public boolean isExifCompatibleType(final String fileName){
        matcher = exifCompatiblePattern.matcher(fileName);
        return matcher.matches();

    }

    @Override
    public boolean isVideoType(String fileName) {
        matcher = videoPattern.matcher(fileName);
        return matcher.matches();
    }
}
