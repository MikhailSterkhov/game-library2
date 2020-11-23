package org.stonlexx.gamelibrary.common.netty.packet.callback.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.common.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.common.netty.packet.callback.NettyPacketCallbackHandler;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ConsumerPacketCallbackHandler<P extends NettyPacket> extends NettyPacketCallbackHandler<P> {

    private final Consumer<P> callbackPacketConsumer;

    @Override
    public void handleCallback(@NonNull P callbackNettyPacket) {
        callbackPacketConsumer.accept(callbackNettyPacket);
    }
}
