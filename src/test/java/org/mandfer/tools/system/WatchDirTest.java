package org.mandfer.tools.system;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by andreufm on 17/03/2016.
 */
public class WatchDirTest {

    @Test
    public void testWatchDirCreateFiles(){

        // TODO: Follow tutorial
        // https://docs.oracle.com/javase/tutorial/essential/io/notification.html

    }


    @Test
    public void testDirSize(){

        long bytesSize = FileUtils.sizeOfDirectory(new File("D:\\VMS"));
        System.out.println("Folder Size: " + bytesSize + " bytes");
        double kiloBytes = bytesSize / 1024;
        System.out.println("Folder Size: " + String.format( "%.3f", kiloBytes ) + " Kb");
        double megaBytes = kiloBytes / 1024;
        System.out.println("Folder Size: " + String.format( "%.3f", megaBytes ) + " Mb");
        double gigaBytes = megaBytes / 1024;
        System.out.println("Folder Size: " + String.format( "%.2f", gigaBytes ) + " Gb");

        double d = 35.7087689769876;

        System.out.println( String.format( "%.2f", d ) );

    }
}
