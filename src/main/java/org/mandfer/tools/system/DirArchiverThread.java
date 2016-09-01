package org.mandfer.tools.system;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

/**
 * Created by marc on 22/08/16.
 */
public class DirArchiverThread implements DirArchiver  {

    private final Logger logger = LoggerFactory.getLogger(DirArchiverThread.class);
    private final Path destPath;
    private final Path failPath;
    private final ArchiverService archiverService;
    private final BlockingQueue<Path> blqQueue;


    @Inject
    public DirArchiverThread(@Assisted("destPath") Path destPath,
                             @Assisted("failPath") Path failPath,
                             ArchiverService archiverService,
                             @Assisted BlockingQueue<Path> blqQueue) {
        this.destPath = destPath;
        this.failPath = failPath;
        this.archiverService = archiverService;
        this.blqQueue = blqQueue;
    }

    @Override
    public void archiveNext() throws InterruptedException, IOException {
        Path next = blqQueue.take();
        archiverService.archivePhoto(next, destPath, failPath);
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                archiveNext();
            }
        } catch (InterruptedException consumed) {
            logger.debug(consumed.getMessage(), consumed);
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        }
    }
}
