package org.mandfer.tools.guice;


import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.mandfer.tools.config.ConfigLoader;
import org.mandfer.tools.config.DefaultsConfigLoader;
import org.mandfer.tools.format.FormatterBasic;
import org.mandfer.tools.format.StringFormatter;
import org.mandfer.tools.system.*;
import org.mandfer.tools.utils.DateUtils;
import org.mandfer.tools.validation.FileTypeValidator;
import org.mandfer.tools.validation.FileTypeValidatorRegExp;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Guice technology configuration module class for tools box package.
 *
 * @author marcandreuf
 */
public class ToolsBoxGuiceModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(ConfigLoader.class).to(DefaultsConfigLoader.class);

        bind(OS.class);
        bind(FileTypeValidator.class).to(FileTypeValidatorRegExp.class);
        bind(StringFormatter.class).to(FormatterBasic.class);
        bind(DateUtils.class);
        bind(ExifService.class);
        bind(MediaService.class);
        bind(ArchiverService.class);
        bind(new TypeLiteral<BlockingQueue<Path>>(){}).toInstance(new SynchronousQueue<>());

        install(new FactoryModuleBuilder()
                .implement(DirArchiver.class, DirArchiverThread.class)
                .build(DirArchiverFactory.class));


        //TODO: bind DirWatcher

        //TODO: bind Archbot

    }
}
