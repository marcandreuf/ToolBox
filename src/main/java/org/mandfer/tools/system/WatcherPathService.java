package org.mandfer.tools.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static com.sun.jmx.mbeanserver.Util.cast;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * Created by marc on 23/08/16.
 */
public class WatcherPathService {

    private static Logger logger = LoggerFactory.getLogger(WatcherPathService.class);
    private final WatchService watcher;
    private final Path path;
    private final WatchKey registeredKey;



    public WatcherPathService(Path path, WatchEvent.Kind<Path> eventType)
            throws IOException {
        this.path = path;
        watcher = FileSystems.getDefault().newWatchService();
        registeredKey = path.register(watcher, eventType);
    }


    public List<Path> getListOfFilesByEvent() throws Exception {
        List<Path> newFiles = new ArrayList<>();
        WatchKey key = getKeyWithLastEvents();
        if(isRegisteredKey(key)){
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
                if (isOverflowEvent(kind)) continue;
                Path resolvedPath = getCreatedFilePath(event);
                newFiles.add(resolvedPath);
            }
        }
        return newFiles;
    }

    private WatchKey getKeyWithLastEvents() throws Exception {
        try {
            return watcher.take();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new Exception("Watcher key not found.");
        }
    }

    private boolean isRegisteredKey(WatchKey key) {
        if(key == this.registeredKey){
            return true;
        }
        logger.debug("Watch key not recognized!");
        return false;
    }

    private boolean isOverflowEvent(WatchEvent.Kind kind) {
        if (kind == OVERFLOW) {
            logger.debug(" !!! OVERFLOW event !!!!");
            return true;
        }
        return false;
    }

    private Path getCreatedFilePath(WatchEvent<?> event) {
        WatchEvent<Path> pathEvent = cast(event);
        Path name = pathEvent.context();
        Path pathResolved = path.resolve(name);
        logger.debug(event.kind().name() + ", " + pathResolved);
        return pathResolved;
    }
}
