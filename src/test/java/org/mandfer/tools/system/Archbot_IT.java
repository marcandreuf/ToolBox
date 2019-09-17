package org.mandfer.tools.system;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mandfer.tools.guice.ToolsBoxFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Archbot_IT {

    private static Logger logger = LoggerFactory.getLogger(Archbot.class);
    private static OS os = ToolsBoxFactory.getInstance(OS.class);
    private static Path originPath;
    private static Path destinationPath;
    private static Path failedPath;


    @Test
    public void testProcessNewFile() throws Exception {
        originPath = os.getPath("/home/marc/archbot/origin");
        destinationPath = os.getPath("/home/marc/archbot/dest");
        failedPath = os.getPath("/home/marc/archbot/failed");

        DirWatcher watcher = ToolsBoxFactory.getDirWatcherInstance(originPath);
        DirArchiver archiver = ToolsBoxFactory.getDirArchiverInstance(destinationPath, failedPath);

        File sampleImage = new File("src/test/resources/adobeJpeg1.jpg");
        Path samplePath = sampleImage.toPath();

        Files.copy(samplePath, originPath.resolve(sampleImage.getName()));

        watcher.watch();

        archiver.archiveNext();

        Assert.assertTrue(!originPath.resolve(sampleImage.getName()).toFile().exists());
        File destImage = destinationPath.resolve("2003/NOV/"+sampleImage.getName()).toFile();
        Assert.assertTrue(destImage.exists());

        FileUtils.deleteQuietly(destinationPath.resolve("2003").toFile());
    }

}