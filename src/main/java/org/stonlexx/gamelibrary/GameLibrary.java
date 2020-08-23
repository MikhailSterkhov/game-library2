package org.stonlexx.gamelibrary;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import org.stonlexx.gamelibrary.core.CoreLogger;
import org.stonlexx.gamelibrary.core.GameLibraryCore;

@Getter
public final class GameLibrary {


// ================================================================================================================== //

    @Getter
    private static final GameLibrary instance = new GameLibrary();

// ================================================================================================================== //


    private final GameLibraryCore libraryCore                                           = new GameLibraryCore();
    private final EventLoopGroup eventExecutors                                         = new NioEventLoopGroup();

    @Setter
    private CoreLogger logger                                                           = new CoreLogger();
}
