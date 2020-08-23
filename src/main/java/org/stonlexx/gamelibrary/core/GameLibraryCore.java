package org.stonlexx.gamelibrary.core;

import lombok.Getter;
import org.stonlexx.gamelibrary.core.bean.CoreBeanManager;
import org.stonlexx.gamelibrary.core.configuration.LibraryCoreConfiguration;
import org.stonlexx.gamelibrary.core.netty.CoreNettyManager;

@Getter
public class GameLibraryCore {

    private final CoreBeanManager beanManager                                          = new CoreBeanManager();
    private final CoreNettyManager nettyManager                                        = new CoreNettyManager();
    private final LibraryCoreConfiguration coreConfiguration                           = new LibraryCoreConfiguration();
}
