package org.stonlexx.gamelibrary.core.netty.packet.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.core.netty.bootstrap.reconnect.NettyReconnectHandler;

@RequiredArgsConstructor
public class NettyServerReconnectHandler extends ChannelInboundHandlerAdapter {

    private final NettyReconnectHandler nettyReconnectHandler;


    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        Channel channel = channelHandlerContext.channel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        Channel channel = channelHandlerContext.channel();
    }

}
