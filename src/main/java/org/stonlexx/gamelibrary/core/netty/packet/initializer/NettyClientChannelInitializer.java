package org.stonlexx.gamelibrary.core.netty.packet.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.reconnect.server.AbstractNettyReconnect;
import org.stonlexx.gamelibrary.core.netty.reconnect.server.NettyServerReconnectHandler;

public class NettyClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    protected void addAutoReconnect(@NonNull NioSocketChannel socketChannel,
                                    AbstractNettyReconnect nettyReconnect) {

        socketChannel.pipeline().addLast("reconnect-handler", new NettyServerReconnectHandler(nettyReconnect));
    }

    @Override
    protected void initChannel(NioSocketChannel socketChannel) throws Exception {

    }

}
