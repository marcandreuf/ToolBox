package org.mandfer.tools.system;

import com.google.inject.Inject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    public void archive(Path filePath, Path destPath, Path failPath) throws IOException {
        try {
            logger.debug("Processing file: " + filePath);
            DateTime dateTime = mediaService.findCreationDate(filePath);
            Path relativePath = os.calcDateRelPath(filePath, dateTime);
            Path movingPath = destPath.resolve(relativePath);
            os.moveFileTo(filePath, movingPath);
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            moveFileToBackup(filePath, failPath);
        }
    }


    private void moveFileToBackup(Path path, Path failedPath) throws IOException {
        logger.debug("Moving file " + path + " to backup folder.");
        os.moveFileTo(path, failedPath);
    }

}
