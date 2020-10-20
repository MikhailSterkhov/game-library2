package org.stonlexx.gamelibrary.core.netty.reconnect.client.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.reconnect.client.AbstractNettyClientInactive;

public class NettyEmptyClientInactive extends AbstractNettyClientInactive {

    @Override
    public void onClientInactive(@NonNull Channel clientChannel) { }

}
