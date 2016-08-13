package org.mandfer.tools.system;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by andreufm on 19/07/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Files.class, OS.class, ImageMetadataReader.class, Metadata.class})
public class OSTest {

    private Metadata mocked_metadata;
    private ExifIFD0Directory mocked_exifIFD0Directory;
    private ExifSubIFDDirectory mocked_exifSubIFDDirectory;
    private Date currentTime;
    private File mocked_file;
    private Path mocked_path;
    private OS os;


    @Rule
    ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        mockStatic(Files.class);
        mockStatic(ImageMetadataReader.class);
        mocked_file = mock(File.class);
        mocked_path = mock(Path.class);
        mocked_metadata = mock(Metadata.class);
        mocked_exifIFD0Directory = mock(ExifIFD0Directory.class);
        mocked_exifSubIFDDirectory = mock(ExifSubIFDDirectory.class);
        currentTime = Calendar.getInstance().getTime();
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
    public void getImageMetadataAfterSecondAttempt() throws Exception {
        when(ImageMetadataReader.readMetadata(mocked_file))
                .thenThrow(IOException.class)
                .thenReturn(mocked_metadata);

        os.getImageMetadata(mocked_file);

        PowerMockito.verifyStatic(times(2));
        ImageMetadataReader.readMetadata(mocked_file);
    }

    @Test
    public void failGetImageMetadata() throws ImageProcessingException, IOException {
        String samplePath = "abc";
        when(ImageMetadataReader.readMetadata(mocked_file)).thenThrow(IOException.class);
        when(mocked_file.getAbsolutePath()).thenReturn(samplePath);
        expectedException.expect(ImageProcessingException.class);
        expectedException.expectMessage(containsString(samplePath));

        os.getImageMetadata(mocked_file);
    }




    @Test
    public void getMetadataCreationTimeFromExifIFD0Directory() throws Exception {
        when(mocked_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(mocked_exifIFD0Directory);
        when(mocked_exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME)).thenReturn(currentTime);

        Date date = os.getImageExifCreationTime(mocked_metadata);

        assertTrue(date != null);
        assertTrue(currentTime == date);
        verify(mocked_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mocked_exifIFD0Directory).getDate(ExifIFD0Directory.TAG_DATETIME);
    }

    @Test
    public void getMetadataCreationTimeFromExifSubIFDDirectoryORIGINAL_withoutDateInExifIFD0Directory()  throws Exception {
        when(mocked_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(mocked_exifIFD0Directory);
        when(mocked_exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME)).thenReturn(null);
        when(mocked_metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)).thenReturn(mocked_exifSubIFDDirectory);
        when(mocked_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)).thenReturn(currentTime);

        Date date = os.getImageExifCreationTime(mocked_metadata);

        assertTrue(date != null);
        assertTrue(currentTime == date);
        verify(mocked_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mocked_exifIFD0Directory).getDate(ExifIFD0Directory.TAG_DATETIME);
        verify(mocked_metadata).getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        verify(mocked_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
    }


    @Test
    public void getMetadataCreationTimeFromExifSubIFDDirectoryORIGINAL_withoutExifIFD0Directory() throws Exception {
        when(mocked_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(null);
        when(mocked_metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)).thenReturn(mocked_exifSubIFDDirectory);
        when(mocked_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)).thenReturn(currentTime);

        Date date = os.getImageExifCreationTime(mocked_metadata);

        assertTrue(date != null);
        assertTrue(currentTime == date);
        verify(mocked_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mocked_metadata).getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        verify(mocked_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        Mockito.verifyNoMoreInteractions(mocked_exifSubIFDDirectory);
    }


    @Test
    public void getMetadataCreationTimeFromExifSubIFDDirectoryDIGITALIZED() throws Exception {
        when(mocked_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(null);
        when(mocked_metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)).thenReturn(mocked_exifSubIFDDirectory);
        when(mocked_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)).thenReturn(null);
        when(mocked_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED)).thenReturn(currentTime);

        Date date = os.getImageExifCreationTime(mocked_metadata);

        assertTrue(date != null);
        assertTrue(currentTime == date);
        verify(mocked_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mocked_metadata).getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        verify(mocked_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        verify(mocked_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
        Mockito.verifyNoMoreInteractions(mocked_exifSubIFDDirectory);

    }



    @Test
    public void failToGetMetadataCreationTimeFromAnyExifDirectory() throws ImageProcessingException {
        when(mocked_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(null);
        when(mocked_metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)).thenReturn(null);
        expectedException.expect(ImageProcessingException.class);
        expectedException.expectMessage(containsString("Exif creation date not found."));

        os.getImageExifCreationTime(mocked_metadata);

        verify(mocked_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mocked_metadata).getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Mockito.verifyNoMoreInteractions(mocked_metadata);
    }

    @Test
    public void failToGetMetadataCreationTimeFromAnyDateOfExifDirectory() throws Exception {
        when(mocked_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(mocked_exifIFD0Directory);
        when(mocked_exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME)).thenReturn(null);
        when(mocked_metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)).thenReturn(mocked_exifSubIFDDirectory);
        when(mocked_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)).thenReturn(null);
        when(mocked_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED)).thenReturn(null);
        expectedException.expect(ImageProcessingException.class);
        expectedException.expectMessage(containsString("Exif creation date not found."));

        Date date = os.getImageExifCreationTime(mocked_metadata);

        verify(mocked_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mocked_exifIFD0Directory).getDate(ExifIFD0Directory.TAG_DATETIME);
        verify(mocked_metadata).getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        verify(mocked_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        verify(mocked_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
    }



}
