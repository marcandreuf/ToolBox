package org.mandfer.tools.system;

import com.drew.metadata.Metadata;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mandfer.tools.guice.ToolsBoxFactory;
import org.mandfer.tools.utils.DateUtils;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


//@RunWith(PowerMockRunner.class)
//@PrepareForTest({Paths.class, FileSystems.class, ToolsBoxFactory.class, Metadata.class})
public class ArchbotTest {

//    private Metadata mock_metadata;
//    private String sampleOrigin;
//    private String sampleDest;
//    private String sampleFailed;
//    private OS mock_Os;
//    private Path mock_path, mock_destPath, mock_relPath, mock_fullPath;
//    private DateUtils mock_dateUtils;
//    private String sampleFileName = "sampleFileName";
//    private DateTime currentTime;
//    private MediaService mock_mediaService;
//    private Archbot archbot;

//    @Rule
//    ExpectedException expectedException = ExpectedException.none();
//
//    @Before
//    public void setUP() throws FileNotFoundException {
//        PowerMockito.mockStatic(Paths.class);
//        PowerMockito.mockStatic(FileSystems.class);
//        PowerMockito.mockStatic(ToolsBoxFactory.class);
//
//        sampleOrigin = System.getProperty("user.home");
//        sampleDest = System.getProperty("user.dir");
//        sampleFailed = System.getProperty("user.dir")+"/failed";
//        mock_Os = mock(OS.class);
//        mock_path = mock(Path.class);
//        mock_destPath = mock(Path.class);
//        mock_relPath = mock(Path.class);
//        mock_fullPath = mock(Path.class);
//        File mock_file = mock(File.class);
//        mock_metadata = mock(Metadata.class);
//        mock_dateUtils = mock(DateUtils.class);
//        currentTime = DateTime.now();
//        mock_mediaService = mock(MediaService.class);
//
//        PowerMockito.when(ToolsBoxFactory.getInstance(OS.class)).thenReturn(mock_Os);
//        PowerMockito.when(ToolsBoxFactory.getInstance(DateUtils.class)).thenReturn(mock_dateUtils);
//
//        when(mock_file.getName()).thenReturn(sampleFileName);
//        when(mock_path.toFile()).thenReturn(mock_file);
//    }

    //TODO refactor path validation into the Services.
//    @Test
//    public void testValidPathsArchbotInstantiation() throws FileNotFoundException {
//        archbot = new Archbot(mock_mediaService, sampleOrigin, sampleDest, sampleFailed);
//        assertTrue(archbot != null);
//        verify(mock_Os).getPath(sampleOrigin);
//        verify(mock_Os).getPath(sampleDest);
//        verify(mock_Os).getPath(sampleFailed);
//        verify(mock_Os, Mockito.times(3)).checkIsDirectory(Mockito.any(Path.class));
//        verify(mock_Os, Mockito.times(3)).checkIsReadable(Mockito.any(Path.class));
//        verify(mock_Os, Mockito.times(3)).checkIsWritable(Mockito.any(Path.class));
//    }
//
//    @Test
//    public void testNonValidPathArchbotInstantiation() throws FileNotFoundException {
//        when(mock_Os.getPath("NonValidpath")).thenReturn(mock_path);
//        expectedException.expect(FileNotFoundException.class);
//        PowerMockito.doThrow(new FileNotFoundException()).when(mock_Os).checkIsDirectory(mock_path);
//
//        archbot = new Archbot(mock_mediaService, "NonValidpath", sampleDest, sampleFailed);
//    }



}