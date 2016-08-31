package org.mandfer.tools.system;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

/**
 * Created by marc on 29/08/16.
 */
public class DirWatcherThread implements DirWatcher, Runnable {

    private final Logger logger = LoggerFactory.getLogger(DirWatcherThread.class);
    private final WatcherPathService watcherPathService;
    private final OS os;
    private final BlockingQueue<Path> blockingQueue;


    @Inject
    public DirWatcherThread(@Assisted WatcherPathService watcherPathService,
                            OS os,
                            BlockingQueue<Path> blockingQueue) {
        this.watcherPathService = watcherPathService;
        this.os = os;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void watch() throws Exception {
        List<Path> newFiles = watcherPathService.getListOfFiles();
        List<Path> newImageFiles = newFiles.stream().filter(path -> os.isImageFile(path)).collect(Collectors.toList());
        if(!newImageFiles.isEmpty()) {
            blockingQueue.addAll(newImageFiles);
        }
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                watch();
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
    }
}
