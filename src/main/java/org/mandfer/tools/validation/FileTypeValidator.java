package org.mandfer.tools.validation;

/**
 * Created by marc on 02/04/16.
 */
public interface FileTypeValidator {

    /**
     * Validate that the extension of the file name is a media type file.
     *
     * @param imageFileName fileName for validation
     * @return true valid fileName, false invalid fileName
     */
    boolean isMediaType(String imageFileName);
}
