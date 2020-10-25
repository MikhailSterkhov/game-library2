package org.stonlexx.gamelibrary.core.netty.handler.server.reconnect.impl;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.core.netty.handler.server.reconnect.AbstractNettyReconnect;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class NettyConsumerReconnect extends AbstractNettyReconnect {

    private final Consumer<Channel> channelConsumer;

    @Override
    public void onReconnect() {
        channelConsumer.accept(channel);
    }

}
