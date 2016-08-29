package org.mandfer.tools.guice;

import org.junit.Assert;
import org.junit.Test;
import org.mandfer.tools.system.DirArchiver;
import org.mandfer.tools.system.DirWatcher;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by marc on 23/08/16.
 */
public class GuiceBindingTest {

    @Test
    public void testCreateDirArchiver() {
        String sampleDest = System.getProperty("user.dir")+"/archbot/dest";
        String sampleFailed = System.getProperty("user.dir")+"/archbot/failed";
        Path destPath = Paths.get(sampleDest);
        Path failedPath = Paths.get(sampleFailed);

        DirArchiver dirArchiver = ToolsBoxFactory.getDirArchiverInstance(destPath, failedPath);

        Assert.assertNotNull(dirArchiver);

    }

//    @Test
//    public void testCreateDirWatcher() {
//        String sampleOrigin = System.getProperty("user.dir")+"/archbot/org";
//        Path originPath = Paths.get(sampleOrigin);
//
//        DirWatcher dirWatcher = ToolsBoxFactory.getDirWatcherInstance(originPath);
//
//        Assert.assertNotNull(dirWatcher);
//
//    }

    //TODO Get BlockingQueue singleton instance.


}
