package org.mandfer.tools.system;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by marc on 21/08/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Path.class, DateTime.class})
public class ArchiverServiceTest {



    private Path mock_path, mock_destPath, mock_failed, mock_relPath, mock_fullPath;
    private DateTime mock_creationDate;
    private OS mock_Os;
    private MediaService mock_mediaService;
    private ArchiverService archiverService;



    @Before
    public void setUP() throws FileNotFoundException {
        mock_path = mock(Path.class);
        mock_destPath = mock(Path.class);
        mock_failed = mock(Path.class);
        mock_relPath = mock(Path.class);
        mock_fullPath = mock(Path.class);
        mock_creationDate = PowerMockito.mock(DateTime.class);
        mock_Os = mock(OS.class);
        mock_mediaService = mock(MediaService.class);

        archiverService = new ArchiverService(mock_Os, mock_mediaService);
    }

    @Test
    public void testArchivePhoto() throws Exception {
        when(mock_mediaService.findCreationDate(mock_path)).thenReturn(mock_creationDate);
        when(mock_Os.calcDateRelPath(mock_path, mock_creationDate)).thenReturn(mock_relPath);
        when(mock_destPath.resolve(mock_relPath)).thenReturn(mock_fullPath);

        archiverService.archivePhoto(mock_path, mock_destPath, mock_failed);

        verify(mock_mediaService).findCreationDate(mock_path);
        verify(mock_Os).calcDateRelPath(mock_path, mock_creationDate);
        verify(mock_Os).moveFileTo(mock_path, mock_fullPath);
    }




    @Test
    public void moveFileToBackupFolderIfThereIsAnyException() throws Exception {
        when(mock_mediaService.findCreationDate(mock_path)).thenThrow(FileNotFoundException.class);

        archiverService.archivePhoto(mock_path, mock_destPath, mock_failed);

        verify(mock_Os).moveFileTo(mock_path, mock_failed);
    }
}
