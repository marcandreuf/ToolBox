package org.mandfer.tools.system;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mandfer.tools.guice.ToolsBoxFactory;
import org.mandfer.tools.utils.DateUtils;
import org.mandfer.tools.validation.FileTypeValidator;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by coder on 17/05/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Paths.class, FileSystems.class, ToolsBoxFactory.class, Metadata.class})
public class ArchbotTest {

    private static Logger logger = LoggerFactory.getLogger(ArchbotTest.class);
    private Archbot archbot;

    private Metadata mock_metadata;
    private String sampleOrigin;
    private String sampleDest;
    private String sampleFailed;
    private OS mock_Os;
    private File mock_file;
    private Path mock_path, mock_destPath, mock_relPath, mock_fullPath;
    private FileTypeValidator mock_fileTypeValidator;
    private DateUtils mock_dateUtils;
    private String sampleFileName = "sampleFileName";
    private DateTime currentTime;

    @Rule
    ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUP() throws FileNotFoundException {
        PowerMockito.mockStatic(Paths.class);
        PowerMockito.mockStatic(FileSystems.class);
        PowerMockito.mockStatic(ToolsBoxFactory.class);

        sampleOrigin = System.getProperty("user.home");
        sampleDest = System.getProperty("user.dir");
        sampleFailed = System.getProperty("user.dir")+"/failed";
        mock_Os = mock(OS.class);
        mock_path = mock(Path.class);
        mock_destPath = mock(Path.class);
        mock_relPath = mock(Path.class);
        mock_fullPath = mock(Path.class);
        mock_file = mock(File.class);
        mock_metadata = mock(Metadata.class);
        mock_fileTypeValidator = mock(FileTypeValidator.class);
        mock_dateUtils = mock(DateUtils.class);
        currentTime = DateTime.now();

        PowerMockito.when(ToolsBoxFactory.getInstance(OS.class)).thenReturn(mock_Os);
        PowerMockito.when(ToolsBoxFactory.getInstance(FileTypeValidator.class)).thenReturn(mock_fileTypeValidator);
        PowerMockito.when(ToolsBoxFactory.getInstance(DateUtils.class)).thenReturn(mock_dateUtils);

        when(mock_file.getName()).thenReturn(sampleFileName);
        when(mock_path.toFile()).thenReturn(mock_file);


    }

    @Test
    public void testValidPathsArchbotInstantiation() throws FileNotFoundException {
        archbot = new Archbot(sampleOrigin, sampleDest, sampleFailed);
        assertTrue(archbot != null);
        verify(mock_Os).getPath(sampleOrigin);
        verify(mock_Os).getPath(sampleDest);
        verify(mock_Os).getPath(sampleFailed);
        verify(mock_Os, Mockito.times(3)).checkIsDirectory(Mockito.any(Path.class));
        verify(mock_Os, Mockito.times(3)).checkIsReadable(Mockito.any(Path.class));
        verify(mock_Os, Mockito.times(3)).checkIsWritable(Mockito.any(Path.class));
    }

    @Test
    public void testNonValidPathArchbotInstantiation() throws FileNotFoundException {
        when(mock_Os.getPath("NonValidpath")).thenReturn(mock_path);
        expectedException.expect(FileNotFoundException.class);
        PowerMockito.doThrow(new FileNotFoundException()).when(mock_Os).checkIsDirectory(mock_path);
        archbot = new Archbot(sampleOrigin, sampleDest, sampleFailed);

        archbot = new Archbot( "NonValidpath", sampleDest, sampleFailed);
    }


    @Test
    public void testFindCreationDateInMetadata() throws Exception {
        when(mock_Os.getImageMetadata(mock_path)).thenReturn(mock_metadata);
        archbot = new Archbot(sampleOrigin, sampleDest, sampleFailed);

        archbot.findCreationDate(mock_path);

        verify(mock_Os).getImageMetadata(mock_path);
        verify(mock_Os).getImageExifCreationTime(mock_metadata);
    }

