package org.stonlexx.gamelibrary.core.netty.handler.client.active.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.core.netty.handler.client.active.AbstractNettyClientActive;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class NettyConsumerClientActive extends AbstractNettyClientActive {

    private final Consumer<Channel> channelConsumer;

    @Override
    public void onClientActive(@NonNull Channel clientChannel) {
        channelConsumer.accept(clientChannel);
    }

}
