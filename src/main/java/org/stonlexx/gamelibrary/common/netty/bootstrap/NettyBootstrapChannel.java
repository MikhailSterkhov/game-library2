package org.stonlexx.gamelibrary.common.netty.bootstrap;

import io.netty.channel.Channel;
import org.stonlexx.gamelibrary.common.netty.NettyConnection;

import java.net.InetSocketAddress;

public interface NettyBootstrapChannel {

    InetSocketAddress getSocketAddress();
    Channel getChannel();

    NettyConnection getNettyConnection();
}
