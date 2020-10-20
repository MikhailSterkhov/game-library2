package org.stonlexx.gamelibrary.core.netty.reconnect.server.impl;

import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.reconnect.server.AbstractNettyReconnect;

@RequiredArgsConstructor
public class NettyPrintableReconnect extends AbstractNettyReconnect {

    private final String messageToPrint;

    @Override
    public void onReconnect() {
        GameLibrary.getInstance().getLogger().warning(messageToPrint);
    }

}
