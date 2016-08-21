package org.mandfer.tools.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

/**
 * Created by marc on 22/08/16.
 */
public class DirArchiver implements Runnable  {

    private final Logger logger = LoggerFactory.getLogger(DirArchiver.class);
    private final Path destPath;
    private final Path failPath;
    private final Archiver archiver;
    private final BlockingQueue<Path> blqQueue;


    public DirArchiver(Path destPath, Path failPath, Archiver archiver, BlockingQueue<Path> blqQueue) {
        this.destPath = destPath;
        this.failPath = failPath;
        this.archiver = archiver;
        this.blqQueue = blqQueue;
    }

    public void archiveNext() throws InterruptedException, IOException {
        Path next = blqQueue.take();
        archiver.archivePhoto(next, destPath, failPath);
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
