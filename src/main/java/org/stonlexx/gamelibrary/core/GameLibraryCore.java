package org.stonlexx.gamelibrary.core;

import lombok.Getter;
import org.stonlexx.gamelibrary.core.bean.manager.CoreBeanManager;
import org.stonlexx.gamelibrary.core.configuration.LibraryCoreConfiguration;
import org.stonlexx.gamelibrary.core.event.EventManager;
import org.stonlexx.gamelibrary.core.netty.CoreNettyManager;
import org.stonlexx.gamelibrary.core.scheduler.SchedulerManager;

import java.lang.invoke.MethodHandles;

@Getter
public class GameLibraryCore {

    private final CoreNettyManager nettyManager                                         = new CoreNettyManager();

    private final LibraryCoreConfiguration coreConfiguration                            = new LibraryCoreConfiguration();

    private final SchedulerManager schedulerManager                                     = new SchedulerManager();
    private final EventManager eventManager                                             = new EventManager();

    private final CoreBeanManager beanManager                                           = new CoreBeanManager();


    private final MethodHandles.Lookup publicLookup                                     = MethodHandles.publicLookup();
}
