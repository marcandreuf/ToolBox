package org.mandfer.tools.system;

import com.google.inject.Inject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by marc on 21/08/16.
 */
public class ArchiverService {

    private final Logger logger = LoggerFactory.getLogger(ArchiverService.class);
    private final OS os;
    private final MediaService mediaService;

    @Inject
    public ArchiverService(OS os, MediaService mediaService) {
        this.os = os;
        this.mediaService = mediaService;
    }


    public void archivePhoto(Path imagePath, Path destinationPath, Path failedPath) throws IOException {
//        if(os.isImageFile(imagePath)) {
            try {
                DateTime dateTime = mediaService.findCreationDate(imagePath);
                Path relativePath = os.calcDateRelPath(imagePath, dateTime);
                os.moveFileTo(imagePath, destinationPath.resolve(relativePath));
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
                moveFileToBackup(imagePath, failedPath);
            }
//        }else{
//            logger.warn("Media type file not supported "+imagePath.toFile().getName());
//            moveFileToBackup(imagePath, failedPath);
//        }
    }


    private void moveFileToBackup(Path path, Path failedPath) throws IOException {
        logger.debug("Moving file "+path+" to backup folder.");
        os.moveFileTo(path, failedPath);
    }

}
