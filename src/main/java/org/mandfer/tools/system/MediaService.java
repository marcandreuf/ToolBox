package org.mandfer.tools.system;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
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
    private final OS os;


    public MediaService(OS os) {
        this.os = os;
    }


    public DateTime findCreationDate(Path filePath) throws FileNotFoundException {
        DateTime date;
        try {
            Metadata metadata = os.getImageMetadata(filePath);
            date = os.getImageExifCreationTime(metadata);
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
