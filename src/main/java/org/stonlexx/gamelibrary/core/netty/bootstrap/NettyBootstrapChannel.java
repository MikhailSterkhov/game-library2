package org.stonlexx.gamelibrary.core.netty.bootstrap;

import io.netty.channel.Channel;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;

import java.net.InetSocketAddress;

public interface NettyBootstrapChannel {

    InetSocketAddress getSocketAddress();

    Channel getChannel();


    void sendPacket(@NonNull NettyPacket nettyPacket);
}
