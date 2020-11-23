package org.stonlexx.gamelibrary.common.netty.handler.server.active.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.common.netty.handler.server.active.AbstractNettyServerActive;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class NettyConsumerServerActive extends AbstractNettyServerActive {

    private final Consumer<Channel> channelConsumer;

    @Override
    public void onServerActive(@NonNull Channel serverChannel) {
        channelConsumer.accept(serverChannel);
    }

}
