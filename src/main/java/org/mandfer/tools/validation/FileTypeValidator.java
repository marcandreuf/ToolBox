package org.mandfer.tools.validation;

/**
 * Created by marc on 02/04/16.
 */
public interface FileTypeValidator {

    /**
     * Validate that it is a file compatible with Exif metadata.
     *
     * @param imageFileName fileName for validation
     * @return true valid fileName, false non valid fileName
     */
    boolean isExifCompatibleType(String imageFileName);

    /**
     * Validate that the extension of the file name is a video type file.
     *
     * @param fileName
     * @return true valid fileName, false non valid fileName
     */
    boolean isVideoType(String fileName);
}
