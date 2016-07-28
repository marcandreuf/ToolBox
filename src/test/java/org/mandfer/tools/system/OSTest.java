package org.mandfer.tools.system;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by andreufm on 19/07/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Files.class, OS.class, ImageMetadataReader.class})
public class OSTest {

    private File mocked_file;
    private Path mocked_path;
    private OS os;

    @Before
    public void setUp() {
        mockStatic(Files.class);
        mockStatic(ImageMetadataReader.class);
        mocked_file = mock(File.class);
        mocked_path = mock(Path.class);
        os = new OS();
    }

    @Test
    public void readFileCreationDate() throws IOException {
        long currentTime = System.currentTimeMillis();
        FileTime stubbed_filetime = FileTime.fromMillis(currentTime);
        BasicFileAttributes mocked_attribs = mock(BasicFileAttributes.class);
        when(mocked_file.toPath()).thenReturn(mocked_path);
        when(Files.readAttributes(Mockito.any(Path.class), Mockito.any(Class.class))).thenReturn(mocked_attribs);
        when(mocked_attribs.creationTime()).thenReturn(stubbed_filetime);

        Date date = os.readFileCreationDate(mocked_file);

        PowerMockito.verifyStatic();
        Files.readAttributes(mocked_path, BasicFileAttributes.class);
        assertEquals(date.getTime(), currentTime);
    }


    @Test
    public void getImageMetadataCreationTimeOrImageFileTimeByDefault() throws Exception {
        Metadata stubbed_metadata = new Metadata();
        when(ImageMetadataReader.readMetadata(mocked_file))
                .thenThrow(IOException.class)
                .thenReturn(stubbed_metadata);

        os.getImageMetadata(mocked_file);

        PowerMockito.verifyStatic(times(2));
        ImageMetadataReader.readMetadata(mocked_file);
    }

    //TODO Test getImageCreationTime ..............

}
