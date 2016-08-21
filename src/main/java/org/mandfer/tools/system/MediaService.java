package org.mandfer.tools.system;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.google.inject.Inject;
import org.joda.time.DateTime;
import org.mandfer.tools.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

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
        DateTime date;
        try {
            Metadata metadata = os.getImageMetadata(filePath);
            date = exifService.getImageExifCreationTime(metadata);
        } catch (ImageProcessingException e) {
            try {
                logger.debug( "There is no EXIF metadata for " + filePath );
                date = os.readFileCreationDate(filePath);
            } catch (IOException e1) {
                throw new FileNotFoundException(filePath+" does not have creation date.");
            }
        }
        return date;
    }


}
