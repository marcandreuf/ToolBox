package org.mandfer.tools.system;

import java.io.IOException;

/**
 * Created by marc on 28/08/16.
 */
public interface DirArchiver {

    void archiveNext() throws InterruptedException, IOException;
}
