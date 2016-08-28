package org.mandfer.tools.guice;

import org.junit.Assert;
import org.junit.Test;
import org.mandfer.tools.system.DirArchiver;

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
        DirArchiverFactory dirArchiverFactory = ToolsBoxFactory.getInstance(DirArchiverFactory.class);

        DirArchiver dirArchiver = dirArchiverFactory.create(destPath, failedPath);

        Assert.assertNotNull(dirArchiver);

    }

}
