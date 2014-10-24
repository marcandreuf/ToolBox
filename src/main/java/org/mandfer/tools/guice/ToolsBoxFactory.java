package org.mandfer.tools.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * This factory is a helper class to create objects without Guice syntax.
 * This factory can be used by other applications that use this commons package, but do not use Guice technology.
 * <p/>
 * The use of this class is optional. Internally in the commons project and externally from other applications that
 * use this package.
 *
 * @author marcandreuf
 */
public class ToolsBoxFactory {

    private static Injector tafCommonsInjector = Guice.createInjector(new ToolsBoxGuiceModule());


    /**
     * Get instance of any type in the library.
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<T> type) {
        return tafCommonsInjector.getInstance(type);
    }


}
