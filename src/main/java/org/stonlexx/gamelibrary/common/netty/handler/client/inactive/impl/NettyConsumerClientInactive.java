package org.stonlexx.gamelibrary.common.netty.handler.client.inactive.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.common.netty.handler.client.inactive.AbstractNettyClientInactive;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class NettyConsumerClientInactive extends AbstractNettyClientInactive {

    private final Consumer<Channel> channelConsumer;

    @Override
    public void onClientInactive(@NonNull Channel clientChannel) {
        channelConsumer.accept(clientChannel);
    }

}
