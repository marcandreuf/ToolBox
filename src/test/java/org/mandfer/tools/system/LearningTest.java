package org.mandfer.tools.system;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by marc on 14/08/16.
 */
public class LearningTest {

    private static Logger logger = LoggerFactory.getLogger(LearningTest.class);

    @Test
    public void testGetPathFailures(){

        Path path = Paths.get("^.?*12abcNotAPath");


        logger.debug("Path: "+path);


    }

}
