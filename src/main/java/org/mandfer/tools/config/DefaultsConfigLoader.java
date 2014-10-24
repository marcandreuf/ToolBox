package org.mandfer.tools.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Default configuration loader from properties file.
 * <p/>
 * This load a properties file. If the file has a reference to a
 * default properties file name with the key "ref.defaults.fileName"
 * pointing to another properties file, this file is loaded first and
 * the original file is reloaded to override default values with
 * specific ones.
 * <p/>
 * The default files could be chained with default values.
 *
 * @author marcandreuf
 */
public class DefaultsConfigLoader implements ConfigLoader {

    protected static final String DEFAULT_PROPERTIES_FILENAME_KEY =
            "ref.defaults.fileName";

    private Logger logger =
            LoggerFactory.getLogger(DefaultsConfigLoader.class);

    @Override
    public Properties loadConfig(String fileName)
            throws FileNotFoundException {
        Properties fileConfig = loadFile(fileName);

        String defaultFileName = fileConfig.getProperty(
                DEFAULT_PROPERTIES_FILENAME_KEY);
        if (defaultFileName != null) {
            fileConfig = loadConfig(defaultFileName);
        }

        overloadConfigInto(fileName, fileConfig);

        logger.debug("Load config file: " + fileName);
        return fileConfig;
    }

    protected void overloadConfigInto(
            String fileName, Properties config) throws FileNotFoundException {
        try (InputStream configInputStream
                     = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            config.load(configInputStream);
        } catch (Exception e) {
            throw new FileNotFoundException(e.getMessage());
        }

    }

    protected Properties loadFile(String fileName) throws FileNotFoundException {
        Properties config = new Properties();
        try (InputStream configInputStream
                     = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            config.load(configInputStream);
            return config;
        } catch (Exception e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

}
