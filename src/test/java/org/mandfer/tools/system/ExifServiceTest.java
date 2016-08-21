package org.mandfer.tools.system;

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
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.FileNotFoundException;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;

/**
 * Created by marc on 21/08/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Metadata.class)
public class ExifServiceTest {

    private DateUtils mock_dateUtils;
    private Metadata mock_metadata;
    private ExifIFD0Directory mock_exifIFD0Directory;
    private ExifSubIFDDirectory mock_exifSubIFDDirectory;
    private DateTime currentTime;
    private ExifService exifService;


    @Rule
    ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUP() throws FileNotFoundException {
        currentTime = DateTime.now();
        mock_dateUtils = mock(DateUtils.class);
        mock_metadata = mock(Metadata.class);
        mock_exifIFD0Directory = mock(ExifIFD0Directory.class);
        mock_exifSubIFDDirectory = mock(ExifSubIFDDirectory.class);
        exifService = new ExifService(mock_dateUtils);
    }


    @Test
    public void getMetadataCreationTimeFromExifIFD0Directory() throws Exception {
        when(mock_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(mock_exifIFD0Directory);
        when(mock_exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME)).thenReturn(currentTime.toDate());

        exifService.getImageExifCreationTime(mock_metadata);

        verify(mock_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mock_exifIFD0Directory).getDate(ExifIFD0Directory.TAG_DATETIME);
        verify(mock_dateUtils).createJodaDateTime(currentTime.toDate());
    }

    @Test
    public void getMetadataCreationTimeFromExifSubIFDDirectoryORIGINAL_withoutDateInExifIFD0Directory()  throws Exception {
        when(mock_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(mock_exifIFD0Directory);
        when(mock_exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME)).thenReturn(null);
        when(mock_metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)).thenReturn(mock_exifSubIFDDirectory);
        when(mock_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)).thenReturn(currentTime.toDate());

        exifService.getImageExifCreationTime(mock_metadata);

        verify(mock_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mock_exifIFD0Directory).getDate(ExifIFD0Directory.TAG_DATETIME);
        verify(mock_metadata).getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        verify(mock_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        verify(mock_dateUtils).createJodaDateTime(currentTime.toDate());
    }


    @Test
    public void getMetadataCreationTimeFromExifSubIFDDirectoryORIGINAL_withoutExifIFD0Directory() throws Exception {
        when(mock_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(null);
        when(mock_metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)).thenReturn(mock_exifSubIFDDirectory);
        when(mock_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)).thenReturn(currentTime.toDate());

        exifService.getImageExifCreationTime(mock_metadata);

        verify(mock_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mock_metadata).getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        verify(mock_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        Mockito.verifyNoMoreInteractions(mock_exifSubIFDDirectory);
        verify(mock_dateUtils).createJodaDateTime(currentTime.toDate());
    }


    @Test
    public void getMetadataCreationTimeFromExifSubIFDDirectoryDIGITALIZED() throws Exception {
        when(mock_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(null);
        when(mock_metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)).thenReturn(mock_exifSubIFDDirectory);
        when(mock_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)).thenReturn(null);
        when(mock_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED)).thenReturn(currentTime.toDate());

        exifService.getImageExifCreationTime(mock_metadata);

        verify(mock_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mock_metadata).getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        verify(mock_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        verify(mock_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
        Mockito.verifyNoMoreInteractions(mock_exifSubIFDDirectory);
        verify(mock_dateUtils).createJodaDateTime(currentTime.toDate());

    }

    @Test
    public void failToGetMetadataCreationTimeFromAnyExifDirectory() throws ImageProcessingException {
        when(mock_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(null);
        when(mock_metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)).thenReturn(null);
        expectedException.expect(ImageProcessingException.class);
        expectedException.expectMessage(containsString("Exif creation date not found."));

        exifService.getImageExifCreationTime(mock_metadata);

        verify(mock_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mock_metadata).getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Mockito.verifyNoMoreInteractions(mock_metadata);
        verify(mock_dateUtils).createJodaDateTime(currentTime.toDate());
    }

    @Test
    public void failToGetMetadataCreationTimeFromAnyDateOfExifDirectory() throws Exception {
        when(mock_metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(mock_exifIFD0Directory);
        when(mock_exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME)).thenReturn(null);
        when(mock_metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)).thenReturn(mock_exifSubIFDDirectory);
        when(mock_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)).thenReturn(null);
        when(mock_exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED)).thenReturn(null);
        expectedException.expect(ImageProcessingException.class);
        expectedException.expectMessage(containsString("Exif creation date not found."));

        exifService.getImageExifCreationTime(mock_metadata);

        verify(mock_metadata).getFirstDirectoryOfType(ExifIFD0Directory.class);
        verify(mock_exifIFD0Directory).getDate(ExifIFD0Directory.TAG_DATETIME);
        verify(mock_metadata).getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        verify(mock_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        verify(mock_exifSubIFDDirectory).getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
        verify(mock_dateUtils).createJodaDateTime(currentTime.toDate());
    }


}
