package org.mandfer.tools.guice;


import com.google.inject.AbstractModule;
import org.mandfer.tools.config.ConfigLoader;
import org.mandfer.tools.config.DefaultsConfigLoader;

/**
 * Guice technology configuration module class for commons package.
 *
 * @author marcandreuf
 */
public class ToolsBoxGuiceModule extends AbstractModule {

    @Override
    protected void configure() {

        // PropertiesLoader constructor dependency
        bind(ConfigLoader.class).to(DefaultsConfigLoader.class);


//        bind(DomLoader.class).to(W3CDOMLoader.class).in(Singleton.class);
//        bind(DomCreator.class).to(W3CDOMCreator.class);
//        bind(XMLFinder.class).to(JAXENFinder.class);
//        bind(XMLWriter.class).to(XMLWriterImpl.class);
//        bind(DomHandler.class).to(DomHandlerImpl.class);

        //ssh utils
//        bind(SshCmd.class).to(SchUtil.class);
//        bind(JSch.class);
//        bind(UserInfo.class).to(LocalUserInfo.class);
    }
}
