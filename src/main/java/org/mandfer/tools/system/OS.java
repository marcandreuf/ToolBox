package org.mandfer.tools.system;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

/**
 * Created by andreufm on 19/07/2016.
 */
public class OS {

    private static final int MAX_READ_METADATA_ATTEPTS = 3;
    private static Logger logger = LoggerFactory.getLogger(OS.class);

    public Date readFileCreationDate(File file) throws IOException {
        BasicFileAttributes attib = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        return new Date(attib.creationTime().toMillis());
    }

    public Metadata getImageMetadata(File file) throws InterruptedException {
        Metadata metadata = null;
        int attempts = 0;
        do{
            try{
                logger.debug("Try to read file metadata " + file.getAbsolutePath());
                metadata = ImageMetadataReader.readMetadata(file);
                break;
            }catch (Throwable t){
                logger.debug("wait 500", t);
                Thread.sleep(500);
            }
            attempts ++;
        }while (attempts <= MAX_READ_METADATA_ATTEPTS);
        return metadata;
    }

    public Date getImageCreationTime(File file) throws InterruptedException, IOException {
        Date date = null;

        Metadata metadata = getImageMetadata(file);

        if( metadata != null ){
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if(directory != null) {
                date = directory.getDate(ExifIFD0Directory.TAG_DATETIME);
            }
        }

        if(date == null){
            logger.debug("File metadata ExifIFD0Directory not found: "+file
                    +". Reading file creation time instead");
            date = readFileCreationDate(file);
        }

        if(date == null){
            throw new FileNotFoundException(
                    "Metadata and File creation time not found for file "+file.getAbsolutePath());
        }

        return date;
    }


}
