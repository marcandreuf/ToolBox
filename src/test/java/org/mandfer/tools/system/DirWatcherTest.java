package org.mandfer.tools.system;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.mockito.Mockito.*;

/**
 * Created by marc on 28/08/16.
 */
public class DirWatcherTest {

    private Path mock_newFilePath;
    private List<Path> stub_newFilesList;
    private WatcherPathService mock_watcherPathService;
    private OS mock_Os;
    private DirWatcher dirWatcher;
    private BlockingQueue<Path> mock_blqQueue;

    @Before
    public void setUp(){
        mock_newFilePath = mock(Path.class);
        stub_newFilesList = new ArrayList<>();
        stub_newFilesList.add(mock_newFilePath);
        mock_watcherPathService = mock(WatcherPathService.class);
        mock_Os = mock(OS.class);
        mock_blqQueue = mock(BlockingQueue.class);

        dirWatcher = new DirWatcherThread(mock_watcherPathService, mock_Os, mock_blqQueue);
    }

    @Test
    public void testGetValidImageFiles() throws Exception {
        when(mock_watcherPathService.getListOfFiles()).thenReturn(stub_newFilesList);
        when(mock_Os.isImageFile(mock_newFilePath)).thenReturn(true);

        dirWatcher.watch();

        verify(mock_watcherPathService).getListOfFiles();
        verify(mock_Os).isImageFile(mock_newFilePath);
        verify(mock_blqQueue).addAll(anyCollection());
    }


    @Test
    public void testGetNonImageFiles() throws Exception {
        when(mock_watcherPathService.getListOfFiles()).thenReturn(stub_newFilesList);
        when(mock_Os.isImageFile(mock_newFilePath)).thenReturn(false);

        dirWatcher.watch();

        verify(mock_watcherPathService).getListOfFiles();
        verify(mock_Os).isImageFile(mock_newFilePath);
        verifyNoMoreInteractions(mock_blqQueue);
    }

    @Test
    public void testGetNonNewFiles() throws Exception {
        when(mock_watcherPathService.getListOfFiles()).thenReturn(new ArrayList<>());

        dirWatcher.watch();

        verify(mock_watcherPathService).getListOfFiles();
        verifyNoMoreInteractions(mock_Os);
        verifyNoMoreInteractions(mock_blqQueue);
    }



}
