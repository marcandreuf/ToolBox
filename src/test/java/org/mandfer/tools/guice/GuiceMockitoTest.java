package org.mandfer.tools.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mandfer.tools.system.OS;
import org.mandfer.tools.system.WatcherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static org.mockito.Mockito.mock;

/**
 * Created by marc on 23/08/16.
 */
public class GuiceMockitoTest {

    private static Logger logger = LoggerFactory.getLogger(OS.class);
    private Path mock_path;
    private WatcherService watcherService;

    @Before
    public void setUp() {
        mock_path = mock(Path.class);
        Injector injector = Guice.createInjector(new TestModule());
        watcherService = injector.getInstance(WatcherService.class);
    }

    private class TestModule extends AbstractModule {
        protected void configure() {
            bind(new TypeLiteral<Map<String, WatchKey>>(){}).toInstance(new HashMap<>());
        }
    }

    @Test
    public void testInjectMapBinding() throws IOException {
        watcherService.registerDirEventsListener(mock_path, ENTRY_CREATE);
        boolean isRegistered = watcherService.isRegistered(mock_path, ENTRY_CREATE);
        Assert.assertTrue(isRegistered);
    }

}
