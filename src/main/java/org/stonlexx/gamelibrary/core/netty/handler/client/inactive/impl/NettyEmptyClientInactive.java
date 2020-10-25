package org.stonlexx.gamelibrary.core.netty.handler.client.inactive.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.handler.client.inactive.AbstractNettyClientInactive;

public class NettyEmptyClientInactive extends AbstractNettyClientInactive {

    @Override
    public void onClientInactive(@NonNull Channel clientChannel) { }

}
