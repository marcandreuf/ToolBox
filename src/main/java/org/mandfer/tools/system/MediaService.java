package org.mandfer.tools.system;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.google.inject.Inject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by marcandreuf on 21/08/16.
 */
public class MediaService {

    private final Logger logger = LoggerFactory.getLogger(MediaService.class);
    private final ExifService exifService;
    private final OS os;

    @Inject
    public MediaService(OS os, ExifService exifService) {
        this.os = os;
        this.exifService = exifService;
    }


    public DateTime findCreationDate(Path filePath) throws FileNotFoundException {
        DateTime date = DateTime.now();
        try {
            if(os.isExifImageFile(filePath)) {
                Metadata metadata = os.getImageMetadata(filePath);
                date = exifService.getImageExifCreationTime(metadata);
            }else if(os.isVideoFile(filePath)){
                date = getFileCreationDate(filePath);
            }
        } catch (ImageProcessingException e) {
            logger.debug(e.getMessage());
            date = getFileCreationDate(filePath);
        }
        return date;
    }

    private DateTime getFileCreationDate(Path filePath) throws FileNotFoundException {
        DateTime date;
        try {
            date = os.readFileCreationDate(filePath);
            logger.debug("File creation date: "+date);
        } catch (IOException e1) {
            throw new FileNotFoundException(e1.getMessage());
        }
        return date;
    }


}
