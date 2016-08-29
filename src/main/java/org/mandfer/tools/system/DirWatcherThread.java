package org.mandfer.tools.system;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

/**
 * Created by marc on 29/08/16.
 */
public class DirWatcherThread implements DirWatcher {

    private final WatcherPathService watcherPathService;
    private final OS os;
    private final BlockingQueue<Path> blockingQueue;

    public DirWatcherThread(WatcherPathService watcherPathService, OS os, BlockingQueue<Path> blockingQueue) {
        this.watcherPathService = watcherPathService;
        this.os = os;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void watch() throws Exception {
        List<Path> newFiles = watcherPathService.getListOfFilesByEvent();
        List<Path> newImageFiles = newFiles.stream().filter(path -> os.isImageFile(path)).collect(Collectors.toList());
        if(!newImageFiles.isEmpty()) {
            blockingQueue.addAll(newImageFiles);
        }
    }
}
