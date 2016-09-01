package org.mandfer.tools.system;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import org.joda.time.DateTime;
import org.mandfer.tools.guice.ToolsBoxFactory;
import org.mandfer.tools.validation.FileTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * Created by marcandreuf on 21/08/16.
 */
public class Archbot {

    private static Logger logger = LoggerFactory.getLogger(Archbot.class);
    private static OS os = ToolsBoxFactory.getInstance(OS.class);
    private static Path originPath;
    private static Path destinationPath;
    private static Path failedPath;


    private static Path validatePath(String strPath) throws FileNotFoundException {
        Path path = os.getPath(strPath);
        os.checkIsDirectory(path);
        os.checkIsReadable(path);
        os.checkIsWritable(path);
        return path;
    }

    public static void main(String[] args) throws Exception {
        if(args.length == 3){
            originPath = validatePath(args[0]);
            destinationPath = validatePath(args[1]);
            failedPath = validatePath(args[2]);

            DirWatcher watcher = ToolsBoxFactory.getDirWatcherInstance(originPath);
            DirArchiver archiver = ToolsBoxFactory.getDirArchiverInstance(destinationPath, failedPath);

            (new Thread(watcher)).start();
            (new Thread(archiver)).start();

        }else{
            howToUseInfo();
        }
    }

    private static void howToUseInfo(){
        logger.info("Usage: java -jar ToolBox \"MonitoringPath\" \"ArchivingPath\" \"FailedPath\" ." +
                "\n The directories needs to be valid, readable and writable for the user running the process.");
        System.exit(-1);
    }

}
