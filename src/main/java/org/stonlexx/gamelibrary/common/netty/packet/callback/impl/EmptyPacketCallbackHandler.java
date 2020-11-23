package org.stonlexx.gamelibrary.common.netty.packet.callback.impl;

import lombok.NonNull;
import org.stonlexx.gamelibrary.common.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.common.netty.packet.callback.NettyPacketCallbackHandler;

public class EmptyPacketCallbackHandler<P extends NettyPacket> extends NettyPacketCallbackHandler<P> {

    @Override
    public void handleCallback(@NonNull P callbackNettyPacket) { }
}
