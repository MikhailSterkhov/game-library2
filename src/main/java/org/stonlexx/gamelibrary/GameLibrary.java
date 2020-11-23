package org.stonlexx.gamelibrary;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import org.stonlexx.gamelibrary.common.CommonLogger;
import org.stonlexx.gamelibrary.common.bean.manager.BeanManager;
import org.stonlexx.gamelibrary.common.configuration.PropertyConfigurationManager;
import org.stonlexx.gamelibrary.common.event.EventManager;
import org.stonlexx.gamelibrary.common.netty.NettyManager;
import org.stonlexx.gamelibrary.common.scheduler.SchedulerManager;

import java.lang.invoke.MethodHandles;

@Getter
public final class GameLibrary {


// ================================================================================================================== //

    @Getter
    private static final GameLibrary instance = new GameLibrary();

// ================================================================================================================== //

    private final EventLoopGroup eventExecutors                                         = new NioEventLoopGroup();
    private final NettyManager nettyManager                                             = new NettyManager();

    private final PropertyConfigurationManager propertyManager                          = new PropertyConfigurationManager();

    private final SchedulerManager schedulerManager                                     = new SchedulerManager();
    private final EventManager eventManager                                             = new EventManager();

    private final BeanManager beanManager                                               = new BeanManager();

    private final MethodHandles.Lookup publicLookup                                     = MethodHandles.publicLookup();


    @Setter
    private CommonLogger logger                                                           = new CommonLogger();

}
