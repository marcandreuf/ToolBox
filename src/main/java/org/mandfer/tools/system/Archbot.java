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


public class Archbot {

    private static Logger logger = LoggerFactory.getLogger(Archbot.class);

    private final FileTypeValidator fileTypeValidator = ToolsBoxFactory.getInstance(FileTypeValidator.class);
    private final OS os = ToolsBoxFactory.getInstance(OS.class);

    private final Path originPath;
    private final Path destinationPath;
    private final Path failedPath;

    private WatchService watcher;
    private WatchKey registeredKey;
    private WatchKey key;


    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }


    public Archbot(String originPath, String destinationPath, String failedPath) throws FileNotFoundException {
        this.originPath = os.getPath(originPath);
        this.destinationPath = os.getPath(destinationPath);
        this.failedPath = os.getPath(failedPath);
        validatePaths();
    }

    private void validatePaths() throws FileNotFoundException {
            os.checkIsDirectory(originPath);
            os.checkIsDirectory(destinationPath);
            os.checkIsDirectory(failedPath);
            os.checkIsReadable(originPath);
            os.checkIsReadable(destinationPath);
            os.checkIsReadable(failedPath);
            os.checkIsWritable(originPath);
            os.checkIsWritable(destinationPath);
            os.checkIsWritable(failedPath);
    }

    public static void main(String[] args) throws Exception {
        if(args.length == 3){
            new Archbot(args[0], args[1], args[2]).start();
        }else{
            howToUseInfo();
        }
    }

    private static void howToUseInfo(){
        logger.info("Usage: java -jar ToolBox \"MonitoringPath\" \"ArchivingPath\" \"FailedPath\" ");
        System.exit(-1);
    }


    private void start() throws IOException {
        setUpFileCreationWatcherForOriginPath();
        for(;;){
            logger.info("---- Start processing events ----");
            try {
                extractKey();
                if (isRegisteredKey()) {
                    processEvents();
                }
            } catch (Exception e) {
                analiseFailure(e);
            } finally {
                boolean valid = key.reset();
                if (!valid) {
                    logger.error("Key is not valid, stop watcher.");
                    break;
                }
            }
            logger.info("------------------");
        }
    }

    private void analiseFailure(Exception e) {
        if(e != null && e.getMessage() != null){
            if(e.getMessage().contains("Stream ended before file")){
                logger.debug("File metadata not readable.", e);
            }else {
                logger.error(e.getMessage(), e);
            }
        }else{
            if(e != null) {
                logger.error("No exception error ", e);
            }else{
                logger.error("No exception available. ");
            }
        }
    }

    private void processEvents() {
        for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind kind = event.kind();
            if (isOverflowEvent(kind)) continue;
            Path path = getCreatedFilePath(event);

            //TODO: Achiever(path).start() thread to send valid files to a queue
            try {
                archivePhoto(path);
            }catch (Throwable t){
                logger.debug(t.getMessage(), t);
            }
        }
    }

    private Path getCreatedFilePath(WatchEvent<?> event) {
        WatchEvent<Path> pathEvent = cast(event);
        Path name = pathEvent.context();
        Path path = originPath.resolve(name);
        logger.debug(event.kind().name() + ", " + path);
        return path;
    }

    private boolean isOverflowEvent(WatchEvent.Kind kind) {
        if (kind == OVERFLOW) {
            logger.debug(" !!! OVERFLOW event !!!!");
            return true;
        }
        return false;
    }

    private void setUpFileCreationWatcherForOriginPath() throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
        registeredKey = this.originPath.register(watcher, ENTRY_CREATE);
        logger.info("Listening create events at "+ originPath);
    }

    private boolean isRegisteredKey() {
        if(key == this.registeredKey){
            return true;
        }
        logger.debug("Watch key not recognized!");
        return false;
    }

    private void extractKey() throws Exception {
        try {
            key = watcher.take();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new Exception("Watcher key not found.");
        }
    }

    //TODO: new thread to archive a file. Consumer of the queue
    public void archivePhoto(Path pathFile) throws IOException {
        if(fileTypeValidator.isMediaType(pathFile.toFile().getName())) {
            try {
                DateTime dateTime = findCreationDate(pathFile);
                Path relativePath = os.calcDateRelPath(pathFile, dateTime);
                os.moveFileTo(pathFile, destinationPath.resolve(relativePath));
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
                moveFileToBackup(pathFile);
            }
        }else{
            logger.warn("Media type file not supported "+pathFile.toFile().getName());
            moveFileToBackup(pathFile);
        }
    }

    private void moveFileToBackup(Path path) throws IOException {
        logger.debug("Moving file "+path+" to backup folder.");
        os.moveFileTo(path, failedPath);
    }

    public DateTime findCreationDate(Path filePath) throws FileNotFoundException {
        DateTime date;
        try {
            Metadata metadata = os.getImageMetadata(filePath);
            date = os.getImageExifCreationTime(metadata);
        } catch (ImageProcessingException e) {
            try {
                logger.debug( "There is no EXIF metadata for " + filePath );
                date = os.readFileCreationDate(filePath);
            } catch (IOException e1) {
                throw new FileNotFoundException(filePath+" does not have creation date.");
            }
        }
        return date;
    }

}
