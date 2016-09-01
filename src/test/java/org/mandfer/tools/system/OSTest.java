package org.mandfer.tools.system;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mandfer.tools.utils.DateUtils;
import org.mandfer.tools.validation.FileTypeValidator;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by andreufm on 19/07/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Files.class, OS.class, ImageMetadataReader.class, Metadata.class, DateTime.class})
public class OSTest {

    private Metadata mock_metadata;
    private DateTime currentTime, mock_dateTime;
    private File mock_file;
    private Path mock_path;
    private DateUtils mock_dateUtils;
    private FileTypeValidator mock_fiteTypeValidator;
    private OS os;
    private String sampleFileName = "sampleFileName";


    @Rule
    ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        mockStatic(Files.class);
        mockStatic(ImageMetadataReader.class);
        mock_file = mock(File.class);
        mock_path = mock(Path.class);
        mock_metadata = mock(Metadata.class);
        currentTime = DateTime.now();
        mock_dateTime = PowerMockito.mock(DateTime.class);
        mock_dateUtils = mock(DateUtils.class);
        mock_fiteTypeValidator = mock(FileTypeValidator.class);

        os = new OS(mock_dateUtils, mock_fiteTypeValidator);

        when(mock_file.getName()).thenReturn(sampleFileName);
        when(mock_path.toFile()).thenReturn(mock_file);
        when(mock_dateUtils.createJodaDateTime(any(Date.class))).thenReturn(currentTime);
    }

    @Test
    public void readFileCreationDate() throws IOException {
        FileTime stubbed_filetime = FileTime.fromMillis(currentTime.getMillis());
        BasicFileAttributes mocked_attribs = mock(BasicFileAttributes.class);
        when(Files.readAttributes(Mockito.any(Path.class), Mockito.any(Class.class))).thenReturn(mocked_attribs);
        when(mocked_attribs.creationTime()).thenReturn(stubbed_filetime);

        DateTime date = os.readFileCreationDate(mock_path);

        PowerMockito.verifyStatic();
        Files.readAttributes(mock_path, BasicFileAttributes.class);
        assertTrue(currentTime.equals(date));
        assertEquals(date, currentTime);
    }


    @Test
    public void getImageMetadataAfterSecondAttempt() throws Exception {
        when(ImageMetadataReader.readMetadata(mock_file))
                .thenThrow(IOException.class)
                .thenReturn(mock_metadata);

        os.getImageMetadata(mock_path);

        PowerMockito.verifyStatic(times(2));
        ImageMetadataReader.readMetadata(mock_file);
    }

    @Test
    public void failGetImageMetadata() throws ImageProcessingException, IOException {
        String samplePath = "abc";
        when(mock_path.toString()).thenReturn(samplePath);
        when(ImageMetadataReader.readMetadata(mock_file)).thenThrow(IOException.class);
        expectedException.expect(ImageProcessingException.class);
        expectedException.expectMessage(containsString(samplePath));

        os.getImageMetadata(mock_path);
    }


    @Test
    public void testCheckIsDirectory() throws FileNotFoundException {
        when(Files.isDirectory(mock_path)).thenReturn(true);

        os.checkIsDirectory(mock_path);

        PowerMockito.verifyStatic();
        Files.isDirectory(mock_path);
    }


    @Test
    public void failedTestCheckIsDirectory() throws FileNotFoundException {
        when(Files.isDirectory(mock_path)).thenReturn(false);
        expectedException.expect(FileNotFoundException.class);
        expectedException.expectMessage(containsString("Path "+sampleFileName+" is not a directory."));

        os.checkIsDirectory(mock_path);
    }


    @Test
    public void testCheckIsReadable() throws FileNotFoundException {
        when(Files.isReadable(mock_path)).thenReturn(true);

        os.checkIsReadable(mock_path);

        PowerMockito.verifyStatic();
        Files.isReadable(mock_path);
    }


    @Test
    public void failedTestCheckIsReadable() throws FileNotFoundException {
        when(Files.isDirectory(mock_path)).thenReturn(false);
        expectedException.expect(FileNotFoundException.class);
        expectedException.expectMessage(containsString("Path "+sampleFileName+" is not a readable directory."));

        os.checkIsReadable(mock_path);
    }


    @Test
    public void testCheckIsWritable() throws FileNotFoundException {
        when(Files.isWritable(mock_path)).thenReturn(true);

        os.checkIsWritable(mock_path);

        PowerMockito.verifyStatic();
        Files.isWritable(mock_path);
    }


    @Test
    public void failedTestCheckIsWritable() throws FileNotFoundException {
        when(Files.isDirectory(mock_path)).thenReturn(false);
        expectedException.expect(FileNotFoundException.class);
        expectedException.expectMessage(containsString("Path "+sampleFileName+" is not a writable directory."));

        os.checkIsWritable(mock_path);
    }


    @Test
    public void testCalcRelativePath(){
        String sampleMonth = "ago";
        int sampleYear = 2008;
        when(mock_dateTime.getYear()).thenReturn(sampleYear);
        when(mock_dateUtils.getShortMonth(mock_dateTime, Locale.getDefault())).thenReturn(sampleMonth);

        Path testPath = os.calcDateRelPath(mock_path, mock_dateTime);

        assertEquals(sampleYear +
                File.separator + sampleMonth.toUpperCase() +
                File.separator + sampleFileName, testPath.toString());
    }


    @Test
    public void testIsMediaType(){
        os.isImageFile(mock_path);

        verify(mock_fiteTypeValidator).isMediaType(sampleFileName);
    }

}
