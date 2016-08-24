package org.mandfer.tools.system;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static org.mockito.Mockito.*;


/**
 * Created by marc on 23/08/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileSystems.class, Path.class, WatcherService.class})
public class WatcherServiceTest {


    private Path mock_path;
    private WatchService mock_watcher;
    private WatchKey mock_registeredKey;
    private WatchKey mock_key;
    private FileSystem mock_fileSystem;
    private Map<String, WatchKey> mock_mapResgistry;
    private WatcherService watcherService;
    private String strPath;
    private String cacheKey;

    @Before
    public void setUp() throws IOException {
        mock_path = mock(Path.class);
        mock_fileSystem = PowerMockito.mock(FileSystem.class);
        mock_mapResgistry = (Map<String, WatchKey>) mock(Map.class);
        watcherService = new WatcherService(mock_mapResgistry);

        strPath = "samplePath";
        cacheKey = strPath + ENTRY_CREATE;
        when(mock_path.toString()).thenReturn(strPath);
    }


    @Test
    public void testRegisterWatcher() throws IOException {
        PowerMockito.mockStatic(FileSystems.class);
        when(FileSystems.getDefault()).thenReturn(mock_fileSystem);
        when(mock_fileSystem.newWatchService()).thenReturn(mock_watcher);
        when(mock_path.register(mock_watcher, ENTRY_CREATE)).thenReturn(mock_registeredKey);

        watcherService.registerDirEventsListener(mock_path, ENTRY_CREATE);

        PowerMockito.verifyStatic();
        FileSystems.getDefault();
        verify(mock_fileSystem).newWatchService();
        verify(mock_path).register(mock_watcher, ENTRY_CREATE);
        verify(mock_mapResgistry).put(cacheKey, mock_registeredKey);
    }

    @Test
    public void testCheckIfPathEventIsRegistered(){
        watcherService.isRegistered(mock_path, ENTRY_CREATE);
        verify(mock_mapResgistry).containsKey(cacheKey);
    }


    //TODO: test getListOfNewFiles()

}
