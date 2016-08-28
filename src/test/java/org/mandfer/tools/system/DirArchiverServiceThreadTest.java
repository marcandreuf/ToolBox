package org.mandfer.tools.system;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by marc on 22/08/16.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(Path.class)
public class DirArchiverServiceThreadTest {

    private ArchiverService mock_archiverService;
    private Path mock_destPath, mock_failPath, mock_nextPath;
    private BlockingQueue<Path> mock_blqQueue;
    private DirArchiverThread dirArchiverThread;


    @Before
    public void setUp(){
        mock_archiverService = mock(ArchiverService.class);
        mock_destPath = mock(Path.class);
        mock_failPath = mock(Path.class);
        mock_nextPath = mock(Path.class);
        mock_blqQueue = mock(BlockingQueue.class);

        dirArchiverThread = new DirArchiverThread(mock_destPath, mock_failPath, mock_archiverService, mock_blqQueue);
    }


    @Test
    public void testArchiveNext() throws InterruptedException, IOException {
        when(mock_blqQueue.take()).thenReturn(mock_nextPath);

        dirArchiverThread.archiveNext();

        verify(mock_blqQueue).take();
        verify(mock_archiverService).archivePhoto(mock_nextPath, mock_destPath, mock_failPath);
    }




}


