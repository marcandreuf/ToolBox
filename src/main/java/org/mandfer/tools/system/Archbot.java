package org.mandfer.tools.system;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created by andreufm on 21/03/2016.
 */
public class Archbot {

    private static Logger logger = LoggerFactory.getLogger(Archbot.class);
    private final WatchService watcher;
    private final Path monitoredFolder;
    private final Path archiveFolder;
    private final WatchKey registeredKey;
    private WatchKey key;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    public static void main(String[] args) throws Exception {
        if(args.length == 0 || args.length > 3){
            howToUseInfo();
        }
        new Archbot(Paths.get(args[0]), Paths.get(args[1])).processEvents();
    }

    private static void howToUseInfo(){
        logger.info("Usage: java -jar ToolBox \"MonitoringPath\" \"ArchivingPath\" ");
        System.exit(-1);
    }

    private Archbot(Path monitoredFolder, Path archiveFolder) throws IOException {
        this.monitoredFolder = monitoredFolder;
        this.archiveFolder = archiveFolder;
        this.watcher = FileSystems.getDefault().newWatchService();
        registeredKey = this.monitoredFolder.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
    }

    private void processEvents() throws Exception {

        for(;;){
            extractKey();
            if (isRegisteredKey()) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind kind = event.kind();

                    if (kind == OVERFLOW) {
                        logger.warn("Overflow event");
                        continue;
                    }

                    WatchEvent<Path> pathEvent = cast(event);
                    Path name = pathEvent.context();
                    Path child = monitoredFolder.resolve(name);
                    logger.debug(event.kind().name() + ", " + child);

                    archivePhoto(child.toFile());

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
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

    private void archivePhoto(File file) {
        Date date;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class );
            date = getDate(file, directory);

            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int day = localDate.getDayOfMonth();


            String destinationPath = archiveFolder + File.separator +
                        year + file.separator +
                        month + file.separator +
                        day + file.separator +
                        file.getName();

            File checkFile = new File(destinationPath);
            Random rand = new Random();
            if(checkFile.exists()){
                destinationPath = archiveFolder + File.separator +
                           year + file.separator +
                           month + file.separator +
                           day + file.separator + "_" + (rand.nextInt(8)+1) +
                           file.getName();
            }
            logger.debug("Move file "+ file.getAbsolutePath() +" to: " + destinationPath);

            Path origPath = file.toPath();
            Path destPath = FileSystems.getDefault().getPath(destinationPath);
            if(destPath.toFile().mkdirs()){
                Files.move(origPath, destPath, StandardCopyOption.REPLACE_EXISTING );
                logger.info("File moved from " + origPath + " to " + destPath );
            }
        } catch (ImageProcessingException e) {
            logger.debug(e.getMessage(), e);
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        }
    }

    private Date getDate(File file, Directory directory) throws IOException {
        Date date;
        if( directory != null ){
            date = directory.getDate( ExifIFD0Directory.TAG_DATETIME );
        } else {
            logger.error( "There is no EXIF metadata for file " + file.getName() );
            BasicFileAttributes bfattrib = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            date = new Date(bfattrib.creationTime().toMillis());
        }
        return date;
    }

}