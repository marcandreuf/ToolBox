package org.mandfer.tools.system;

import com.drew.imaging.ImageProcessingException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mandfer.tools.format.StringFormatter;
import org.mandfer.tools.validation.FileTypeValidator;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        archbot = new Archbot( sampleOrigin, sampleDest, sampleFailed, mock_Os, mock_fileTypeValidator, mock_stringFormatter);
    }

    @Test
    public void testValidPathsArchbotInstantiation() throws FileNotFoundException {
        assertTrue(archbot != null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testNonValidOriginPathArchbotInstantiation() throws FileNotFoundException {
        sampleOrigin = "NonValidpath";
        archbot = new Archbot( sampleOrigin, sampleDest, sampleFailed, mock_Os, mock_fileTypeValidator, mock_stringFormatter);
    }

    @Test(expected = FileNotFoundException.class)
    public void testNonValidDestinationPathArchbotInstantiation() throws FileNotFoundException {
        sampleDest = "NonValidpath";
        archbot = new Archbot( sampleOrigin, sampleDest, sampleFailed, mock_Os, mock_fileTypeValidator, mock_stringFormatter);
    }


    //TODO TCs of different paths of archivePhoto method.

    @Test
    public void testArchivePhoto() throws Exception {


        //TODO: prepare OS interactions.

        //archbot.archivePhoto(mocked_Path);

        //TODO: verify OS interactions.

    }

}
