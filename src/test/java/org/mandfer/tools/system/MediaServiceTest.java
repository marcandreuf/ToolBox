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
import org.mandfer.tools.guice.ToolsBoxFactory;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Metadata.class)
public class MediaServiceTest {

    private Metadata mock_metadata;
    private OS mock_Os;
    private Path mock_path;
    private ExifService mock_exifService;
    private String sampleFileName = "sampleFileName";
    private MediaService mediaService;


    @Rule
    ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUP() throws FileNotFoundException {
        mock_Os = mock(OS.class);
        mock_exifService = mock(ExifService.class);
        mock_path = mock(Path.class);
        File mock_file = mock(File.class);
        mock_metadata = mock(Metadata.class);

        mediaService = new MediaService(mock_Os, mock_exifService);

        when(mock_file.getName()).thenReturn(sampleFileName);
        when(mock_path.toFile()).thenReturn(mock_file);
    }


    @Test
    public void testFindCreationDateInMetadata() throws Exception {
        when(mock_Os.getImageMetadata(mock_path)).thenReturn(mock_metadata);

        mediaService.findCreationDate(mock_path);

        verify(mock_Os).getImageMetadata(mock_path);
        verify(mock_exifService).getImageExifCreationTime(mock_metadata);
    }

    @Test
    public void testFindCreationDateInFile() throws Exception {
        when(mock_Os.getImageMetadata(mock_path)).thenThrow(ImageProcessingException.class);

        mediaService.findCreationDate(mock_path);

        verify(mock_Os).getImageMetadata(mock_path);
        verify(mock_Os).readFileCreationDate(mock_path);
    }

    @Test
    public void failedTestFindCreationDate() throws Exception {
        String path = "samplePath";
        expectedException.expect(FileNotFoundException.class);
        when(mock_path.toString()).thenReturn(path);
        when(mock_Os.getImageMetadata(mock_path)).thenThrow(ImageProcessingException.class);
        when(mock_Os.readFileCreationDate(mock_path)).thenThrow(IOException.class);

        mediaService.findCreationDate(mock_path);

        verify(mock_Os).getImageMetadata(mock_path);
        verify(mock_Os).readFileCreationDate(mock_path);
    }
}
