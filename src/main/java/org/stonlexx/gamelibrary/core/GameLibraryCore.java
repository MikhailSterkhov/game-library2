package org.stonlexx.gamelibrary.core;

import lombok.Getter;
import org.stonlexx.gamelibrary.core.bean.manager.BeanManager;
import org.stonlexx.gamelibrary.core.configuration.LibraryCoreConfiguration;
import org.stonlexx.gamelibrary.core.event.EventManager;
import org.stonlexx.gamelibrary.core.netty.NettyManager;
import org.stonlexx.gamelibrary.core.scheduler.SchedulerManager;

import java.lang.invoke.MethodHandles;

@Getter
public class GameLibraryCore {

    private final NettyManager nettyManager                                         = new NettyManager();

    private final LibraryCoreConfiguration coreConfiguration                            = new LibraryCoreConfiguration();

    private final SchedulerManager schedulerManager                                     = new SchedulerManager();
    private final EventManager eventManager                                             = new EventManager();

    private final BeanManager beanManager                                           = new BeanManager();


    private final MethodHandles.Lookup publicLookup                                     = MethodHandles.publicLookup();
}
