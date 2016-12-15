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
    private final BlockingQueue<Path> bloqingQueueFilesToProcess;


    @Inject
    public DirArchiverThread(@Assisted("destPath") Path destPath,
                             @Assisted("failPath") Path failPath,
                             ArchiverService archiverService,
                             @Assisted BlockingQueue<Path> bloqingQueueFilesToProcess) {
        this.destPath = destPath;
        this.failPath = failPath;
        this.archiverService = archiverService;
        this.bloqingQueueFilesToProcess = bloqingQueueFilesToProcess;
    }

    @Override
    public void archiveNext() throws InterruptedException, IOException {
        Path filePath = bloqingQueueFilesToProcess.take();
        archiverService.archive(filePath, destPath, failPath);
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
