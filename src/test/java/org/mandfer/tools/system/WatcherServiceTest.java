package org.mandfer.tools.system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import static org.mockito.Mockito.*;


/**
 * Created by marc on 23/08/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileSystems.class, Path.class, WatcherService.class})
public class WatcherServiceTest {


    private Path mock_path, mock_pathName, mock_pathResult;
    private List<WatchEvent<?>> stubbed_events;
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
        mock_key = mock(WatchKey.class);
        mock_pathResult = mock(Path.class);
        mock_path = mock(Path.class);
        mock_pathName = mock(Path.class);
        mock_watcher = mock(WatchService.class);
        mock_fileSystem = PowerMockito.mock(FileSystem.class);
        mock_mapResgistry = (Map<String, WatchKey>) mock(Map.class);
        watcherService = new WatcherService(mock_mapResgistry);

        strPath = "samplePath";
        cacheKey = strPath + ENTRY_CREATE;
        when(mock_path.toString()).thenReturn(strPath);
        PowerMockito.mockStatic(FileSystems.class);
        when(FileSystems.getDefault()).thenReturn(mock_fileSystem);
        when(mock_fileSystem.newWatchService()).thenReturn(mock_watcher);
        when(mock_path.register(mock_watcher, ENTRY_CREATE)).thenReturn(mock_registeredKey);
    }


    //TODO create a WatcherPathService
    // Verify constructor creates the watcher and makes the path.register(...

    @Test
    public void testRegisterWatcher() throws IOException {

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


    //TODO: WatcherService needs to be for one path.
    // WatcherPathService (Path path)
    // Watcher and registered key are unique for the path event kind.
    // Not necessary a map.


    @Test
    public void testGetListOfNewFiles() throws Exception {
        //WatchEvent<Path> mock_eventPath = mock(WatchEvent.class);
        //WatchEvent.Kind overFlow_eventKind = OVERFLOW;
        //WatchEvent.Kind entryCrete_eventKind = ENTRY_CREATE;

        //stubbed_events = new ArrayList<>();
        //stubbed_events.add(mock_eventPath);
        //when(mock_eventPath.kind()).thenReturn(entryCrete_eventKind);
        //when(mock_eventPath.context()).thenReturn(mock_pathName);
        //when(mock_path.resolve(mock_pathName)).thenReturn(mock_pathResult);
        //when(mock_watcher.take()).thenReturn(mock_key);
        //when(mock_key.pollEvents()).thenReturn(stubbed_events);
        watcherService.registerDirEventsListener(mock_path, ENTRY_CREATE);

        List<Path> files = watcherService.getListOfFilesByEvent(mock_path, ENTRY_CREATE);

        verify(mock_watcher).take();
        //verify(mock_mapResgistry).containsKey(mock_key);
        //verify(mock_key).pollEvents();
        //verify(mock_eventPath).kind();
        //verify(mock_path).resolve(mock_pathName);
        //Assert.assertTrue(files.contains(mock_pathResult));
    }

}
