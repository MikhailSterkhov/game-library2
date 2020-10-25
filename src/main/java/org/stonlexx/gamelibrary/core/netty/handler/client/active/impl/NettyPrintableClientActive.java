package org.stonlexx.gamelibrary.core.netty.handler.client.active.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.handler.client.active.AbstractNettyClientActive;

@RequiredArgsConstructor
public class NettyPrintableClientActive extends AbstractNettyClientActive {

    private final String messageToPrint;

    @Override
    public void onClientActive(@NonNull Channel clientChannel) {
        GameLibrary.getInstance().getLogger().warning(messageToPrint);
    }

}
