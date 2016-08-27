package org.mandfer.tools.system;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * Created by marc on 23/08/16.
 */
public class WatcherService {

    private static Logger logger = LoggerFactory.getLogger(WatcherService.class);
    private WatchService watcher;
    private WatchKey registeredKey;
    private WatchKey key;
    private final Map<String, WatchKey> mapRegistry;


    @Inject
    public WatcherService(Map<String, WatchKey> mapRegistry) {
        this.mapRegistry = mapRegistry;
    }


    public void registerDirEventsListener(Path path, WatchEvent.Kind<Path> eventType) throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
        registeredKey = path.register(watcher, eventType);
        String cachedKey = createCacheKey(path, eventType);
        mapRegistry.put(cachedKey, registeredKey);
        logger.info("Listening create events at: "+ path);
    }

    private String createCacheKey(Path path, WatchEvent.Kind<Path> eventType){
        return path.toString() + eventType;
    }

    public boolean isRegistered(Path path, WatchEvent.Kind<Path> eventType) {
        String cachedKey = createCacheKey(path, eventType);
        return mapRegistry.containsKey(cachedKey);
    }

    public List<Path> getListOfFilesByEvent(Path path, WatchEvent.Kind<Path> eventType) throws Exception {
        try {
            key = watcher.take();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new Exception("Watcher key not found.");
        }
        return null;
    }
}
