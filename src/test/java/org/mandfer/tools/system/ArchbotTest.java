package org.mandfer.tools.system;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mandfer.tools.format.StringFormatter;
import org.mandfer.tools.validation.FileTypeValidator;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by coder on 17/05/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Paths.class, FileSystems.class})
public class ArchbotTest {

    private static Logger logger = LoggerFactory.getLogger(ArchbotTest.class);
    private Archbot archbot;

    private String sampleOrigin;
    private String sampleDest;
    private Path mocked_samplePathOrigin;
    private Path mocked_samplePathDest;
    private FileTypeValidator mock_fileTypeValidator;
    private StringFormatter mock_stringFormatter;

    @Before
    public void setUP(){
        PowerMockito.mockStatic(Paths.class);
        PowerMockito.mockStatic(FileSystems.class);

        sampleOrigin = System.getProperty("user.home");
        sampleDest = System.getProperty("user.dir");
        mocked_samplePathOrigin = mock(Path.class);
        mocked_samplePathDest = mock(Path.class);
        mock_fileTypeValidator = mock(FileTypeValidator.class);
        mock_stringFormatter = mock(StringFormatter.class);
    }

    @Test
    public void ValidPathsArchbotInstantiation() throws FileNotFoundException {
        when(Paths.get(Mockito.anyString()))
                .thenReturn(mocked_samplePathOrigin, mocked_samplePathDest);

            archbot = new Archbot(
                    sampleOrigin,
                    sampleDest,
                    mock_fileTypeValidator,
                    mock_stringFormatter);
    }


    @Test(expected = FileNotFoundException.class)
    public void NonValidOriginPathArchbotInstantiation() throws FileNotFoundException {
        sampleOrigin = "NonValidpath";

        archbot = new Archbot(
                sampleOrigin,
                sampleDest,
                mock_fileTypeValidator,
                mock_stringFormatter);
    }

    @Test(expected = FileNotFoundException.class)
    public void NonValidDestinationPathArchbotInstantiation() throws FileNotFoundException {
        sampleDest = "NonValidpath";

        archbot = new Archbot(
                sampleOrigin,
                sampleDest,
                mock_fileTypeValidator,
                mock_stringFormatter);
    }

    //        WatchService mocked_watcher = mock(WatchService.class);
//        FileSystem mocked_filesystem = mock(FileSystem.class);
//        WatchKey mocked_registeredKey = mock(WatchKey.class);

    /*            when(FileSystems.getDefault()).thenReturn(mocked_filesystem);
            when(mocked_filesystem.newWatchService()).thenReturn(mocked_watcher);

            when(mocked_samplePathOrigin.register(mocked_watcher, ENTRY_CREATE))
                    .thenReturn(mocked_registeredKey);*/

}
