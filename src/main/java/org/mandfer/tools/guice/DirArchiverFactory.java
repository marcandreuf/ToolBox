package org.mandfer.tools.guice;

import com.google.inject.assistedinject.Assisted;
import org.mandfer.tools.system.DirArchiverThread;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

/**
 * Created by marc on 28/08/16.
 */
public interface DirArchiverFactory {
    DirArchiverThread create(
            @Assisted("destPath")Path dest,
            @Assisted("failPath")Path failed,
            @Assisted BlockingQueue<Path> blqQueue);
}