    @Test
    public void testFindCreationDateInFile() throws Exception {
        when(mock_Os.getImageMetadata(mock_path)).thenThrow(ImageProcessingException.class);
        archbot = new Archbot(sampleOrigin, sampleDest, sampleFailed);


        archbot.findCreationDate(mock_path);

        verify(mock_Os).getImageMetadata(mock_path);
        verify(mock_Os).readFileCreationDate(mock_path);
    }

    @Test
    public void failedTestFindCreationDate() throws Exception {
        String path = "samplePath";
        expectedException.expect(FileNotFoundException.class);
        expectedException.expectMessage(equalTo(path+" does not have creation date."));
        when(mock_path.toString()).thenReturn(path);
        when(mock_Os.getImageMetadata(mock_path))
                .thenThrow(ImageProcessingException.class);
        when(mock_Os.readFileCreationDate(mock_path))
                .thenThrow(IOException.class);
        archbot = new Archbot(sampleOrigin, sampleDest, sampleFailed);

        archbot.findCreationDate(mock_path);

        verify(mock_Os).getImageMetadata(mock_path);
        verify(mock_Os).readFileCreationDate(mock_path);
    }


    @Test
    public void testArchivePhoto() throws Exception {
        when(mock_Os.getPath(sampleOrigin)).thenReturn(mock_path);
        when(mock_Os.getPath(sampleDest)).thenReturn(mock_destPath);
        when(mock_fileTypeValidator.isMediaType(sampleFileName)).thenReturn(true);
        when(mock_Os.getImageMetadata(mock_path)).thenReturn(mock_metadata);
        when(mock_Os.getImageExifCreationTime(mock_metadata)).thenReturn(currentTime);
        when(mock_Os.calcDateRelPath(mock_path, currentTime)).thenReturn(mock_relPath);
        when(mock_destPath.resolve(mock_relPath)).thenReturn(mock_fullPath);
        archbot = new Archbot(sampleOrigin, sampleDest, sampleFailed);

        archbot.archivePhoto(mock_path);

        verify(mock_fileTypeValidator).isMediaType(sampleFileName);
        verify(mock_Os).calcDateRelPath(mock_path, currentTime);
        verify(mock_destPath).resolve(mock_relPath);
        verify(mock_Os).moveFileTo(mock_path, mock_fullPath);
    }


    @Test
    public void moveFileToBackupFolderIfItIsNotAMediaFile() throws Exception {
        when(mock_fileTypeValidator.isMediaType(sampleFileName)).thenReturn(false);
        when(mock_Os.getPath(sampleOrigin)).thenReturn(mock_path);
        when(mock_Os.getPath(sampleFailed)).thenReturn(mock_destPath);
        archbot = new Archbot(sampleOrigin, sampleDest, sampleFailed);

        archbot.archivePhoto(mock_path);

        verify(mock_Os).moveFileTo(mock_path, mock_destPath);
    }


    @Test
    public void moveFileToBackupFolderIfThereIsAnyException() throws Exception {
        when(mock_fileTypeValidator.isMediaType(sampleFileName)).thenReturn(true);
        when(mock_Os.getImageMetadata(mock_path)).thenReturn(mock_metadata);
        when(mock_Os.getImageExifCreationTime(mock_metadata)).thenReturn(currentTime);
        when(mock_Os.calcDateRelPath(mock_path, currentTime)).thenThrow(Exception.class);
        when(mock_Os.getPath(sampleOrigin)).thenReturn(mock_path);
        when(mock_Os.getPath(sampleFailed)).thenReturn(mock_destPath);
        archbot = new Archbot(sampleOrigin, sampleDest, sampleFailed);

        archbot.archivePhoto(mock_path);

        verify(mock_Os).moveFileTo(mock_path, mock_destPath);
    }
}