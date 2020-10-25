package org.stonlexx.gamelibrary.core.netty.handler.client.active.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.handler.client.active.AbstractNettyClientActive;

public class NettyEmptyClientActive extends AbstractNettyClientActive {

    @Override
    public void onClientActive(@NonNull Channel clientChannel) { }

}
