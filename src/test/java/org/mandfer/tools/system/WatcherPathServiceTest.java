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

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import static org.mockito.Mockito.*;


/**
 * Created by marc on 23/08/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileSystems.class, Path.class, WatcherPathService.class})
public class WatcherPathServiceTest {


    private Path mock_path, mock_pathName, mock_pathResolved;

    private WatchService mock_watcher;
    private WatchKey mock_registeredKey;
    private FileSystem mock_fileSystem;
    private WatcherPathService watcherPathService;

    @Before
    public void setUp() throws IOException {
        mock_registeredKey = mock(WatchKey.class);
        mock_pathResolved = mock(Path.class);
        mock_path = mock(Path.class);
        mock_pathName = mock(Path.class);
        mock_watcher = mock(WatchService.class);
        mock_fileSystem = PowerMockito.mock(FileSystem.class);

        PowerMockito.mockStatic(FileSystems.class);
        when(FileSystems.getDefault()).thenReturn(mock_fileSystem);
        when(mock_fileSystem.newWatchService()).thenReturn(mock_watcher);
        when(mock_path.register(mock_watcher, ENTRY_CREATE)).thenReturn(mock_registeredKey);

        watcherPathService = new WatcherPathService(mock_path, ENTRY_CREATE);
    }

    @Test
    public void testCreateWatcherPathService() throws IOException {
        PowerMockito.verifyStatic();
        FileSystems.getDefault();
        verify(mock_fileSystem).newWatchService();
        verify(mock_path).register(mock_watcher, ENTRY_CREATE);
    }

    @Test
    public void testGetListOfNewFiles() throws Exception {
        when(mock_watcher.take()).thenReturn(mock_registeredKey);
        WatchEvent<Path> mock_eventPath = mock(WatchEvent.class);
        WatchEvent.Kind eventKind = ENTRY_CREATE;
        when(mock_eventPath.kind()).thenReturn(eventKind);
        List<WatchEvent<?>> stubbed_events = new ArrayList<>();
        stubbed_events.add(mock_eventPath);
        when(mock_registeredKey.pollEvents()).thenReturn(stubbed_events);
        when(mock_eventPath.context()).thenReturn(mock_pathName);
        when(mock_path.resolve(mock_pathName)).thenReturn(mock_pathResolved);

        List<Path> files = watcherPathService.getListOfFiles();

        verify(mock_watcher).take();
        verify(mock_registeredKey).pollEvents();
        verify(mock_eventPath, times(2)).kind();
        verify(mock_path).resolve(mock_pathName);
        verify(mock_registeredKey).reset();
        Assert.assertTrue(files.contains(mock_pathResolved));
    }

    @Test
    public void testGetOverflowEvents() throws Exception {
        when(mock_watcher.take()).thenReturn(mock_registeredKey);
        WatchEvent<Path> mock_eventPath = mock(WatchEvent.class);
        WatchEvent.Kind eventKind = OVERFLOW;
        when(mock_eventPath.kind()).thenReturn(eventKind);
        List<WatchEvent<?>> stubbed_events = new ArrayList<>();
        stubbed_events.add(mock_eventPath);
        when(mock_registeredKey.pollEvents()).thenReturn(stubbed_events);

        List<Path> files = watcherPathService.getListOfFiles();

        verify(mock_path).register(mock_watcher, ENTRY_CREATE);
        verify(mock_watcher).take();
        verify(mock_registeredKey).pollEvents();
        verify(mock_eventPath).kind();
        verifyNoMoreInteractions(mock_path);
        verify(mock_registeredKey).reset();
        Assert.assertNotNull(files);
        Assert.assertFalse(files.contains(mock_pathResolved));

    }

    @Test
    public void testGetUnregisteredKey() throws Exception {
        WatchKey mock_diffKey = mock(WatchKey.class);
        when(mock_watcher.take()).thenReturn(mock_diffKey);

        List<Path> files = watcherPathService.getListOfFiles();

        verify(mock_watcher).take();
        verifyNoMoreInteractions(mock_registeredKey);
        verify(mock_diffKey).reset();
        Assert.assertNotNull(files);
        Assert.assertFalse(files.contains(mock_pathResolved));

    }

}
