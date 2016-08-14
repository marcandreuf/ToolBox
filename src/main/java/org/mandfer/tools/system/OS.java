package org.mandfer.tools.system;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public Metadata getImageMetadata(File file) throws ImageProcessingException {
        Metadata metadata = null;
        int attempts = 0;
        do{
            try{
                logger.debug("Try to read file metadata " + file.getAbsolutePath());
                metadata = ImageMetadataReader.readMetadata(file);
                break;
            }catch (Throwable t){
                logger.debug("wait 500", t);
                sleep(500);
            }
            attempts ++;
        }while (attempts <= MAX_READ_METADATA_ATTEPTS);

        if(metadata != null){
            return metadata;
        }else{
            throw new ImageProcessingException("No metadata found for file: "+file.getAbsolutePath());
        }

    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.debug(e.getMessage(), e);
        }
    }


    public Date getImageExifCreationTime(Metadata metadata) throws ImageProcessingException {
        Date date = null;

        Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if(exifIFD0Directory != null) {
            date = exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME);
        }

        if(date == null){
            ExifSubIFDDirectory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if(exifSubIFDDirectory != null) {
                date = exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if(date == null){
                    date = exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
                }
            }
        }

        if(date != null){
            return date;
        }else{
            throw new ImageProcessingException("Exif creation date not found.");
        }

    }

    public Path getPath(String strPath){
        return Paths.get(strPath);
    }

    public void checkIsDirectory(Path path) throws FileNotFoundException {
        if(!Files.isDirectory(path)){
            throw new FileNotFoundException("Path "+path.toFile().getName()+" is not a directory.");
        }
    }

    public void checkIsReadable(Path path) throws FileNotFoundException {
        if(!Files.isReadable(path)){
            throw new FileNotFoundException("Path "+path.toFile().getName()+" is not a readable directory.");
        }
    }

    public void checkIsWritable(Path path) throws FileNotFoundException {
        if(!Files.isWritable(path)){
            throw new FileNotFoundException("Path "+path.toFile().getName()+" is not a writable directory.");
        }
    }

}
