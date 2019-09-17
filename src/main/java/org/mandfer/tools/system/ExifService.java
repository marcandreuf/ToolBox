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

import java.util.Date;

/**
 * Created by marc on 21/08/16.
 */
public class ExifService {

    private final Logger logger = LoggerFactory.getLogger(ExifService.class);
    private final DateUtils dateUtils;

    @Inject
    public ExifService(DateUtils dateUtils) {
        this.dateUtils = dateUtils;
    }


    public DateTime getImageExifCreationTime(Metadata metadata) throws ImageProcessingException {
        Date date = null;

        Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if(exifIFD0Directory != null) {
            logger.debug("Read TAG_DATETIME.");
            date = exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME);
        }

        if(date == null){
            ExifSubIFDDirectory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if(exifSubIFDDirectory != null) {
                logger.debug("Read TAG_DATETIME_ORIGINAL.");
                date = exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if(date == null){
                    logger.debug("Read TAG_DATETIME_DIGITIZED.");
                    date = exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
                }
            }
        }

        if(date != null){
            logger.debug("Found Exif date "+date);
            return dateUtils.createJodaDateTime(date);
        }else{
            throw new ImageProcessingException("Exif creation date not found.");
        }
    }
}
