package org.mandfer.tools.system;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

/**
 * Created by andreufm on 19/07/2016.
 */
public class OS {

    public static Date readFileCreationDate(File file) throws IOException {
        BasicFileAttributes attib = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        return new Date(attib.creationTime().toMillis());
    }

}
