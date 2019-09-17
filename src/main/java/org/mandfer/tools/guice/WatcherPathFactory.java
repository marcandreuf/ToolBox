package org.mandfer.tools.guice;

import com.google.inject.assistedinject.Assisted;
import org.mandfer.tools.system.DirArchiverThread;
import org.mandfer.tools.system.WatcherPathService;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.BlockingQueue;

/**
 * Created by marc on 28/08/16.
 */
public interface WatcherPathFactory {
    WatcherPathService create(
            @Assisted Path originPath,
            @Assisted WatchEvent.Kind<Path> eventType);
}
