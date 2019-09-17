package org.mandfer.tools.system;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by marc on 30/08/16.
 */
public interface WatcherPath {
    List<Path> getListOfFiles() throws Exception;
}
