package org.stonlexx.gamelibrary.core.netty.handler.server.active.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.handler.server.active.AbstractNettyServerActive;

@RequiredArgsConstructor
public class NettyPrintableServerActive extends AbstractNettyServerActive {

    private final String messageToPrint;

    @Override
    public void onServerActive(@NonNull Channel serverChannel) {
        GameLibrary.getInstance().getLogger().warning(messageToPrint);
    }

}
