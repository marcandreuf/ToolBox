package org.mandfer.tools.system;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mandfer.tools.format.StringFormatter;
import org.mandfer.tools.validation.FileTypeValidator;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.file.*;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by coder on 17/05/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Paths.class})
public class ArchbotTest {

    private static Logger logger = LoggerFactory.getLogger(ArchbotTest.class);
    private Archbot archbot;

    private String sampleOrigin;
    private String sampleDest;
    private String sampleFailed;
    private OS mock_Os;
    private Path mocked_Path;
    private FileTypeValidator mock_fileTypeValidator;
    private StringFormatter mock_stringFormatter;

    @Rule
    ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUP() throws FileNotFoundException {
        PowerMockito.mockStatic(Paths.class);
        PowerMockito.mockStatic(FileSystems.class);

        sampleOrigin = System.getProperty("user.home");
        sampleDest = System.getProperty("user.dir");
        sampleFailed = System.getProperty("user.dir")+"/failed";
        mock_Os = mock(OS.class);
        mocked_Path = mock(Path.class);
        mock_fileTypeValidator = mock(FileTypeValidator.class);
        mock_stringFormatter = mock(StringFormatter.class);
    }

    @Test
    public void testValidPathsArchbotInstantiation() throws FileNotFoundException {
        archbot = new Archbot( sampleOrigin, sampleDest, sampleFailed, mock_Os, mock_fileTypeValidator, mock_stringFormatter);

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
        expectedException.expect(FileNotFoundException.class);
        expectedException.expectMessage(containsString(sampleOrigin));
        sampleOrigin = "NonValidpath";
        when(mock_Os.getPath(Mockito.anyString())).thenReturn(mocked_Path);
        doThrow(FileNotFoundException.class).when(mock_Os).checkIsDirectory(mocked_Path);

        archbot = new Archbot( sampleOrigin, sampleDest, sampleFailed, mock_Os, mock_fileTypeValidator, mock_stringFormatter);

    }


    //TODO TCs of different paths of archivePhoto method.
    @Test
    @Ignore
    public void testArchivePhoto() throws Exception {


        //TODO: prepare OS interactions.

        //archbot.archivePhoto(mocked_Path);

        //TODO: verify OS interactions.

    }

}
