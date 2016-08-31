package org.mandfer.tools.guice;

import com.google.inject.assistedinject.Assisted;
import org.mandfer.tools.system.DirWatcherThread;

import java.nio.file.Path;

/**
 * Created by marc on 29/08/16.
 */
public interface DirWatcherFactory {
    DirWatcherThread create(Path originPath);
}
