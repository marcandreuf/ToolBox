package org.mandfer.tools.system;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.mandfer.tools.format.FormatterBasic;
import org.mandfer.tools.format.StringFormatter;
import org.mandfer.tools.validation.FileTypeValidator;
import org.mandfer.tools.validation.FileTypeValidatorRegExp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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
    private final FileTypeValidator fileTypeValidator;
    private final StringFormatter stringFormatter;

    private WatchService watcher;
    private WatchKey registeredKey;
    private WatchKey key;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }


    public Archbot(String originPath, String destinationPath,
                   FileTypeValidator fileTypeValidator, StringFormatter stringFormatter)
            throws FileNotFoundException {
        this.originPath = Paths.get(originPath);
        this.destinationPath = Paths.get(destinationPath);
        validatePaths();
        this.fileTypeValidator = fileTypeValidator;
        this.stringFormatter = stringFormatter;
    }

    private void validatePaths() throws FileNotFoundException {
        if(!Files.exists(originPath)){
            throw new FileNotFoundException(
                    "Origin directory "+originPath+" is not a valid path.");
        }
        if(!Files.exists(destinationPath)){
            throw new FileNotFoundException(
                    "Destination directory "+destinationPath+" is not a valid path.");
        }
    }

    public static void main(String[] args) throws Exception {
        if(args.length == 2){
            new Archbot(args[0],
                        args[1],
                        new FileTypeValidatorRegExp(),
                        new FormatterBasic()).processEvents();
        }else{
            howToUseInfo();
        }
    }

    private static void howToUseInfo(){
        logger.info("Usage: java -jar ToolBox \"MonitoringPath\" \"ArchivingPath\" ");
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

                        archivePhoto(path);
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
            throw new Exception("Whatcher key not found.");
        }
    }

    public void archivePhoto(Path pathFile) throws ImageProcessingException, IOException, InterruptedException {

        File currentFile = pathFile.toFile();
        if(fileTypeValidator.isMediaType(currentFile.getName())) {
            Metadata metadata = tryToReadMetadata(currentFile);
            Date date = getDateFromMetadataOrFile(currentFile, metadata);
            String destinationPath = calcDestinationDatePath(currentFile, date);
            move(currentFile, destinationPath);
        }else{
            logger.warn("Media type file not supported "+pathFile.getFileName().toString());
        }
    }

    public Metadata tryToReadMetadata(File file) throws InterruptedException {
        Metadata metadata = null;
        int attempts = 0;
        boolean isRead = false;
        do{
            try{
                logger.debug("Try to read file " + file.getAbsolutePath());
                metadata = ImageMetadataReader.readMetadata(file);
                isRead = true;
            }catch (Throwable t){
                logger.debug("wait 500", t);
                Thread.sleep(500);
            }
            attempts ++;
        }while (isRead == false && attempts <= MAX_READ_METADATA_ATTEPTS);
        return metadata;
    }

    public Date getDateFromMetadataOrFile(File file, Metadata metadata) throws IOException {
        Date date = null;
        if( metadata != null ){
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if(directory != null) {
                date = directory.getDate(ExifIFD0Directory.TAG_DATETIME);
            }else{
                logger.debug("File metadata does not have date: "+file);
            }
        }
        if(date == null){
            logger.debug( "There is no EXIF metadata for file " + file.getName() );
            date = readFileCreationDate(file);
        }
        if(date == null){
            throw new FileNotFoundException(
                    "Metadata and File creation time not found for file "+file.getAbsolutePath());
        }
        return date;
    }

    public Date readFileCreationDate(File file) throws IOException {
        return OS.readFileCreationDate(file);
    }

    public void move(File file, String destinationPath) throws IOException {
        Path origPath = file.toPath();
        Path destPath = FileSystems.getDefault().getPath(destinationPath);
        if(destPath.toFile().mkdirs()){
            logger.debug("New path created "+destPath.getParent());
        }
        Files.move(origPath, destPath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File moved from " + origPath + " to " + destPath);
    }

    public String calcDestinationDatePath(File file, Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        String formattedMonth = stringFormatter.formatNumber(month, 2);
        String formattedDay = stringFormatter.formatNumber(day, 2);

        return destinationPath + File.separator +
                year + file.separator +
                formattedMonth + file.separator +
                formattedDay + file.separator +
                file.getName();
    }


}
