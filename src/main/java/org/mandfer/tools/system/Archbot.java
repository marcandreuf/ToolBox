package org.mandfer.tools.system;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import org.mandfer.tools.format.StringFormatter;
import org.mandfer.tools.guice.ToolsBoxFactory;
import org.mandfer.tools.validation.FileTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * Created by andreufm on 21/03/2016.
 *
 *
 * TODOS:
 *
 * 1. Add OS and MetaDataExtractor all as constructor depenencies.
 *
 * 2. Create a ServiceFactory and encapsulate all system related dependencies.
 *    OS, FileTypeValidator, StringFormatter, ....
 *
 * 3. Implement all java.io and java.nio static usages into OS dependency.
 *
 * //Note: remove all static dependencies.
 *
 */
public class Archbot {

    private static final int MAX_READ_METADATA_ATTEPTS = 3;
    private static Logger logger = LoggerFactory.getLogger(Archbot.class);

    private final Path originPath;
    private final Path destinationPath;
    private final Path failedPath;
    private final FileTypeValidator fileTypeValidator;
    private final StringFormatter stringFormatter;
    private final OS os;

    private WatchService watcher;
    private WatchKey registeredKey;
    private WatchKey key;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }


    public Archbot(String originPath, String destinationPath, String failedPath) throws FileNotFoundException {
        this.os = ToolsBoxFactory.getInstance(OS.class);
        this.fileTypeValidator = ToolsBoxFactory.getInstance(FileTypeValidator.class);
        this.stringFormatter = ToolsBoxFactory.getInstance(StringFormatter.class);

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
            new Archbot(args[0], args[1], args[2]).processEvents();
        }else{
            howToUseInfo();
        }
    }

    private static void howToUseInfo(){
        logger.info("Usage: java -jar ToolBox \"MonitoringPath\" \"ArchivingPath\" \"FailedPath\" ");
        System.exit(-1);
    }


    private void processEvents() throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
        registeredKey = this.originPath.register(watcher, ENTRY_CREATE);
        logger.info("Listening create events at "+ originPath);

        for(;;){
            logger.debug("---- Start processing events ----");
            try {
                extractKey();
                if (isRegisteredKey()) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind kind = event.kind();

                        if (kind == OVERFLOW) {
                            logger.warn(" !!! OVERFLOW event !!!!");
                            continue;
                        }

                        WatchEvent<Path> pathEvent = cast(event);
                        Path name = pathEvent.context();
                        Path path = originPath.resolve(name);
                        logger.debug(event.kind().name() + ", " + path);


                        //TODO: SentFileToProcess(path).start() thread to send valid files to a queue
                        // if is media type file then add Path to the queue
                        // else moveFileTo file to a backup location
                        try {
                            archivePhoto(path);
                        }catch (Throwable t){
                            logger.debug(t.getMessage(), t);
                        }
                    }
                }
            } catch (Exception e) {
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
            } finally {
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
            logger.debug("------------------");
        }
    }

    private boolean isRegisteredKey() {
        if(key == this.registeredKey){
            return true;
        }
        logger.warn("Watch key not recognized!");
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

    //TODO implement UTs when all method dependencies has been extracted.
    //TODO: new thread to archive a file. Consumer of the queue
    public void archivePhoto(Path pathFile) throws IOException {
        if(fileTypeValidator.isMediaType(pathFile.toFile().getName())) {
            try {
                Date date = findCreationDate(pathFile);
                String fileDestinationPath = calcDestinationDatePath(pathFile, date);
                moveFileTo(pathFile, fileDestinationPath);
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
                moveFileToBackup(pathFile);
            }
        }else{
            logger.warn("Media type file not supported "+pathFile.getFileName().toString());
            moveFileToBackup(pathFile);
        }
    }

    private void moveFileToBackup(Path filePath) throws IOException {
        logger.debug("Moving file "+filePath+" to backup folder.");
        moveFileTo(filePath, failedPath.toString());
    }

    //TODO move to OS and implement UTs
    public Date findCreationDate(Path filePath) throws Exception {
        File currentFile = filePath.toFile();
        Date date;
        try {
            Metadata metadata = os.getImageMetadata(filePath.toFile());
            date = os.getImageExifCreationTime(metadata);
        } catch (ImageProcessingException e) {
            try {
                logger.debug( "There is no EXIF metadata for file " + currentFile.getName() );
                date = os.readFileCreationDate(currentFile);
            } catch (IOException e1) {
                throw new Exception("File "+currentFile+" does not have creation date.");
            }
        }
        return date;
    }


    //TODO: moveFileTo function to OS and implement UTs
    public void moveFileTo(Path origPath, String destinationPath) throws IOException {
        Path destPath = FileSystems.getDefault().getPath(destinationPath);
        if(destPath.toFile().mkdirs()){
            logger.debug("New path created "+destPath.getParent());
        }
        Files.move(origPath, destPath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File moved from " + origPath + " to " + destPath);
    }

    //TODO: moveFileTo function to OS and implement UTs
    //TODO: Destination PathBuilder based on given pattern. MMM-yyyy, dd-MMM-yyyy, yyyy.
    public String calcDestinationDatePath(Path filePath, Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        String formattedMonth = stringFormatter.formatNumber(month, 2);
        String formattedDay = stringFormatter.formatNumber(day, 2);

        return destinationPath + File.separator +
                year + File.separator +
                formattedMonth + File.separator +
                formattedDay + File.separator +
                filePath.toFile().getName();
    }


}
