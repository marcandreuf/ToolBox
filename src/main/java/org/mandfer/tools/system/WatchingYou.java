package org.mandfer.tools.system;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;



/**
 * Created by andreufm on 21/03/2016.
 */
public class WatchingYou {

    private static Logger logger = LoggerFactory.getLogger(WatchingYou.class);
    private final WatchService watcher;
    private final Path dir;
    private final WatchKey registeredKey;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    private WatchingYou(Path dir) throws IOException {
        this.dir = dir;
        this.watcher = FileSystems.getDefault().newWatchService();
        registeredKey = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE);

    }

    public static void main(String[] args) throws IOException {
        if(args.length == 0 || args.length > 2){
            usage();
        }
        new WatchingYou(Paths.get(args[0])).processEvents();
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
                Path child = dir.resolve(name);
                logger.debug(event.kind().name() + ", "+child);

                if(dir != null) {
                    long bytesSize = FileUtils.sizeOfDirectory(dir.toFile());
                    double kiloBytes = bytesSize / 1024;
                    //double megaBytes = kiloBytes / 1024;
                    //double gigaBytes = megaBytes / 1024;
                    logger.info("Current size of: " + dir + " is:" + String.format("%.2f", kiloBytes));
                }

                try {
                    List<File> list = new ArrayList<>();

                    Files.list(dir)
                            .filter(d -> d.toFile().isFile())
                            .forEach(f -> list.add(f.toFile()));
                    list.sort(LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
                    list.stream().forEach((file) -> {logfileInfo(file);} );


                    //TODO: Calculate what is the list of 90% size of oldest content.

                } catch (IOException e) {
                    logger.debug(e.getMessage(), e);
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

            logger.debug("------------------");
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
        logger.info("Usage: java WatchingYou \"dirPath\"");
        System.exit(-1);
    }

}
