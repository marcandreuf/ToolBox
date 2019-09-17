package org.mandfer.tools.guice;

import org.junit.Assert;
import org.junit.Test;
import org.mandfer.tools.system.DirArchiver;
import org.mandfer.tools.system.DirWatcher;
import org.mandfer.tools.system.WatcherPathService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

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


    @Test
    public void testCreateWatcherPathService(){
        String sampleOrigin = System.getProperty("user.dir");
        Path originPath = Paths.get(sampleOrigin);

        WatcherPathService watcherPathService =
                ToolsBoxFactory.getWatcherPathServiceInstance(originPath);

        Assert.assertNotNull(watcherPathService);
    }

    @Test
    public void testCreateDirWatcher() {
        String sampleOrigin = System.getProperty("user.dir");
        Path originPath = Paths.get(sampleOrigin);

        DirWatcher dirWatcher = ToolsBoxFactory.getDirWatcherInstance(originPath);

        Assert.assertNotNull(dirWatcher);

    }

    @Test
    public void testGetSingletonBlockingQueue(){
        BlockingQueue<Path> queueRef1 = ToolsBoxFactory.getBlockingQueue();
        BlockingQueue<Path> queueRef2 = ToolsBoxFactory.getBlockingQueue();
        Assert.assertTrue( queueRef1 == queueRef2);
    }


}
