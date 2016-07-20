package org.mandfer.tools.system;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by andreufm on 19/07/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Files.class, OS.class})
public class OSTest {

    @Before
    public void setUp() {
        mockStatic(Files.class);
    }

    @Test
    public void testReadFileCreationDate() throws IOException {
        long currentTime = System.currentTimeMillis();
        File mocked_file = mock(File.class);
        Path mocked_path = mock(Path.class);
        FileTime stubbed_filetime = FileTime.fromMillis(currentTime);
        BasicFileAttributes mocked_attribs = mock(BasicFileAttributes.class);
        when(mocked_file.toPath()).thenReturn(mocked_path);
        when(Files.readAttributes(Mockito.any(Path.class), Mockito.any(Class.class))).thenReturn(mocked_attribs);
        when(mocked_attribs.creationTime()).thenReturn(stubbed_filetime);

        Date date = OS.readFileCreationDate(mocked_file);

        PowerMockito.verifyStatic();
        Files.readAttributes(mocked_path, BasicFileAttributes.class);
        assertEquals(date.getTime(), currentTime);
    }
}
