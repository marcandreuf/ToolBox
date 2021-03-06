package org.mandfer.tools.system;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

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
    public void testFindExifCreationDateInImageFile() throws Exception {
        when(mock_Os.getImageMetadata(mock_path)).thenReturn(mock_metadata);
        when(mock_Os.isExifImageFile(mock_path)).thenReturn(true);

        mediaService.findCreationDate(mock_path);

        verify(mock_Os).isExifImageFile(mock_path);
        verify(mock_Os).getImageMetadata(mock_path);
        verify(mock_exifService).getImageExifCreationTime(mock_metadata);
    }

    @Test
    public void testFindImageFileCreationDate() throws Exception {
        when(mock_Os.getImageMetadata(mock_path)).thenThrow(ImageProcessingException.class);
        when(mock_Os.isExifImageFile(mock_path)).thenReturn(true);

        mediaService.findCreationDate(mock_path);

        verify(mock_Os).isExifImageFile(mock_path);
        verify(mock_Os).getImageMetadata(mock_path);
        verify(mock_Os).readFileCreationDate(mock_path);
    }


    @Test
    public void testFindVideoCreationDate() throws Exception {
        when(mock_Os.isExifImageFile(mock_path)).thenReturn(false);
        when(mock_Os.isVideoFile(mock_path)).thenReturn(true);

        mediaService.findCreationDate(mock_path);

        verify(mock_Os).isExifImageFile(mock_path);
        verify(mock_Os).isVideoFile(mock_path);
        verify(mock_Os).readFileCreationDate(mock_path);
    }

    @Test
    public void failedTestFindCreationDate() throws Exception {
        when(mock_Os.isExifImageFile(mock_path)).thenReturn(true);

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
