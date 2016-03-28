package org.mandfer.tools.system;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.nio.file.StandardWatchEventKinds.*;



/**
 * Created by andreufm on 21/03/2016.
 */
public class Archbot {

    private static final double LIMIT_FACTOR = 0.85;
    private static Logger logger = LoggerFactory.getLogger(Archbot.class);
    private final WatchService watcher;
    private final Path monitoredFolder;
    private final int folderLimit;
    private final Path archiveFolder;
    private final WatchKey registeredKey;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private double currentFolderSize;

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    public static void main(String[] args) throws IOException {
        if(args.length == 0 || args.length > 3){
            usage();
        }
        new Archbot(Paths.get(args[0]), Integer.parseInt(args[1]), Paths.get(args[2])).processEvents();
    }

    private Archbot(Path monitoredFolder, int sizeLimit, Path archiveFolder) throws IOException {
        this.monitoredFolder = monitoredFolder;
        this.archiveFolder = archiveFolder;
        this.folderLimit = sizeLimit;
        this.watcher = FileSystems.getDefault().newWatchService();
        registeredKey = this.monitoredFolder.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
    }

    private void processEvents(){
        for(;;){

            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
                return;
            }

            if(key != this.registeredKey){
                logger.warn("Watchkey not recognized!");
                continue;
            }

            for(WatchEvent<?> event : key.pollEvents()){
                WatchEvent.Kind kind = event.kind();

                if(kind == OVERFLOW){
                    logger.warn("Overflow event");
                    continue;
                }

                WatchEvent<Path> pathEvent = cast(event);
                Path name = pathEvent.context();
                Path child = monitoredFolder.resolve(name);
                logger.debug(event.kind().name() + ", "+child);

                moveToArchivedFolder(child.toFile());

                /*
                if(monitoredFolder != null) {
                    long bytesSize = FileUtils.sizeOfDirectory(monitoredFolder.toFile());
                    double kiloBytes = bytesSize / 1024;
                    currentFolderSize = kiloBytes / 1024;
                    //double gigaBytes = megaBytes / 1024;
                    logger.info("Current size of: " + monitoredFolder + " is:" +
                            String.format("%.2f", currentFolderSize) +" Mb");
                }*/


                //if(currentFolderSize >= (folderLimit * LIMIT_FACTOR)) {
                    try {
                        //List<File> list = new ArrayList<>();

                        //Files.list(monitoredFolder)
                        //        .filter(d -> d.toFile().isFile())
                        //        .forEach(f -> list.add(f.toFile()));
                        //TODO: Change for a creation time comparator reading image metadata.
                        // Get Apache Commons code and extend LastModifiedFileComparator and
                        // Implement a CreationTimeFileComparator reading image metadata.
                        // Setup a list of 5 sample images
                        // Setup test cases
                        //list.sort(LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
                        //list.stream().forEach((file) -> {
                        //    logfileInfo(file);
                        //});


                        //int listSize = list.size();
                        //int endRange = (int) (listSize * LIMIT_FACTOR);
                        //List<File> listToProcess = list.subList(0, endRange);

                        //for(File file : listToProcess){
                            //moveToArchivedFolder(file);
                        //}

                    } catch (IOException e) {
                        logger.debug(e.getMessage(), e);
                    }
                //}else{
                //    double usedPer = (currentFolderSize / folderLimit) * 100;
                //   logger.info("Used quota "+usedPer+" %");
                //}

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

            logger.debug("------------------");
        }
    }

    private void moveToArchivedFolder(File file) {

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class );
            if( directory != null )
            {
                //TODO: if there is not efix metadata, use file metadata.
                Date date = directory.getDate( ExifIFD0Directory.TAG_DATETIME );

                java.time.LocalDate localDate = date.toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
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
            } else {
                logger.error( "There is no EXIF metadata for file " + file.getName() );
            }

        } catch (ImageProcessingException e) {
            logger.debug(e.getMessage(), e);
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        }

    }

    private void logfileInfo(File file){
        String info = "";
        BasicFileAttributes attributes = null;
        try {
            attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        }

        info = info
                .concat(file.getPath())
                .concat("creationTime: "+ (attributes != null ?
                        sdf.format(attributes.creationTime().toMillis()):""));

        logger.debug(info);
    }

    private static void usage(){
        //TODO: max size should be possible to define with a units argument (Mb, Gb)
        logger.info("Usage: java Archbot \"MonitoringPath\" maxSizeMb \"ArchivingPath\" ");
        System.exit(-1);
    }

}
