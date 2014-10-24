package org.mandfer.tools.config;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertTrue;


/**
 * @author marcandreuf on 24/10/2014.
 *         <p/>
 *         This implementation allows to load config files which point
 *         to a default properties file to be loaded first.
 *         <p/>
 *         The properties files can be chained from generic to specifics,
 *         this allows to remove repetitions in config files.
 */
public class DefaultsConfigLoaderTest {

    public static final String TEST_SAMPLE_PROPS_FILE = 
      "test_sample.properties";

    private ConfigLoader confLoader;

    @Before
    public void setUp() {
        confLoader = new DefaultsConfigLoader();
    }

    @Test
    public void loadConfigFileWithDefaults() throws IOException {
        Properties propsTest =
                confLoader.loadConfig(TEST_SAMPLE_PROPS_FILE);

        assertTrue(propsTest.isEmpty() == false);

        assertTrue(propsTest.getProperty("topDefaultKey")
                .equals("topDefaultValue"));

        assertTrue(propsTest.getProperty("defaultKey")
                .equals("defaultValue"));

        assertTrue(propsTest.getProperty("specificKey")
                .equals("specialValue"));
    }


    @Test(expected = FileNotFoundException.class)
    public void failLoadConfigFile() throws IOException {
        confLoader.loadConfig("notExistentFile.properties");
    }


}
