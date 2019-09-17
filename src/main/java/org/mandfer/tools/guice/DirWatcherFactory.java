package org.mandfer.tools.guice;

import com.google.inject.assistedinject.Assisted;
import org.mandfer.tools.system.DirWatcherThread;
import org.mandfer.tools.system.WatcherPathService;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

/**
 * Created by marc on 29/08/16.
 */
public interface DirWatcherFactory {
    DirWatcherThread create(
            @Assisted WatcherPathService watcherPathService,
            @Assisted BlockingQueue<Path> blqQueue);
}
