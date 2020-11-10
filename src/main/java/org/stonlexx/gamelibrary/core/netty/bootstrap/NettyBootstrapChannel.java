package org.stonlexx.gamelibrary.core.netty.bootstrap;

import io.netty.channel.Channel;
import org.stonlexx.gamelibrary.core.netty.NettyConnection;

import java.net.InetSocketAddress;

public interface NettyBootstrapChannel {

    InetSocketAddress getSocketAddress();
    Channel getChannel();

    NettyConnection getNettyConnection();
}
