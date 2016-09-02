package org.mandfer.tools.system;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.google.inject.Inject;
import org.joda.time.DateTime;
import org.mandfer.tools.utils.DateUtils;
import org.mandfer.tools.validation.FileTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Locale;

/**
 * Created by marcandreuf on 19/07/2016.
 */
public class OS {
    private static final int MAX_READ_METADATA_ATTEPTS = 3;
    private static Logger logger = LoggerFactory.getLogger(OS.class);

    private final DateUtils dateUtils;
    private final FileTypeValidator fileTypeValidator;

    @Inject
    public OS(DateUtils dateUtils, FileTypeValidator fileTypeValidator) {
        this.dateUtils = dateUtils;
        this.fileTypeValidator = fileTypeValidator;
    }

    public DateTime readFileCreationDate(Path path) throws IOException {
        BasicFileAttributes attrib = Files.readAttributes(path, BasicFileAttributes.class);
        return new DateTime(attrib.creationTime().toMillis());
    }

    public Metadata getImageMetadata(Path path) throws ImageProcessingException {
        Metadata metadata = null;
        int attempts = 0;
        do{
            try{
                logger.debug("Try to read file metadata " + path);
                metadata = ImageMetadataReader.readMetadata(path.toFile());
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
            throw new ImageProcessingException("No metadata found for file: "+path);
        }

    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.debug(e.getMessage(), e);
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


    /**
     * Calculate relative destination path based on Month and Year of the Date.
     *
     * @param filePath
     * @param dateTime
     * @return  yyyy/MMM/fileName.ext
     */
    public Path calcDateRelPath(Path filePath, DateTime dateTime) {
        int year = dateTime.getYear();
        String month = dateUtils.getShortMonth(dateTime, Locale.getDefault()).toUpperCase();
        return Paths.get(year +
                File.separator + month +
                File.separator + filePath.toFile().getName());
    }

    public void moveFileTo(Path origPath, Path destPath) throws IOException {
        if(destPath.toFile().mkdirs()){
            logger.debug("New path created "+destPath.getParent());
        }
        Files.move(origPath, destPath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File moved from " + origPath + " to " + destPath);
    }

    public boolean isExifCompatibleImageFile(Path path) {
        return fileTypeValidator.isExifCompatibleType(path.toFile().getName());
    }
}
