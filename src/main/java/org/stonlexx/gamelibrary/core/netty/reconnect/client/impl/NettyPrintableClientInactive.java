package org.stonlexx.gamelibrary.core.netty.reconnect.client.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.reconnect.client.AbstractNettyClientInactive;

@RequiredArgsConstructor
public class NettyPrintableClientInactive extends AbstractNettyClientInactive {

    private final String messageToPrint;

    @Override
    public void onClientInactive(@NonNull Channel clientChannel) {
        GameLibrary.getInstance().getLogger().warning(messageToPrint);
    }

}
