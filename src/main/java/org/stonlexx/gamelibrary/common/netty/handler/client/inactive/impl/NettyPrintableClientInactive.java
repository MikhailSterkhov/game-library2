package org.stonlexx.gamelibrary.common.netty.handler.client.inactive.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.common.netty.handler.client.inactive.AbstractNettyClientInactive;

@RequiredArgsConstructor
public class NettyPrintableClientInactive extends AbstractNettyClientInactive {

    private final String messageToPrint;

    @Override
    public void onClientInactive(@NonNull Channel clientChannel) {
        GameLibrary.getInstance().getLogger().warning(messageToPrint);
    }

}
