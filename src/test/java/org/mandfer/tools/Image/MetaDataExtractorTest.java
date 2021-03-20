package org.mandfer.tools.Image;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by andreufm on 22/03/2016.
 */
public class MetaDataExtractorTest {

    private static Logger logger = LoggerFactory.getLogger(MetaDataExtractorTest.class);

    @Test
    @Ignore //Learning test
    public void testReadImageDate() throws ImageProcessingException, IOException {

        File sampleImage = new File("src/test/resources/adobeJpeg1.jpg");
        Metadata metadata = ImageMetadataReader.readMetadata(sampleImage);



        //print(metadata);
        Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class );
        if( directory != null ) {
            Date date = directory.getDate(ExifIFD0Directory.TAG_DATETIME);

            java.time.LocalDate localDate = date.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int day = localDate.getDayOfMonth();

            System.out.println("y: "+year+", m: "+month+", d: "+day);
        }

    }

    private static void print(Metadata metadata)
    {
        System.out.println("-------------------------------------");

        // Iterate over the data and print to System.out

        //
        // A Metadata object contains multiple Directory objects
        //
        for (Directory directory : metadata.getDirectories()) {

            //
            // Each Directory stores values in Tag objects
            //
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }

            //
            // Each Directory may also contain error messages
            //
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.println("ERROR: " + error);
                }
            }
        }
    }
}
