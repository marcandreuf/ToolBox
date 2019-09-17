package org.mandfer.tools.system;

/**
 * Created by marc on 28/08/16.
 */
public interface DirWatcher extends Runnable {

    void watch() throws Exception;
}
